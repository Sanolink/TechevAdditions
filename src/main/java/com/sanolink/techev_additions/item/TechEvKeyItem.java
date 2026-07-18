package com.sanolink.techev_additions.item;

import com.sanolink.techev_additions.client.ClientProgressionHandler;
import com.sanolink.techev_additions.progression.TechEvProgression;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;

public class TechEvKeyItem extends Item {

    public TechEvKeyItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (TechEvProgression.hasNoTreePunchingBypass(player)) {
            if (!level.isClientSide()) {
                player.displayClientMessage(
                        Component.translatable("message.techev_additions.key_already_used").withStyle(ChatFormatting.GRAY),
                        true);
            }
            return InteractionResultHolder.pass(stack);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            TechEvProgression.grantStage(serverPlayer, TechEvProgression.NO_TREE_PUNCHING_BYPASS);

            serverPlayer.sendSystemMessage(
                    Component.translatable("message.techev_additions.key_used").withStyle(ChatFormatting.GREEN));

            level.playSound(null, serverPlayer.blockPosition(), SoundEvents.PLAYER_LEVELUP,
                    SoundSource.PLAYERS, 0.7F, 1.2F);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        if (alreadyUsed()) {
            tooltip.add(Component.translatable("tooltip.techev_additions.techev_key.unlocked")
                    .withStyle(ChatFormatting.GREEN));
        } else {
            tooltip.add(Component.translatable("tooltip.techev_additions.techev_key.action")
                    .withStyle(ChatFormatting.GOLD));
            tooltip.add(Component.translatable("tooltip.techev_additions.techev_key.explain1")
                    .withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.translatable("tooltip.techev_additions.techev_key.once")
                    .withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(Component.translatable("tooltip.techev_additions.techev_key.keep")
                .withStyle(ChatFormatting.AQUA));
    }

    private static boolean alreadyUsed() {
        return Boolean.TRUE.equals(DistExecutor.unsafeCallWhenOn(Dist.CLIENT,
                () -> () -> ClientProgressionHandler.localPlayerHasStage(TechEvProgression.NO_TREE_PUNCHING_BYPASS)));
    }
}
