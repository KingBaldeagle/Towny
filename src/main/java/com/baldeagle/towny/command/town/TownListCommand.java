package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.town.Town;

import java.util.Comparator;
import java.util.stream.Collectors;

public final class TownListCommand implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.list";
    }

    @Override
    public int execute(TownyCommandContext context) {
        String townNames = context.universe()
            .getTowns()
            .stream()
            .map(Town::getName)
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(", "));

        if (townNames.isBlank()) {
            return context.success("No towns registered.");
        }

        return context.success("Towns: " + townNames);
    }
}
