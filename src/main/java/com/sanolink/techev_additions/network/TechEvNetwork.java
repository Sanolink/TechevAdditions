package com.sanolink.techev_additions.network;

import com.sanolink.techev_additions.TechevAdditions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class TechEvNetwork {

    private static final String VERSION = "1";

    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(TechevAdditions.MOD_ID, "main"))
            .networkProtocolVersion(() -> VERSION)
            .clientAcceptedVersions(VERSION::equals)
            .serverAcceptedVersions(VERSION::equals)
            .simpleChannel();

    private TechEvNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(0, SyncPlayerStagesPacket.class,
                SyncPlayerStagesPacket::encode,
                SyncPlayerStagesPacket::decode,
                SyncPlayerStagesPacket::handle);
    }

    public static void syncStages(ServerPlayer player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncPlayerStagesPacket(player.getTags()));
    }
}
