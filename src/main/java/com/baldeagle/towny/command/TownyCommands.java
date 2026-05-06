package com.baldeagle.towny.command;

import com.baldeagle.towny.Towny;
import com.baldeagle.towny.Config;
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
import com.baldeagle.towny.command.town.TownInviteCommand;
import com.baldeagle.towny.command.town.TownKickCommand;
import com.baldeagle.towny.command.town.TownLeaveCommand;
import com.baldeagle.towny.command.town.TownListCommand;
import com.baldeagle.towny.command.town.TownNewCommand;
import com.baldeagle.towny.command.town.TownAcceptCommand;
import com.baldeagle.towny.command.town.TownDenyCommand;
import com.baldeagle.towny.command.town.TownDeleteCommand;
import com.baldeagle.towny.command.town.TownUnclaimCommand;
import com.baldeagle.towny.command.town.TownRankCommand;
import com.baldeagle.towny.command.town.TownSetCommand;
import com.baldeagle.towny.command.town.TownSpawnCommand;
import com.baldeagle.towny.command.town.TownNationJoinCommand;
import com.baldeagle.towny.command.town.TownNationLeaveCommand;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.economy.TownyEconomyHandler;
import com.baldeagle.towny.object.economy.TownyEconomyService;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.service.TownyWarService;
import com.baldeagle.towny.service.TownyPlaceholderService;
import com.baldeagle.towny.service.TownyChatService;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.concurrent.TimeUnit;

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
        REGISTRY.register("town", new TownInviteCommand());
        REGISTRY.register("town", new TownAcceptCommand());
        REGISTRY.register("town", new TownDenyCommand());
        REGISTRY.register("town", new TownDeleteCommand());
        REGISTRY.register("town", new TownRankCommand());
        REGISTRY.register("town", new TownSetCommand());
        REGISTRY.register("town", new TownSpawnCommand());
        REGISTRY.register("town", new TownNationJoinCommand());
        REGISTRY.register("town", new TownNationLeaveCommand());
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
            .then(Commands.literal("invite")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getResidents().stream().map(Resident::getName),
                        builder
                    ))
                    .executes(context -> REGISTRY.execute("town", "invite", context))))
            .then(Commands.literal("accept")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "accept", context)))
            .then(Commands.literal("deny")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "deny", context)))
            .then(Commands.literal("rank")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("name", StringArgumentType.word())
                    .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                        TownyUniverse.getInstance().getResidents().stream().map(Resident::getName),
                        builder
                    ))
                    .then(Commands.argument("rank", StringArgumentType.word())
                        .executes(context -> REGISTRY.execute("town", "rank", context)))))
            .then(Commands.literal("set")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.argument("key", StringArgumentType.word())
                    .then(Commands.argument("value", StringArgumentType.greedyString())
                        .executes(context -> REGISTRY.execute("town", "set", context)))))
            .then(Commands.literal("spawn")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "spawn", context)))
            .then(Commands.literal("delete")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .executes(context -> REGISTRY.execute("town", "delete", context)))
            .then(Commands.literal("nation")
                .requires(source -> source.hasPermission(0) && source.getPlayer() != null)
                .then(Commands.literal("join")
                    .then(Commands.argument("nation", StringArgumentType.word())
                        .suggests((context, builder) -> SharedSuggestionProvider.suggest(
                            TownyUniverse.getInstance().getNations().stream().map(com.baldeagle.towny.object.nation.Nation::getName),
                            builder
                        ))
                        .executes(context -> REGISTRY.execute("town", "nation_join", context))))
                .then(Commands.literal("leave")
                    .executes(context -> REGISTRY.execute("town", "nation_leave", context))))
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


        dispatcher.register(Commands.literal("tc")
            .requires(source -> source.getPlayer() != null)
            .then(Commands.argument("message", StringArgumentType.greedyString())
                .executes(context -> { var p = context.getSource().getPlayer(); var r = TownyUniverse.getInstance().registerResident(p.getUUID(), p.getName().getString()); TownyChatService.send(TownyChatService.Channel.TOWN, p, r, StringArgumentType.getString(context, "message")); return 1; })));
        dispatcher.register(Commands.literal("nc")
            .requires(source -> source.getPlayer() != null)
            .then(Commands.argument("message", StringArgumentType.greedyString())
                .executes(context -> { var p = context.getSource().getPlayer(); var r = TownyUniverse.getInstance().registerResident(p.getUUID(), p.getName().getString()); TownyChatService.send(TownyChatService.Channel.NATION, p, r, StringArgumentType.getString(context, "message")); return 1; })));
        dispatcher.register(Commands.literal("lc")
            .requires(source -> source.getPlayer() != null)
            .then(Commands.argument("message", StringArgumentType.greedyString())
                .executes(context -> { var p = context.getSource().getPlayer(); var r = TownyUniverse.getInstance().registerResident(p.getUUID(), p.getName().getString()); TownyChatService.send(TownyChatService.Channel.LOCAL, p, r, StringArgumentType.getString(context, "message")); return 1; })));
        dispatcher.register(Commands.literal("townywar")
            .requires(source -> source.getPlayer() != null)
            .then(Commands.literal("status").executes(context -> { context.getSource().sendSuccess(() -> Component.literal("Active wars=" + TownyWarService.activeWarCount()), false); return 1; }))
            .then(Commands.literal("declare").then(Commands.argument("nation", StringArgumentType.word()).executes(context -> {
                var p = context.getSource().getPlayer();
                var r = TownyUniverse.getInstance().registerResident(p.getUUID(), p.getName().getString());
                if (!r.hasTown() || !r.getTown().hasNation()) return 0;
                var n = TownyUniverse.getInstance().getNation(StringArgumentType.getString(context, "nation"));
                if (n.isEmpty()) return 0;
                boolean ok = TownyWarService.declareWar(r.getTown().getNation(), n.get());
                context.getSource().sendSuccess(() -> Component.literal(ok ? "War declared." : "War already active."), false);
                return ok ? 1 : 0;
            }))));
        dispatcher.register(Commands.literal("townypapi")
            .requires(source -> source.getPlayer() != null)
            .then(Commands.argument("placeholder", StringArgumentType.word())
                .executes(context -> { var p = context.getSource().getPlayer(); var r = TownyUniverse.getInstance().registerResident(p.getUUID(), p.getName().getString()); String key = StringArgumentType.getString(context, "placeholder"); context.getSource().sendSuccess(() -> Component.literal(key + "=" + TownyPlaceholderService.resolve(r, key)), false); return 1; })));

        dispatcher.register(Commands.literal("towny")
            .requires(source -> source.hasPermission(0))
            .executes(context -> {
                context.getSource().sendSuccess(() -> Component.literal(
                    "Phase 3 command set complete: town/resident/nation/plot core commands enabled"
                ), false);
                return 1;
            })
            .then(Commands.literal("economy")
                .executes(context -> {
                    String provider = Config.ECONOMY_PROVIDER.get();
                    int delinquentCount = TownyEconomyService.delinquentResidentCount();
                    int txCount = TownyEconomyService.recentTransactions().size();
                    context.getSource().sendSuccess(() -> Component.literal(
                        "Economy provider=" + provider
                            + ", resident tax interval=" + Config.TAX_COLLECTION_INTERVAL_TICKS.get() + " ticks"
                            + ", nation tax=" + Config.NATION_TAX_PERCENT.get() + "%"
                            + ", delinquent residents=" + delinquentCount
                            + ", tx log entries=" + txCount
                            + ", sample format=" + TownyEconomyHandler.provider().format(12_345)
                    ), false);
                    return 1;
                })));
    }

    private static int executeJail(com.mojang.brigadier.context.CommandContext<CommandSourceStack> context, boolean withFine) {
        String playerName = StringArgumentType.getString(context, "player");
        String durationInput = StringArgumentType.getString(context, "duration");
        long durationMillis = parseDurationMillis(durationInput);
        if (durationMillis <= 0L) {
            context.getSource().sendFailure(Component.literal("Invalid duration. Use formats like 10m, 2h, 1d."));
            return 0;
        }
        var targetOpt = TownyUniverse.getInstance().getResident(playerName);
        if (targetOpt.isEmpty()) {
            context.getSource().sendFailure(Component.literal("Resident not found: " + playerName));
            return 0;
        }
        var target = targetOpt.get();
        if (target.isJailed()) {
            context.getSource().sendFailure(Component.literal("Resident is already jailed."));
            return 0;
        }
        if (withFine) {
            int fine = IntegerArgumentType.getInteger(context, "fine");
            if (fine > 0) {
                String targetAccount = TownyEconomyHandler.accountIdForPlayer(target.getUUID());
                if (!TownyEconomyHandler.provider().withdraw(targetAccount, fine)) {
                    context.getSource().sendFailure(Component.literal("Resident cannot pay fine of " + fine + " copper."));
                    return 0;
                }
            }
        }
        target.jailForMillis(durationMillis);
        context.getSource().sendSuccess(() -> Component.literal("Jailed " + target.getName() + " for " + durationInput + "."), false);
        return 1;
    }

    private static long parseDurationMillis(String input) {
        if (input == null || input.isBlank()) {
            return -1L;
        }
        String value = input.trim().toLowerCase();
        if (value.length() < 2) {
            return -1L;
        }
        char unit = value.charAt(value.length() - 1);
        long amount;
        try {
            amount = Long.parseLong(value.substring(0, value.length() - 1));
        } catch (NumberFormatException ignored) {
            return -1L;
        }
        if (amount <= 0L) {
            return -1L;
        }
        return switch (unit) {
            case 'm' -> TimeUnit.MINUTES.toMillis(amount);
            case 'h' -> TimeUnit.HOURS.toMillis(amount);
            case 'd' -> TimeUnit.DAYS.toMillis(amount);
            default -> -1L;
        };
    }

}
