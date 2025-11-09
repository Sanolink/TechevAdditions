package com.sanolink.techev_additions.mixin.ae2;

import appeng.blockentity.networking.WirelessBlockEntity;
import com.sanolink.techev_additions.TechevAdditionsConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.aeinfinitybooster.setup.ModItems;

@Mixin(WirelessBlockEntity.class)
public class WirelessBlockEntityMixin {


    @Inject(method = "updatePower", at = @At("HEAD"), cancellable = true, remap = false)
    private void updatePower(CallbackInfo ci) {
        WirelessBlockEntity self = (WirelessBlockEntity) (Object) this;

        ItemStack stack = self.getInternalInventory().getStackInSlot(0);

        if (stack.is(ModItems.INFINITY_CARD.get())) {
            self.getMainNode().setIdlePowerUsage(TechevAdditionsConfig.INFINITY_CARD_DRAIN.get());
            ci.cancel();
        }

        if (stack.is(ModItems.DIMENSION_CARD.get())) {
            self.getMainNode().setIdlePowerUsage(TechevAdditionsConfig.DIMENSION_CARD_DRAIN.get());
            ci.cancel();
        }
    }
}
