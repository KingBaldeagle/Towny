package com.baldeagle.towny.listener;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = Towny.MODID)
public final class TownyHudTickListener {
    private static int tickCounter = 0;
    private TownyHudTickListener() {}

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (!Config.HUD_ENABLED.get()) return;
        tickCounter++;
        if (tickCounter < Math.max(1, Config.HUD_INTERVAL_TICKS.get())) return;
        tickCounter = 0;
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            var coord = TownyProtectionService.toWorldCoord(player.level(), player.blockPosition());
            String area = TownyUniverse.getInstance().getTownBlock(coord).map(tb -> tb.getTown().getName()).orElse("Wilderness");
            player.displayClientMessage(Component.literal("[Towny] " + area + " @ " + coord.coord().x() + "," + coord.coord().z()), true);
        }
    }
}
