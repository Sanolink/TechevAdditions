package com.sanolink.techev_additions.mixin.ae2;

import appeng.menu.implementations.WirelessMenu;
import appeng.menu.slot.RestrictedInputSlot;
import com.sanolink.techev_additions.TechevAdditionsConfig;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.aeinfinitybooster.setup.ModItems;

@Mixin(value = WirelessMenu.class)
public abstract class WirelessMenuMixin {


    @Shadow
    protected abstract void setDrain(long drain);

    @Shadow
    @Final
    private RestrictedInputSlot boosterSlot;

    @Inject(method = "broadcastChanges", at = @At(value = "INVOKE", target = "Lappeng/menu/AEBaseMenu;broadcastChanges()V", shift = At.Shift.BEFORE))
    private void broadcastChanges(CallbackInfo ci) {

        if (this.boosterSlot.getItem().is(ModItems.INFINITY_CARD.get())) {
            this.setDrain((long) 100 * TechevAdditionsConfig.INFINITY_CARD_DRAIN.get());
        }

        if (this.boosterSlot.getItem().is(ModItems.DIMENSION_CARD.get())) {
            this.setDrain((long) 100 * TechevAdditionsConfig.DIMENSION_CARD_DRAIN.get());
        }
    }
}
