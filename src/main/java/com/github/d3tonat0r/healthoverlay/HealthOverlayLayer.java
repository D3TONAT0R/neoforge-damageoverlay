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

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().options.hideGui)
            return;
        var player = Minecraft.getInstance().player;
        if (player == null || player.isCreative() || player.isSpectator())
            return;

        int healthMax = 10;
        int healthMin = 1;
        float health = player.getHealth();

        float alpha = 1f - Math.clamp((health - healthMin) / (float) (healthMax - healthMin), 0, 1);
        float smoothAlpha = -(float) Math.cos(alpha * Math.PI) * 0.5f + 0.5f;
        // float *= alphaScale;

        guiGraphics.setColor(1, 1, 1, smoothAlpha);
        // RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        // RenderSystem.defaultAlphaFunc();
        guiGraphics.blit(OVERLAY_TEXTURE, 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(), 0, 0,
                guiGraphics.guiWidth(), guiGraphics.guiHeight(), guiGraphics.guiWidth(), guiGraphics.guiHeight());
        guiGraphics.setColor(1, 1, 1, 1);
        guiGraphics.drawString(
                Minecraft.getInstance().font,
                String.format("Alpha: %.3f -> %.3f", alpha, smoothAlpha),
                4, 4, 0xFFFFFFFF);
    }
}
