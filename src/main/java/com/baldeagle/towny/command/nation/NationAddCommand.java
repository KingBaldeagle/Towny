package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

import java.util.Optional;

public final class NationAddCommand implements SubCommand {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getPermission() {
        return "towny.command.nation.add";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null || !actor.hasTown() || !actor.isMayor() || !actor.getTown().hasNation()) {
            return context.fail("Only a capital-town mayor can add towns to a nation.");
        }

        Nation nation = actor.getTown().getNation();
        if (nation.getCapital() != actor.getTown()) {
            return context.fail("Only the capital town mayor can add member towns.");
        }

        Optional<Town> townOptional = context.getTown("town");
        if (townOptional.isEmpty()) {
            return context.fail("Town not found: " + context.getString("town"));
        }

        Town targetTown = townOptional.get();
        if (!context.universe().addTownToNation(targetTown, nation)) {
            return context.fail("Unable to add town to nation.");
        }

        return context.success("Added town " + targetTown.getName() + " to nation " + nation.getName());
    }
}
