package com.sanolink.techev_additions.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

import java.util.HashSet;
import java.util.Set;

public final class ClientProgressionHandler {

    private ClientProgressionHandler() {
    }

    public static void applyStages(Set<String> stages) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player == null) {
            return;
        }

        new HashSet<>(player.getTags()).forEach(player::removeTag);
        stages.forEach(player::addTag);
    }

    public static boolean localPlayerHasStage(String stage) {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getTags().contains(stage);
    }
}
