package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class TownNationLeaveCommand implements SubCommand {
    public String getName() { return "nation_leave"; }
    public String getPermission() { return "towny.command.town.nation.leave"; }

    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can leave a nation.");

        Town town = actor.getTown();
        Nation nation = town.getNation();
        if (nation == null) return context.fail("Town does not belong to a nation.");
        if (!context.universe().removeTownFromNation(town, nation)) return context.fail("Unable to leave nation.");
        return context.success("Town left nation " + nation.getName());
    }
}
