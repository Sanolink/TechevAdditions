package com.sanolink.techev_additions.mixin.twilightforest;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import twilightforest.block.FieryBlock;

import java.util.Objects;

@Mixin(FieryBlock.class)
public class FieryBlockMixin {

    @Inject(method = "stepOn", at = @At("HEAD"), cancellable = true)
    public void stepOnMixin(Level level, BlockPos pos, BlockState state, Entity entity, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            String id = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType())).toString();
            if (id.equals("productivebees:configurable_bee")) {
                ci.cancel();
            }
        }
    }
}