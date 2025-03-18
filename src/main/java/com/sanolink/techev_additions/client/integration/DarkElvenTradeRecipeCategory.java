package com.sanolink.techev_additions.client.integration;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.TechevBlocks;
import com.sanolink.techev_additions.recipes.DarkElvenTradeRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.common.lib.ResourceLocationHelper;

public class DarkElvenTradeRecipeCategory implements IRecipeCategory<DarkElvenTradeRecipe> {

    public static final RecipeType<DarkElvenTradeRecipe> TYPE = RecipeType.create(TechevAdditions.MOD_ID, "dark_elven_trade", DarkElvenTradeRecipe.class);
    private final Component localizedName;
    private final IDrawable background;
    private final IDrawable overlay;
    private final IDrawable icon;

    public DarkElvenTradeRecipeCategory(IGuiHelper guiHelper) {
        System.out.println("TEST");
        System.out.println(Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(new ResourceLocation(TechevAdditions.MOD_ID, "block/svartalfheim_portal_swirl")));
        System.out.println("TEST2");
        ResourceLocation testLoc = new ResourceLocation(TechevAdditions.MOD_ID, "block/svartalfheim_portal_swirl");
        System.out.println("Testing ResourceLocation: " + testLoc);
        System.out.println("FINTEST");

        localizedName = Component.translatable("techev_additions.nei.darkelvenTrade");
        background = guiHelper.createBlankDrawable(145, 95);
        overlay = guiHelper.createDrawable(new ResourceLocation(TechevAdditions.MOD_ID, "textures/gui/dark_elven_trade_overlay.png"), 0, 15, 140, 90);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(TechevBlocks.SVARTALFPORTAL.get()));
    }

    @NotNull
    @Override
    public RecipeType<DarkElvenTradeRecipe> getRecipeType() {
        return TYPE;
    }

    @NotNull
    @Override
    public Component getTitle() {
        return localizedName;
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(@NotNull DarkElvenTradeRecipe recipe, @NotNull IRecipeSlotsView slotsView, @NotNull PoseStack matrices, double mouseX, double mouseY) {
        RenderSystem.enableBlend();
        overlay.draw(matrices, 0, 4);
        RenderSystem.disableBlend();

        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(TechevAdditions.MOD_ID, "block/svartalfheim_portal_swirl"));
        MultiBufferSource.BufferSource immediate = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer v = immediate.getBuffer(RenderType.solid());
        int startX = 22;
        int startY = 25;
        int stopX = 70;
        int stopY = 73;
        Matrix4f mat = matrices.last().pose();
        Matrix3f n = matrices.last().normal();
        v.vertex(mat, startX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, startX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU0(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, stopX, stopY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV1()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        v.vertex(mat, stopX, startY, 0).color(1f, 1f, 1f, 1f).uv(sprite.getU1(), sprite.getV0()).uv2(0xF000F0).normal(n, 1, 0, 0).endVertex();
        immediate.endBatch();

    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull DarkElvenTradeRecipe recipe, @NotNull IFocusGroup focusGroup) {
        int posX = 42;
        for (var ingr : recipe.getIngredients()) {
            builder.addSlot(RecipeIngredientRole.INPUT, posX, 0)
                    .addIngredients(ingr);
            posX += 18;
        }

        int outIdx = 0;
        for (var stack : recipe.getOutputs()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 93 + outIdx % 2 * 20, 41 + outIdx / 2 * 20)
                    .addItemStack(stack);
            outIdx++;
        }
    }
}