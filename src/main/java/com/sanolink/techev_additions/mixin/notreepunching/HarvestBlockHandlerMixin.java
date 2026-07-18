package com.sanolink.techev_additions.mixin.notreepunching;

import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.sanolink.techev_additions.progression.VanillaHarvestRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = HarvestBlockHandler.class, remap = false)
public abstract class HarvestBlockHandlerMixin {

    @Inject(method = "setup", at = @At("HEAD"))
    private static void techev$captureVanillaHarvestRules(CallbackInfo ci) {
        VanillaHarvestRules.captureOnce();
    }
}
