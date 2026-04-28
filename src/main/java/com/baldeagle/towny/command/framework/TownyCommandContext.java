package com.baldeagle.towny.command.framework;

import com.baldeagle.towny.Config;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.WorldCoord;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * Typed helpers for Towny brigadier command handlers.
 */
public final class TownyCommandContext {
    private final CommandContext<CommandSourceStack> brigadierContext;

    public TownyCommandContext(CommandContext<CommandSourceStack> brigadierContext) {
        this.brigadierContext = brigadierContext;
    }

    public CommandSourceStack source() {
        return brigadierContext.getSource();
    }

    public TownyUniverse universe() {
        return TownyUniverse.getInstance();
    }

    public ServerPlayer getPlayerOrNull() {
        return source().getPlayer();
    }

    public Resident getOrCreatePlayerResidentOrNull() {
        ServerPlayer player = getPlayerOrNull();
        if (player == null) {
            return null;
        }
        return universe().registerResident(player.getUUID(), player.getName().getString());
    }

    public String getString(String argumentName) {
        return StringArgumentType.getString(brigadierContext, argumentName);
    }

    public Optional<Resident> getResident(String argumentName) {
        return universe().getResident(getString(argumentName));
    }

    public Optional<Town> getTown(String argumentName) {
        return universe().getTown(getString(argumentName));
    }

    public WorldCoord getPlayerWorldCoordOrNull() {
        ServerPlayer player = getPlayerOrNull();
        if (player == null) {
            return null;
        }

        int blockSize = Math.max(1, Config.TOWN_BLOCK_SIZE.get());
        int townBlockX = Math.floorDiv(player.blockPosition().getX(), blockSize);
        int townBlockZ = Math.floorDiv(player.blockPosition().getZ(), blockSize);
        String worldName = player.level().dimension().location().toString();
        return WorldCoord.of(worldName, townBlockX, townBlockZ);
    }

    public Optional<TownBlock> getTownBlockAtPlayerOrEmpty() {
        WorldCoord worldCoord = getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return Optional.empty();
        }
        return universe().getTownBlock(worldCoord);
    }

    public Optional<Plot> getPlotAtPlayerOrEmpty() {
        WorldCoord worldCoord = getPlayerWorldCoordOrNull();
        if (worldCoord == null) {
            return Optional.empty();
        }
        return universe().getPlot(worldCoord);
    }

    public int fail(String message) {
        source().sendFailure(Component.literal(message));
        return 0;
    }

    public int success(String message) {
        source().sendSuccess(() -> Component.literal(message), false);
        return 1;
    }
}
