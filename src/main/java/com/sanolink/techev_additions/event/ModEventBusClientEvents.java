package com.sanolink.techev_additions.event;

import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import com.sanolink.techev_additions.block.entity.renderer.SvartalfheimPortalBlockEntityRenderer;
import com.sanolink.techev_additions.block.entity.renderer.VillagerBellRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechevAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(TechevBlockEntities.VILLAGER_BELL_BE.get(), VillagerBellRenderer::new);
        event.registerBlockEntityRenderer(TechevBlockEntities.SVARTALFPORTAL_BE.get(), SvartalfheimPortalBlockEntityRenderer::new);
    }
}
