package com.github.d3tonat0r.healthoverlay;

import org.slf4j.Logger;

import com.ibm.icu.impl.Relation;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(HealthOverlay.MODID)
public class HealthOverlay {
	// Define mod id in a common place for everything to reference
	public static final String MODID = "healthoverlay";
	// Directly reference a slf4j logger
	public static final Logger LOGGER = LogUtils.getLogger();

	// The constructor for the mod class is the first code that is run when your mod
	// is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and
	// pass them in automatically.
	public HealthOverlay(IEventBus modEventBus, ModContainer modContainer) {
		// Register the commonSetup method for modloading
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::onOverlayRegister);

		// Register our mod's ModConfigSpec so that FML can create and load the config
		// file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		// Some common setup code
		LOGGER.info("Common setup for mod: {}", MODID);
	}

	private void onOverlayRegister(final RegisterGuiLayersEvent event) {
		event.registerBelowAll(ResourceLocation.fromNamespaceAndPath(MODID, "health"), new HealthOverlayLayer());
	}
}
