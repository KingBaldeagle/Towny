package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.town.Town;

import java.util.Optional;

public final class TownShowCommand implements SubCommand {
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.show";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Optional<Town> town = context.universe().getTown(context.getString("name"));
        if (town.isEmpty()) {
            return context.fail("Town not found: " + context.getString("name"));
        }

        Town found = town.get();
        String nationName = found.hasNation() ? found.getNation().getName() : "None";
        return context.success("Town " + found.getName() + " | mayor=" + found.getMayor().getName()
            + " | residents=" + found.getResidents().size()
            + " | nation=" + nationName
            + " | blocks=" + found.getTownBlocks().size());
    }
}
