package com.sanolink.techev_additions.client.integration;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.recipes.create.TechevFanRecipe;
import com.simibubi.create.compat.jei.category.ProcessingViaFanCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedKinetics;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TechevFanCategory extends ProcessingViaFanCategory.MultiOutput<TechevFanRecipe> {

    public static final RecipeType<TechevFanRecipe> TYPE = RecipeType.create(TechevAdditions.MOD_ID, "techev_fan", TechevFanRecipe.class);

    private TechevFanRecipe currentRecipe;

    public TechevFanCategory(Info<TechevFanRecipe> info) {
        super(info);
    }

    @NotNull
    @Override
    public RecipeType<TechevFanRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public void draw(TechevFanRecipe recipe, IRecipeSlotsView slots, PoseStack matrixStack, double mouseX, double mouseY) {
        this.currentRecipe = recipe;
        super.draw(recipe, slots, matrixStack, mouseX, mouseY);
        this.currentRecipe = null;
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(TechevFanRecipe recipe, IRecipeSlotsView view, double mouseX, double mouseY) {
        int X = 74;
        int Y = 16;
        int W = 32;
        int H = 32;

        if (mouseX >= X && mouseX <= X + W &&
                mouseY >= Y && mouseY <= Y + H) {

            TechevFanRecipe.Catalyst cat = recipe.getFirstCatalyst();
            if (cat == null) return Collections.emptyList();

            if (cat.block() != null) {
                Block b = ForgeRegistries.BLOCKS.getValue(cat.block());
                if (b != null)
                    return Collections.singletonList(Component.literal("Catalyst: ").append(b.getName()));
            } else if (cat.fluid() != null) {
                Fluid f = ForgeRegistries.FLUIDS.getValue(cat.fluid());
                if (f != null)
                    return Collections.singletonList(Component.literal("Catalyst: ").append(f.getFluidType().getDescription()));
            }
        }

        return Collections.emptyList();
    }


    @Override
    protected void renderAttachedBlock(@NotNull PoseStack matrixStack) {
        if (currentRecipe == null || currentRecipe.getFirstCatalyst() == null)
            return;

        var catalyst = currentRecipe.getFirstCatalyst();

        if (catalyst.fluid() != null) {
            var fluid = ForgeRegistries.FLUIDS.getValue(catalyst.fluid());
            if (fluid != null) {
                GuiGameElement.of(fluid)
                        .scale(SCALE)
                        .atLocal(0, 0, 2)
                        .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                        .render(matrixStack);
                return;
            }
        }

        if (catalyst.block() != null) {
            var block = ForgeRegistries.BLOCKS.getValue(catalyst.block());
            if (block != null) {
                GuiGameElement.of(block.defaultBlockState())
                        .scale(SCALE)
                        .atLocal(0, 0, 2)
                        .lighting(AnimatedKinetics.DEFAULT_LIGHTING)
                        .render(matrixStack);
            }
        }
    }
}
