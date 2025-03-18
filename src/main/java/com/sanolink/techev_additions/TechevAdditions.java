package com.sanolink.techev_additions;


import com.mojang.logging.LogUtils;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import com.sanolink.techev_additions.item.TechevItems;

import com.sanolink.techev_additions.block.TechevBlocks;
import com.sanolink.techev_additions.recipes.TechevRecipeTypes;
import com.sanolink.techev_additions.sound.TechevSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


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
        TechevRecipeTypes.register(modEventBus);
        
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
