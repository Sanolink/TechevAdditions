package com.sanolink.techev_additions.progression;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public final class VanillaHarvestRules {

    private static final Set<Block> REQUIRES_CORRECT_TOOL =
            Collections.newSetFromMap(new IdentityHashMap<>());

    private static volatile boolean captured;

    private VanillaHarvestRules() {
    }

    public static void captureOnce() {
        if (captured) {
            return;
        }
        captured = true;

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block.defaultBlockState().requiresCorrectToolForDrops()) {
                REQUIRES_CORRECT_TOOL.add(block);
            }
        }
    }

    public static boolean requiresCorrectTool(BlockState state) {
        return REQUIRES_CORRECT_TOOL.contains(state.getBlock());
    }
}
