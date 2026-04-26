package com.baldeagle.towny.object.economy.provider;

import com.palmergames.bukkit.towny.object.Resident;

/**
 * Minimal EconomyProvider interface required by NeoTowny.
 * All amounts are expressed in copper units (long).
 */
public interface EconomyProvider {
    /**
     * Deposit amount into resident's account.
     * @return true if successful
     */
    boolean deposit(Resident resident, long amount);

    /**
     * Withdraw amount from resident's account.
     * @return true if sufficient funds and successful
     */
    boolean withdraw(Resident resident, long amount);

    /**
     * Get current balance in copper units.
     */
    long getBalance(Resident resident);

    /**
     * Transfer amount from one resident to another.
     */
    boolean transfer(Resident from, Resident to, long amount);
}
