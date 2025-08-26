package com.github.d3tonat0r.healthoverlay;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public class HealthOverlayLayer implements LayeredDraw.Layer {

    private static final ResourceLocation OVERLAY_TEXTURE = ResourceLocation.fromNamespaceAndPath(HealthOverlay.MODID,
            "textures/gui/damage_overlay.png");

    float currentAlpha = 0;

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

        int healthMax = 10;
        int healthMin = 1;
        float health = player.getHealth();

        float targetAlpha = 1f - Math.clamp((health - healthMin) / (float) (healthMax - healthMin), 0, 1);
        float targetAlphaSmoothed = -(float) Math.cos(targetAlpha * Math.PI) * 0.5f + 0.5f;
        currentAlpha = lerp(currentAlpha, targetAlphaSmoothed, deltaTracker.getRealtimeDeltaTicks() * 0.2f);

        guiGraphics.setColor(1, 1, 1, currentAlpha);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0, 0,
                guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        /*
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                String.format("Alpha: %.3f -> %.3f -> %.3f", targetAlpha, targetAlphaSmoothed, currentAlpha),
                4, 4, 0xFFFFFFFF);
        */
    }

    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }
}
