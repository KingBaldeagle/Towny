package com.baldeagle.towny.object.resident;

import com.palmergames.bukkit.towny.object.Resident;
import com.baldeagle.towny.object.economy.provider.EconomyProvider;
import java.util.UUID;

/**
 * Simplified resident representation for NeoTowny.
 * Wraps the original Towny Resident to hold a UUID, name and a reference to the economy provider.
 */
public class Resident {
    private final UUID uuid;
    private final String name;
    private final EconomyProvider economyProvider;

    public Resident(UUID uuid, String name, EconomyProvider economyProvider) {
        this.uuid = uuid;
        this.name = name;
        this.economyProvider = economyProvider;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    // Economy delegation methods
    public boolean deposit(long amount) {
        return economyProvider.deposit(this, amount);
    }

    public boolean withdraw(long amount) {
        return economyProvider.withdraw(this, amount);
    }

    public long getBalance() {
        return economyProvider.getBalance(this);
    }
}
