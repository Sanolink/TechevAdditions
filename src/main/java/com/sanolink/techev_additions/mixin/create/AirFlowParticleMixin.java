package com.sanolink.techev_additions.mixin.create;

import com.sanolink.techev_additions.accessors.create.AirCurrentCatalystAccess;
import com.sanolink.techev_additions.recipes.create.TechevFanProcessingTypes;
import com.sanolink.techev_additions.recipes.create.TechevFanRecipe;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.AirFlowParticle;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AirFlowParticle.class)
public class AirFlowParticleMixin {

    @Final
    @Shadow(remap = false)
    private IAirCurrentSource source;

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;morphAirFlow(Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType$AirFlowParticleAccess;Lnet/minecraft/util/RandomSource;)V"
            )
    )
    private void beforeMorphAirFlow(CallbackInfo ci) {
        AirCurrent airCurrent = source.getAirCurrent();
        if (airCurrent instanceof AirCurrentCatalystAccess access) {
            TechevFanRecipe.Catalyst catalyst = access.techevAdditions$getCurrentCatalyst();
            TechevFanProcessingTypes.CatalystContext.set(catalyst);
        }
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;morphAirFlow(Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType$AirFlowParticleAccess;Lnet/minecraft/util/RandomSource;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterMorphAirFlow(CallbackInfo ci) {
        TechevFanProcessingTypes.CatalystContext.clear();
    }
}