package com.github.d3tonat0r.healthoverlay;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class HealthOverlayLayer implements LayeredDraw.Layer {

    private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(HealthOverlay.MODID,
            "textures/gui/damage_overlay.png");

    private float currentAlpha = 0;
    private float currentFlashAlpha = 0;
    private float lastKnownHealth;

    private void onPlayerDamage(float damage) {
        float flashAlpha = Math.clamp(damage * 0.125f, 0, 1);
        currentFlashAlpha = Math.max(currentFlashAlpha, flashAlpha);
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        var player = Minecraft.getInstance().player;
        if (player == null)
            return;

        checkForDamage(player);

        if (Minecraft.getInstance().options.hideGui) {
            currentAlpha = 0;
            return;
        }
        if (player.isCreative() || player.isSpectator()) {
            currentAlpha = 0;
            return;
        }

        renderHealthOverlay(guiGraphics, deltaTracker, player);
        renderFlashOverlay(guiGraphics, deltaTracker);
    }

    private void checkForDamage(LocalPlayer player) {
        float currentHealth = player.getHealth();
        boolean survival = !player.isCreative() && !player.isSpectator();
        //Check if at least half a heart of damage was dealt
        if (currentHealth < (lastKnownHealth + 0.499f)) {
            if (survival) {
                float damage = lastKnownHealth - currentHealth;
                onPlayerDamage(damage);
            }
        }
        lastKnownHealth = currentHealth;
    }

    private void renderHealthOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        float fadeEndHealth = Config.OVERLAY_END_HEALTH.getAsInt();
        float fadeStartHealth = Config.OVERLAY_START_HEALTH.getAsInt();
        if (fadeStartHealth < fadeEndHealth + 1) {
            // Ensure separation of 1 health
            fadeStartHealth = fadeEndHealth + 1;
        }
        float health = player.getHealth();
        float targetAlpha = Math.clamp((health - fadeStartHealth) / (fadeEndHealth - fadeStartHealth), 0, 1);
        float targetAlphaSmoothed = -(float) Math.cos(targetAlpha * Math.PI) * 0.5f + 0.5f;
        currentAlpha = lerp(currentAlpha, targetAlphaSmoothed,
                deltaTracker.getRealtimeDeltaTicks() * (float) Config.OVERLAY_FADE_SPEED.getAsDouble());
        float brightness = (float)Config.BRIGHTNESS.getAsDouble();

        if (currentAlpha > 0.001f) {
            guiGraphics.setColor(brightness, brightness, brightness, currentAlpha * (float) Config.OVERLAY_OPACITY.getAsDouble());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0, 0,
                    guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        }

        //Debug stuff
        /*
        guiGraphics.setColor(brightness, brightness, brightness, 1);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                String.format("Alpha: %.3f -> %.3f -> %.3f", targetAlpha, targetAlphaSmoothed, currentAlpha),
                4, 4, 0xFFFFFFFF);
        */
    }

    private void renderFlashOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        float brightness = (float)Config.BRIGHTNESS.getAsDouble();
        float flashOpacity = (float)Config.FLASH_OPACITY.getAsDouble();
        currentFlashAlpha = lerp(currentFlashAlpha, 0,
                deltaTracker.getRealtimeDeltaTicks() * (float) Config.FLASH_FADE_SPEED.getAsDouble());
        if (currentFlashAlpha > 0.001f) {
            guiGraphics.setColor(0.8f * brightness, 0.1f * brightness, 0.1f * brightness, currentFlashAlpha * flashOpacity);
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0xFFFFFFFF);
        }
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
