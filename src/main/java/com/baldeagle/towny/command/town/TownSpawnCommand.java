package com.baldeagle.towny.command.town;

import com.baldeagle.towny.command.framework.SubCommand;
import com.baldeagle.towny.command.framework.TownyCommandContext;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.service.TownyTeleportService;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public final class TownSpawnCommand implements SubCommand {
    @Override
    public String getName() {
        return "spawn";
    }

    @Override
    public String getPermission() {
        return "towny.command.town.spawn";
    }

    @Override
    public int execute(TownyCommandContext context) {
        ServerPlayer player = context.getPlayerOrNull();
        Resident resident = context.getOrCreatePlayerResidentOrNull();
        if (player == null || resident == null || !resident.hasTown()) {
            return context.fail("You must be in a town to use /town spawn.");
        }

        Town town = resident.getTown();
        if (!town.hasHomeBlock()) {
            return context.fail("Your town has no home block set.");
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return context.fail("Unable to resolve server level.");
        }

        int blockSize = Math.max(1, com.baldeagle.towny.Config.TOWN_BLOCK_SIZE.get());
        int x = town.getHomeBlock().coord().x() * blockSize + (blockSize / 2);
        int z = town.getHomeBlock().coord().z() * blockSize + (blockSize / 2);
        Vec3 destination = new Vec3(x + 0.5D, player.getY(), z + 0.5D);

        TownyTeleportService.beginTownTeleport(player, serverLevel, destination);
        return 1;
    }
}
