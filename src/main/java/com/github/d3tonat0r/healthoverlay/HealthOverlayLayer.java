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

    public void onPlayerDamage(float damage) {
        float flashAlpha = Math.clamp(damage * 0.125f, 0, 1);
        currentFlashAlpha = Math.max(currentFlashAlpha, flashAlpha);
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui) {
            currentAlpha = 0;
            return;
        }
        var player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator()) {
            currentAlpha = 0;
            return;
        }

        renderHealthOverlay(guiGraphics, deltaTracker, player);
        renderFlashOverlay(guiGraphics, deltaTracker);
    }

    private void renderHealthOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, LocalPlayer player) {
        float fadeEndHealth = Config.OVERLAY_END_HEALTH.getAsInt();
        float fadeStartHealth = Config.OVERLAY_START_HEALTH.getAsInt();
        if(fadeStartHealth < fadeEndHealth + 1) {
            //Ensure separation of 1 health
            fadeStartHealth = fadeEndHealth + 1;
        }
        float health = player.getHealth();
        float targetAlpha = Math.clamp((health - fadeStartHealth) / (fadeEndHealth - fadeStartHealth), 0, 1);
        float targetAlphaSmoothed = -(float) Math.cos(targetAlpha * Math.PI) * 0.5f + 0.5f;
        currentAlpha = lerp(currentAlpha, targetAlphaSmoothed, deltaTracker.getRealtimeDeltaTicks() * (float)Config.OVERLAY_FADE_SPEED.getAsDouble());

        if(currentAlpha > 0.001f) {
            guiGraphics.setColor(1, 1, 1, currentAlpha * (float)Config.OVERLAY_OPACITY.getAsDouble());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0, 0,
                    guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        }

        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                String.format("Alpha: %.3f -> %.3f -> %.3f", targetAlpha, targetAlphaSmoothed, currentAlpha),
                4, 4, 0xFFFFFFFF);
    }

    private void renderFlashOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        currentFlashAlpha = lerp(currentFlashAlpha, 0, deltaTracker.getRealtimeDeltaTicks() * (float)Config.FLASH_FADE_SPEED.getAsDouble());
        if(currentFlashAlpha > 0.001f) {
            guiGraphics.setColor(0.8f, 0.1f, 0.1f, currentFlashAlpha * (float)Config.FLASH_OPACITY.getAsDouble());
            guiGraphics.fill(0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0xFFFFFFFF);
        }
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
