package com.baldeagle.towny.object.economy.provider;

import java.util.Map;

/**
 * Economy abstraction for NeoTowny.
 * <p>
 * All values are represented in copper units to avoid floating-point precision
 * issues and to match the Lightman's Currency denomination model documented in
 * dev-docs.
 */
public interface EconomyProvider {
    long getBalance(String accountId);

    default boolean has(String accountId, long copperAmount) {
        if (copperAmount < 0) {
            return false;
        }
        return getBalance(accountId) >= copperAmount;
    }

    boolean withdraw(String accountId, long copperAmount);

    void deposit(String accountId, long copperAmount);

    default boolean transfer(String fromAccountId, String toAccountId, long copperAmount) {
        if (!withdraw(fromAccountId, copperAmount)) {
            return false;
        }
        deposit(toAccountId, copperAmount);
        return true;
    }

    String format(long copperAmount);

    Map<LightmansCurrencyProvider.Denomination, Long> breakdown(long copperAmount);
}
