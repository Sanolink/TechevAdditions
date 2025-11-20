package com.sanolink.techev_additions.mixin.create;

import com.sanolink.techev_additions.accessors.create.AirCurrentCatalystAccess;
import com.sanolink.techev_additions.recipes.create.TechevCreateRecipeTypes;
import com.sanolink.techev_additions.recipes.create.TechevFanProcessingTypes;
import com.sanolink.techev_additions.recipes.create.TechevFanRecipe;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.fan.AirCurrent;
import com.simibubi.create.content.kinetics.fan.IAirCurrentSource;
import com.simibubi.create.content.kinetics.fan.processing.FanProcessingType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

@Mixin(AirCurrent.class)
public class AirCurrentMixin implements AirCurrentCatalystAccess {

    @Final
    @Shadow(remap = false)
    public IAirCurrentSource source;

    @Unique
    @Nullable
    private TechevFanRecipe.Catalyst techevAdditions$currentCatalyst;

    @Unique
    public void techevAdditions$setCurrentCatalyst(@Nullable TechevFanRecipe.Catalyst catalyst) {
        this.techevAdditions$currentCatalyst = catalyst;
    }

    @Unique
    @Nullable
    public TechevFanRecipe.Catalyst techevAdditions$getCurrentCatalyst() {
        return this.techevAdditions$currentCatalyst;
    }

    @Inject(
            method = "tickAffectedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;spawnProcessingParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V"
            ),
            remap = false
    )
    private void beforeSpawnProcessingParticlesEntities(CallbackInfo ci) {
        TechevFanRecipe.Catalyst catalyst = techevAdditions$getCurrentCatalyst();
        TechevFanProcessingTypes.CatalystContext.set(catalyst);
    }

    @Inject(
            method = "tickAffectedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;spawnProcessingParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void afterSpawnProcessingParticlesEntities(CallbackInfo ci) {
        TechevFanProcessingTypes.CatalystContext.clear();
    }

    @Inject(
            method = "lambda$tickAffectedHandlers$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;spawnProcessingParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V"
            ),
            remap = false
    )
    private void beforeSpawnProcessingParticlesHandlers(Level world, FanProcessingType processingType, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
        TechevFanRecipe.Catalyst catalyst = techevAdditions$getCurrentCatalyst();
        TechevFanProcessingTypes.CatalystContext.set(catalyst);
    }

    @Inject(
            method = "lambda$tickAffectedHandlers$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;spawnProcessingParticles(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/phys/Vec3;)V",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void afterSpawnProcessingParticlesHandlers(Level world, FanProcessingType processingType, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
        TechevFanProcessingTypes.CatalystContext.clear();
    }

    @Inject(method = "rebuild", at = @At("RETURN"), remap = false)
    private void onRebuild(CallbackInfo ci) {
        techevAdditions$updateCatalyst();
    }

    @Unique
    private void techevAdditions$updateCatalyst() {
        if (source == null || source.getAirCurrentWorld() == null) {
            techevAdditions$setCurrentCatalyst(null);
            return;
        }

        Level level = source.getAirCurrentWorld();
        BlockPos pos = source.getAirCurrentPos();

        BlockPos checkPos = pos.relative(source.getAirflowOriginSide());

        FluidState fluidState = level.getFluidState(checkPos);
        BlockState blockState = level.getBlockState(checkPos);

        List<TechevFanRecipe> recipes = level.getRecipeManager()
                .getAllRecipesFor(TechevCreateRecipeTypes.TECHEV_FAN.getType());

        for (TechevFanRecipe recipe : recipes) {
            for (TechevFanRecipe.Catalyst cat : recipe.getCatalysts()) {
                if (cat.fluid() != null && fluidState.getType() == ForgeRegistries.FLUIDS.getValue(cat.fluid())) {
                    techevAdditions$setCurrentCatalyst(cat);
                    return;
                }
                if (cat.block() != null && blockState.is(Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(cat.block())))) {
                    techevAdditions$setCurrentCatalyst(cat);
                    return;
                }
            }
        }

        techevAdditions$setCurrentCatalyst(null);

    }

    @Inject(
            method = "tickAffectedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessing;applyProcessing(Lnet/minecraft/world/entity/item/ItemEntity;Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;)Z"
            ),
            remap = false
    )
    private void beforeApplyProcessingEntities(CallbackInfo ci) {
        TechevFanRecipe.Catalyst catalyst = techevAdditions$getCurrentCatalyst();
        TechevFanProcessingTypes.CatalystContext.set(catalyst);
    }

    @Inject(
            method = "tickAffectedEntities",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessing;applyProcessing(Lnet/minecraft/world/entity/item/ItemEntity;Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;)Z",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void afterApplyProcessingEntities(CallbackInfo ci) {
        TechevFanProcessingTypes.CatalystContext.clear();
    }

    @Inject(
            method = "lambda$tickAffectedHandlers$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessing;applyProcessing(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;)Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;"
            ),
            remap = false
    )
    private void beforeProcessHandler(Level world, FanProcessingType processingType, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
        if (processingType == TechevFanProcessingTypes.TECHEV_FAN) {
            TechevFanRecipe.Catalyst catalyst = techevAdditions$getCurrentCatalyst();
            TechevFanProcessingTypes.CatalystContext.set(catalyst);
        }
    }

    @Inject(
            method = "lambda$tickAffectedHandlers$2",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessing;applyProcessing(Lcom/simibubi/create/content/kinetics/belt/transport/TransportedItemStack;Lnet/minecraft/world/level/Level;Lcom/simibubi/create/content/kinetics/fan/processing/FanProcessingType;)Lcom/simibubi/create/content/kinetics/belt/behaviour/TransportedItemStackHandlerBehaviour$TransportedResult;",
                    shift = At.Shift.AFTER
            ),
            remap = false
    )
    private void afterProcessHandler(Level world, FanProcessingType processingType, TransportedItemStackHandlerBehaviour handler, TransportedItemStack transported, CallbackInfoReturnable<TransportedItemStackHandlerBehaviour.TransportedResult> cir) {
        if (processingType == TechevFanProcessingTypes.TECHEV_FAN) {
            TechevFanProcessingTypes.CatalystContext.clear();
        }
    }
}