package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.nation.Nation;

import java.util.Comparator;
import java.util.stream.Collectors;

public final class NationListCommand implements SubCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPermission() {
        return "towny.command.nation.list";
    }

    @Override
    public int execute(TownyCommandContext context) {
        String nationNames = context.universe()
            .getNations()
            .stream()
            .map(Nation::getName)
            .sorted(Comparator.naturalOrder())
            .collect(Collectors.joining(", "));

        if (nationNames.isBlank()) {
            return context.success("No nations registered.");
        }

        return context.success("Nations: " + nationNames);
    }
}
