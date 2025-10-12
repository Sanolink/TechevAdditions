package com.sanolink.techev_additions.mixin.balancedflight;

import com.vice.balancedflight.content.flightAnchor.FlightController;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.UUID;

@Mixin(FlightController.class)
public class FlightControllerMixin {

    @Unique
    private static final HashMap<UUID, Boolean> techev$balancedFlightGrantedFlight = new HashMap<>();

    @Inject(
            method = "startFlying",
            at = @At("HEAD"),
            remap = false
    )
    private static void markFlightAsGrantedByBalancedFlight(Player player, CallbackInfo ci) {
        if (!player.isCreative() && !player.isSpectator()) {
            techev$balancedFlightGrantedFlight.put(player.getUUID(), true);
        }
    }

    @Inject(
            method = "stopFlying",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void preventStopFlyingIfNotGrantedByUs(Player player, CallbackInfo ci) {
        if (player.isCreative() || player.isSpectator()) {
            ci.cancel();
            return;
        }

        UUID playerId = player.getUUID();
        Boolean weGrantedFlight = techev$balancedFlightGrantedFlight.get(playerId);

        if (weGrantedFlight == null || !weGrantedFlight) {
            ci.cancel();
            return;
        }

        techev$balancedFlightGrantedFlight.put(playerId, false);
    }

    @Inject(
            method = "tick",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void skipTickIfPlayerHasFlightFromOtherSource(Player player, CallbackInfo ci) {
        if (player.isCreative() || player.isSpectator()) {
            ci.cancel();
            return;
        }

        UUID playerId = player.getUUID();
        Boolean weGrantedFlight = techev$balancedFlightGrantedFlight.getOrDefault(playerId, false);

        if (player.getAbilities().mayfly && !weGrantedFlight) {
            ci.cancel();
        }
    }
}