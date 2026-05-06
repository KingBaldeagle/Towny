package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;

import java.util.Optional;

public final class TownRankCommand implements SubCommand {
    public String getName() { return "rank"; }
    public String getPermission() { return "towny.command.town.rank"; }

    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can set ranks.");

        Optional<Resident> targetOpt = context.getResident("name");
        if (targetOpt.isEmpty()) return context.fail("Resident not found: " + context.getString("name"));
        Resident target = targetOpt.get();
        if (target.getTown() != actor.getTown()) return context.fail("Resident is not in your town.");

        String rank = context.getString("rank");
        target.addTownRank(rank);
        return context.success("Assigned rank '" + rank + "' to " + target.getName());
    }
}
