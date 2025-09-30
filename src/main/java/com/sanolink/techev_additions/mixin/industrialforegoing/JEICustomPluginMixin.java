package com.sanolink.techev_additions.mixin.industrialforegoing;

import com.buuz135.industrial.plugin.jei.JEICustomPlugin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(JEICustomPlugin.class)
public class JEICustomPluginMixin {

    @Redirect(method = "lambda$registerRecipes$8", at = @At(value = "INVOKE", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"))
    private static String redirectDustReplace(String original, CharSequence target, CharSequence replacement) {
        if (replacement.equals("forge:dusts/")) {
            return original.replace("create:crushed_raw_materials/", "industrialforegoing:fermented_dusts/");
        }
        return original.replace(target, replacement);
    }

    @Redirect(method = "lambda$registerRecipes$7", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"))
    private static boolean redirectCrushedReplace(String instance, String prefix) {
        return instance.startsWith("create:crushed_raw_materials/");
    }

}

