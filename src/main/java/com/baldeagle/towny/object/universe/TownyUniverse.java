package com.baldeagle.towny.object.universe;

import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central in-memory registry for Towny domain objects.
 * <p>
 * This is the first step toward the "TownyUniverse" described in the dev-docs.
 * Persistence hooks and world/chunk models will build on top of this.
 */
public final class TownyUniverse {
    private static final TownyUniverse INSTANCE = new TownyUniverse();

    private final Map<UUID, Resident> residentsById = new ConcurrentHashMap<>();
    private final Map<String, Resident> residentsByName = new ConcurrentHashMap<>();

    private final Map<UUID, Town> townsById = new ConcurrentHashMap<>();
    private final Map<String, Town> townsByName = new ConcurrentHashMap<>();

    private final Map<UUID, Nation> nationsById = new ConcurrentHashMap<>();
    private final Map<String, Nation> nationsByName = new ConcurrentHashMap<>();

    private TownyUniverse() {}

    public static TownyUniverse getInstance() {
        return INSTANCE;
    }

    public Resident registerResident(UUID uuid, String name) {
        String normalized = normalize(name);
        Resident existingByName = residentsByName.get(normalized);
        if (existingByName != null) {
            return existingByName;
        }

        Resident resident = new Resident(uuid, name);
        residentsById.put(resident.getUUID(), resident);
        residentsByName.put(normalized, resident);
        return resident;
    }

    public Optional<Resident> getResident(UUID uuid) {
        return Optional.ofNullable(residentsById.get(uuid));
    }

    public Optional<Resident> getResident(String name) {
        return Optional.ofNullable(residentsByName.get(normalize(name)));
    }

    public Collection<Resident> getResidents() {
        return Collections.unmodifiableCollection(residentsById.values());
    }

    public boolean hasTown(String name) {
        return townsByName.containsKey(normalize(name));
    }

    public Optional<Town> getTown(String name) {
        return Optional.ofNullable(townsByName.get(normalize(name)));
    }

    public Optional<Town> getTown(UUID uuid) {
        return Optional.ofNullable(townsById.get(uuid));
    }

    public Collection<Town> getTowns() {
        return Collections.unmodifiableCollection(townsById.values());
    }

    public Town createTown(String name, Resident mayor) {
        if (mayor == null) {
            throw new IllegalArgumentException("mayor must not be null");
        }
        if (mayor.hasTown()) {
            throw new IllegalStateException("Mayor already belongs to a town");
        }

        String normalized = normalize(name);
        if (townsByName.containsKey(normalized)) {
            throw new IllegalStateException("Town already exists: " + name);
        }

        Town town = new Town(UUID.randomUUID(), name, mayor);
        townsById.put(town.getUUID(), town);
        townsByName.put(normalized, town);
        return town;
    }

    public boolean addResidentToTown(Resident resident, Town town) {
        if (resident == null || town == null || resident.hasTown()) {
            return false;
        }
        return town.addResident(resident);
    }

    public boolean removeResidentFromTown(Resident resident, Town town) {
        if (resident == null || town == null) {
            return false;
        }
        return town.removeResident(resident);
    }

    public Optional<Nation> getNation(String name) {
        return Optional.ofNullable(nationsByName.get(normalize(name)));
    }

    public Optional<Nation> getNation(UUID uuid) {
        return Optional.ofNullable(nationsById.get(uuid));
    }

    public Collection<Nation> getNations() {
        return Collections.unmodifiableCollection(nationsById.values());
    }

    public Nation createNation(String name, Town capitalTown) {
        if (capitalTown == null) {
            throw new IllegalArgumentException("capitalTown must not be null");
        }
        if (capitalTown.hasNation()) {
            throw new IllegalStateException("Town already belongs to a nation");
        }

        String normalized = normalize(name);
        if (nationsByName.containsKey(normalized)) {
            throw new IllegalStateException("Nation already exists: " + name);
        }

        Nation nation = new Nation(UUID.randomUUID(), name, capitalTown);
        nationsById.put(nation.getUUID(), nation);
        nationsByName.put(normalized, nation);
        return nation;
    }

    private String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
