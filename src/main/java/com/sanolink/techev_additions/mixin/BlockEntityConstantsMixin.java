package com.sanolink.techev_additions.mixin;

import com.google.common.collect.ImmutableSet;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.common.block.block_entity.BlockEntityConstants;

import java.util.Set;

@Mixin(BlockEntityConstants.class)
public class BlockEntityConstantsMixin {

    @Final
    @Shadow(remap = false) @Mutable
    public static Set<BlockEntityType<?>> SELF_WANDADBLE_BES;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void modifySelfWandadbleBes(CallbackInfo ci) {
        SELF_WANDADBLE_BES = new ImmutableSet.Builder<BlockEntityType<?>>()
                .addAll(SELF_WANDADBLE_BES)
                .add(TechevBlockEntities.SVARTALFPORTAL_BE.get())
                .build();
    }

}
