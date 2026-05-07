package com.baldeagle.towny.command.town;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import com.baldeagle.towny.object.economy.provider.LightmansCurrencyProvider;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.world.WorldCoord;
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

        WorldCoord worldCoord = context.getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return context.fail("Unable to resolve your world coordinate.");
        }
        if (context.getTownBlockAtPlayerOrEmpty().isPresent()) {
            return context.fail("You must stand on an unclaimed TownBlock to create a town.");
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

        final Town town;
        try {
            town = context.universe().createTown(townName, resident);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            TownyEconomyHandler.provider().deposit(playerAccountId, townCreationCost);
            return context.fail("Unable to create town: " + ex.getMessage());
        }

        try {
            context.universe().claimTownBlock(town, worldCoord);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            context.universe().disbandTown(town);
            TownyEconomyHandler.provider().deposit(playerAccountId, townCreationCost);
            return context.fail("Unable to create town: " + ex.getMessage());
        }

        String townAccountId = TownyEconomyHandler.accountIdForTown(town.getName());
        if (TownyEconomyHandler.provider() instanceof LightmansCurrencyProvider lcProvider) {
            lcProvider.ensureTeamAccount(townAccountId, player, town.getName());
        }
        TownyEconomyHandler.provider().deposit(townAccountId, townCreationCost);

        return context.success(
            "Town created: " + townName + " (cost " + TownyEconomyHandler.provider().format(townCreationCost) + ")"
        );
    }
}
