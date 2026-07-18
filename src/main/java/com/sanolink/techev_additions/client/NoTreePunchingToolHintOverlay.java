package com.sanolink.techev_additions.client;

import com.alcatrazescapee.notreepunching.util.ToolType;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sanolink.techev_additions.TechevAdditions;
import com.sanolink.techev_additions.mixin.notreepunching.HarvestBlockHandlerAccessor;
import com.sanolink.techev_additions.progression.TechEvProgression;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TechevAdditions.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class NoTreePunchingToolHintOverlay implements IGuiOverlay {

    public static final NoTreePunchingToolHintOverlay INSTANCE = new NoTreePunchingToolHintOverlay();

    private NoTreePunchingToolHintOverlay() {
    }

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("ntp_tool_hint", INSTANCE);
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int screenWidth, int screenHeight) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || minecraft.options.hideGui) {
            return;
        }
        if (!minecraft.options.keyAttack.isDown()
                || !(minecraft.hitResult instanceof BlockHitResult hit)
                || hit.getType() != HitResult.Type.BLOCK) {
            return;
        }
        if (TechEvProgression.hasNoTreePunchingBypass(player)) {
            return;
        }

        Level level = player.level;
        BlockPos pos = hit.getBlockPos();
        BlockState state = level.getBlockState(pos);

        if (state.isAir() || state.getDestroySpeed(level, pos) < 0.0F) {
            return;
        }
        if (state.getDestroyProgress(player, level, pos) > 0.0F) {
            return;
        }

        ItemStack tool = requiredTool(state);
        if (tool.isEmpty()) {
            return;
        }

        int x = screenWidth / 2 + 12;
        int y = screenHeight / 2 - 8;
        minecraft.getItemRenderer().renderGuiItem(tool, x, y);
    }

    private static final Item[] PICKAXES = {Items.WOODEN_PICKAXE, Items.STONE_PICKAXE, Items.IRON_PICKAXE, Items.DIAMOND_PICKAXE};
    private static final Item[] AXES = {Items.WOODEN_AXE, Items.STONE_AXE, Items.IRON_AXE, Items.DIAMOND_AXE};
    private static final Item[] SHOVELS = {Items.WOODEN_SHOVEL, Items.STONE_SHOVEL, Items.IRON_SHOVEL, Items.DIAMOND_SHOVEL};
    private static final Item[] HOES = {Items.WOODEN_HOE, Items.STONE_HOE, Items.IRON_HOE, Items.DIAMOND_HOE};

    private static ItemStack requiredTool(BlockState state) {
        ToolType type = HarvestBlockHandlerAccessor.getBlockToolTypes().get(state.getBlock());
        if (type == null) {
            return ItemStack.EMPTY;
        }

        int tier = tierIndex(state);
        if (type == ToolType.PICKAXE) {
            return new ItemStack(PICKAXES[tier]);
        }
        if (type == ToolType.AXE) {
            return new ItemStack(AXES[tier]);
        }
        if (type == ToolType.SHOVEL) {
            return new ItemStack(SHOVELS[tier]);
        }
        if (type == ToolType.HOE) {
            return new ItemStack(HOES[tier]);
        }
        if (type == ToolType.SHARP) {
            return new ItemStack(Items.SHEARS);
        }
        return ItemStack.EMPTY;
    }

    private static int tierIndex(BlockState state) {
        if (state.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return 3;
        }
        if (state.is(BlockTags.NEEDS_IRON_TOOL)) {
            return 2;
        }
        if (state.is(BlockTags.NEEDS_STONE_TOOL)) {
            return 1;
        }
        return 0;
    }
}
