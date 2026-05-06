package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

public final class TownSetCommand implements SubCommand {
    public String getName() { return "set"; }
    public String getPermission() { return "towny.command.town.set"; }

    public int execute(TownyCommandContext context) {
        Resident actor = context.getOrCreatePlayerResidentOrNull();
        if (actor == null) return context.fail("This command can only be used by a player.");
        if (!actor.hasTown()) return context.fail("You are not in a town.");
        if (!actor.isMayor()) return context.fail("Only the mayor can set town values.");

        Town town = actor.getTown();
        String key = context.getString("key");
        String value = context.getString("value");
        return switch (key.toLowerCase()) {
            case "tag" -> { town.setTag(value); yield context.success("Town tag updated."); }
            case "board" -> { town.setBoard(value); yield context.success("Town board updated."); }
            case "public" -> { town.setPublic(Boolean.parseBoolean(value)); yield context.success("Town public flag updated."); }
            case "open" -> { town.setOpen(Boolean.parseBoolean(value)); yield context.success("Town open flag updated."); }
            default -> context.fail("Unknown key. Supported: tag, board, public, open");
        };
    }
}
