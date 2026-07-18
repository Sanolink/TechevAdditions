package com.sanolink.techev_additions;


import com.mojang.logging.LogUtils;
import com.sanolink.techev_additions.block.entity.TechevBlockEntities;
import com.sanolink.techev_additions.item.TechevItems;

import com.sanolink.techev_additions.block.TechevBlocks;
import com.sanolink.techev_additions.command.TechEvCommands;
import com.sanolink.techev_additions.event.LootrKeyHandler;
import com.sanolink.techev_additions.event.ProgressionSyncHandler;
import com.sanolink.techev_additions.network.TechEvNetwork;
import com.sanolink.techev_additions.recipes.TechevRecipeTypes;
import com.sanolink.techev_additions.recipes.create.TechevCreateRecipeTypes;
import com.sanolink.techev_additions.recipes.create.TechevFanProcessingTypes;
import com.sanolink.techev_additions.sound.TechevSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;


@Mod(TechevAdditions.MOD_ID)
public class TechevAdditions
{
    
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "techev_additions";

    public static final String TECHEV_KEY_SLOT = "techev_key";

    public TechevAdditions(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        TechevSounds.register(modEventBus);
        TechevItems.register(modEventBus);
        TechevBlocks.register(modEventBus);
        TechevBlockEntities.register(modEventBus);
        TechevCreateRecipeTypes.values();
        TechevRecipeTypes.register(modEventBus);

        TechevFanProcessingTypes.register();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::enqueueIMC);

        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.SERVER, TechevAdditionsConfig.SERVER_CONFIG);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TechEvNetwork.register();

            MinecraftForge.EVENT_BUS.register(new ProgressionSyncHandler());
            MinecraftForge.EVENT_BUS.register(new TechEvCommands());

            if (ModList.get().isLoaded("lootr")) {
                MinecraftForge.EVENT_BUS.register(new LootrKeyHandler());
            }
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("curios")) {
            InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE,
                    () -> new SlotTypeMessage.Builder(TECHEV_KEY_SLOT)
                            .icon(ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "slot/empty_techev_key_slot"))
                            .size(1)
                            .build());
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }

}
