package com.baldeagle.towny.object.universe;

import com.baldeagle.towny.object.town.Town;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory town invitation tracker for /town invite, /town accept, /town deny.
 */
public final class TownInviteRegistry {
    private static final TownInviteRegistry INSTANCE = new TownInviteRegistry();
    private final Map<UUID, Town> invitesByResidentId = new ConcurrentHashMap<>();

    private TownInviteRegistry() {}

    public static TownInviteRegistry getInstance() {
        return INSTANCE;
    }

    public void invite(UUID residentId, Town town) {
        invitesByResidentId.put(residentId, town);
    }

    public Optional<Town> getInvite(UUID residentId) {
        return Optional.ofNullable(invitesByResidentId.get(residentId));
    }

    public Optional<Town> consumeInvite(UUID residentId) {
        return Optional.ofNullable(invitesByResidentId.remove(residentId));
    }

    public boolean denyInvite(UUID residentId) {
        return invitesByResidentId.remove(residentId) != null;
    }
}
