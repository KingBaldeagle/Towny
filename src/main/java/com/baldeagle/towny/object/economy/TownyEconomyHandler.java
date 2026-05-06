package com.baldeagle.towny.object.economy;

import com.baldeagle.towny.object.economy.provider.EconomyProvider;
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

    public static String accountIdForPlayer(UUID playerUuid) {
        return "player:" + playerUuid;
    }
}
