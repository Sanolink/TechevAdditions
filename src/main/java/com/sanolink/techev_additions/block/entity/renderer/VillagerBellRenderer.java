package com.sanolink.techev_additions.block.entity.renderer;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sanolink.techev_additions.block.entity.VillagerBellBlockEntity;
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
    public static final Material BELL_RESOURCE_LOCATION;
    private static final String BELL_BODY = "bell_body";
    private final ModelPart bellBody;

    public VillagerBellRenderer(BlockEntityRendererProvider.Context pContext) {
        ModelPart $$1 = pContext.bakeLayer(ModelLayers.BELL);
        this.bellBody = $$1.getChild("bell_body");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        PartDefinition $$2 = $$1.addOrReplaceChild("bell_body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -6.0F, -3.0F, 6.0F, 7.0F, 6.0F), PartPose.offset(8.0F, 12.0F, 8.0F));
        $$2.addOrReplaceChild("bell_base", CubeListBuilder.create().texOffs(0, 13).addBox(4.0F, 4.0F, 4.0F, 8.0F, 2.0F, 8.0F), PartPose.offset(-8.0F, -12.0F, -8.0F));
        return LayerDefinition.create($$0, 32, 32);
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

        this.bellBody.xRot = $$7;
        this.bellBody.zRot = $$8;
        VertexConsumer $$10 = BELL_RESOURCE_LOCATION.buffer(pBufferSource, RenderType::entitySolid);
        this.bellBody.render(pPoseStack, $$10, pPackedLight, pPackedOverlay);
    }

    static {
        BELL_RESOURCE_LOCATION = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("entity/bell/bell_body"));
    }
}