# NeoTowny Implementation Plan

This document outlines the high‑level steps required to re‑implement the Bukkit Towny plugin as a NeoForge 1.21.1 mod, integrating Lightman's Currency for the economy.

## 1. Project scaffolding (already done)
- Gradle build files, `mods.toml`, main mod class `NeoTownyMod`.
- Add basic logging and event bus registration.

## 2. Core data model migration
1. **Resident** – port `Resident` class and related persistence.
2. **Town** – port `Town` with claim handling, residents list, permissions.
3. **Nation** – port `Nation` and nation‑level mechanics.
4. **Plot** – port plot data structures and ownership.
5. **World / TownyUniverse** – central registry for all objects.

### Current Status (as of 2026-04-28)
- ✅ Resident/Town/Nation linkage skeleton is implemented.
- ✅ Initial in-memory `TownyUniverse` registry added (resident/town/nation/plot create + lookup flows).
- ✅ Added phase-1 value/object types for world/claim modeling (`Coord`, `WorldCoord`, `TownyWorld`, `TownBlock`, `TownBlockType`) and wired basic claim flow in `TownyUniverse`.
- ✅ **Phase 1 core data model migration is complete** (Resident, Town, Nation, Plot, World/TownyUniverse in place).
- 🚧 Persistence wiring is intentionally deferred to Phase 2, along with command integration.

## 3. Configuration & persistence
- Convert `towny.yml` and other YAML configs to NeoForge‑compatible config (ForgeConfigSpec).
- Implement data storage using Minecraft’s `LevelSavedData` (NBT) for towns, nations, residents.
- Provide migration scripts from existing flat‑file format if needed.

### Phase 2 Kickoff Status (as of 2026-04-28)
- ✅ Added `TownyDataSource` abstraction for load/save operations.
- ✅ Added `JsonTownyDataSource` bootstrap implementation writing to `config/towny/universe.json`.
- ✅ JSON snapshot now covers Residents, Towns, Nations, Worlds, TownBlocks, and Plots.
- ✅ Added directory-based JSON storage (`residents/`, `towns/`, `nations/`, `townblocks/`, `worlds/`, `plots/`).
- ✅ Added persistence backend config selection (`json_snapshot` vs `directory_json`) via common config.
- ✅ **Phase 2 is complete for the current JSON persistence track.**
- 🚧 Optional future enhancement: move from JSON backends to `LevelSavedData` once command/event lifecycle is fully wired.

## 4. Command framework migration
- Replace Bukkit command registration with NeoForge's `CommandDispatcher` (`net.minecraft.commands.Commands`).
- Implement `/town`, `/nation`, `/plot`, `/resident` sub‑commands following the spec files in `dev-docs/specs/commands*.md`.
- Hook commands into the new data model.

### Phase 3 Progress Status (as of 2026-04-28)
- ✅ Added a lightweight command framework (`CommandRegistry` + `SubCommand`) for modular subcommand wiring.
- ✅ Registered NeoForge command families with aliases: `/town|/t`, `/resident|/res`, `/nation|/n`, `/plot claim`, and `/towny` status output.
- ✅ Wired `/town new` to the in-memory `TownyUniverse` so player residents/towns are created through existing domain logic.
- ✅ Added typed command-context helpers for resident lookup, player-townblock mapping, and plot/townblock lookups.
- ✅ Implemented core Phase 3 command families and aliases: `/town|/t` (`new`,`list`,`here`,`add`,`kick`,`leave`,`claim`,`unclaim`), `/resident|/res` (`self`,`show`), `/nation|/n` (`new`,`list`,`add`,`kick`,`leave`), and `/plot` (`claim`,`unclaim`).
- ✅ **Phase 3 command migration is complete for the currently scoped in-memory model implementation.**
- 🚧 Next: finish parity polish from `commands_detailed.md` (advanced flags/options), add Towny permission-node resolver, and integrate persistence-backed command side effects.

### Command Boilerplate Reduction Strategy

To reduce the 17k+ lines of command boilerplate, implement a custom command framework:

#### 4.1 Base Command Class

```java
public abstract class TownyCommand {
    public abstract String getPermission();
    public abstract List<String> onTabComplete(String[] args);
    public abstract boolean execute(ServerPlayer player, String[] args);
    
    // Auto-generates help, tab complete, error handling
}
```

#### 4.2 Subcommand Registry Pattern

```java
public class CommandRegistry {
    private final Map<String, SubCommand> subCommands = new HashMap<>();
    
    public void register(String parent, String sub, SubCommand cmd) {
        subCommands.put(parent + "." + sub, cmd);
    }
    
    public void execute(String path, CommandContext ctx) {
        subCommands.get(path).execute(ctx);
    }
}
```

#### 4.3 Argument Type Wrappers

```java
// Instead of raw arguments
StringArgumentType.getString(context, "name")

// Use typed wrappers
ctx.getTown("townname");      // Validates town exists
ctx.getResident("playername"); // Validates resident exists
ctx.getWorldCoord();           // Gets WorldCoord from player position
ctx.getTownBlock();            // Gets TownBlock from position
```

#### 4.4 Command Builder (Optional)

```java
CommandBuilder.town("new")
    .arg("name", StringArgumentType.word(), TownyValidation::validateTownName)
    .permission("towny.command.town.new")
    .executes(ctx -> TownCommand.newTown(ctx.getSource().getPlayer(), ctx.get("name")));
```

