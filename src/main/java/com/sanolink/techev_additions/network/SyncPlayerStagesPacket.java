package com.sanolink.techev_additions.network;

import com.sanolink.techev_additions.client.ClientProgressionHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;


public record SyncPlayerStagesPacket(Set<String> stages) {

    public static void encode(SyncPlayerStagesPacket packet, FriendlyByteBuf buffer) {
        buffer.writeCollection(packet.stages(), FriendlyByteBuf::writeUtf);
    }

    public static SyncPlayerStagesPacket decode(FriendlyByteBuf buffer) {
        return new SyncPlayerStagesPacket(buffer.readCollection(HashSet::new, FriendlyByteBuf::readUtf));
    }

    public static void handle(SyncPlayerStagesPacket packet, Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context ctx = context.get();

        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientProgressionHandler.applyStages(packet.stages())));

        ctx.setPacketHandled(true);
    }
}
