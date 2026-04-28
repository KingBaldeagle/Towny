package com.baldeagle.towny.command.resident;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;

public final class ResidentSelfCommand implements SubCommand {
    @Override
    public String getName() {
        return "self";
    }

    @Override
    public String getPermission() {
        return "towny.command.resident.self";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        String townName = resident.hasTown() ? resident.getTown().getName() : "None";
        String mayorFlag = resident.isMayor() ? "yes" : "no";
        return context.success("Resident " + resident.getName() + " | town=" + townName + " | mayor=" + mayorFlag);
    }
}
