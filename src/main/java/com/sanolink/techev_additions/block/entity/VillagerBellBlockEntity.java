package com.sanolink.techev_additions.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Iterator;
import java.util.List;

public class VillagerBellBlockEntity extends BlockEntity {
    private static final int DURATION = 50;
    private static final int WITHER_DURATION = 60;
    private static final int MIN_TICKS_BETWEEN_SEARCHES = 60;
    private static final int MAX_RESONATION_TICKS = 40;
    private static final int TICKS_BEFORE_RESONATION = 5;
    private static final int SEARCH_RADIUS = 48;
    private static final int HEAR_BELL_RADIUS = 32;
    private static final int HIGHLIGHT_RAIDERS_RADIUS = 48;
    private long lastRingTimestamp;
    public int ticks;
    public boolean shaking;
    public Direction clickDirection;
    private List<LivingEntity> nearbyEntities;
    private boolean resonating;
    private int resonationTicks;

    public VillagerBellBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(TechevBlockEntities.VILLAGER_BELL_BE.get(), pPos, pBlockState);
    }

    public boolean triggerEvent(int pId, int pType) {
        if (pId == 1) {
            this.updateEntities();
            this.resonationTicks = 0;
            this.clickDirection = Direction.from3DDataValue(pType);
            this.ticks = 0;
            this.shaking = true;
            return true;
        } else {
            return super.triggerEvent(pId, pType);
        }
    }

    private static void tick(Level pLevel, BlockPos pPos, BlockState pState, VillagerBellBlockEntity pBlockEntity, VillagerBellBlockEntity.ResonationEndAction pResonationEndAction) {
        if (pBlockEntity.shaking) {
            ++pBlockEntity.ticks;
        }

        if (pBlockEntity.ticks >= 50) {
            pBlockEntity.shaking = false;
            pBlockEntity.ticks = 0;
        }

        if (pBlockEntity.ticks >= 5 && pBlockEntity.resonationTicks == 0 && areRaidersNearby(pPos, pBlockEntity.nearbyEntities)) {
            pBlockEntity.resonating = true;
            pLevel.playSound((Player)null, pPos, SoundEvents.BELL_RESONATE, SoundSource.BLOCKS, 1.0F, 1.0F);
        }

        if (pBlockEntity.resonating) {
            if (pBlockEntity.resonationTicks < 40) {
                ++pBlockEntity.resonationTicks;
            } else {
                pResonationEndAction.run(pLevel, pPos, pBlockEntity.nearbyEntities);
                pBlockEntity.resonating = false;
            }
        }

    }

    public static void clientTick(Level pLevel, BlockPos pPos, BlockState pState, VillagerBellBlockEntity pBlockEntity) {
        tick(pLevel, pPos, pState, pBlockEntity, VillagerBellBlockEntity::showBellParticles);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, VillagerBellBlockEntity pBlockEntity) {
        tick(pLevel, pPos, pState, pBlockEntity, VillagerBellBlockEntity::makeRaidersWither);
    }

    public void onHit(Direction pDirection) {
        BlockPos $$1 = this.getBlockPos();
        this.clickDirection = pDirection;
        if (this.shaking) {
            this.ticks = 0;
        } else {
            this.shaking = true;
        }

        this.level.blockEvent($$1, this.getBlockState().getBlock(), 1, pDirection.get3DDataValue());
    }

    private void updateEntities() {
        BlockPos $$0 = this.getBlockPos();
        if (this.level.getGameTime() > this.lastRingTimestamp + 60L || this.nearbyEntities == null) {
            this.lastRingTimestamp = this.level.getGameTime();
            AABB $$1 = (new AABB($$0)).inflate(48.0);
            this.nearbyEntities = this.level.getEntitiesOfClass(LivingEntity.class, $$1);
        }

        if (!this.level.isClientSide) {
            Iterator var4 = this.nearbyEntities.iterator();

            while(var4.hasNext()) {
                LivingEntity $$2 = (LivingEntity)var4.next();
                if ($$2.isAlive() && !$$2.isRemoved() && $$0.closerToCenterThan($$2.position(), 32.0)) {
                    $$2.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, this.level.getGameTime());
                }
            }
        }

    }

    private static boolean areRaidersNearby(BlockPos pPos, List<LivingEntity> pRaiders) {
        Iterator var2 = pRaiders.iterator();

        LivingEntity $$2;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            $$2 = (LivingEntity)var2.next();
        } while(!$$2.isAlive() || $$2.isRemoved() || !pPos.closerToCenterThan($$2.position(), 32.0) || !$$2.getType().is(EntityTypeTags.RAIDERS));

        return true;
    }

    private static void makeRaidersWither(Level p_155187_, BlockPos p_155188_, List<LivingEntity> p_155189_) {
        p_155189_.stream().filter((p_155219_) -> {
            return isRaiderWithinRange(p_155188_, p_155219_);
        }).forEach(VillagerBellBlockEntity::wither);
    }

    private static void showBellParticles(Level p_155208_, BlockPos p_155209_, List<LivingEntity> p_155210_) {
        MutableInt $$3 = new MutableInt(16700985);
        int $$4 = (int)p_155210_.stream().filter((p_155216_) -> {
            return p_155209_.closerToCenterThan(p_155216_.position(), 48.0);
        }).count();
        p_155210_.stream().filter((p_155213_) -> {
            return isRaiderWithinRange(p_155209_, p_155213_);
        }).forEach((p_155195_) -> {
            float $$5 = 1.0F;
            double $$6 = Math.sqrt((p_155195_.getX() - (double)p_155209_.getX()) * (p_155195_.getX() - (double)p_155209_.getX()) + (p_155195_.getZ() - (double)p_155209_.getZ()) * (p_155195_.getZ() - (double)p_155209_.getZ()));
            double $$7 = (double)((float)p_155209_.getX() + 0.5F) + 1.0 / $$6 * (p_155195_.getX() - (double)p_155209_.getX());
            double $$8 = (double)((float)p_155209_.getZ() + 0.5F) + 1.0 / $$6 * (p_155195_.getZ() - (double)p_155209_.getZ());
            int $$9 = Mth.clamp(($$4 - 21) / -2, 3, 15);

            for(int $$10 = 0; $$10 < $$9; ++$$10) {
                int $$11 = $$3.addAndGet(5);
                double $$12 = (double) FastColor.ARGB32.red($$11) / 255.0;
                double $$13 = (double) FastColor.ARGB32.green($$11) / 255.0;
                double $$14 = (double) FastColor.ARGB32.blue($$11) / 255.0;
                p_155208_.addParticle(ParticleTypes.ENTITY_EFFECT, $$7, (double)((float)p_155209_.getY() + 0.5F), $$8, $$12, $$13, $$14);
            }

        });
    }

    private static boolean isRaiderWithinRange(BlockPos pPos, LivingEntity pRaider) {
        return pRaider.isAlive() && !pRaider.isRemoved() && pPos.closerToCenterThan(pRaider.position(), 48.0) && pRaider.getType().is(EntityTypeTags.RAIDERS);
    }

    private static void wither(LivingEntity p_58841_) {
        p_58841_.addEffect(new MobEffectInstance(MobEffects.WITHER, 600));
    }

    @FunctionalInterface
    interface ResonationEndAction {
        void run(Level var1, BlockPos var2, List<LivingEntity> var3);
    }
}