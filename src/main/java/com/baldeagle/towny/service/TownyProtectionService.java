package com.baldeagle.towny.service;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.WorldCoord;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * Minimal phase-4 protection logic for block interactions.
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

        // Town members can build freely in this phase-4 baseline.
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
}
