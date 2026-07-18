package com.sanolink.techev_additions.mixin.cataclysm;

import com.min01.archaeology.blockentity.DecoratedPotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = DecoratedPotBlockEntity.class, remap = false)
public abstract class DecoratedPotBlockEntityMixin {

    public Level getLevel() {
        return ((BlockEntity) (Object) this).level;
    }

    public BlockPos getBlockPos() {
        return ((BlockEntity) (Object) this).worldPosition;
    }

}
