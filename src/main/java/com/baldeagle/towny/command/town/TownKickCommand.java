package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

import java.util.Optional;

public final class TownKickCommand implements SubCommand {
    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.kick";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!actor.hasTown() || !actor.isMayor()) {
            return context.fail("Only the mayor can kick residents.");
        }

        Optional<Resident> targetResident = context.getResident("name");
        if (targetResident.isEmpty()) {
            return context.fail("Resident not found: " + context.getString("name"));
        }

        Resident target = targetResident.get();
        Town town = actor.getTown();
        if (target.getTown() != town) {
            return context.fail("Resident is not in your town.");
        }
        if (!context.universe().removeResidentFromTown(target, town)) {
            return context.fail("Unable to kick resident.");
        }

        return context.success("Kicked " + target.getName() + " from town " + town.getName());
    }
}
