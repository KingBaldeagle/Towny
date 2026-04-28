package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.world.WorldCoord;

public final class TownUnclaimCommand implements SubCommand {
    @Override
    public String getName() {
        return "unclaim";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.unclaim";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!resident.hasTown() || !resident.isMayor()) {
            return context.fail("Only a mayor can unclaim town blocks.");
        }

        WorldCoord worldCoord = context.getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return context.fail("Unable to resolve your world coordinate.");
        }

        Town town = resident.getTown();
        if (!context.universe().unclaimTownBlock(town, worldCoord)) {
            return context.fail("This location is not claimed by your town.");
        }

        return context.success("Unclaimed town block " + worldCoord.coord() + " from " + town.getName());
    }
}
