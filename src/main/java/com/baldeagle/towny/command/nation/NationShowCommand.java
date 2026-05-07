package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;

import java.util.Optional;

public final class NationShowCommand implements SubCommand {
    @Override
    public String getName() {
        return "show";
    }

    @Override
    public String getPermission() {
        return "towny.command.nation.show";
    }

    @Override
    public int execute(TownyCommandContext context) {
        Optional<Nation> nation = context.universe().getNation(context.getString("name"));
        if (nation.isEmpty()) {
            return context.fail("Nation not found: " + context.getString("name"));
        }

        Nation found = nation.get();
        String capitalName = found.getCapital() != null ? found.getCapital().getName() : "None";
        return context.success("Nation " + found.getName() + " | capital=" + capitalName
            + " | towns=" + found.getTowns().size());
    }
}
