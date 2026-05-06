package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownInviteRegistry;

import java.util.Optional;

public final class TownInviteCommand implements SubCommand {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.invite";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can invite residents.");

        Optional<Resident> targetResident = context.getResident("name");
        if (targetResident.isEmpty()) return context.fail("Resident not found: " + context.getString("name"));

        Resident target = targetResident.get();
        if (target.hasTown()) return context.fail("Resident already belongs to a town.");

        Town town = actor.getTown();
        TownInviteRegistry.getInstance().invite(target.getUUID(), town);
        return context.success("Invited " + target.getName() + " to town " + town.getName() + ".");
    }
}
