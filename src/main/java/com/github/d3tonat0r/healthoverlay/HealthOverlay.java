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

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(value = HealthOverlay.MODID, dist = Dist.CLIENT)
public class HealthOverlay {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "healthoverlay";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();

	private HealthOverlayLayer overlayLayer;

	// The constructor for the mod class is the first code that is run when your mod
	// is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and
	// pass them in automatically.
	public HealthOverlay(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::onOverlayRegister);
		NeoForge.EVENT_BUS.addListener(this::onEntityDamage);

		// Register our mod's ModConfigSpec so that FML can create and load the config
		// file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

		// Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
	}

	private void onOverlayRegister(final RegisterGuiLayersEvent event) {
		overlayLayer = new HealthOverlayLayer();
		event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(MODID, "health"), overlayLayer);
	}

	private void onEntityDamage(LivingDamageEvent.Post event) {
		if (event.getEntity() instanceof Player) {
			Player p = (Player) event.getEntity();
			if (p.getUUID().equals(Minecraft.getInstance().player.getUUID())) {
				overlayLayer.onPlayerDamage(event.getNewDamage());
			}
		}
	}
}
