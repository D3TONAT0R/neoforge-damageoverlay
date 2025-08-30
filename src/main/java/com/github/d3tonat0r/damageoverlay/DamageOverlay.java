package com.github.d3tonat0r.damageoverlay;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.api.distmarker.Dist;

@Mod(value = DamageOverlay.MODID, dist = Dist.CLIENT)
public class DamageOverlay {
	public static final String MODID = "damageoverlay";
	public static final Logger LOGGER = LogUtils.getLogger();

	private DamageOverlayLayer overlayLayer;

	public DamageOverlay(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::onOverlayRegister);
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	private void onOverlayRegister(final RegisterGuiLayersEvent event) {
		overlayLayer = new DamageOverlayLayer();
		event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(MODID, "health"), overlayLayer);
	}
}
