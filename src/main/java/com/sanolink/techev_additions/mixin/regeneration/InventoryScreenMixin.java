package com.sanolink.techev_additions.mixin.regeneration;

import com.sanolink.techev_additions.TechevAdditionsConfig;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// A mixin to move the Regeneration Mod Button
@Mixin(value = {CreativeModeInventoryScreen.class, InventoryScreen.class})
public abstract class InventoryScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void afterInit(CallbackInfo ci) {
        Screen screen = (Screen) (Object) this;

        for (var widget : screen.renderables) {
            if (widget instanceof ImageButton btn && btn.getWidth() == 19 && btn.getHeight() == 18) {
                btn.setPosition(TechevAdditionsConfig.regenButtonX.get(), TechevAdditionsConfig.regenButtonY.get());
            }
        }
    }
}