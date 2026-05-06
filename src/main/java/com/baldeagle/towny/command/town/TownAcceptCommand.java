package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownInviteRegistry;

import java.util.Optional;

public final class TownAcceptCommand implements SubCommand {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.accept";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) return context.fail("This command can only be used by a player.");
        if (resident.hasTown()) return context.fail("You are already in a town.");

        Optional<Town> invite = TownInviteRegistry.getInstance().consumeInvite(resident.getUUID());
        if (invite.isEmpty()) return context.fail("You do not have a pending town invite.");

        Town town = invite.get();
        if (!context.universe().addResidentToTown(resident, town)) {
            return context.fail("Unable to join town.");
        }
        return context.success("You joined town " + town.getName() + ".");
    }
}
