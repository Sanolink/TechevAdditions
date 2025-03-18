package com.sanolink.techev_additions.mixin;

import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;

@Mixin(PylonBlockEntity.class)
public interface IPylonBlockEntityMixin {

    @Accessor(value = "activated", remap = false)
    void setActivated(boolean activated);

    @Accessor(value = "activated", remap = false)
    boolean getActivated();

    @Accessor(value = "centerPos", remap = false)
    void setCenterPos(BlockPos pos);

    @Accessor(value = "centerPos", remap = false)
    BlockPos getCenterPos();

    @Accessor(value = "ticks", remap = false)
    int getTicks();


}