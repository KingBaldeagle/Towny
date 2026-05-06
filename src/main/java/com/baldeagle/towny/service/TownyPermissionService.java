package com.baldeagle.towny.service;

import com.baldeagle.towny.object.resident.Resident;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * Phase-8 permission resolver with extensible node handling and optional LuckPerms hook.
 */
public final class TownyPermissionService {
    private static final Set<String> MAYOR_ONLY = Set.of(
        "towny.command.town.add",
        "towny.command.town.kick",
        "towny.command.town.claim",
        "towny.command.town.unclaim",
        "towny.command.town.set",
        "towny.command.town.delete",
        "towny.command.town.rank",
        "towny.command.town.nation.join",
        "towny.command.town.nation.leave"
    );

    private TownyPermissionService() {}

    public static boolean canExecute(CommandSourceStack source, Resident resident, String permissionNode) {
        if (source.hasPermission(2)) {
            return true;
        }
        if (permissionNode == null || permissionNode.isBlank()) {
            return true;
        }

        ServerPlayer player = source.getPlayer();
        if (player != null) {
            Boolean lpResult = checkLuckPerms(player, permissionNode);
            if (lpResult != null) {
                return lpResult;
            }
        }

        if (resident == null) {
            return !MAYOR_ONLY.contains(permissionNode);
        }

        if (hasWildcard(resident, permissionNode)) {
            return true;
        }

        if (MAYOR_ONLY.contains(permissionNode)) {
            return resident.isMayor();
        }

        return true;
    }

    private static boolean hasWildcard(Resident resident, String permissionNode) {
        for (String rank : resident.getTownRanks()) {
            String normalized = rank.trim().toLowerCase();
            if (normalized.equals("*") || normalized.equals("towny.*")) {
                return true;
            }
            if (normalized.endsWith(".*")) {
                String prefix = normalized.substring(0, normalized.length() - 2);
                if (permissionNode.toLowerCase().startsWith(prefix)) {
                    return true;
                }
            }
            if (normalized.equalsIgnoreCase(permissionNode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return null when LuckPerms is unavailable, otherwise decision from LP.
     */
    private static Boolean checkLuckPerms(ServerPlayer player, String node) {
        try {
            Class<?> providerClass = Class.forName("net.luckperms.api.LuckPermsProvider");
            Method get = providerClass.getMethod("get");
            Object luckPerms = get.invoke(null);
            Method getUserManager = luckPerms.getClass().getMethod("getUserManager");
            Object userManager = getUserManager.invoke(luckPerms);
            Method getUser = userManager.getClass().getMethod("getUser", java.util.UUID.class);
            Object user = getUser.invoke(userManager, player.getUUID());
            if (user == null) {
                return null;
            }
            Method getCachedData = user.getClass().getMethod("getCachedData");
            Object cachedData = getCachedData.invoke(user);
            Method getPermissionData = cachedData.getClass().getMethod("getPermissionData");
            Object permissionData = getPermissionData.invoke(cachedData);
            Method checkPermission = permissionData.getClass().getMethod("checkPermission", String.class);
            Object result = checkPermission.invoke(permissionData, node);
            Method asBoolean = result.getClass().getMethod("asBoolean");
            return (Boolean) asBoolean.invoke(result);
        } catch (Throwable ignored) {
            return null;
        }
    }
}
