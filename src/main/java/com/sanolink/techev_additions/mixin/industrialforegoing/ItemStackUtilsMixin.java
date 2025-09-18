package com.sanolink.techev_additions.mixin.industrialforegoing;

import com.buuz135.industrial.utils.ItemStackUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemStackUtils.class)
public class ItemStackUtilsMixin {

    @Redirect(method = "getOreTag", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"), remap = false)
    private static boolean redirectStartsWith(String instance, String prefix) {
        return instance.startsWith("create:crushed_raw_materials/");
    }
}
