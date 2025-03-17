package com.sanolink.techev_additions;


import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.logging.LogUtils;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import com.sanolink.techev_additions.item.TechevItems;

import com.sanolink.techev_additions.block.TechevBlocks;
import com.sanolink.techev_additions.sound.TechevSounds;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.model.renderable.ITextureRenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.HashMap;


@Mod(TechevAdditions.MOD_ID)
public class TechevAdditions
{
    
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "techev_additions";

    public TechevAdditions()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        TechevSounds.register(modEventBus);
        TechevItems.register(modEventBus);
        TechevBlocks.register(modEventBus);
        TechevBlockEntities.register(modEventBus);
        
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

}
