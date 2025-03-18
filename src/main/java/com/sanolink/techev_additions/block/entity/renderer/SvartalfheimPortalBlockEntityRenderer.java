package com.sanolink.techev_additions.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.entity.SvartalfheimPortalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;
import vazkii.botania.client.core.handler.ClientTickHandler;

public class SvartalfheimPortalBlockEntityRenderer implements BlockEntityRenderer<SvartalfheimPortalBlockEntity> {

    public SvartalfheimPortalBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {}

    @Override
    public void render(@NotNull SvartalfheimPortalBlockEntity portal, float f, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
        AlfheimPortalState state = portal.getBlockState().getValue(BotaniaStateProperties.ALFPORTAL_STATE);
        if (state == AlfheimPortalState.OFF) {
            return;
        }

        float alpha = (float) Math.min(1F, (Math.sin((ClientTickHandler.ticksInGame + f) / 8D) + 1D) / 5D + 0.8D) * (Math.min(60, portal.ticksOpen) / 60F) * 0.8F;

        ms.pushPose();
        if (state == AlfheimPortalState.ON_X) {
            ms.translate(0.75, 1, 2);
            ms.mulPose(Vector3f.YP.rotationDegrees(90));
        } else {
            ms.translate(-1, 1, 0.75);
        }
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(new ResourceLocation(TechevAdditions.MOD_ID, "block/svartalfheim_portal_swirl"));
        renderIcon(ms, buffers, sprite, 0, 0, 3, 3, alpha, overlay);
        ms.popPose();

        ms.pushPose();
        if (state == AlfheimPortalState.ON_X) {
            ms.translate(0.25, 1, -1);
            ms.mulPose(Vector3f.YP.rotationDegrees(90));
        } else {
            ms.translate(2, 1, 0.25);
        }
        ms.mulPose(Vector3f.YP.rotationDegrees(180));
        renderIcon(ms, buffers, sprite, 0, 0, 3, 3, alpha, overlay);
        ms.popPose();
    }

    public void renderIcon(PoseStack ms, MultiBufferSource buffers, TextureAtlasSprite icon, int x, int y, int width, int height, float alpha, int overlay) {
        VertexConsumer buffer = buffers.getBuffer(Sheets.translucentItemSheet());
        Matrix4f model = ms.last().pose();
        Matrix3f normal = ms.last().normal();
        buffer.vertex(model, x, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x + width, y + height, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV1()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x + width, y, 0).color(1, 1, 1, alpha).uv(icon.getU1(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
        buffer.vertex(model, x, y, 0).color(1, 1, 1, alpha).uv(icon.getU0(), icon.getV0()).overlayCoords(overlay).uv2(0xF000F0).normal(normal, 1, 0, 0).endVertex();
    }

}
