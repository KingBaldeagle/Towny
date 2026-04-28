package com.baldeagle.towny.object.universe;

import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.world.TownyWorld;
import com.baldeagle.towny.object.world.WorldCoord;

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

    private final Map<WorldCoord, TownBlock> townBlocksByWorldCoord = new ConcurrentHashMap<>();
    private final Map<String, TownyWorld> worldsByName = new ConcurrentHashMap<>();
    private final Map<UUID, Plot> plotsById = new ConcurrentHashMap<>();
    private final Map<WorldCoord, Plot> plotsByWorldCoord = new ConcurrentHashMap<>();

    private TownyUniverse() {}

    public static TownyUniverse getInstance() {
        return INSTANCE;
    }

    public void clear() {
        residentsById.clear();
        residentsByName.clear();
        townsById.clear();
        townsByName.clear();
        nationsById.clear();
        nationsByName.clear();
        townBlocksByWorldCoord.clear();
        worldsByName.clear();
        plotsById.clear();
        plotsByWorldCoord.clear();
    }

    public Resident registerResident(Resident resident) {
        if (resident == null) {
            throw new IllegalArgumentException("resident must not be null");
        }
        residentsById.put(resident.getUUID(), resident);
        residentsByName.put(normalize(resident.getName()), resident);
        return resident;
    }

    public Resident registerResident(UUID uuid, String name) {
        String normalized = normalize(name);
        Resident existingByName = residentsByName.get(normalized);
        if (existingByName != null) {
            return existingByName;
        }

        Resident resident = new Resident(uuid, name);
        return registerResident(resident);
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
        return registerTown(town);
    }

    public Town registerTown(Town town) {
        if (town == null) {
            throw new IllegalArgumentException("town must not be null");
        }
        townsById.put(town.getUUID(), town);
        townsByName.put(normalize(town.getName()), town);
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
        return registerNation(nation);
    }

    public Nation registerNation(Nation nation) {
        if (nation == null) {
            throw new IllegalArgumentException("nation must not be null");
        }
        nationsById.put(nation.getUUID(), nation);
        nationsByName.put(normalize(nation.getName()), nation);
        return nation;
    }

    public TownyWorld registerWorld(String worldName) {
        String normalized = normalize(worldName);
        return worldsByName.computeIfAbsent(normalized, key -> new TownyWorld(UUID.randomUUID(), worldName));
    }

    public TownyWorld registerWorld(TownyWorld world) {
        if (world == null) {
            throw new IllegalArgumentException("world must not be null");
        }
        worldsByName.put(normalize(world.getName()), world);
        return world;
    }

    public Optional<TownyWorld> getWorld(String worldName) {
        return Optional.ofNullable(worldsByName.get(normalize(worldName)));
    }

    public Collection<TownyWorld> getWorlds() {
        return Collections.unmodifiableCollection(worldsByName.values());
    }

    public TownBlock claimTownBlock(Town town, WorldCoord worldCoord) {
        if (town == null || worldCoord == null) {
            throw new IllegalArgumentException("town/worldCoord must not be null");
        }
        TownyWorld world = registerWorld(worldCoord.worldName());
        if (!world.isUsingTowny() || !world.isClaimable()) {
            throw new IllegalStateException("World is not claimable: " + world.getName());
        }
        if (townBlocksByWorldCoord.containsKey(worldCoord)) {
            throw new IllegalStateException("Town block already claimed: " + worldCoord);
        }

        TownBlock townBlock = new TownBlock(UUID.randomUUID(), town.getName() + "-" + worldCoord.coord(), worldCoord, town);
        town.addTownBlock(townBlock);
        townBlocksByWorldCoord.put(worldCoord, townBlock);
        if (!town.hasHomeBlock()) {
            town.setHomeBlock(worldCoord);
        }
        return townBlock;
    }

    public Optional<TownBlock> getTownBlock(WorldCoord worldCoord) {
        return Optional.ofNullable(townBlocksByWorldCoord.get(worldCoord));
    }

    public Collection<TownBlock> getTownBlocks() {
        return Collections.unmodifiableCollection(townBlocksByWorldCoord.values());
    }

    public TownBlock registerTownBlock(TownBlock townBlock) {
        if (townBlock == null) {
            throw new IllegalArgumentException("townBlock must not be null");
        }
        townBlocksByWorldCoord.put(townBlock.getWorldCoord(), townBlock);
        if (townBlock.getTown() != null) {
            townBlock.getTown().addTownBlock(townBlock);
        }
        return townBlock;
    }

    public Plot createPlot(String name, WorldCoord worldCoord, Town town, Resident owner) {
        if (worldCoord == null || town == null) {
            throw new IllegalArgumentException("worldCoord/town must not be null");
        }
        if (!townBlocksByWorldCoord.containsKey(worldCoord)) {
            throw new IllegalStateException("Cannot create plot in unclaimed wilderness: " + worldCoord);
        }
        if (plotsByWorldCoord.containsKey(worldCoord)) {
            throw new IllegalStateException("Plot already exists at: " + worldCoord);
        }

        Plot plot = new Plot(UUID.randomUUID(), name, worldCoord, town, owner);
        plotsById.put(plot.getUUID(), plot);
        plotsByWorldCoord.put(worldCoord, plot);
        return plot;
    }

    public Optional<Plot> getPlot(UUID uuid) {
        return Optional.ofNullable(plotsById.get(uuid));
    }

    public Optional<Plot> getPlot(WorldCoord worldCoord) {
        return Optional.ofNullable(plotsByWorldCoord.get(worldCoord));
    }

    public Collection<Plot> getPlots() {
        return Collections.unmodifiableCollection(plotsById.values());
    }

    public Plot registerPlot(Plot plot) {
        if (plot == null) {
            throw new IllegalArgumentException("plot must not be null");
        }
        plotsById.put(plot.getUUID(), plot);
        plotsByWorldCoord.put(plot.getWorldCoord(), plot);
        return plot;
    }

    private String normalize(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name must not be blank");
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }
}
