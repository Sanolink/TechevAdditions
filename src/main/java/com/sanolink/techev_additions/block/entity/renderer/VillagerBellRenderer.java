package com.sanolink.techev_additions.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.entity.VillagerBellBlockEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class VillagerBellRenderer implements BlockEntityRenderer<VillagerBellBlockEntity> {

    public static final ModelLayerLocation VILLAGER_BELL_LAYER =
            new ModelLayerLocation(new ResourceLocation(TechevAdditions.MOD_ID, "villager_bell"), "main");

    private static final ResourceLocation VILLAGER_BELL_TEXTURE =
            new ResourceLocation(TechevAdditions.MOD_ID, "textures/entity/villager_bell/villager_bell_body.png");

    private static final String VILLAGER_BELL_BODY = "villager_bell_body";
    private final ModelPart villager_bellBody;

    public VillagerBellRenderer(BlockEntityRendererProvider.Context pContext) {
        ModelPart modelPart = pContext.bakeLayer(VILLAGER_BELL_LAYER);
        this.villager_bellBody = modelPart.getChild(VILLAGER_BELL_BODY);
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition root = meshDefinition.getRoot();
        PartDefinition body = root.addOrReplaceChild("villager_bell_body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F), PartPose.offset(8.0F, 12.0F, 8.0F));
        body.addOrReplaceChild("villager_bell_base", CubeListBuilder.create().texOffs(0, 13).addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F), PartPose.offset(-8.0F, -12.0F, -8.0F));
        return LayerDefinition.create(meshDefinition, 32, 32);
    }

    public void render(VillagerBellBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        float $$6 = (float) pBlockEntity.ticks + pPartialTick;
        float $$7 = 0.0F;
        float $$8 = 0.0F;
        if (pBlockEntity.shaking) {
            float $$9 = Mth.sin($$6 / 3.1415927F) / (4.0F + $$6 / 3.0F);
            if (pBlockEntity.clickDirection == Direction.NORTH) {
                $$7 = -$$9;
            } else if (pBlockEntity.clickDirection == Direction.SOUTH) {
                $$7 = $$9;
            } else if (pBlockEntity.clickDirection == Direction.EAST) {
                $$8 = -$$9;
            } else if (pBlockEntity.clickDirection == Direction.WEST) {
                $$8 = $$9;
            }
        }

        this.villager_bellBody.xRot = $$7;
        this.villager_bellBody.zRot = $$8;
        VertexConsumer buffer = pBufferSource.getBuffer(RenderType.entitySolid(VILLAGER_BELL_TEXTURE));
        this.villager_bellBody.render(pPoseStack, buffer, pPackedLight, pPackedOverlay);
    }

}