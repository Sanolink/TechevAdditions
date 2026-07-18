package com.sanolink.techev_additions.event;

import com.sanolink.techev_additions.item.TechevItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import noobanidus.mods.lootr.api.IHasOpeners;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.api.entity.ILootCart;
import top.theillusivec4.curios.api.CuriosApi;

public class LootrKeyHandler {

    private static final boolean CURIOS_LOADED = ModList.get().isLoaded("curios");

    @SubscribeEvent
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().getBlockEntity(event.getPos()) instanceof ILootBlockEntity loot
                && shouldLock(loot, event.getEntity())) {
            deny(event);
        }
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof ILootCart cart && shouldLock(cart, event.getEntity())) {
            deny(event);
        }
    }

    private static boolean shouldLock(IHasOpeners container, Player player) {
        return !player.isCreative()
                && !container.getOpeners().contains(player.getUUID())
                && !hasKey(player);
    }

    private static void deny(PlayerInteractEvent event) {
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);

        if (!event.getLevel().isClientSide()) {
            event.getEntity().displayClientMessage(
                    Component.translatable("message.techev_additions.lootr_locked").withStyle(ChatFormatting.RED),
                    true);
        }
    }

    private static boolean hasKey(Player player) {
        Item key = TechevItems.TECHEV_KEY.get();

        Inventory inventory = player.getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
            if (inventory.getItem(slot).is(key)) {
                return true;
            }
        }

        return CURIOS_LOADED && CuriosApi.getCuriosHelper().findFirstCurio(player, key).isPresent();
    }
}
