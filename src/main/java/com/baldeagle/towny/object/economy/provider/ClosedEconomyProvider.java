package com.baldeagle.towny.object.economy.provider;

import java.util.Collections;
import java.util.Map;

/**
 * No-op economy provider used for closed-economy mode.
 */
public class ClosedEconomyProvider implements EconomyProvider {
    @Override
    public long getBalance(String accountId) {
        return 0L;
    }

    @Override
    public boolean withdraw(String accountId, long copperAmount) {
        return false;
    }

    @Override
    public void deposit(String accountId, long copperAmount) {
        // Closed economy intentionally ignores deposits.
    }

    @Override
    public String format(long copperAmount) {
        return copperAmount + " copper";
    }

    @Override
    public Map<LightmansCurrencyProvider.Denomination, Long> breakdown(long copperAmount) {
        return Collections.emptyMap();
    }
}
