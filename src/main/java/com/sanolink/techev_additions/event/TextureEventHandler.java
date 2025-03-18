package com.sanolink.techev_additions.event;

import com.sanolink.techev_additions.TechevAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TextureEventHandler {
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (event.getAtlas().location().equals(InventoryMenu.BLOCK_ATLAS)) {
            event.addSprite(new ResourceLocation(TechevAdditions.MOD_ID, "block/svartalfheim_portal_swirl"));
        }
    }
}