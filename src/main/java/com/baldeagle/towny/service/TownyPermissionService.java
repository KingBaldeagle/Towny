package com.baldeagle.towny.service;

import com.baldeagle.towny.object.resident.Resident;
import net.minecraft.commands.CommandSourceStack;

import java.util.Set;

/**
 * Phase-8 permission resolver for Towny command nodes and role hierarchy.
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
        if (resident == null) {
            return !MAYOR_ONLY.contains(permissionNode);
        }
        if (MAYOR_ONLY.contains(permissionNode)) {
            return resident.isMayor();
        }
        return true;
    }
}
