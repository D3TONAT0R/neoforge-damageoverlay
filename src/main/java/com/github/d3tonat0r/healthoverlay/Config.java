package com.github.d3tonat0r.healthoverlay;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    
    public static final ModConfigSpec.IntValue OVERLAY_START_HEALTH = BUILDER
            .comment("The number of hearts at which the overlay starts becoming visible.")
            .defineInRange("overlayStartHealth", 10, 1, 20);

    static final ModConfigSpec SPEC = BUILDER.build();
}
