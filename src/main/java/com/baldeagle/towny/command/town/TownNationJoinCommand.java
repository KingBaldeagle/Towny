package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

import java.util.Optional;

public final class TownNationJoinCommand implements SubCommand {
    public String getName() { return "nation_join"; }
    public String getPermission() { return "towny.command.town.nation.join"; }

    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can join a nation.");

        Town town = actor.getTown();
        if (town.hasNation()) return context.fail("Town already belongs to a nation.");

        Optional<Nation> nation = context.universe().getNation(context.getString("nation"));
        if (nation.isEmpty()) return context.fail("Nation not found: " + context.getString("nation"));
        if (!context.universe().addTownToNation(town, nation.get())) return context.fail("Unable to join nation.");
        return context.success("Town joined nation " + nation.get().getName());
    }
}
