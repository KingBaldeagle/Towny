package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

/**
 * Phase-4 baseline block protection listener.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyBlockProtectionListener {
    private TownyBlockProtectionListener() {}

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        if (!TownyProtectionService.isTownyEnabled(player.level(), event.getPos())) {
            return;
        }
        if (!TownyProtectionService.canBuildAt(player, event.getPos())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("You cannot break blocks in this town block."));
        }
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!TownyProtectionService.isTownyEnabled(player.level(), event.getPos())) {
            return;
        }
        if (!TownyProtectionService.canBuildAt(player, event.getPos())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("You cannot place blocks in this town block."));
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }
        if (!TownyProtectionService.isTownyEnabled(player.level(), event.getPos())) {
            return;
        }
        if (!TownyProtectionService.canBuildAt(player, event.getPos())) {
            event.setCanceled(true);
            player.sendSystemMessage(Component.literal("This town block is protected."));
        }
    }
}
