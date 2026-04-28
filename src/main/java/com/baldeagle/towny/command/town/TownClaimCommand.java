package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.world.WorldCoord;

public final class TownClaimCommand implements SubCommand {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.claim";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!resident.hasTown() || !resident.isMayor()) {
            return context.fail("Only a mayor can claim town blocks.");
        }

        WorldCoord worldCoord = context.getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return context.fail("Unable to resolve your world coordinate.");
        }

        Town town = resident.getTown();
        try {
            context.universe().claimTownBlock(town, worldCoord);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return context.fail("Unable to claim town block: " + ex.getMessage());
        }

        return context.success("Claimed town block " + worldCoord.coord() + " for " + town.getName());
    }
}
