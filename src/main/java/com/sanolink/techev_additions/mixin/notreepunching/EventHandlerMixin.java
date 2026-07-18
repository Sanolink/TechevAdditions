package com.sanolink.techev_additions.mixin.notreepunching;

import com.alcatrazescapee.notreepunching.EventHandler;
import com.sanolink.techev_additions.progression.TechEvProgression;
import com.sanolink.techev_additions.progression.VanillaHarvestRules;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EventHandler.class, remap = false)
public class EventHandlerMixin {

    @Inject(method = "modifyHarvestCheck", at = @At("HEAD"), cancellable = true)
    private static void techev$bypassHarvestCheck(Player player, BlockState state, BlockPos pos,
                                                  boolean canHarvest, CallbackInfoReturnable<Boolean> cir) {
        if (TechEvProgression.hasNoTreePunchingBypass(player)) {
            cir.setReturnValue(!VanillaHarvestRules.requiresCorrectTool(state) || canHarvest);
        }
    }

    @Inject(method = "modifyBreakSpeed", at = @At("HEAD"), cancellable = true)
    private static void techev$bypassBreakSpeed(Player player, BlockState state, BlockPos pos,
                                                float newSpeed, CallbackInfoReturnable<Float> cir) {
        if (TechEvProgression.hasNoTreePunchingBypass(player)) {
            cir.setReturnValue(newSpeed);
        }
    }
}
