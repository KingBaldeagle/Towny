package com.baldeagle.towny.command;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.command.framework.CommandRegistry;
import com.baldeagle.towny.command.nation.NationAddCommand;
import com.baldeagle.towny.command.nation.NationKickCommand;
import com.baldeagle.towny.command.nation.NationLeaveCommand;
import com.baldeagle.towny.command.nation.NationListCommand;
import com.baldeagle.towny.command.nation.NationNewCommand;
import com.baldeagle.towny.command.plot.PlotClaimCommand;
import com.baldeagle.towny.command.plot.PlotUnclaimCommand;
import com.baldeagle.towny.command.resident.ResidentSelfCommand;
import com.baldeagle.towny.command.resident.ResidentShowCommand;
import com.baldeagle.towny.command.town.TownAddCommand;
import com.baldeagle.towny.command.town.TownClaimCommand;
import com.baldeagle.towny.command.town.TownHereCommand;
import com.baldeagle.towny.command.town.TownKickCommand;
import com.baldeagle.towny.command.town.TownLeaveCommand;
import com.baldeagle.towny.command.town.TownListCommand;
import com.baldeagle.towny.command.town.TownNewCommand;
import com.baldeagle.towny.command.town.TownUnclaimCommand;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * Phase-3 command entry point.
 */
@EventBusSubscriber(modid = Towny.MODID)
public final class TownyCommands {
    private static final CommandRegistry REGISTRY = new CommandRegistry();

    private TownyCommands() {}

    static {
        REGISTRY.register("town", new TownNewCommand());
        REGISTRY.register("town", new TownListCommand());
        REGISTRY.register("town", new TownHereCommand());
        REGISTRY.register("town", new TownAddCommand());
        REGISTRY.register("town", new TownKickCommand());
        REGISTRY.register("town", new TownLeaveCommand());
        REGISTRY.register("town", new TownClaimCommand());
        REGISTRY.register("town", new TownUnclaimCommand());

        REGISTRY.register("resident", new ResidentShowCommand());
        REGISTRY.register("resident", new ResidentSelfCommand());

        REGISTRY.register("nation", new NationNewCommand());
        REGISTRY.register("nation", new NationListCommand());
        REGISTRY.register("nation", new NationAddCommand());
        REGISTRY.register("nation", new NationKickCommand());
        REGISTRY.register("nation", new NationLeaveCommand());

        REGISTRY.register("plot", new PlotClaimCommand());
        REGISTRY.register("plot", new PlotUnclaimCommand());
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("town")
            .then(Commands.literal("new")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .executes(context -> REGISTRY.execute("town", "new", context))))
            .then(Commands.literal("list")
                .requires(source -> source.hasPermission(0))
                .executes(context -> REGISTRY.execute("town", "list", context)))
            .then(Commands.literal("here")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "here", context)))
            .then(Commands.literal("add")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getResidents().stream().map(Resident::getName),
                        builder
                    ))
                    .executes(context -> REGISTRY.execute("town", "add", context))))
            .then(Commands.literal("kick")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getResidents().stream().map(Resident::getName),
                        builder
                    ))
                    .executes(context -> REGISTRY.execute("town", "kick", context))))
            .then(Commands.literal("leave")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "leave", context)))
            .then(Commands.literal("claim")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "claim", context)))
            .then(Commands.literal("unclaim")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "unclaim", context))));

        dispatcher.register(Commands.literal("t")
            .redirect(dispatcher.getRoot().getChild("town")));

        dispatcher.register(Commands.literal("resident")
            .requires(source -> source.hasPermission(0))
            .executes(context -> REGISTRY.execute("resident", "self", context))
            .then(Commands.argument("name", StringArgumentType.word())
                .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                    TownyUniverse.getInstance().getResidents().stream().map(Resident::getName),
                    builder
                ))
                .executes(context -> REGISTRY.execute("resident", "show", context))));

        dispatcher.register(Commands.literal("res")
            .redirect(dispatcher.getRoot().getChild("resident")));

        dispatcher.register(Commands.literal("nation")
            .then(Commands.literal("new")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .executes(context -> REGISTRY.execute("nation", "new", context))))
            .then(Commands.literal("list")
                .requires(source -> source.hasPermission(0))
                .executes(context -> REGISTRY.execute("nation", "list", context)))
            .then(Commands.literal("add")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("town", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getTowns().stream().map(Town::getName),
                        builder
                    ))
                    .executes(context -> REGISTRY.execute("nation", "add", context))))
            .then(Commands.literal("kick")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("town", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getTowns().stream().map(Town::getName),
                        builder
                    ))
                    .executes(context -> REGISTRY.execute("nation", "kick", context))))
            .then(Commands.literal("leave")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("nation", "leave", context))));

        dispatcher.register(Commands.literal("n")
            .redirect(dispatcher.getRoot().getChild("nation")));

        dispatcher.register(Commands.literal("plot")
            .then(Commands.literal("claim")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("plot", "claim", context)))
            .then(Commands.literal("unclaim")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("plot", "unclaim", context))));

        dispatcher.register(Commands.literal("towny")
            .requires(source -> source.hasPermission(0))
            .executes(context -> {
                context.getSource().sendSuccess(() -> Component.literal(
                    "Phase 3 command set complete: town/resident/nation/plot core commands enabled"
                ), false);
                return 1;
            }));
    }
}
