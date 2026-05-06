package com.baldeagle.towny.object.economy.provider;

import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyView;
import io.github.lightman314.lightmanscurrency.api.money.value.holder.IMoneyHolder;
import io.github.lightman314.lightmanscurrency.api.teams.ITeam;
import io.github.lightman314.lightmanscurrency.api.teams.TeamAPI;
import io.github.lightman314.lightmanscurrency.common.teams.Team;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
    private final Map<String, Long> teamIdsByAccount = new ConcurrentHashMap<>();

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
        String normalized = normalize(accountId);
        if (normalized.startsWith("player:")) {
            ServerPlayer player = resolvePlayer(normalized);
            if (player != null) {
                IMoneyHolder holder = MoneyAPI.getApi().GetPlayersMoneyHandler(player);
                return totalCoreValue(holder.getStoredMoney());
            }
        }
        if (isGovernmentAccount(normalized)) {
            ITeam team = getTeamForAccount(normalized);
            if (team != null && team.hasBankAccount()) {
                return totalCoreValue(team.getBankAccount().getStoredMoney());
            }
        }
        return balancesInCopper.getOrDefault(normalized, 0L);
    }

    @Override
    public boolean withdraw(String accountId, long copperAmount) {
        if (copperAmount < 0) {
            return false;
        }
        String normalized = normalize(accountId);
        if (normalized.startsWith("player:")) {
            ServerPlayer player = resolvePlayer(normalized);
            if (player != null) {
                IMoneyHolder holder = MoneyAPI.getApi().GetPlayersMoneyHandler(player);
                MoneyValue template = templateValue(holder.getStoredMoney());
                if (template == null) {
                    return false;
                }
                MoneyValue requested = template.fromCoreValue(copperAmount);
                MoneyValue remainder = holder.extractMoney(requested, false);
                return remainder == null || remainder.isEmpty();
            }
        }
        if (isGovernmentAccount(normalized)) {
            ITeam team = getTeamForAccount(normalized);
            if (team != null && team.hasBankAccount()) {
                MoneyValue template = templateValue(team.getBankAccount().getStoredMoney());
                if (template == null) {
                    return false;
                }
                MoneyValue remainder = team.getBankAccount().withdrawMoney(template.fromCoreValue(copperAmount));
                return remainder == null || remainder.isEmpty();
            }
        }
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
        if (normalized.startsWith("player:")) {
            ServerPlayer player = resolvePlayer(normalized);
            if (player != null) {
                IMoneyHolder holder = MoneyAPI.getApi().GetPlayersMoneyHandler(player);
                MoneyValue template = templateValue(holder.getStoredMoney());
                if (template != null) {
                    holder.insertMoney(template.fromCoreValue(copperAmount), false);
                    return;
                }
            }
        }
        if (isGovernmentAccount(normalized)) {
            ITeam team = getTeamForAccount(normalized);
            if (team != null && team.hasBankAccount()) {
                MoneyValue template = templateValue(team.getBankAccount().getStoredMoney());
                if (template != null) {
                    team.getBankAccount().depositMoney(template.fromCoreValue(copperAmount));
                    return;
                }
            }
        }
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

    private ServerPlayer resolvePlayer(String normalizedAccountId) {
        try {
            UUID uuid = UUID.fromString(normalizedAccountId.substring("player:".length()));
            if (ServerLifecycleHooks.getCurrentServer() == null) {
                return null;
            }
            return ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(uuid);
        } catch (IllegalArgumentException ignored) {
            return null;
        }
    }

    public void ensureTeamAccount(String accountId, ServerPlayer owner, String teamName) {
        String normalized = normalize(accountId);
        if (!isGovernmentAccount(normalized) || owner == null) {
            return;
        }

        teamIdsByAccount.computeIfAbsent(normalized, ignored -> {
            ITeam created = TeamAPI.getApi().CreateTeam(owner, teamName);
            if (created == null) {
                return -1L;
            }
            if (!created.hasBankAccount() && created instanceof Team concreteTeam) {
                concreteTeam.createBankAccount(owner);
            }
            return created.getID();
        });
    }

    private boolean isGovernmentAccount(String normalizedAccountId) {
        return normalizedAccountId.startsWith("town:") || normalizedAccountId.startsWith("nation:");
    }

    private ITeam getTeamForAccount(String normalizedAccountId) {
        Long teamId = teamIdsByAccount.get(normalizedAccountId);
        if (teamId == null || teamId < 0) {
            return null;
        }
        return TeamAPI.getApi().GetTeam(false, teamId);
    }

    private long totalCoreValue(MoneyView view) {
        long total = 0L;
        List<MoneyValue> values = view.allValues();
        for (MoneyValue value : values) {
            total += value.getCoreValue();
        }
        return total;
    }

    private MoneyValue templateValue(MoneyView view) {
        List<MoneyValue> values = view.allValues();
        if (!values.isEmpty()) {
            return values.getFirst();
        }
        return view.getRandomValue();
    }
}
