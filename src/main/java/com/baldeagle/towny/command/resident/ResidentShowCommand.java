package com.baldeagle.towny.command.resident;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;

import java.util.Optional;

public final class ResidentShowCommand implements SubCommand {
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getPermission() {
        return "towny.command.resident.show";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Optional<Resident> resident = context.getResident("name");
        if (resident.isEmpty()) {
            return context.fail("Resident not found: " + context.getString("name"));
        }

        Resident found = resident.get();
        String townName = found.hasTown() ? found.getTown().getName() : "None";
        String mayorFlag = found.isMayor() ? "yes" : "no";
        return context.success("Resident " + found.getName() + " | town=" + townName + " | mayor=" + mayorFlag);
    }
}
