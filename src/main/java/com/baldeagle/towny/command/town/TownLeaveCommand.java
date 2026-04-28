package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class TownLeaveCommand implements SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.leave";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!resident.hasTown()) {
            return context.fail("You are not in a town.");
        }
        if (resident.isMayor()) {
            return context.fail("Mayor cannot leave town. Set a new mayor first.");
        }

        Town town = resident.getTown();
        if (!context.universe().removeResidentFromTown(resident, town)) {
            return context.fail("Unable to leave town.");
        }
        return context.success("You left town " + town.getName());
    }
}
