package com.sanolink.techev_additions.mixin;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(PylonBlockEntity.class)
public interface PylonBlockEntityMixin {

    @Accessor("activated")
    void setActivated(boolean activated);

    @Accessor("centerPos")
    void setCenterPos(BlockPos pos);
}