#### 4.6 Boilerplate Comparison

**Normal NeoForge Command (verbose):**

```java
// ~30 lines for ONE simple subcommand
dispatcher.register(literal("town")
    .then(literal("new")
        .requires(source -> source.getPlayer() != null)
        .then(argument("name", StringArgumentType.word())
            .suggests((ctx, builder) -> {
                return SharedSuggestionsProvider.suggest(
                    Arrays.asList("town1", "town2", "town3"),
                    builder
                );
            })
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                String name = context.getArgument("name", String.class);
                
                // Permission check
                if (!player.hasPermission("towny.command.town.new")) {
                    player.sendSystemMessage(Component.literal("No permission"));
                    return 0;
                }
                
                // Validation
                if (TownyUniverse.getInstance().hasTown(name)) {
                    player.sendSystemMessage(Component.literal("Town already exists"));
                    return 0;
                }
                
                // Execute
                try {
                    TownyUniverse.getInstance().newTown(name);
                    player.sendSystemMessage(Component.literal("Town created!"));
                } catch (Exception e) {
                    player.sendSystemMessage(Component.literal("Error: " + e.getMessage()));
                }
                return 1;
            })
        )
    )
    .then(literal("claim")
        .requires(source -> source.getPlayer() != null)
        .executes(context -> {
            // ... another 30 lines
        })
    )
    // Repeat for 50+ subcommands
);
```

---

**With Our Framework (concise):**

```java
// Framework base class (~50 lines, written once)
public abstract class TownyCommand {
    public abstract String getPermission();
    public abstract List<String> tabComplete(String[] args);
    public abstract boolean execute(ServerPlayer player, String[] args);
    
    // Common error handling built-in
    protected void sendError(ServerPlayer player, String msg) { ... }
    protected void sendSuccess(ServerPlayer player, String msg) { ... }
    protected boolean hasPermission(ServerPlayer player) { ... }
}

// Individual commands (~5 lines each)
public class TownNewCommand extends TownyCommand {
    public String getPermission() { return "towny.command.town.new"; }
    
    public boolean execute(ServerPlayer player, String[] args) {
        String name = args[0];
        
        if (TownyUniverse.getInstance().hasTown(name))
            return sendError(player, "Town already exists");
        
        TownyUniverse.getInstance().newTown(name);
        return sendSuccess(player, "Town created!");
    }
    
    public List<String> tabComplete(String[] args) {
        return args.length == 1 ? Arrays.asList("town1", "town2") : Collections.emptyList();
    }
}

// Registration (~3 lines per command)
CommandRegistry.register("town", "new", new TownNewCommand());
CommandRegistry.register("town", "claim", new TownClaimCommand());
CommandRegistry.register("town", "spawn", new TownSpawnCommand());
// ... 50+ more commands

// Single dispatcher entry point (~10 lines)
dispatcher.register(literal("town")
    .executes(ctx -> {
        ServerPlayer player = ctx.getSource().getPlayer();
        return CommandRegistry.execute("town", ctx.getInput().split(" "), player);
    })
);
```

---

**Line Count Comparison:**

| Approach | 50 Subcommands |
|----------|----------------|
| Raw NeoForge | ~1,500 lines |
| Our Framework | ~300 lines (250 cmd + 50 base) |

**Savings: ~80% reduction in command boilerplate**

## 5. Event handling
- Replace Bukkit listeners with NeoForge events (`net.minecraftforge.eventbus.api.SubscribeEvent`).
- Migrate essential listeners: player login/logout, block break/place, entity damage, teleport, tick updates.
- Ensure events fire before/after core actions to allow other mods to intercept.

## 6. Economy integration (Lightmans Currency)
1. Define `EconomyProvider` interface (done).
2. Implement `LightmansCurrencyProvider` with proper API calls (TODO – replace fallback map with real wallet handling).
3. Add provider selection based on `towny.yml` (read during mod init).
4. Update all monetary operations (taxes, plot sales, town banks) to use the provider.
5. Add fallback `LegacyProvider` for servers without Lightmans Currency.

## 7. Tax, Upkeep & Daily tasks
- Implement daily scheduler using `TickEvent` or `ScheduledExecutor` to run `TownyTimerTask` equivalents.
- Port tax calculation logic from `tax.md` and `levels_upkeep.md`.

## 8. Permissions system
- Migrate Bukkit permission checks to NeoForge’s permission API or use a lightweight custom system.
- Preserve compatibility with LuckPerms via integration hooks.

## 9. GUI / UI (optional for now)
- Port essential GUIs (town info, nation list) using NeoForge’s screen system.
- Defer advanced UI work until core mechanics are stable.

## 10. Testing & QA
- Write unit tests for core data classes (use JUnit 5).
- Create integration tests that spin up a test server, execute commands, and verify state.
- Verify Lightmans Currency transactions work in‑game.

## 11. Documentation & Packaging
- Update `dev-docs/README.md` with build/run instructions.
- Publish to CurseForge/Maven with proper versioning.

---
### Execution Order Summary
1. Scaffold (already complete)
2. Core data model
3. Config & persistence
4. Command framework
5. Event handling
6. Economy provider
7. Tax & daily tasks
8. Permissions
9. Optional GUIs
10. Testing & QA
11. Docs & release

Follow this order to ensure each subsystem has its dependencies satisfied before moving to the next stage.
