package com.sanolink.techev_additions.mixin.industrialforegoing;

import com.buuz135.industrial.block.resourceproduction.tile.WashingFactoryTile;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WashingFactoryTile.class)
public class WashingFactoryTileMixin {

    private static final TagKey<Item> CRUSHED_RAW_MATERIALS =
            TagKey.create(Registry.ITEM_REGISTRY, ResourceLocation.fromNamespaceAndPath("create", "crushed_raw_materials"));

    @Redirect(method = "lambda$new$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;is(Lnet/minecraft/tags/TagKey;)Z"))
    private static boolean redirectIsCrushed(ItemStack instance, TagKey<Item> originalTag) {
        return instance.is(CRUSHED_RAW_MATERIALS);
    }

    @Redirect(method = "lambda$new$1", at = @At(value = "INVOKE", target = "Ljava/lang/String;startsWith(Ljava/lang/String;)Z"))
    private static boolean redirectCrushedReplace(String instance, String prefix) {
        return instance.startsWith("create:crushed_raw_materials/");
    }
}
