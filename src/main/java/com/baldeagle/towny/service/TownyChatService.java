package com.baldeagle.towny.service;

import com.baldeagle.towny.object.resident.Resident;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public final class TownyChatService {
    public enum Channel { LOCAL, TOWN, NATION, ALLY }

    private TownyChatService() {}

    public static void send(Channel channel, ServerPlayer sender, Resident resident, String message) {
        PlayerList players = sender.server.getPlayerList();
        String prefix = "[" + channel.name().toLowerCase() + "] " + sender.getName().getString() + ": ";
        for (ServerPlayer target : players.getPlayers()) {
            if (channel == Channel.LOCAL && target.distanceTo(sender) > 128f) continue;
            target.sendSystemMessage(Component.literal(prefix + message));
        }
    }
}
