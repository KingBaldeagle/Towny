package com.baldeagle.towny.command.plot;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.world.WorldCoord;

import java.util.Optional;

public final class PlotClaimCommand implements SubCommand {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getPermission() {
        return "towny.command.plot.claim";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!resident.hasTown()) {
            return context.fail("You must be in a town to claim a plot.");
        }

        WorldCoord worldCoord = context.getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return context.fail("Unable to resolve your world coordinate.");
        }

        Optional<TownBlock> townBlock = context.universe().getTownBlock(worldCoord);
        if (townBlock.isEmpty()) {
            return context.fail("This location is not claimed by any town.");
        }

        Town town = townBlock.get().getTown();
        if (town == null || town != resident.getTown()) {
            return context.fail("You can only claim plots inside your own town.");
        }

        if (context.universe().getPlot(worldCoord).isPresent()) {
            return context.fail("A plot already exists at this location.");
        }

        String plotName = resident.getName() + "-plot";
        Plot plot = context.universe().createPlot(plotName, worldCoord, town, resident);
        return context.success("Plot claimed: " + plot.getName());
    }
}
