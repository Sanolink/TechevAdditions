package com.sanolink.techev_additions.block;

import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import com.sanolink.techev_additions.block.entity.VillagerBellBlockEntity;
import com.sanolink.techev_additions.sound.TechevSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class VillagerBellBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING;
    public static final EnumProperty<BellAttachType> ATTACHMENT;
    public static final BooleanProperty POWERED;
    private static final VoxelShape NORTH_SOUTH_FLOOR_SHAPE;
    private static final VoxelShape EAST_WEST_FLOOR_SHAPE;
    private static final VoxelShape BELL_TOP_SHAPE;
    private static final VoxelShape BELL_BOTTOM_SHAPE;
    private static final VoxelShape BELL_SHAPE;
    private static final VoxelShape NORTH_SOUTH_BETWEEN;
    private static final VoxelShape EAST_WEST_BETWEEN;
    private static final VoxelShape TO_WEST;
    private static final VoxelShape TO_EAST;
    private static final VoxelShape TO_NORTH;
    private static final VoxelShape TO_SOUTH;
    private static final VoxelShape CEILING_SHAPE;
    public static final int EVENT_BELL_RING = 1;

    public VillagerBellBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(ATTACHMENT, BellAttachType.FLOOR)).setValue(POWERED, false));
    }

    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        boolean $$6 = pLevel.hasNeighborSignal(pPos);
        if ($$6 != (Boolean)pState.getValue(POWERED)) {
            if ($$6) {
                this.attemptToRing(pLevel, pPos, (Direction)null);
            }

            pLevel.setBlock(pPos, (BlockState)pState.setValue(POWERED, $$6), 3);
        }

    }

    public void onProjectileHit(Level pLevel, BlockState pState, BlockHitResult pHit, Projectile pProjectile) {
        Entity $$4 = pProjectile.getOwner();
        Player $$5 = $$4 instanceof Player ? (Player)$$4 : null;
        this.onHit(pLevel, pState, pHit, $$5, true);
    }

    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        return this.onHit(pLevel, pState, pHit, pPlayer, true) ? InteractionResult.sidedSuccess(pLevel.isClientSide) : InteractionResult.PASS;
    }

    public boolean onHit(Level pLevel, BlockState pState, BlockHitResult pResult, @Nullable Player pPlayer, boolean pCanRingBell) {
        Direction $$5 = pResult.getDirection();
        BlockPos $$6 = pResult.getBlockPos();
        boolean $$7 = !pCanRingBell || this.isProperHit(pState, $$5, pResult.getLocation().y - (double)$$6.getY());
        if ($$7) {
            boolean $$8 = this.attemptToRing(pPlayer, pLevel, $$6, $$5);
            if ($$8 && pPlayer != null) {
                pPlayer.awardStat(Stats.BELL_RING);
            }

            return true;
        } else {
            return false;
        }
    }

    private boolean isProperHit(BlockState pPos, Direction pDirection, double pDistanceY) {
        if (pDirection.getAxis() != Direction.Axis.Y && !(pDistanceY > 0.8123999834060669)) {
            Direction $$3 = (Direction)pPos.getValue(FACING);
            BellAttachType $$4 = (BellAttachType)pPos.getValue(ATTACHMENT);
            switch ($$4) {
                case FLOOR:
                    return $$3.getAxis() == pDirection.getAxis();
                case SINGLE_WALL:
                case DOUBLE_WALL:
                    return $$3.getAxis() != pDirection.getAxis();
                case CEILING:
                    return true;
                default:
                    return false;
            }
        } else {
            return false;
        }
    }

    public boolean attemptToRing(Level pLevel, BlockPos pPos, @Nullable Direction pDirection) {
        return this.attemptToRing((Entity)null, pLevel, pPos, pDirection);
    }

    public boolean attemptToRing(@Nullable Entity pEntity, Level pLevel, BlockPos pPos, @Nullable Direction pDirection) {
        BlockEntity $$4 = pLevel.getBlockEntity(pPos);
        if (!pLevel.isClientSide && $$4 instanceof VillagerBellBlockEntity) {
            if (pDirection == null) {
                pDirection = (Direction)pLevel.getBlockState(pPos).getValue(FACING);
            }

            ((VillagerBellBlockEntity)$$4).onHit(pDirection);
            pLevel.playSound((Player)null, pPos, TechevSounds.VILLAGER_BELL_BLOCK.get(), SoundSource.BLOCKS, 2.0F, 1.0F);
            pLevel.gameEvent(pEntity, GameEvent.BLOCK_CHANGE, pPos);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShape getVoxelShape(BlockState pState) {
        Direction $$1 = (Direction)pState.getValue(FACING);
        BellAttachType $$2 = (BellAttachType)pState.getValue(ATTACHMENT);
        if ($$2 == BellAttachType.FLOOR) {
            return $$1 != Direction.NORTH && $$1 != Direction.SOUTH ? EAST_WEST_FLOOR_SHAPE : NORTH_SOUTH_FLOOR_SHAPE;
        } else if ($$2 == BellAttachType.CEILING) {
            return CEILING_SHAPE;
        } else if ($$2 == BellAttachType.DOUBLE_WALL) {
            return $$1 != Direction.NORTH && $$1 != Direction.SOUTH ? EAST_WEST_BETWEEN : NORTH_SOUTH_BETWEEN;
        } else if ($$1 == Direction.NORTH) {
            return TO_NORTH;
        } else if ($$1 == Direction.SOUTH) {
            return TO_SOUTH;
        } else {
            return $$1 == Direction.EAST ? TO_EAST : TO_WEST;
        }
    }
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getVoxelShape(pState);
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.getVoxelShape(pState);
    }

    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction $$1 = pContext.getClickedFace();
        BlockPos $$2 = pContext.getClickedPos();
        Level $$3 = pContext.getLevel();
        Direction.Axis $$4 = $$1.getAxis();
        BlockState $$7;
        if ($$4 == Direction.Axis.Y) {
            $$7 = (BlockState)((BlockState)this.defaultBlockState().setValue(ATTACHMENT, $$1 == Direction.DOWN ? BellAttachType.CEILING : BellAttachType.FLOOR)).setValue(FACING, pContext.getHorizontalDirection());
            if ($$7.canSurvive(pContext.getLevel(), $$2)) {
                return $$7;
            }
        } else {
            boolean $$6 = $$4 == Direction.Axis.X && $$3.getBlockState($$2.west()).isFaceSturdy($$3, $$2.west(), Direction.EAST) && $$3.getBlockState($$2.east()).isFaceSturdy($$3, $$2.east(), Direction.WEST) || $$4 == Direction.Axis.Z && $$3.getBlockState($$2.north()).isFaceSturdy($$3, $$2.north(), Direction.SOUTH) && $$3.getBlockState($$2.south()).isFaceSturdy($$3, $$2.south(), Direction.NORTH);
            $$7 = (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, $$1.getOpposite())).setValue(ATTACHMENT, $$6 ? BellAttachType.DOUBLE_WALL : BellAttachType.SINGLE_WALL);
            if ($$7.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
                return $$7;
            }

            boolean $$8 = $$3.getBlockState($$2.below()).isFaceSturdy($$3, $$2.below(), Direction.UP);
            $$7 = (BlockState)$$7.setValue(ATTACHMENT, $$8 ? BellAttachType.FLOOR : BellAttachType.CEILING);
            if ($$7.canSurvive(pContext.getLevel(), pContext.getClickedPos())) {
                return $$7;
            }
        }

        return null;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        BellAttachType $$6 = (BellAttachType)pState.getValue(ATTACHMENT);
        Direction $$7 = getConnectedDirection(pState).getOpposite();
        if ($$7 == pFacing && !pState.canSurvive(pLevel, pCurrentPos) && $$6 != BellAttachType.DOUBLE_WALL) {
            return Blocks.AIR.defaultBlockState();
        } else {
            if (pFacing.getAxis() == ((Direction)pState.getValue(FACING)).getAxis()) {
                if ($$6 == BellAttachType.DOUBLE_WALL && !pFacingState.isFaceSturdy(pLevel, pFacingPos, pFacing)) {
                    return (BlockState)((BlockState)pState.setValue(ATTACHMENT, BellAttachType.SINGLE_WALL)).setValue(FACING, pFacing.getOpposite());
                }

                if ($$6 == BellAttachType.SINGLE_WALL && $$7.getOpposite() == pFacing && pFacingState.isFaceSturdy(pLevel, pFacingPos, (Direction)pState.getValue(FACING))) {
                    return (BlockState)pState.setValue(ATTACHMENT, BellAttachType.DOUBLE_WALL);
                }
            }

            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction $$3 = getConnectedDirection(pState).getOpposite();
        return $$3 == Direction.UP ? Block.canSupportCenter(pLevel, pPos.above(), Direction.DOWN) : FaceAttachedHorizontalDirectionalBlock.canAttach(pLevel, pPos, $$3);
    }

    private static Direction getConnectedDirection(BlockState pState) {
        switch ((BellAttachType)pState.getValue(ATTACHMENT)) {
            case FLOOR:
                return Direction.UP;
            case CEILING:
                return Direction.DOWN;
            default:
                return ((Direction)pState.getValue(FACING)).getOpposite();
        }
    }

    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.DESTROY;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(new Property[]{FACING, ATTACHMENT, POWERED});
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new VillagerBellBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, TechevBlockEntities.VILLAGER_BELL_BE.get(), pLevel.isClientSide ? VillagerBellBlockEntity::clientTick : VillagerBellBlockEntity::serverTick);
    }

    public boolean isPathfindable(BlockState pState, BlockGetter pLevel, BlockPos pPos, PathComputationType pType) {
        return false;
    }

    static {
        FACING = HorizontalDirectionalBlock.FACING;
        ATTACHMENT = BlockStateProperties.BELL_ATTACHMENT;
        POWERED = BlockStateProperties.POWERED;
        NORTH_SOUTH_FLOOR_SHAPE = Block.box(0.0, 0.0, 4.0, 16.0, 16.0, 12.0);
        EAST_WEST_FLOOR_SHAPE = Block.box(4.0, 0.0, 0.0, 12.0, 16.0, 16.0);
        BELL_TOP_SHAPE = Block.box(5.0, 6.0, 5.0, 11.0, 13.0, 11.0);
        BELL_BOTTOM_SHAPE = Block.box(4.0, 4.0, 4.0, 12.0, 6.0, 12.0);
        BELL_SHAPE = Shapes.or(BELL_BOTTOM_SHAPE, BELL_TOP_SHAPE);
        NORTH_SOUTH_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 16.0));
        EAST_WEST_BETWEEN = Shapes.or(BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 16.0, 15.0, 9.0));
        TO_WEST = Shapes.or(BELL_SHAPE, Block.box(0.0, 13.0, 7.0, 13.0, 15.0, 9.0));
        TO_EAST = Shapes.or(BELL_SHAPE, Block.box(3.0, 13.0, 7.0, 16.0, 15.0, 9.0));
        TO_NORTH = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 0.0, 9.0, 15.0, 13.0));
        TO_SOUTH = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 3.0, 9.0, 15.0, 16.0));
        CEILING_SHAPE = Shapes.or(BELL_SHAPE, Block.box(7.0, 13.0, 7.0, 9.0, 16.0, 9.0));
    }
}