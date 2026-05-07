package com.baldeagle.towny.listener;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.WorldCoord;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Phase-6 timer tasks: upkeep, territory health regeneration, and plot regeneration.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyPhase6TickListener {
    private static int economyTickCounter = 0;
    private static int healthTickCounter = 0;
    private static int plotTickCounter = 0;
    private static final Map<WorldCoord, Integer> UNOWNED_PLOT_CYCLES = new HashMap<>();

    private TownyPhase6TickListener() {}

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickUpkeep();
        tickHealthRegen(event);
        tickPlotRegen();
    }

    private static void tickUpkeep() {
        int interval = Math.max(20, Config.TAX_COLLECTION_INTERVAL_TICKS.get());
        economyTickCounter++;
        if (economyTickCounter < interval) {
            return;
        }
        economyTickCounter = 0;

        long upkeepPerBlock = Math.max(0L, Config.DAILY_UPKEEP_PER_TOWN_BLOCK_COPPER.get());
        if (upkeepPerBlock <= 0L) {
            return;
        }

        for (Town town : TownyUniverse.getInstance().getTowns()) {
            long upkeep = upkeepPerBlock * town.getTownBlocks().size();
            if (upkeep <= 0L) {
                continue;
            }
            String townAccountId = TownyEconomyHandler.accountIdForTown(town.getUUID());
            boolean paid = TownyEconomyHandler.provider().withdraw(townAccountId, upkeep);
            if (!paid) {
                Towny.LOGGER.warn("Town {} failed upkeep payment of {}.", town.getName(), upkeep);
            }
        }
    }

    private static void tickHealthRegen(ServerTickEvent.Post event) {
        if (!Config.HEALTH_REGEN_ENABLED.get()) {
            return;
        }
        int interval = Math.max(1, Config.HEALTH_REGEN_INTERVAL_TICKS.get());
        healthTickCounter++;
        if (healthTickCounter < interval) {
            return;
        }
        healthTickCounter = 0;

        float healAmount = (float) Math.max(0d, Config.HEALTH_REGEN_HEARTS.get() * 2d);
        if (healAmount <= 0f) {
            return;
        }

        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            if (player.getHealth() >= player.getMaxHealth()) {
                continue;
            }
            if (!isInFriendlyTerritory(player)) {
                continue;
            }
            player.heal(healAmount);
        }
    }

    private static void tickPlotRegen() {
        if (!Config.PLOT_REGEN_ENABLED.get()) {
            UNOWNED_PLOT_CYCLES.clear();
            return;
        }
        int interval = Math.max(20, Config.PLOT_REGEN_INTERVAL_TICKS.get());
        plotTickCounter++;
        if (plotTickCounter < interval) {
            return;
        }
        plotTickCounter = 0;

        int graceCycles = Math.max(1, Config.PLOT_REGEN_GRACE_CYCLES.get());
        TownyUniverse universe = TownyUniverse.getInstance();

        for (Plot plot : new ArrayList<>(universe.getPlots())) {
            if (plot.getOwner() != null || plot.isForSale()) {
                UNOWNED_PLOT_CYCLES.remove(plot.getWorldCoord());
                continue;
            }
            int next = UNOWNED_PLOT_CYCLES.getOrDefault(plot.getWorldCoord(), 0) + 1;
            if (next >= graceCycles) {
                universe.removePlot(plot.getWorldCoord());
                UNOWNED_PLOT_CYCLES.remove(plot.getWorldCoord());
                Towny.LOGGER.debug("Phase-6 regenerated plot {} at {}.", plot.getName(), plot.getWorldCoord());
            } else {
                UNOWNED_PLOT_CYCLES.put(plot.getWorldCoord(), next);
            }
        }
    }

    private static boolean isInFriendlyTerritory(ServerPlayer player) {
        TownyUniverse universe = TownyUniverse.getInstance();
        Resident actor = universe.registerResident(player.getUUID(), player.getName().getString());
        WorldCoord here = TownyProtectionService.toWorldCoord(player.level(), player.blockPosition());
        Optional<Plot> plot = universe.getPlot(here);
        if (plot.isPresent() && plot.get().getOwner() != null) {
            return plot.get().getOwner().getUUID().equals(actor.getUUID());
        }

        Optional<Town> townOptional = universe.getTownBlock(here).map(tb -> tb.getTown());
        if (townOptional.isEmpty()) {
            return false;
        }
        Town town = townOptional.get();
        if (actor.getTown() == town) {
            return true;
        }

        Nation actorNation = actor.hasTown() ? actor.getTown().getNation() : null;
        Nation areaNation = town.getNation();
        return actorNation != null && areaNation != null && actorNation.getUUID().equals(areaNation.getUUID());
    }
}
