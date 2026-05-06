package com.baldeagle.towny.listener;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.economy.TownyEconomyService;
import com.baldeagle.towny.object.universe.TownyUniverse;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

/**
 * Phase-5 daily style economy tasks (tax collection baseline).
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyEconomyTickListener {
    private static int tickCounter = 0;

    private TownyEconomyTickListener() {}

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        int interval = Math.max(20, Config.TAX_COLLECTION_INTERVAL_TICKS.get());
        tickCounter++;
        if (tickCounter < interval) {
            return;
        }
        tickCounter = 0;

        int collections = TownyEconomyService.collectDailyTownTaxes(TownyUniverse.getInstance().getTowns());
        int nationCollections = TownyEconomyService.collectNationTaxes(
            TownyUniverse.getInstance().getNations(),
            Config.NATION_TAX_PERCENT.get()
        );
        if (collections > 0) {
            Towny.LOGGER.info("Towny collected {} resident tax payment(s).", collections);
        }
        if (nationCollections > 0) {
            Towny.LOGGER.info("Towny collected {} nation contribution payment(s).", nationCollections);
        }
    }
}
