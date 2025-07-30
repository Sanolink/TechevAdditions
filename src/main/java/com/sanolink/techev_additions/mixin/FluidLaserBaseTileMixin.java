package com.sanolink.techev_additions.mixin;

import com.buuz135.industrial.block.resourceproduction.tile.FluidLaserBaseTile;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(FluidLaserBaseTile.class)
public class FluidLaserBaseTileMixin {

    @Redirect(method = "lambda$onWork$10", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"), remap = false)
    private boolean cancelDamage(LivingEntity entity, DamageSource source, float amount) {
        return entity.hurt(source, 0.0F);
    }
}
