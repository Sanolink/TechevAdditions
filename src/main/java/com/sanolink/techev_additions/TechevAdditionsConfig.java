package com.sanolink.techev_additions;

import com.google.gson.Gson;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class TechevAdditionsConfig {

    public static ForgeConfigSpec.IntValue DIMENSION_CARD_DRAIN;
    public static ForgeConfigSpec.IntValue INFINITY_CARD_DRAIN;

    public static final ForgeConfigSpec SERVER_CONFIG;

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
        SERVER_CONFIG = builder.build();
    }

    //Create Deco Coins
    private static List<String> coinMaterials;

    public static List<String> getCoinMaterials() {
        if (coinMaterials == null) coinMaterials = loadCoinMaterials();
        return coinMaterials;
    }

    private static List<String> loadCoinMaterials() {
        Path path = FMLPaths.CONFIGDIR.get().resolve("techev_additions-coins.json");
        try {
            if (Files.notExists(path)) {
                Files.writeString(path, "[]");
            }
            String[] materials = new Gson().fromJson(Files.readString(path), String[].class);
            TechevAdditions.LOGGER.info("[TechEv] Loaded coin materials: {}", (Object) materials);
            return List.of(materials);
        } catch (Exception e) {
            TechevAdditions.LOGGER.error("[TechEv] Failed to read techev_coins.json, no coins added", e);
            return List.of();
        }
    }
}