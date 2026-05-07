package com.baldeagle.towny.listener;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@EventBusSubscriber(modid = Towny.MODID)
public final class TownyHudTickListener {
    private static int tickCounter = 0;
    private static final Map<UUID, UUID> LAST_AREA_BY_PLAYER = new ConcurrentHashMap<>();
    private static final UUID WILDERNESS_AREA_ID = new UUID(0L, 0L);
    private TownyHudTickListener() {}

    public static void clearPlayerArea(ServerPlayer player) {
        LAST_AREA_BY_PLAYER.remove(player.getUUID());
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!Config.HUD_ENABLED.get()) return;
        tickCounter++;
        if (tickCounter < Math.max(1, Config.HUD_INTERVAL_TICKS.get())) return;
        tickCounter = 0;
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            var coord = TownyProtectionService.toWorldCoord(player.level(), player.blockPosition());
            Town areaTown = TownyUniverse.getInstance().getTownBlock(coord).map(tb -> tb.getTown()).orElse(null);
            UUID currentAreaId = areaTown == null ? WILDERNESS_AREA_ID : areaTown.getUUID();
            UUID previousAreaId = LAST_AREA_BY_PLAYER.put(player.getUUID(), currentAreaId);
            if (currentAreaId.equals(previousAreaId)) {
                continue;
            }

            String areaName = areaTown == null ? "Wilderness" : areaTown.getName();
            player.displayClientMessage(Component.literal("[Towny] " + areaName), true);
        }
    }
}
