package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class NationNewCommand implements SubCommand {
    @Override
    public String getName() {
        return "new";
    }

    @Override
    public String getPermission() {
        return "towny.command.nation.new";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (resident == null) {
            return context.fail("This command can only be used by a player.");
        }
        if (!resident.hasTown()) {
            return context.fail("You must belong to a town to create a nation.");
        }

        Town town = resident.getTown();
        if (!resident.isMayor()) {
            return context.fail("Only the town mayor can create a nation.");
        }
        if (town.hasNation()) {
            return context.fail("Your town already belongs to a nation.");
        }

        String nationName = context.getString("name");
        try {
            context.universe().createNation(nationName, town);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            return context.fail("Unable to create nation: " + ex.getMessage());
        }

        return context.success("Nation created: " + nationName);
    }
}
