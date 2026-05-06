package com.baldeagle.towny.command.nation;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import com.baldeagle.towny.object.economy.provider.LightmansCurrencyProvider;
import com.baldeagle.towny.object.nation.Nation;
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
        long nationCreationCost = Config.PRICE_NEW_NATION_IN_COPPER.get();
        String playerAccountId = TownyEconomyHandler.accountIdForPlayer(resident.getUUID());
        if (!TownyEconomyHandler.provider().withdraw(playerAccountId, nationCreationCost)) {
            return context.fail(
                "Unable to create nation: insufficient funds. Required "
                    + TownyEconomyHandler.provider().format(nationCreationCost)
            );
        }

        final Nation nation;
        try {
            nation = context.universe().createNation(nationName, town);
        } catch (IllegalStateException | IllegalArgumentException ex) {
            TownyEconomyHandler.provider().deposit(playerAccountId, nationCreationCost);
            return context.fail("Unable to create nation: " + ex.getMessage());
        }

        String nationAccountId = TownyEconomyHandler.accountIdForNation(nation.getUUID());
        if (context.getPlayerOrNull() instanceof net.minecraft.server.level.ServerPlayer player
            && TownyEconomyHandler.provider() instanceof LightmansCurrencyProvider lcProvider) {
            lcProvider.ensureTeamAccount(nationAccountId, player, "nation_" + nation.getName());
        }
        TownyEconomyHandler.provider().deposit(nationAccountId, nationCreationCost);

        return context.success(
            "Nation created: " + nationName + " (cost " + TownyEconomyHandler.provider().format(nationCreationCost) + ")"
        );
    }
}
