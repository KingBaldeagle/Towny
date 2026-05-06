package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.service.TownyTeleportService;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Towny.MODID)
public final class TownyTeleportListener {
    private TownyTeleportListener() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof net.minecraft.server.level.ServerLevel serverLevel)) {
            return;
        }
        TownyTeleportService.tick(serverLevel);
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TownyTeleportService.cancelByMovement(player);
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingDamageEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            TownyTeleportService.cancelByDamage(player);
        }
    }
}
