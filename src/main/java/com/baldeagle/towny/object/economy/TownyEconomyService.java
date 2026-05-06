package com.baldeagle.towny.object.economy;

import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Phase 5 economy flows: taxes and plot sales.
 */
public final class TownyEconomyService {
    private static final Set<UUID> DELINQUENT_RESIDENTS = ConcurrentHashMap.newKeySet();
    private static final List<TownyTransactionRecord> TRANSACTION_LOG = Collections.synchronizedList(new ArrayList<>());
    private static final int MAX_TRANSACTION_LOG_SIZE = 500;

    private TownyEconomyService() {}

    public static boolean collectResidentTax(Resident resident, Town town, long taxInCopper) {
        Objects.requireNonNull(resident, "resident");
        Objects.requireNonNull(town, "town");
        if (taxInCopper < 0) {
            throw new IllegalArgumentException("taxInCopper must be >= 0");
        }

        String residentAccountId = TownyEconomyHandler.accountIdForPlayer(resident.getUUID());
        String townAccountId = TownyEconomyHandler.accountIdForTown(town.getUUID());
        boolean paid = TownyEconomyHandler.provider().transfer(residentAccountId, townAccountId, taxInCopper);
        if (paid) {
            DELINQUENT_RESIDENTS.remove(resident.getUUID());
        } else {
            DELINQUENT_RESIDENTS.add(resident.getUUID());
        }
        record("resident_tax", residentAccountId, townAccountId, taxInCopper, paid, resident.getName());
        return paid;
    }

    public static int collectDailyTownTaxes(Iterable<Town> towns) {
        int successfulCollections = 0;
        for (Town town : towns) {
            long taxInCopper = Math.max(0L, Math.round(town.getTaxes()));
            if (taxInCopper <= 0L) {
                continue;
            }

            for (Resident resident : town.getResidents()) {
                if (resident.getUUID().equals(town.getMayor().getUUID())) {
                    continue;
                }
                if (resident.isNPC()) {
                    continue;
                }
                if (collectResidentTax(resident, town, taxInCopper)) {
                    successfulCollections++;
                }
            }
        }
        return successfulCollections;
    }

    public static int collectNationTaxes(Iterable<Nation> nations, int nationTaxPercent) {
        int clampedPercent = Math.max(0, Math.min(100, nationTaxPercent));
        int successfulCollections = 0;

        for (Nation nation : nations) {
            for (Town town : nation.getTowns()) {
                long townBalance = TownyEconomyHandler.provider()
                    .getBalance(TownyEconomyHandler.accountIdForTown(town.getUUID()));
                long contribution = (townBalance * clampedPercent) / 100L;
                if (contribution <= 0L) {
                    continue;
                }
                if (collectTownToNationTax(town, nation, contribution)) {
                    successfulCollections++;
                }
            }
        }

        return successfulCollections;
    }

    public static boolean collectTownToNationTax(Town town, Nation nation, long taxInCopper) {
        Objects.requireNonNull(town, "town");
        Objects.requireNonNull(nation, "nation");
        if (taxInCopper < 0) {
            throw new IllegalArgumentException("taxInCopper must be >= 0");
        }

        String townAccountId = TownyEconomyHandler.accountIdForTown(town.getUUID());
        String nationAccountId = TownyEconomyHandler.accountIdForNation(nation.getUUID());
        boolean paid = TownyEconomyHandler.provider().transfer(townAccountId, nationAccountId, taxInCopper);
        record("nation_tax", townAccountId, nationAccountId, taxInCopper, paid, nation.getName());
        return paid;
    }

    public static boolean purchasePlot(Resident buyer, Plot plot) {
        Objects.requireNonNull(buyer, "buyer");
        Objects.requireNonNull(plot, "plot");

        if (!plot.isForSale()) {
            return false;
        }
        if (isResidentDelinquent(buyer)) {
            return false;
        }

        Town town = plot.getTown();
        if (town == null) {
            return false;
        }

        long price = plot.getPriceInCopper();
        String buyerAccountId = TownyEconomyHandler.accountIdForPlayer(buyer.getUUID());
        String townAccountId = TownyEconomyHandler.accountIdForTown(town.getUUID());

        if (!TownyEconomyHandler.provider().transfer(buyerAccountId, townAccountId, price)) {
            record("plot_purchase", buyerAccountId, townAccountId, price, false, plot.getName());
            return false;
        }

        plot.setOwner(buyer);
        plot.setForSale(false);
        plot.setPriceInCopper(0L);
        record("plot_purchase", buyerAccountId, townAccountId, price, true, plot.getName());
        return true;
    }

    public static boolean isResidentDelinquent(Resident resident) {
        return resident != null && DELINQUENT_RESIDENTS.contains(resident.getUUID());
    }

    public static int delinquentResidentCount() {
        return DELINQUENT_RESIDENTS.size();
    }

    public static List<TownyTransactionRecord> recentTransactions() {
        synchronized (TRANSACTION_LOG) {
            return List.copyOf(TRANSACTION_LOG);
        }
    }

    public static void clearTransientState() {
        DELINQUENT_RESIDENTS.clear();
        TRANSACTION_LOG.clear();
    }

    private static void record(String type, String from, String to, long amount, boolean success, String details) {
        synchronized (TRANSACTION_LOG) {
            TRANSACTION_LOG.add(new TownyTransactionRecord(type, from, to, amount, success, System.currentTimeMillis(), details));
            if (TRANSACTION_LOG.size() > MAX_TRANSACTION_LOG_SIZE) {
                TRANSACTION_LOG.removeFirst();
            }
        }
    }
}
