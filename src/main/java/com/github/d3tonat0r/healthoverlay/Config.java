package com.github.d3tonat0r.healthoverlay;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue OVERLAY_OPACITY = BUILDER
            .comment("The maximum opacity of the health overlay.")
            .defineInRange("overlayOpacity", 0.75, 0.0, 2.0);

    public static final ModConfigSpec.IntValue OVERLAY_START_HEALTH = BUILDER
            .comment("The health level at which the overlay starts becoming visible, in half-hearts.")
            .defineInRange("overlayStartHealth", 10, 1, 20);

    public static final ModConfigSpec.IntValue OVERLAY_END_HEALTH = BUILDER
            .comment("The health level at which the overlay becomes fully opaque, in half-hearts.")
            .defineInRange("overlayEndHealth", 2, 1, 20);

    public static final ModConfigSpec.DoubleValue OVERLAY_FADE_SPEED = BUILDER
            .comment("The speed at which the overlay fades in and out. Higher is faster.")
            .defineInRange("overlayFadeSpeed", 0.2, 0.01, 1.0);

    public static final ModConfigSpec.DoubleValue FLASH_OPACITY = BUILDER
            .comment("The maximum opacity of the damage flash.")
            .defineInRange("flashOpacity", 1.0, 0.0, 2.0);

    public static final ModConfigSpec.DoubleValue FLASH_FADE_SPEED = BUILDER
            .comment("The speed at which the damage flash fades out. Higher is faster.")
            .defineInRange("flashFadeSpeed", 0.2, 0.01, 1.0);


    static final ModConfigSpec SPEC = BUILDER.build();
}
