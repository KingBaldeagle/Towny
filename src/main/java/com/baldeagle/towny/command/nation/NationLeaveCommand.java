package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class NationLeaveCommand implements SubCommand {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getPermission() {
        return "towny.command.nation.leave";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null || !actor.hasTown() || !actor.isMayor() || !actor.getTown().hasNation()) {
            return context.fail("Only a town mayor can remove the town from nation membership.");
        }

        Town town = actor.getTown();
        Nation nation = town.getNation();
        if (nation.getCapital() == town) {
            return context.fail("Capital town cannot leave the nation.");
        }

        if (!context.universe().removeTownFromNation(town, nation)) {
            return context.fail("Unable to leave nation.");
        }

        return context.success("Town " + town.getName() + " left nation " + nation.getName());
    }
}
