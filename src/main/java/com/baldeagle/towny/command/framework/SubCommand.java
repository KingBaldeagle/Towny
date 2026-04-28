package com.baldeagle.towny.command.framework;

/**
 * Minimal abstraction for Towny sub-commands used by the phase-3 command framework.
 */
public interface SubCommand {
    String getName();

    String getPermission();

    int execute(TownyCommandContext context);
}
