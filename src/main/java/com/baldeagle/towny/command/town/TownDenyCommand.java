package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.universe.TownInviteRegistry;

public final class TownDenyCommand implements SubCommand {
    @Override
    public String getName() {
        return "deny";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.deny";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) return context.fail("This command can only be used by a player.");

        if (!TownInviteRegistry.getInstance().denyInvite(resident.getUUID())) {
            return context.fail("You do not have a pending town invite.");
        }
        return context.success("Town invite denied.");
    }
}
