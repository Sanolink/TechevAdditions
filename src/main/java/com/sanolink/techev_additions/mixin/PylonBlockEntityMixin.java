package com.sanolink.techev_additions.mixin;

import com.sanolink.techev_additions.block.TechevBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.PylonBlock;
import vazkii.botania.common.block.block_entity.PylonBlockEntity;
import vazkii.botania.xplat.BotaniaConfig;

import java.util.Random;

@Mixin(PylonBlockEntity.class)
public abstract class PylonBlockEntityMixin {

    @Inject(method = "commonTick", at = @At("HEAD"), remap = false)
    private static void onCommonTick(Level level, BlockPos worldPosition, BlockState state, PylonBlockEntity self, CallbackInfo ci) {

        PylonBlock.Variant variant = ((PylonBlock) state.getBlock()).variant;
        BlockPos CenterPos = ((IPylonBlockEntityMixin) self).getCenterPos();
        if (CenterPos == null) {
            return;
        }
        Vec3 centerBlock = new Vec3(CenterPos.getX() + 0.5, CenterPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), CenterPos.getZ() + 0.5);

        if (variant == PylonBlock.Variant.GAIA) {
            if (!level.getBlockState(CenterPos).is(TechevBlocks.SVARTALFPORTAL.get())
                    || level.getBlockState(CenterPos).getValue(BotaniaStateProperties.ALFPORTAL_STATE) == AlfheimPortalState.OFF) {
                ((IPylonBlockEntityMixin) self).setActivated(false);
            }
            if (((IPylonBlockEntityMixin) self).getActivated()) {
                if (BotaniaConfig.client().elfPortalParticlesEnabled()) {
                    double worldTime = ((IPylonBlockEntityMixin) self).getTicks();
                    worldTime += new Random(worldPosition.hashCode()).nextInt(1000);
                    worldTime /= 5;

                    float r = 0.75F + (float) Math.random() * 0.05F;
                    double x = worldPosition.getX() + 0.5 + Math.cos(worldTime) * r;
                    double z = worldPosition.getZ() + 0.5 + Math.sin(worldTime) * r;

                    Vec3 ourCoords = new Vec3(x, worldPosition.getY() + 0.25, z);
                    centerBlock = centerBlock.subtract(0, 0.5, 0);
                    Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

                    WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.2F + 0.8F, (float) Math.random() * 0.5F, (float) Math.random() * 0.5F + 0.5F, 1);
                    level.addParticle(data, x, worldPosition.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
                    if (level.random.nextInt(3) == 0) {
                        WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.2F + 0.8F, (float) Math.random() * 0.5F, (float) Math.random() * 0.5F + 0.5F);
                        level.addParticle(data1, x, worldPosition.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
                    }
                }
            }
        }
    }
}