package com.baldeagle.towny.command.plot;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;

import java.util.Optional;

public final class PlotUnclaimCommand implements SubCommand {
    @Override
    public String getName() {
        return "unclaim";
    }

    @Override
    public String getPermission() {
        return "towny.command.plot.unclaim";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null || !resident.hasTown()) {
            return context.fail("This command can only be used by a player in a town.");
        }

        Optional<Plot> plot = context.getPlotAtPlayerOrEmpty();
        if (plot.isEmpty()) {
            return context.fail("No plot exists at this location.");
        }

        Plot targetPlot = plot.get();
        boolean isOwner = targetPlot.getOwner() != null && targetPlot.getOwner().equals(resident);
        boolean isMayor = resident.isMayor();
        if (!isOwner && !isMayor) {
            return context.fail("Only the plot owner or mayor can unclaim this plot.");
        }

        if (!context.universe().removePlot(targetPlot.getWorldCoord())) {
            return context.fail("Unable to unclaim plot.");
        }

        return context.success("Unclaimed plot at " + targetPlot.getWorldCoord().coord());
    }
}
