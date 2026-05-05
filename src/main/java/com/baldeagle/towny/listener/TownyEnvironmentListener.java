package com.baldeagle.towny.listener;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.service.TownyProtectionService;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

/**
 * Phase-4 environment protections for explosions, fire spread, and mob griefing.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyEnvironmentListener {
    private TownyEnvironmentListener() {}

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().removeIf(pos -> !TownyProtectionService.isExplosionAllowedAt(event.getLevel(), pos));
    }

    @SubscribeEvent
    public static void onFarmlandTrample(BlockEvent.FarmlandTrampleEvent event) {
        BlockPos pos = event.getPos();
        if (event.getLevel() instanceof Level level && !TownyProtectionService.isMobGriefAllowedAt(level, pos)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        if (event.getNotifiedSides().isEmpty()) {
            return;
        }

        if (event.getLevel() instanceof Level level && !TownyProtectionService.isFireAllowedAt(level, event.getPos())) {
            event.setCanceled(true);
        }
    }
}
