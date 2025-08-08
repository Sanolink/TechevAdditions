package com.sanolink.techev_additions.mixin;

import mythicbotany.EventListener;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;

@Mixin(EventListener.class)
public class MythicBotanyMixin {

    @Inject(method = "alfPortalUpdate", at = @At("HEAD"), remap = false, cancellable = true)
    private void preventTeleportForCustomPortals(ElvenPortalUpdateEvent event, CallbackInfo ci) {
        BlockEntity portal = event.getPortalTile();
        if (!(portal instanceof AlfheimPortalBlockEntity)) {
            ci.cancel();
        }
    }
}
