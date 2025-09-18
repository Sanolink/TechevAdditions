package com.sanolink.techev_additions.mixin.industrialforegoing;

import com.buuz135.industrial.fluid.OreTitaniumFluidType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Arrays;

@Mixin(OreTitaniumFluidType.class)
public class OreTitaniumFluidTypeMixin {

    @Redirect(method = "getOutputDust", at = @At(value = "INVOKE", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"), remap = false)
    private static String redirectOutputDust(String instance, CharSequence target, CharSequence replacement) {
        return instance.replace("create:crushed_raw_materials/", "industrialforegoing:fermented_dusts/");
    }

    @Redirect(method = "getDescription", at = @At(value = "INVOKE", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"), remap = false)
    private String redirectDescriptionDust(String instance, CharSequence target, CharSequence replacement) {
        return instance.replace("create:crushed_raw_materials/", "industrialforegoing:fermented_dusts/");
    }

    @Redirect(method = "getDescription", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;getString()Ljava/lang/String;"), remap = false)
    private String truncateDescription(Component instance) {
        String s = instance.getString();
        String[] parts = s.split(" ");
        if (parts.length > 2) {
            s = String.join(" ", Arrays.copyOf(parts, parts.length - 2));
        }
        return s;
    }

    @Redirect(method = "getDescription", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;getString()Ljava/lang/String;"), remap = false)
    private String truncateDescriptionMutable(MutableComponent instance) {
        String s = instance.getString();
        String[] parts = s.split(" ");
        if (parts.length > 2) {
            s = String.join(" ", Arrays.copyOf(parts, parts.length - 2));
        }
        return s;
    }

    @Redirect(method = "getDescriptionId", at = @At(value = "INVOKE", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"), remap = false)
    private String redirectDescriptionIdDust(String instance, CharSequence target, CharSequence replacement) {
        return instance.replace("create:crushed_raw_materials/", "industrialforegoing:fermented_dusts/");
    }

    @Redirect(method = "getDescriptionId", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MutableComponent;getString()Ljava/lang/String;"), remap = false)
    private String truncateDescriptionIdMutable(MutableComponent instance) {
        String s = instance.getString();
        String[] parts = s.split(" ");
        if (parts.length > 2) {
            s = String.join(" ", Arrays.copyOf(parts, parts.length - 2));
        }
        return s;
    }

    @Redirect(method = "isValid", at = @At(value = "NEW", target = "net/minecraft/resources/ResourceLocation"), remap = false)
    private static ResourceLocation redirectResourceLocation(String namespaceAndPath) {
        String modified = namespaceAndPath.replace("forge:dusts/", "industrialforegoing:fermented_dusts/").replace("create:crushed_raw_materials/", "");
        return new ResourceLocation(modified);
    }

    @Mixin(OreTitaniumFluidType.Client.class)
    public static class ClientMixin {

        @Redirect(method = "getTintColor", at = @At(value = "INVOKE", target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"), remap = false)
        private String redirectDescriptionDust(String instance, CharSequence target, CharSequence replacement) {
            return instance.replace("create:crushed_raw_materials/", "industrialforegoing:fermented_dusts/");
        }
    }
}

