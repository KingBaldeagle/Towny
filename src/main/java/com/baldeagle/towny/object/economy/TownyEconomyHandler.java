package com.baldeagle.towny.object.economy;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.economy.provider.EconomyProvider;
import com.baldeagle.towny.object.economy.provider.ClosedEconomyProvider;
import com.baldeagle.towny.object.economy.provider.LightmansCurrencyProvider;

import java.util.Objects;
import java.util.UUID;

/**
 * Central access point for Towny economy operations.
 */
public final class TownyEconomyHandler {
    private static volatile EconomyProvider provider = new LightmansCurrencyProvider();

    private TownyEconomyHandler() {}

    public static EconomyProvider provider() {
        return provider;
    }

    public static void setProvider(EconomyProvider newProvider) {
        provider = Objects.requireNonNull(newProvider, "newProvider");
    }

    public static void initializeFromConfig() {
        String configuredProvider = Config.ECONOMY_PROVIDER.get();
        if (configuredProvider == null) {
            configuredProvider = "";
        }

        switch (configuredProvider.trim().toLowerCase()) {
            case "lightmans_currency" -> setProvider(new LightmansCurrencyProvider());
            case "closed" -> setProvider(new ClosedEconomyProvider());
            default -> {
                Towny.LOGGER.warn("Unknown economy provider '{}' - using closed economy.", configuredProvider);
                setProvider(new ClosedEconomyProvider());
            }
        }
    }

    public static String accountIdForPlayer(UUID playerUuid) {
        return "player:" + playerUuid;
    }

    public static String accountIdForTown(UUID townUuid) {
        return "town:" + townUuid;
    }

    public static String accountIdForNation(UUID nationUuid) {
        return "nation:" + nationUuid;
    }
}
