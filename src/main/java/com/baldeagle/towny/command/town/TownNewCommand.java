package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;

public final class TownNewCommand implements SubCommand {
    @Override
    public String getName() {
        return "new";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.new";
    }

    @Override
    public int execute(TownyCommandContext context) {
        if (context.getPlayerOrNull() == null) {
            return context.fail("This command can only be used by a player.");
        }

        String townName = context.getString("name");
        Resident resident = context.universe().registerResident(
            context.getPlayerOrNull().getUUID(),
            context.getPlayerOrNull().getName().getString()
        );

        try {
            context.universe().createTown(townName, resident);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return context.fail("Unable to create town: " + ex.getMessage());
        }

        return context.success("Town created: " + townName);
    }
}
