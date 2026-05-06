package com.baldeagle.towny.service;

import com.baldeagle.towny.Config;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class TownyTeleportService {
    private static final Map<UUID, PendingTeleport> PENDING = new ConcurrentHashMap<>();
    private static final Map<UUID, Long> COOLDOWNS = new ConcurrentHashMap<>();

    private TownyTeleportService() {}

    public static boolean beginTownTeleport(ServerPlayer player, ServerLevel level, Vec3 destination) {
        if (isOnCooldown(player)) {
            long seconds = Math.max(1L, (cooldownEnd(player) - System.currentTimeMillis()) / 1000L);
            player.sendSystemMessage(Component.literal("Towny: Teleport is on cooldown for " + seconds + "s."));
            return false;
        }

        int warmupSeconds = Math.max(0, Config.TP_WARMUP_SECONDS.get());
        if (warmupSeconds == 0) {
            completeTeleport(player, level, destination);
            return true;
        }

        PENDING.put(player.getUUID(), new PendingTeleport(level, destination, player.position(), System.currentTimeMillis() + (warmupSeconds * 1000L)));
        player.sendSystemMessage(Component.literal("Towny: Teleport warm-up started (" + warmupSeconds + "s). Don't move."));
        return true;
    }

    public static void tick(ServerLevel level) {
        long now = System.currentTimeMillis();
        PENDING.entrySet().removeIf(entry -> {
            ServerPlayer player = level.getServer().getPlayerList().getPlayer(entry.getKey());
            if (player == null) {
                return true;
            }
            PendingTeleport pending = entry.getValue();
            if (now >= pending.executeAtMs) {
                completeTeleport(player, pending.level, pending.destination);
                return true;
            }
            return false;
        });
    }

    public static void cancelByMovement(ServerPlayer player) {
        PendingTeleport pending = PENDING.get(player.getUUID());
        if (pending == null || !Config.TP_CANCEL_ON_MOVE.get()) {
            return;
        }

        if (player.position().distanceToSqr(pending.origin) > 0.04D) {
            PENDING.remove(player.getUUID());
            player.sendSystemMessage(Component.literal("Towny: Teleport cancelled due to movement."));
        }
    }

    public static void cancelByDamage(ServerPlayer player) {
        if (!Config.TP_CANCEL_ON_DAMAGE.get()) {
            return;
        }
        if (PENDING.remove(player.getUUID()) != null) {
            player.sendSystemMessage(Component.literal("Towny: Teleport cancelled due to damage."));
        }
    }

    private static void completeTeleport(ServerPlayer player, ServerLevel level, Vec3 destination) {
        player.teleportTo(level, destination.x, destination.y, destination.z, player.getYRot(), player.getXRot());
        int cooldownSeconds = Math.max(0, Config.TP_COOLDOWN_SECONDS.get());
        if (cooldownSeconds > 0) {
            COOLDOWNS.put(player.getUUID(), System.currentTimeMillis() + (cooldownSeconds * 1000L));
        }
        player.sendSystemMessage(Component.literal("Towny: Teleported."));
    }

    private static boolean isOnCooldown(ServerPlayer player) {
        return cooldownEnd(player) > System.currentTimeMillis();
    }

    private static long cooldownEnd(ServerPlayer player) {
        return COOLDOWNS.getOrDefault(player.getUUID(), 0L);
    }

    private record PendingTeleport(ServerLevel level, Vec3 destination, Vec3 origin, long executeAtMs) {}
}
