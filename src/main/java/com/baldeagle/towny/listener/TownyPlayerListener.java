package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Phase-4 player lifecycle listener.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyPlayerListener {
    private TownyPlayerListener() {}

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        TownyUniverse universe = TownyUniverse.getInstance();
        Resident resident = universe.registerResident(player.getUUID(), player.getName().getString());
        resident.setOnline(true);

        if (resident.hasTown()) {
            player.sendSystemMessage(Component.literal("Towny: Welcome back to " + resident.getTown().getName() + "."));
        } else {
            player.sendSystemMessage(Component.literal("Towny: You are currently in the wilderness."));
        }
    }


    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        boolean protectedArea = !TownyProtectionService.isPvpAllowedAt(player.level(), player.blockPosition());
        if (protectedArea) {
            player.sendSystemMessage(Component.literal("Towny: Entered a protected town area."));
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        boolean protectedArea = !TownyProtectionService.isPvpAllowedAt(player.level(), player.blockPosition());
        if (protectedArea) {
            player.sendSystemMessage(Component.literal("Towny: Respawned in a protected town area."));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        TownyUniverse universe = TownyUniverse.getInstance();
        universe.getResident(player.getUUID()).ifPresent(resident -> resident.setOnline(false));
        TownyHudTickListener.clearPlayerArea(player);
    }
}
