package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.world.WorldCoord;

import java.util.Optional;

public final class TownHereCommand implements SubCommand {
    @Override
    public String getName() {
        return "here";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.here";
    }

    @Override
    public int execute(TownyCommandContext context) {
        WorldCoord worldCoord = context.getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return context.fail("This command can only be used by a player.");
        }

        Optional<TownBlock> townBlock = context.universe().getTownBlock(worldCoord);
        if (townBlock.isEmpty()) {
            return context.success("Wilderness at " + worldCoord.worldName() + " " + worldCoord.coord());
        }

        return context.success("You are in town " + townBlock.get().getTown().getName() + " at " + worldCoord.coord());
    }
}
