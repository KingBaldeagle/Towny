package com.baldeagle.towny.service;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.TownyWorld;
import com.baldeagle.towny.object.world.WorldCoord;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import java.util.Optional;

/**
 * Phase-4 protection logic for block/combat/environment checks.
 */
public final class TownyProtectionService {
    private TownyProtectionService() {}

    public static WorldCoord toWorldCoord(Level level, BlockPos blockPos) {
        int blockSize = Math.max(1, Config.TOWN_BLOCK_SIZE.get());
        int townBlockX = Math.floorDiv(blockPos.getX(), blockSize);
        int townBlockZ = Math.floorDiv(blockPos.getZ(), blockSize);
        String worldName = level.dimension().location().toString();
        return WorldCoord.of(worldName, townBlockX, townBlockZ);
    }

    public static boolean isTownyEnabled(Level level, BlockPos blockPos) {
        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(level, blockPos);
        Optional<TownyWorld> world = universe.getWorld(worldCoord.worldName());
        return world.map(TownyWorld::isUsingTowny).orElse(true);
    }

    public static boolean canBuildAt(ServerPlayer player, BlockPos blockPos) {
        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(player.level(), blockPos);
        Optional<TownBlock> townBlockOptional = universe.getTownBlock(worldCoord);
        if (townBlockOptional.isEmpty()) {
            return true;
        }

        Resident actor = universe.registerResident(player.getUUID(), player.getName().getString());
        Town owningTown = townBlockOptional.get().getTown();
        if (owningTown == null) {
            return true;
        }

        if (actor.getTown() == owningTown) {
            return true;
        }

        Optional<Plot> plotOptional = universe.getPlot(worldCoord);
        if (plotOptional.isPresent()) {
            Resident owner = plotOptional.get().getOwner();
            if (owner != null && owner.getUUID().equals(actor.getUUID())) {
                return true;
            }
        }

        return false;
    }

    public static boolean isPvpAllowedAt(Level level, BlockPos blockPos) {
        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(level, blockPos);
        Optional<TownyWorld> worldOptional = universe.getWorld(worldCoord.worldName());
        if (worldOptional.isPresent()) {
            TownyWorld world = worldOptional.get();
            if (!world.isUsingTowny()) {
                return true;
            }
            if (world.isForcePvp()) {
                return world.isPvp();
            }
        }

        return universe.getTownBlock(worldCoord).isEmpty();
    }


    public static boolean isExplosionAllowedAt(LevelAccessor levelAccessor, BlockPos blockPos) {
        if (!(levelAccessor instanceof Level level)) {
            return true;
        }
        return isExplosionAllowedAt(level, blockPos);
    }

    public static boolean isExplosionAllowedAt(Level level, BlockPos blockPos) {
        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(level, blockPos);
        Optional<TownyWorld> worldOptional = universe.getWorld(worldCoord.worldName());
        if (worldOptional.isPresent()) {
            TownyWorld world = worldOptional.get();
            if (!world.isUsingTowny()) {
                return true;
            }
            if (world.isForceExplosion()) {
                return world.isExplosion();
            }
        }

        return universe.getTownBlock(worldCoord).isEmpty();
    }


    public static boolean isFireAllowedAt(LevelAccessor levelAccessor, BlockPos blockPos) {
        if (!(levelAccessor instanceof Level level)) {
            return true;
        }
        return isFireAllowedAt(level, blockPos);
    }

    public static boolean isFireAllowedAt(Level level, BlockPos blockPos) {
        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(level, blockPos);
        Optional<TownyWorld> worldOptional = universe.getWorld(worldCoord.worldName());
        if (worldOptional.isPresent()) {
            TownyWorld world = worldOptional.get();
            if (!world.isUsingTowny()) {
                return true;
            }
            if (world.isForceFire()) {
                return world.isFire();
            }
        }

        return universe.getTownBlock(worldCoord).isEmpty();
    }


    public static boolean isMobGriefAllowedAt(LevelAccessor levelAccessor, BlockPos blockPos) {
        if (!(levelAccessor instanceof Level level)) {
            return true;
        }
        return isMobGriefAllowedAt(level, blockPos);
    }

    public static boolean isMobGriefAllowedAt(Level level, BlockPos blockPos) {
        if (!isTownyEnabled(level, blockPos)) {
            return true;
        }

        TownyUniverse universe = TownyUniverse.getInstance();
        WorldCoord worldCoord = toWorldCoord(level, blockPos);
        return universe.getTownBlock(worldCoord).isEmpty();
    }
}
