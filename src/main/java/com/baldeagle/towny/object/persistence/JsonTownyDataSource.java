package com.baldeagle.towny.object.persistence;

import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.townblock.TownBlockType;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.TownyWorld;
import com.baldeagle.towny.object.world.WorldCoord;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JSON persistence bootstrap for phase 2.
 * <p>
 * Data is stored in a single file under {@code config/towny/universe.json}
 * while the data model is still evolving.
 */
public class JsonTownyDataSource implements TownyDataSource {
    private static final String UNIVERSE_FILE = "universe.json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path rootDir;

    public JsonTownyDataSource(Path rootDir) {
        this.rootDir = rootDir;
    }

    @Override
    public void loadAll(TownyUniverse universe) {
        Path file = rootDir.resolve(UNIVERSE_FILE);
        if (!Files.exists(file)) {
            return;
        }

        try (Reader reader = Files.newBufferedReader(file)) {
            UniverseData data = gson.fromJson(reader, UniverseData.class);
            if (data == null) {
                return;
            }

            universe.clear();

            for (ResidentData residentData : data.residents) {
                Resident resident = new Resident(UUID.fromString(residentData.uuid), residentData.name);
                resident.setDelinquent(residentData.delinquent);
                if (residentData.jailedUntilEpochMillis > 0L) {
                    long remaining = residentData.jailedUntilEpochMillis - System.currentTimeMillis();
                    if (remaining > 0L) {
                        resident.jailForMillis(remaining);
                    }
                }
                universe.registerResident(resident);
            }

            for (TownData townData : data.towns) {
                Resident mayor = universe.getResident(UUID.fromString(townData.mayorUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing mayor for town: " + townData.name));
                Town town = new Town(UUID.fromString(townData.uuid), townData.name, mayor);
                universe.registerTown(town);
            }

            for (TownData townData : data.towns) {
                Town town = universe.getTown(UUID.fromString(townData.uuid))
                    .orElseThrow(() -> new IllegalStateException("Town disappeared during load: " + townData.name));
                for (String residentUuid : townData.residentUuids) {
                    Resident resident = universe.getResident(UUID.fromString(residentUuid))
                        .orElseThrow(() -> new IllegalStateException("Missing resident in town: " + townData.name));
                    if (!town.getResidents().contains(resident)) {
                        town.addResident(resident);
                    }
                }
            }

            for (NationData nationData : data.nations) {
                Town capital = universe.getTown(UUID.fromString(nationData.capitalTownUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing capital for nation: " + nationData.name));
                Nation nation = new Nation(UUID.fromString(nationData.uuid), nationData.name, capital);
                universe.registerNation(nation);
                for (String townUuid : nationData.townUuids) {
                    Town town = universe.getTown(UUID.fromString(townUuid))
                        .orElseThrow(() -> new IllegalStateException("Missing town in nation: " + nationData.name));
                    if (!nation.getTowns().contains(town)) {
                        nation.addTown(town);
                    }
                }
            }

            for (TownyWorldData worldData : data.worlds) {
                TownyWorld world = new TownyWorld(UUID.fromString(worldData.uuid), worldData.name);
                world.setUsingTowny(worldData.usingTowny);
                world.setClaimable(worldData.claimable);
                world.setPvp(worldData.pvp);
                world.setForcePvp(worldData.forcePvp);
                world.setExplosion(worldData.explosion);
                world.setForceExplosion(worldData.forceExplosion);
                world.setFire(worldData.fire);
                world.setForceFire(worldData.forceFire);
                universe.registerWorld(world);
            }

            for (TownBlockData townBlockData : data.townBlocks) {
                Town town = universe.getTown(UUID.fromString(townBlockData.townUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing town for town block: " + townBlockData.name));
                WorldCoord worldCoord = WorldCoord.of(townBlockData.worldName, townBlockData.x, townBlockData.z);
                TownBlock townBlock = new TownBlock(UUID.fromString(townBlockData.uuid), townBlockData.name, worldCoord, town);
                townBlock.setType(TownBlockType.valueOf(townBlockData.type));
                townBlock.setOutpost(townBlockData.outpost);
                townBlock.setForSale(townBlockData.forSale);
                townBlock.setPlotPriceInCopper(townBlockData.plotPriceInCopper);
                if (townBlockData.residentUuid != null) {
                    Resident resident = universe.getResident(UUID.fromString(townBlockData.residentUuid))
                        .orElseThrow(() -> new IllegalStateException("Missing resident for town block: " + townBlockData.name));
                    townBlock.setResident(resident);
                }
                universe.registerTownBlock(townBlock);
            }

            for (PlotData plotData : data.plots) {
                Town town = universe.getTown(UUID.fromString(plotData.townUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing town for plot: " + plotData.name));
                Resident owner = null;
                if (plotData.ownerUuid != null) {
                    owner = universe.getResident(UUID.fromString(plotData.ownerUuid))
                        .orElseThrow(() -> new IllegalStateException("Missing owner for plot: " + plotData.name));
                }
                WorldCoord worldCoord = WorldCoord.of(plotData.worldName, plotData.x, plotData.z);
                Plot plot = new Plot(UUID.fromString(plotData.uuid), plotData.name, worldCoord, town, owner);
                plot.setForSale(plotData.forSale);
                plot.setPriceInCopper(plotData.priceInCopper);
                universe.registerPlot(plot);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Towny universe data", e);
        }
    }

    @Override
    public void saveAll(TownyUniverse universe) {
        try {
            Files.createDirectories(rootDir);
            UniverseData data = new UniverseData();

            for (Resident resident : universe.getResidents()) {
                ResidentData residentData = new ResidentData();
                residentData.uuid = resident.getUUID().toString();
                residentData.name = resident.getName();
                residentData.delinquent = resident.isDelinquent();
                residentData.jailedUntilEpochMillis = resident.getJailedUntilEpochMillis();
                data.residents.add(residentData);
            }

            for (Town town : universe.getTowns()) {
                TownData townData = new TownData();
                townData.uuid = town.getUUID().toString();
                townData.name = town.getName();
                townData.mayorUuid = town.getMayor().getUUID().toString();
                for (Resident resident : town.getResidents()) {
                    townData.residentUuids.add(resident.getUUID().toString());
                }
                data.towns.add(townData);
            }

            for (Nation nation : universe.getNations()) {
                NationData nationData = new NationData();
                nationData.uuid = nation.getUUID().toString();
                nationData.name = nation.getName();
                nationData.capitalTownUuid = nation.getCapital().getUUID().toString();
                for (Town town : nation.getTowns()) {
                    nationData.townUuids.add(town.getUUID().toString());
                }
                data.nations.add(nationData);
            }

            for (TownyWorld world : universe.getWorlds()) {
                TownyWorldData worldData = new TownyWorldData();
                worldData.uuid = world.getUUID().toString();
                worldData.name = world.getName();
                worldData.usingTowny = world.isUsingTowny();
                worldData.claimable = world.isClaimable();
                worldData.pvp = world.isPvp();
                worldData.forcePvp = world.isForcePvp();
                worldData.explosion = world.isExplosion();
                worldData.forceExplosion = world.isForceExplosion();
                worldData.fire = world.isFire();
                worldData.forceFire = world.isForceFire();
                data.worlds.add(worldData);
            }

            for (TownBlock townBlock : universe.getTownBlocks()) {
                TownBlockData townBlockData = new TownBlockData();
                townBlockData.uuid = townBlock.getUUID().toString();
                townBlockData.name = townBlock.getName();
                townBlockData.worldName = townBlock.getWorldCoord().worldName();
                townBlockData.x = townBlock.getWorldCoord().coord().x();
                townBlockData.z = townBlock.getWorldCoord().coord().z();
                townBlockData.townUuid = townBlock.getTown().getUUID().toString();
                townBlockData.residentUuid = townBlock.getResident() != null ? townBlock.getResident().getUUID().toString() : null;
                townBlockData.type = townBlock.getType().name();
                townBlockData.outpost = townBlock.isOutpost();
                townBlockData.forSale = townBlock.isForSale();
                townBlockData.plotPriceInCopper = townBlock.getPlotPriceInCopper();
                data.townBlocks.add(townBlockData);
            }

            for (Plot plot : universe.getPlots()) {
                PlotData plotData = new PlotData();
                plotData.uuid = plot.getUUID().toString();
                plotData.name = plot.getName();
                plotData.worldName = plot.getWorldCoord().worldName();
                plotData.x = plot.getWorldCoord().coord().x();
                plotData.z = plot.getWorldCoord().coord().z();
                plotData.townUuid = plot.getTown().getUUID().toString();
                plotData.ownerUuid = plot.getOwner() != null ? plot.getOwner().getUUID().toString() : null;
                plotData.forSale = plot.isForSale();
                plotData.priceInCopper = plot.getPriceInCopper();
                data.plots.add(plotData);
            }

            Path file = rootDir.resolve(UNIVERSE_FILE);
            try (Writer writer = Files.newBufferedWriter(file)) {
                gson.toJson(data, writer);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to save Towny universe data", e);
        }
    }

    @Override
    public void saveResident(Resident resident) {
        // Phase-2 bootstrap: persist via snapshot save.
        saveAll(TownyUniverse.getInstance());
    }

    @Override
    public void saveTown(Town town) {
        saveAll(TownyUniverse.getInstance());
    }

    @Override
    public void saveNation(Nation nation) {
        saveAll(TownyUniverse.getInstance());
    }

    @Override
    public void saveTownBlock(TownBlock townBlock) {
        saveAll(TownyUniverse.getInstance());
    }

    @Override
    public void savePlot(Plot plot) {
        saveAll(TownyUniverse.getInstance());
    }

    @Override
    public void saveWorld(TownyWorld world) {
        saveAll(TownyUniverse.getInstance());
    }

    private static class UniverseData {
        List<ResidentData> residents = new ArrayList<>();
        List<TownData> towns = new ArrayList<>();
        List<NationData> nations = new ArrayList<>();
        List<TownyWorldData> worlds = new ArrayList<>();
        List<TownBlockData> townBlocks = new ArrayList<>();
        List<PlotData> plots = new ArrayList<>();
    }

    private static class ResidentData {
        String uuid;
        String name;
        boolean delinquent;
        long jailedUntilEpochMillis;
    }

    private static class TownData {
        String uuid;
        String name;
        String mayorUuid;
        List<String> residentUuids = new ArrayList<>();
    }

    private static class NationData {
        String uuid;
        String name;
        String capitalTownUuid;
        List<String> townUuids = new ArrayList<>();
    }

    private static class TownyWorldData {
        String uuid;
        String name;
        boolean usingTowny;
        boolean claimable;
        boolean pvp;
        boolean forcePvp;
        boolean explosion;
        boolean forceExplosion;
        boolean fire;
        boolean forceFire;
    }

    private static class TownBlockData {
        String uuid;
        String name;
        String worldName;
        int x;
        int z;
        String townUuid;
        String residentUuid;
        String type;
        boolean outpost;
        boolean forSale;
        long plotPriceInCopper;
    }

    private static class PlotData {
        String uuid;
        String name;
        String worldName;
        int x;
        int z;
        String townUuid;
        String ownerUuid;
        boolean forSale;
        long priceInCopper;
    }
}
