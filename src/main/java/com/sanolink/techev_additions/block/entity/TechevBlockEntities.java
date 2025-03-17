package com.sanolink.techev_additions.block.entity;

import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.block.TechevBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vazkii.botania.common.block.block_entity.AlfheimPortalBlockEntity;
import vazkii.botania.common.lib.LibBlockNames;

import static vazkii.botania.common.block.BotaniaBlocks.alfPortal;
import static vazkii.botania.common.lib.ResourceLocationHelper.prefix;

public class TechevBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TechevAdditions.MOD_ID);

    public static final RegistryObject<BlockEntityType<VillagerBellBlockEntity>> VILLAGER_BELL_BE =
            BLOCK_ENTITIES.register("villager_bell_be", () ->
                    BlockEntityType.Builder.of(VillagerBellBlockEntity::new,
                            TechevBlocks.VILLAGER_BELL.get()).build(null));

    public static final RegistryObject<BlockEntityType<SvartalfheimPortalBlockEntity>> SVARTALFPORTAL_BE =
            BLOCK_ENTITIES.register("svartalfheim_portal_be", () ->
                    BlockEntityType.Builder.of(SvartalfheimPortalBlockEntity::new, TechevBlocks.SVARTALFPORTAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
