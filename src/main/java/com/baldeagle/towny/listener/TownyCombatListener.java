package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

import java.util.Optional;

/**
 * Phase-4 baseline combat protection in claimed chunks.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyCombatListener {
    private TownyCombatListener() {}

    @SubscribeEvent
    public static void onLivingAttack(LivingIncomingDamageEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer victim)) {
            return;
        }
        if (!(event.getSource().getEntity() instanceof ServerPlayer attacker)) {
            return;
        }

        if (TownyProtectionService.isPvpAllowedAt(victim.level(), victim.blockPosition())) {
            return;
        }

        TownyUniverse universe = TownyUniverse.getInstance();
        Optional<TownBlock> townBlockOptional = universe.getTownBlock(
            TownyProtectionService.toWorldCoord(victim.level(), victim.blockPosition())
        );
        if (townBlockOptional.isEmpty()) {
            return;
        }

        Resident attackerResident = universe.registerResident(attacker.getUUID(), attacker.getName().getString());
        Resident victimResident = universe.registerResident(victim.getUUID(), victim.getName().getString());
        if (attackerResident.getTown() != null && attackerResident.getTown() == victimResident.getTown()) {
            return;
        }

        event.setCanceled(true);
        attacker.sendSystemMessage(Component.literal("PVP is blocked here by town protection."));
    }
}
