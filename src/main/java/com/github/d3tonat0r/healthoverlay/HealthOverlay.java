package com.github.d3tonat0r.healthoverlay;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.api.distmarker.Dist;

@Mod(value = HealthOverlay.MODID, dist = Dist.CLIENT)
public class HealthOverlay {
	public static final String MODID = "healthoverlay";
	public static final Logger LOGGER = LogUtils.getLogger();

	private HealthOverlayLayer overlayLayer;

	public HealthOverlay(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::onOverlayRegister);
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	private void onOverlayRegister(final RegisterGuiLayersEvent event) {
		overlayLayer = new HealthOverlayLayer();
		event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(MODID, "health"), overlayLayer);
	}
}
