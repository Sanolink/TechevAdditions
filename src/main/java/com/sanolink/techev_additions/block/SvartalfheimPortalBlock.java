package com.sanolink.techev_additions.block;

import com.sanolink.techev_additions.block.entity.SvartalfheimPortalBlockEntity;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.common.block.BotaniaBlock;

public class SvartalfheimPortalBlock extends BotaniaBlock implements EntityBlock {

    public SvartalfheimPortalBlock(Properties builder) {
        super(builder);
        registerDefaultState(defaultBlockState().setValue(BotaniaStateProperties.ALFPORTAL_STATE, AlfheimPortalState.OFF));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BotaniaStateProperties.ALFPORTAL_STATE);
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SvartalfheimPortalBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, TechevBlockEntities.SVARTALFPORTAL_BE.get(), SvartalfheimPortalBlockEntity::commonTick);
    }
}