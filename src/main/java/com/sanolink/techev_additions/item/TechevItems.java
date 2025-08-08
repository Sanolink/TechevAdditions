package com.sanolink.techev_additions.item;



import com.sanolink.techev_additions.TechevAdditions;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TechevItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TechevAdditions.MOD_ID);
    
    public static final RegistryObject<Item> TECHEVIUM = ITEMS.register("techevium", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> TELLURITE= ITEMS.register("tellurite", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));
    public static final RegistryObject<Item> CHROMATIC = ITEMS.register("chromatic", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EVOLIUM = ITEMS.register("evolium", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));
    public static final RegistryObject<Item> RAW_AURICARGENTUM = ITEMS.register("raw_auricargentum", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));
    public static final RegistryObject<Item> RAW_CUPERZINATE = ITEMS.register("raw_cuperzinate", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));
    public static final RegistryObject<Item> RAW_LEADOSNITE = ITEMS.register("raw_leadosnite", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));
    public static final RegistryObject<Item> RAW_URATINUMAL = ITEMS.register("raw_uratinumal", () -> new Item(new Item.Properties().tab(TechevCreativeModeTab.TECHEV_TAB)));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
