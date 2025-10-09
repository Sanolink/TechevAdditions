package com.sanolink.techev_additions.recipes;

import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.TechevBlocks;
import com.sanolink.techev_additions.client.integration.DarkElvenTradeRecipeCategory;
import com.sanolink.techev_additions.client.integration.TechevFanCategory;
import com.sanolink.techev_additions.recipes.create.TechevCreateRecipeTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.DoubleItemIcon;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@JeiPlugin
public class JEITechevAdditionsPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TechevAdditions.MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new DarkElvenTradeRecipeCategory(guiHelper));
        registration.addRecipeCategories(new TechevFanCategory(
                new TechevFanCategory.Info<>(
                        TechevFanCategory.TYPE,
                        net.minecraft.network.chat.Component.literal("Techev Fan Processing"),
                        guiHelper.createBlankDrawable(178, 72),
                        new DoubleItemIcon(() -> new ItemStack(AllItems.PROPELLER.get()), () -> new ItemStack(AllItems.BRASS_HAND.get())),
                        null,
                        null
                )
            )
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(DarkElvenTradeRecipeCategory.TYPE, sortRecipes(TechevRecipeTypes.DARK_ELVEN_TRADE_TYPE.get(), BY_ID));
        registration.addRecipes(TechevFanCategory.TYPE, sortRecipes(TechevCreateRecipeTypes.TECHEV_FAN.getType(), BY_ID));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TechevBlocks.SVARTALFPORTAL.get().asItem()),
                DarkElvenTradeRecipeCategory.TYPE);
        registration.addRecipeCatalyst(new ItemStack(AllBlocks.ENCASED_FAN.get().asItem()),
                TechevFanCategory.TYPE);
    }

    private static <T extends Recipe<C>, C extends Container> List<T> sortRecipes(RecipeType<T> type, Comparator<? super T> comparator) {
        @SuppressWarnings("unchecked")
        Collection<T> recipes = (Collection<T>) TechevRecipeTypes.getRecipes(Minecraft.getInstance().level, type).values();
        List<T> list = new ArrayList<>(recipes);
        list.sort(comparator);
        return list;
    }
    private static final Comparator<Recipe<?>> BY_ID = Comparator.comparing(Recipe::getId);
}