package com.sanolink.techev_additions.mixin.jade;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import snownee.jade.addon.harvest.HarvestToolProvider;

@Mixin(value = HarvestToolProvider.class, remap = false)
public class HarvestToolProviderMixin {

    @Redirect(
            method = "appendTooltip",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;getDestroyProgress(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F",
                    remap = true))
    private float techev$showToolInsteadOfUnbreakable(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        float progress = state.getDestroyProgress(player, level, pos);

        if (progress <= 0.0F && state.getDestroySpeed(level, pos) >= 0.0F) {
            return 1.0F;
        }

        return progress;
    }
}
