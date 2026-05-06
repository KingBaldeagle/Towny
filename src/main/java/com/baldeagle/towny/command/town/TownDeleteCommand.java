package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class TownDeleteCommand implements SubCommand {
    public String getName() { return "delete"; }
    public String getPermission() { return "towny.command.town.delete"; }

    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can delete the town.");

        Town town = actor.getTown();
        context.universe().disbandTown(town);
        return context.success("Town deleted: " + town.getName());
    }
}
