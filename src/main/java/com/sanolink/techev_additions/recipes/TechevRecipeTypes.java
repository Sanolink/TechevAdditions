package com.sanolink.techev_additions.recipes;

import com.sanolink.techev_additions.TechevAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.mixin.RecipeManagerAccessor;

import java.util.Map;

public class TechevRecipeTypes {

    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, TechevAdditions.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> TYPES =
            DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, TechevAdditions.MOD_ID);

    public static final RegistryObject<RecipeSerializer<DarkElvenTradeRecipe>> DARK_ELVEN_TRADE_SERIALIZER =
            SERIALIZERS.register("dark_elven_trade", DarkElvenTradeRecipe.Serializer::new);

    public static final RegistryObject<RecipeType<DarkElvenTradeRecipe>> DARK_ELVEN_TRADE_TYPE =
            TYPES.register("dark_elven_trade", () -> new RecipeType<DarkElvenTradeRecipe>() {
                @Override
                public String toString() {
                    return "dark_elven_trade";
                }
            });

    public static <C extends Container, T extends Recipe<C>> Map<ResourceLocation, Recipe<C>> getRecipes(Level world, RecipeType<T> type) {
        return ((RecipeManagerAccessor) world.getRecipeManager()).botania_getAll(type);
    }

    public static void register(IEventBus eventBus) {
        SERIALIZERS.register(eventBus);
        TYPES.register(eventBus);
    }

}
