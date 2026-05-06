package com.baldeagle.towny.command.town;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import com.baldeagle.towny.object.resident.Resident;
import net.minecraft.server.level.ServerPlayer;

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
        ServerPlayer player = context.getPlayerOrNull();
        if (player == null) {
            return context.fail("This command can only be used by a player.");
        }

        if (context.getTownBlockAtPlayerOrEmpty().isEmpty()) {
            return context.fail("You must stand on a claimed TownBlock to create a town.");
        }

        String townName = context.getString("name");
        Resident resident = context.universe().registerResident(
            player.getUUID(),
            player.getName().getString()
        );

        long townCreationCost = Config.PRICE_NEW_TOWN_IN_COPPER.get();
        String playerAccountId = TownyEconomyHandler.accountIdForPlayer(player.getUUID());
        if (!TownyEconomyHandler.provider().withdraw(playerAccountId, townCreationCost)) {
            return context.fail(
                "Unable to create town: insufficient funds. Required "
                    + TownyEconomyHandler.provider().format(townCreationCost)
            );
        }

        try {
            context.universe().createTown(townName, resident);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            TownyEconomyHandler.provider().deposit(playerAccountId, townCreationCost);
            return context.fail("Unable to create town: " + ex.getMessage());
        }

        return context.success(
            "Town created: " + townName + " (cost " + TownyEconomyHandler.provider().format(townCreationCost) + ")"
        );
    }
}
