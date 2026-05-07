package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;

@EventBusSubscriber(modid = Towny.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class TownyConfigListener {
    private TownyConfigListener() {}

    @SubscribeEvent
    public static void onConfigLoaded(ModConfigEvent.Loading event) {
        if (event.getConfig().getModId().equals(Towny.MODID)) {
            TownyEconomyHandler.initializeFromConfig();
        }
    }

    @SubscribeEvent
    public static void onConfigReloaded(ModConfigEvent.Reloading event) {
        if (event.getConfig().getModId().equals(Towny.MODID)) {
            TownyEconomyHandler.initializeFromConfig();
        }
    }
}
