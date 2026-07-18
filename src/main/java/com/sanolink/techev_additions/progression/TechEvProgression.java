package com.sanolink.techev_additions.progression;

import com.sanolink.techev_additions.network.TechEvNetwork;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;


public final class TechEvProgression {

    public static final String NO_TREE_PUNCHING_BYPASS = "ntp_unlocked";

    private TechEvProgression() {
    }

    public static boolean hasStage(Player player, String stage) {
        return player != null && player.getTags().contains(stage);
    }

    public static boolean hasNoTreePunchingBypass(Player player) {
        return hasStage(player, NO_TREE_PUNCHING_BYPASS);
    }

    public static boolean grantStage(ServerPlayer player, String stage) {
        if (!player.addTag(stage)) {
            return false;
        }

        TechEvNetwork.syncStages(player);
        return true;
    }

    public static boolean revokeStage(ServerPlayer player, String stage) {
        if (!player.removeTag(stage)) {
            return false;
        }

        TechEvNetwork.syncStages(player);
        return true;
    }
}
