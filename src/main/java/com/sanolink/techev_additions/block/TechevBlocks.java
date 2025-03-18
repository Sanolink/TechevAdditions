package com.sanolink.techev_additions.block;

import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.item.TechevCreativeModeTab;
import com.sanolink.techev_additions.item.TechevItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.api.state.BotaniaStateProperties;
import vazkii.botania.api.state.enums.AlfheimPortalState;

import java.util.function.Supplier;

public class TechevBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TechevAdditions.MOD_ID);

    public static final RegistryObject<Block> RAW_AURICARGENTUM_BLOCK = registerBlock("raw_auricargentum_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.TUFF).strength(3f)), TechevCreativeModeTab.TECHEV_TAB);
    public static final RegistryObject<Block> RAW_CUPERZINATE_BLOCK = registerBlock("raw_cuperzinate_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.TUFF).strength(3f)), TechevCreativeModeTab.TECHEV_TAB);
    public static final RegistryObject<Block> RAW_LEADOSNITE_BLOCK = registerBlock("raw_leadosnite_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.TUFF).strength(3f)), TechevCreativeModeTab.TECHEV_TAB);
    public static final RegistryObject<Block> RAW_URATINUMAL_BLOCK = registerBlock("raw_uratinumal_block",
            () -> new Block(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.TUFF).strength(3f)), TechevCreativeModeTab.TECHEV_TAB);
    public static final RegistryObject<Block> VILLAGER_BELL = registerBlock("villager_bell",
            () -> new VillagerBellBlock(BlockBehaviour.Properties.copy(Blocks.BELL)), TechevCreativeModeTab.TECHEV_TAB);

    public static final RegistryObject<Block> SVARTALFPORTAL = registerEpicBlock("svartalfheim_portal",
            () -> new SvartalfheimPortalBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(10).sound(SoundType.WOOD)
                    .lightLevel(s -> s.getValue(BotaniaStateProperties.ALFPORTAL_STATE) != AlfheimPortalState.OFF ? 15 : 0)), TechevCreativeModeTab.TECHEV_TAB);

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<T> registerEpicBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerEpicBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends  Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return TechevItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    private static <T extends  Block> RegistryObject<Item> registerEpicBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab) {
        return TechevItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab).rarity(Rarity.EPIC)));
    }

    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}
