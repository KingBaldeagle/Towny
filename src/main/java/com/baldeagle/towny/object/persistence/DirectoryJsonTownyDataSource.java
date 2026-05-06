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
import java.util.stream.Stream;

/**
 * Directory-based JSON persistence matching the phase-2 plan layout.
 */
public class DirectoryJsonTownyDataSource implements TownyDataSource {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Path rootDir;
    private final Path residentsDir;
    private final Path townsDir;
    private final Path nationsDir;
    private final Path worldsDir;
    private final Path townBlocksDir;
    private final Path plotsDir;

    public DirectoryJsonTownyDataSource(Path rootDir) {
        this.rootDir = rootDir;
        this.residentsDir = rootDir.resolve("residents");
        this.townsDir = rootDir.resolve("towns");
        this.nationsDir = rootDir.resolve("nations");
        this.worldsDir = rootDir.resolve("worlds");
        this.townBlocksDir = rootDir.resolve("townblocks");
        this.plotsDir = rootDir.resolve("plots");
    }

    @Override
    public void loadAll(TownyUniverse universe) {
        universe.clear();

        for (ResidentData data : readAll(residentsDir, ResidentData.class)) {
            Resident resident = universe.registerResident(UUID.fromString(data.uuid), data.name);
            resident.setDelinquent(data.delinquent);
        }

        for (TownData data : readAll(townsDir, TownData.class)) {
            Resident mayor = universe.getResident(UUID.fromString(data.mayorUuid))
                .orElseThrow(() -> new IllegalStateException("Missing mayor for town: " + data.name));
            universe.registerTown(new Town(UUID.fromString(data.uuid), data.name, mayor));
        }

        for (TownData data : readAll(townsDir, TownData.class)) {
            Town town = universe.getTown(UUID.fromString(data.uuid))
                .orElseThrow(() -> new IllegalStateException("Missing town during resident link: " + data.name));
            for (String residentUuid : data.residentUuids) {
                Resident resident = universe.getResident(UUID.fromString(residentUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing resident in town: " + data.name));
                if (!town.getResidents().contains(resident)) {
                    town.addResident(resident);
                }
            }
        }

        for (NationData data : readAll(nationsDir, NationData.class)) {
            Town capital = universe.getTown(UUID.fromString(data.capitalTownUuid))
                .orElseThrow(() -> new IllegalStateException("Missing capital for nation: " + data.name));
            Nation nation = new Nation(UUID.fromString(data.uuid), data.name, capital);
            universe.registerNation(nation);
            for (String townUuid : data.townUuids) {
                Town town = universe.getTown(UUID.fromString(townUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing town in nation: " + data.name));
                if (!nation.getTowns().contains(town)) {
                    nation.addTown(town);
                }
            }
        }

        for (TownyWorldData data : readAll(worldsDir, TownyWorldData.class)) {
            TownyWorld world = new TownyWorld(UUID.fromString(data.uuid), data.name);
            world.setUsingTowny(data.usingTowny);
            world.setClaimable(data.claimable);
            world.setPvp(data.pvp);
            world.setForcePvp(data.forcePvp);
            world.setExplosion(data.explosion);
            world.setForceExplosion(data.forceExplosion);
            world.setFire(data.fire);
            world.setForceFire(data.forceFire);
            universe.registerWorld(world);
        }

        for (TownBlockData data : readAll(townBlocksDir, TownBlockData.class)) {
            Town town = universe.getTown(UUID.fromString(data.townUuid))
                .orElseThrow(() -> new IllegalStateException("Missing town for town block: " + data.name));
            TownBlock townBlock = new TownBlock(
                UUID.fromString(data.uuid),
                data.name,
                WorldCoord.of(data.worldName, data.x, data.z),
                town
            );
            townBlock.setType(TownBlockType.valueOf(data.type));
            townBlock.setOutpost(data.outpost);
            townBlock.setForSale(data.forSale);
            townBlock.setPlotPriceInCopper(data.plotPriceInCopper);
            if (data.residentUuid != null) {
                Resident resident = universe.getResident(UUID.fromString(data.residentUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing resident for town block: " + data.name));
                townBlock.setResident(resident);
            }
            universe.registerTownBlock(townBlock);
        }

        for (PlotData data : readAll(plotsDir, PlotData.class)) {
            Town town = universe.getTown(UUID.fromString(data.townUuid))
                .orElseThrow(() -> new IllegalStateException("Missing town for plot: " + data.name));
            Resident owner = data.ownerUuid == null
                ? null
                : universe.getResident(UUID.fromString(data.ownerUuid))
                    .orElseThrow(() -> new IllegalStateException("Missing owner for plot: " + data.name));

            Plot plot = new Plot(
                UUID.fromString(data.uuid),
                data.name,
                WorldCoord.of(data.worldName, data.x, data.z),
                town,
                owner
            );
            plot.setForSale(data.forSale);
            plot.setPriceInCopper(data.priceInCopper);
            universe.registerPlot(plot);
        }
    }

    @Override
    public void saveAll(TownyUniverse universe) {
        ensureDirs();

        clearJsonFiles(residentsDir);
        clearJsonFiles(townsDir);
        clearJsonFiles(nationsDir);
        clearJsonFiles(worldsDir);
        clearJsonFiles(townBlocksDir);
        clearJsonFiles(plotsDir);

        for (Resident resident : universe.getResidents()) {
            ResidentData data = new ResidentData();
            data.uuid = resident.getUUID().toString();
            data.name = resident.getName();
            data.delinquent = resident.isDelinquent();
            write(residentsDir.resolve(data.uuid + ".json"), data);
        }

        for (Town town : universe.getTowns()) {
            TownData data = new TownData();
            data.uuid = town.getUUID().toString();
            data.name = town.getName();
            data.mayorUuid = town.getMayor().getUUID().toString();
            for (Resident resident : town.getResidents()) {
                data.residentUuids.add(resident.getUUID().toString());
            }
            write(townsDir.resolve(data.uuid + ".json"), data);
        }

        for (Nation nation : universe.getNations()) {
            NationData data = new NationData();
            data.uuid = nation.getUUID().toString();
            data.name = nation.getName();
            data.capitalTownUuid = nation.getCapital().getUUID().toString();
            for (Town town : nation.getTowns()) {
                data.townUuids.add(town.getUUID().toString());
            }
            write(nationsDir.resolve(data.uuid + ".json"), data);
        }

        for (TownyWorld world : universe.getWorlds()) {
            TownyWorldData data = new TownyWorldData();
            data.uuid = world.getUUID().toString();
            data.name = world.getName();
            data.usingTowny = world.isUsingTowny();
            data.claimable = world.isClaimable();
            data.pvp = world.isPvp();
            data.forcePvp = world.isForcePvp();
            data.explosion = world.isExplosion();
            data.forceExplosion = world.isForceExplosion();
            data.fire = world.isFire();
            data.forceFire = world.isForceFire();
            write(worldsDir.resolve(data.uuid + ".json"), data);
        }

        for (TownBlock townBlock : universe.getTownBlocks()) {
            TownBlockData data = new TownBlockData();
            data.uuid = townBlock.getUUID().toString();
            data.name = townBlock.getName();
            data.worldName = townBlock.getWorldCoord().worldName();
            data.x = townBlock.getWorldCoord().coord().x();
            data.z = townBlock.getWorldCoord().coord().z();
            data.townUuid = townBlock.getTown().getUUID().toString();
            data.residentUuid = townBlock.getResident() == null ? null : townBlock.getResident().getUUID().toString();
            data.type = townBlock.getType().name();
            data.outpost = townBlock.isOutpost();
            data.forSale = townBlock.isForSale();
            data.plotPriceInCopper = townBlock.getPlotPriceInCopper();
            write(townBlocksDir.resolve(data.uuid + ".json"), data);
        }

        for (Plot plot : universe.getPlots()) {
            PlotData data = new PlotData();
            data.uuid = plot.getUUID().toString();
            data.name = plot.getName();
            data.worldName = plot.getWorldCoord().worldName();
            data.x = plot.getWorldCoord().coord().x();
            data.z = plot.getWorldCoord().coord().z();
            data.townUuid = plot.getTown().getUUID().toString();
            data.ownerUuid = plot.getOwner() == null ? null : plot.getOwner().getUUID().toString();
            data.forSale = plot.isForSale();
            data.priceInCopper = plot.getPriceInCopper();
            write(plotsDir.resolve(data.uuid + ".json"), data);
        }
    }

    @Override
    public void saveResident(Resident resident) { saveAll(TownyUniverse.getInstance()); }
    @Override
    public void saveTown(Town town) { saveAll(TownyUniverse.getInstance()); }
    @Override
    public void saveNation(Nation nation) { saveAll(TownyUniverse.getInstance()); }
    @Override
    public void saveTownBlock(TownBlock townBlock) { saveAll(TownyUniverse.getInstance()); }
    @Override
    public void savePlot(Plot plot) { saveAll(TownyUniverse.getInstance()); }
    @Override
    public void saveWorld(TownyWorld world) { saveAll(TownyUniverse.getInstance()); }

    private void ensureDirs() {
        try {
            Files.createDirectories(rootDir);
            Files.createDirectories(residentsDir);
            Files.createDirectories(townsDir);
            Files.createDirectories(nationsDir);
            Files.createDirectories(worldsDir);
            Files.createDirectories(townBlocksDir);
            Files.createDirectories(plotsDir);
        } catch (IOException e) {
            throw new IllegalStateException("Failed creating persistence directories", e);
        }
    }

    private void clearJsonFiles(Path dir) {
        try (Stream<Path> stream = Files.list(dir)) {
            stream
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .forEach(path -> {
                    try {
                        Files.deleteIfExists(path);
                    } catch (IOException e) {
                        throw new IllegalStateException("Failed clearing JSON file: " + path, e);
                    }
                });
        } catch (IOException e) {
            throw new IllegalStateException("Failed listing persistence directory: " + dir, e);
        }
    }

    private <T> List<T> readAll(Path dir, Class<T> type) {
        if (!Files.exists(dir)) {
            return List.of();
        }
        try (Stream<Path> stream = Files.list(dir)) {
            List<Path> files = stream
                .filter(path -> path.getFileName().toString().endsWith(".json"))
                .toList();
            List<T> result = new ArrayList<>();
            for (Path file : files) {
                try (Reader reader = Files.newBufferedReader(file)) {
                    T value = gson.fromJson(reader, type);
                    if (value != null) {
                        result.add(value);
                    }
                }
            }
            return result;
        } catch (IOException e) {
            throw new IllegalStateException("Failed reading persistence directory: " + dir, e);
        }
    }

    private void write(Path file, Object data) {
        try (Writer writer = Files.newBufferedWriter(file)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw new IllegalStateException("Failed writing data file: " + file, e);
        }
    }

    private static class ResidentData {
        String uuid;
        String name;
        boolean delinquent;
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
