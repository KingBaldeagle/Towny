package com.baldeagle.towny.command.framework;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry that maps parent+child command paths to executable Towny sub-commands.
 */
public final class CommandRegistry {
    private final Map<String, SubCommand> subCommands = new ConcurrentHashMap<>();

    public void register(String parent, SubCommand subCommand) {
        if (parent == null || parent.isBlank()) {
            throw new IllegalArgumentException("parent must not be blank");
        }
        if (subCommand == null) {
            throw new IllegalArgumentException("subCommand must not be null");
        }
        String key = buildKey(parent, subCommand.getName());
        subCommands.put(key, subCommand);
    }

    public int execute(String parent, String child, CommandContext<CommandSourceStack> context) {
        TownyCommandContext typedContext = new TownyCommandContext(context);
        if (!"jail".equalsIgnoreCase(parent) && !"unjail".equalsIgnoreCase(parent)) {
            ServerPlayer player = typedContext.getPlayerOrNull();
            if (player != null) {
                if (typedContext.getOrCreatePlayerResidentOrNull() != null
                    && typedContext.getOrCreatePlayerResidentOrNull().isJailed()) {
                    return typedContext.fail("You cannot use Towny commands while jailed.");
                }
            }
        }
        SubCommand subCommand = subCommands.get(buildKey(parent, child));
        if (subCommand == null) {
            return 0;
        }
        return subCommand.execute(typedContext);
    }

    private String buildKey(String parent, String child) {
        return parent.trim().toLowerCase() + "." + child.trim().toLowerCase();
    }
}
