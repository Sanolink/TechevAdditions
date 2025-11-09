package com.sanolink.techev_additions;

import net.minecraftforge.common.ForgeConfigSpec;

public class TechevAdditionsConfig {

    public static ForgeConfigSpec.IntValue DIMENSION_CARD_DRAIN;
    public static ForgeConfigSpec.IntValue INFINITY_CARD_DRAIN;

    public static final ForgeConfigSpec CLIENT_CONFIG;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("Configuration for TechEv Additions").push("general");

        DIMENSION_CARD_DRAIN = builder
                .comment("The amount of power the Dimension Card drains per tick")
                .defineInRange("dimensionCardDrain", 100, 0, Integer.MAX_VALUE);

        INFINITY_CARD_DRAIN = builder
                .comment("The amount of power the Infinity Card drains per tick")
                .defineInRange("infinityCardDrain", 50, 0, Integer.MAX_VALUE);

        builder.pop();
        CLIENT_CONFIG = builder.build();
    }
}