package com.baldeagle.towny.object.economy.provider;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * LC-backed provider stub.
 * <p>
 * For now this uses in-memory balances as a safe fallback while the real LC
 * wallet/team API wiring is implemented. The exposed API already follows the
 * documented copper-unit denomination model.
 */
public class LightmansCurrencyProvider implements EconomyProvider {
    private final Map<String, Long> balancesInCopper = new ConcurrentHashMap<>();

    public enum Denomination {
        NETHERITE("netherite", 100_000L),
        DIAMOND("diamond", 10_000L),
        EMERALD("emerald", 1_000L),
        GOLD("gold", 100L),
        IRON("iron", 10L),
        COPPER("copper", 1L);

        private final String label;
        private final long copperValue;

        Denomination(String label, long copperValue) {
            this.label = label;
            this.copperValue = copperValue;
        }

        public String getLabel() { return label; }
        public long getCopperValue() { return copperValue; }
    }

    @Override
    public long getBalance(String accountId) {
        return balancesInCopper.getOrDefault(normalize(accountId), 0L);
    }

    @Override
    public boolean withdraw(String accountId, long copperAmount) {
        if (copperAmount < 0) {
            return false;
        }
        String normalized = normalize(accountId);
        synchronized (balancesInCopper) {
            long current = balancesInCopper.getOrDefault(normalized, 0L);
            if (current < copperAmount) {
                return false;
            }
            balancesInCopper.put(normalized, current - copperAmount);
            return true;
        }
    }

    @Override
    public void deposit(String accountId, long copperAmount) {
        if (copperAmount < 0) {
            throw new IllegalArgumentException("copperAmount must be >= 0");
        }
        String normalized = normalize(accountId);
        balancesInCopper.merge(normalized, copperAmount, Long::sum);
    }

    @Override
    public String format(long copperAmount) {
        if (copperAmount < 0) {
            return "-" + format(-copperAmount);
        }

        Map<Denomination, Long> coins = breakdown(copperAmount);
        StringBuilder sb = new StringBuilder();
        for (Denomination denomination : Denomination.values()) {
            long count = coins.getOrDefault(denomination, 0L);
            if (count > 0) {
                if (!sb.isEmpty()) {
                    sb.append(' ');
                }
                sb.append(count).append(' ').append(denomination.getLabel());
            }
        }
        if (sb.isEmpty()) {
            return "0 copper";
        }
        return sb.toString();
    }

    @Override
    public Map<Denomination, Long> breakdown(long copperAmount) {
        if (copperAmount < 0) {
            throw new IllegalArgumentException("copperAmount must be >= 0");
        }

        long remaining = copperAmount;
        EnumMap<Denomination, Long> breakdown = new EnumMap<>(Denomination.class);
        for (Denomination denomination : Denomination.values()) {
            long value = denomination.getCopperValue();
            long count = remaining / value;
            breakdown.put(denomination, count);
            remaining %= value;
        }
        return Collections.unmodifiableMap(breakdown);
    }

    private String normalize(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        return accountId.trim().toLowerCase();
    }
}
