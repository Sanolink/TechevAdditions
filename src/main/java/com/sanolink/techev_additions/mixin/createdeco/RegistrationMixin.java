package com.sanolink.techev_additions.mixin.createdeco;

import com.github.talrey.createdeco.Registration;
import com.github.talrey.createdeco.registry.Props;
import com.sanolink.techev_additions.TechevAdditionsConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registration.class)
public class RegistrationMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void techev_addCoinTypes(CallbackInfo ci) {
        Props.COIN_TYPES.addAll(TechevAdditionsConfig.getCoinMaterials());
    }
}