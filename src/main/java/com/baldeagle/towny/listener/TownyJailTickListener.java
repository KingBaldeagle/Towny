package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.universe.TownyUniverse;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Towny.MODID)
public final class TownyJailTickListener {
    private TownyJailTickListener() {}

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            Resident resident = TownyUniverse.getInstance().registerResident(player.getUUID(), player.getName().getString());
            if (resident.isJailed()) {
                continue;
            }
            if (resident.getJailedUntilEpochMillis() > 0L) {
                resident.unjail();
                player.sendSystemMessage(Component.literal("Your jail sentence has ended."));
            }
        }
    }
}
