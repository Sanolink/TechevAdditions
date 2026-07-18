package com.sanolink.techev_additions.mixin.notreepunching;

import com.alcatrazescapee.notreepunching.util.HarvestBlockHandler;
import com.alcatrazescapee.notreepunching.util.ToolType;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = HarvestBlockHandler.class, remap = false)
public interface HarvestBlockHandlerAccessor {

    @Accessor("BLOCK_TOOL_TYPES")
    static Map<Block, ToolType> getBlockToolTypes() {
        throw new AssertionError();
    }
}
