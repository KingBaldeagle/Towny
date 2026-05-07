package com.baldeagle.towny.service;

import com.baldeagle.towny.object.nation.Nation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class TownyWarService {
    private static final Set<String> ACTIVE_WARS = new HashSet<>();

    private TownyWarService() {}

    public static boolean declareWar(Nation a, Nation b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        if (a.getUUID().equals(b.getUUID())) return false;
        return ACTIVE_WARS.add(key(a.getUUID(), b.getUUID()));
    }

    public static boolean makePeace(Nation a, Nation b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return ACTIVE_WARS.remove(key(a.getUUID(), b.getUUID()));
    }

    public static boolean isAtWar(Nation a, Nation b) {
        return ACTIVE_WARS.contains(key(a.getUUID(), b.getUUID()));
    }

    public static int activeWarCount() { return ACTIVE_WARS.size(); }

    private static String key(UUID a, UUID b) {
        return a.compareTo(b) <= 0 ? a + ":" + b : b + ":" + a;
    }
}
