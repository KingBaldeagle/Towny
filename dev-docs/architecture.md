# NeoTowny Architecture

## Overview

NeoTowny is a port of the Towny plugin (Bukkit/Paper) to NeoForge 1.21.1. It implements a Resident-Town-Nation hierarchy with land protection, economy, and permissions.

This document describes the high-level architecture and design decisions.

---

## Core Design Principles

1. **Data-first**: Core objects (Resident, Town, Nation) defined first; everything else builds on them
2. **Simplification over feature-parity**: Don't replicate every edge case from Bukkit Towny
3. **Native NeoForge**: Use NeoForge idioms (registries, events, commands) rather than Bukkit patterns
4. **JSON persistence**: Simple file-based storage instead of complex YAML/flatfile/MySQL

---

## Package Structure

```
src/main/java/com/baldeagle/towny/
├── Towny.java                    # Mod entry point
├── Config.java                   # Configuration
├── api/
│   └── TownyAPI.java             # Public API for other mods
├── command/
│   ├── CommandRegistry.java      # Command routing
│   ├── TownyCommand.java         # Base command class
│   ├── TownCommand.java          # /town subcommands
│   ├── NationCommand.java        # /nation subcommands
│   ├── PlotCommand.java          # /plot subcommands
│   ├── ResidentCommand.java      # /resident subcommands
│   ├── TownyCommand.java         # /towny main command
│   └── TownyAdminCommand.java    # /townyadmin commands
├── db/
│   ├── DataSource.java           # Data persistence interface
│   └── JsonDataSource.java       # JSON file implementation
├── event/
│   ├── TownyEventBus.java        # Custom event system
│   └── events/                   # Towny-specific events
├── listener/
│   ├── BlockListener.java        # Block protection
│   ├── EntityListener.java       # Combat/mob protection
│   ├── PlayerListener.java       # Login/logout/spawn
│   └── WorldListener.java        # World events
├── object/
│   ├── TownyObject.java          # Base class
│   ├── Government.java           # Town/Nation base
│   ├── Resident.java             # Player data
│   ├── Town.java                 # Town data
│   ├── Nation.java               # Nation data
│   ├── TownBlock.java            # Claimed plot
│   ├── TownyWorld.java           # World settings
│   ├── WorldCoord.java           # Coordinate utility
│   ├── Position.java             # Location utility
│   └── permission/
│       └── TownyPermission.java  # Permission flags
├── settings/
│   ├── TownySettings.java        # Config values
│   └── TownyPermissions.java     # Permission nodes
└── task/
    ├── DailyTimerTask.java       # Taxes/upkeep
    ├── HealthRegenTask.java      # Town healing
    └── PlotRegenTask.java        # Block regeneration
```

---

## Core Objects

### Inheritance Hierarchy

```
TownyObject (abstract)
    ├── Resident
    ├── Government (abstract)
    │   ├── Town
    │   └── Nation
    ├── TownBlock
    ├── TownyWorld
    ├── PlotGroup
    └── District
```

### Key Classes

#### TownyObject
Base class for all Towny objects.
```java
public abstract class TownyObject {
    private final UUID uuid;
    private final String name;
    private final Map<String, Object> metadata = new HashMap<>();
    
    public UUID getUUID();
    public String getName();
    public void setMetadata(String key, Object value);
    public Object getMetadata(String key);
}
```

#### Resident
Represents a player in the system.
```java
public class Resident extends TownyObject {
    private Town town;                    // Current town (can be null)
    private List<Resident> friends;       // Friend list
    private List<TownBlock> townBlocks;   // Personally owned plots
    private String title;                 // Mayoral/nation prefix
    private String surname;               // Postfix
    private List<String> townRanks;       // Town-specific ranks
    private List<String> nationRanks;     // Nation-specific ranks
    private boolean isJailed;
    private JailData jailData;
    
    public boolean hasTown();
    public boolean hasNation();
    public boolean isMayor();
    public boolean isKing();
}
```

#### Town
A collection of residents with claimed land.
```java
public class Town extends Government {
    private final List<Resident> residents;
    private Resident mayor;
    private Nation nation;
    private final Map<WorldCoord, TownBlock> townBlocks;
    private TownBlock homeBlock;          // Spawn location
    private Position spawn;
    
    // Town settings
    private boolean pvp, explosion, fire, mobs;
    private boolean isPublic, isOpen, isNeutral;
    
    // Economy
    private double taxes, plotTax, shopTax, embassyTax;
    private double bankBalance;
    
    public int getMaxTownBlocks();        // Based on residents/level
    public int availableTownBlocks();
}
```

#### Nation
A collection of towns.
```java
public class Nation extends Government {
    private final List<Town> towns;
    private Town capital;
    private final Map<UUID, Nation> allies;
    private final Map<UUID, Nation> enemies;
    private Position spawn;
    
    public int getTotalResidents();       // Sum across all towns
}
```

#### TownBlock
A single claimed plot (default 16x16 chunks).
```java
public class TownBlock extends TownyObject {
    private final WorldCoord worldCoord;  // Immutable
    private Town town;
    private Resident resident;            // Plot owner (optional)
    private TownBlockType type;           // normal/shop/embassy/etc.
    private boolean outpost;              // Is outpost claim
    private TownyPermission permissions;
}
```

#### WorldCoord
Identifies a location in the Towny grid.
```java
public record WorldCoord(String worldName, int x, int z) {
    public static WorldCoord of(Level level, BlockPos pos) {
        int townBlockSize = TownySettings.getTownBlockSize();
        return new WorldCoord(
            level.getName().asString(),
            pos.getX() / townBlockSize,
            pos.getZ() / townBlockSize
        );
    }
}
```

---

## Data Persistence

### JSON-Based Storage

```
config/neotowny/
├── residents/
│   └── {uuid}.json
├── towns/
│   └── {uuid}.json
├── nations/
│   └── {uuid}.json
├── townblocks/
│   └── {world}/
│       └── {x},{z}.json
├── worlds/
│   └── {name}.json
└── plotgroups/
    └── {uuid}.json
```

### DataSource Interface

```java
public interface DataSource {
    void loadAll();
    void saveAll();
    
    // Individual saves
    void saveResident(Resident resident);
    void saveTown(Town town);
    void saveNation(Nation nation);
    void saveTownBlock(TownBlock townBlock);
    void saveTownyWorld(TownyWorld world);
}
```

---

## Command System

### Architecture

```
CommandDispatcher (NeoForge)
    │
    ▼
TownyCommand (base class)
    │
    ├── TownyCommand.execute() - entry point
    ├── TownyCommand.tabComplete() - autocomplete
    │
    ▼
CommandRegistry (routes to specific commands)
    │
    ├── "town" → TownCommand
    ├── "nation" → NationCommand
    ├── "plot" → PlotCommand
    └── "resident" → ResidentCommand
```

### Base Command Class

```java
public abstract class TownyCommand {
    public abstract String getPermission();
    public abstract List<String> tabComplete(String[] args);
    public abstract boolean execute(ServerPlayer player, String[] args);
    
    // Built-in helpers
    protected void sendError(ServerPlayer player, String message);
    protected void sendSuccess(ServerPlayer player, String message);
    protected boolean hasPermission(ServerPlayer player);
    protected Town getTown(String name);           // Validated
    protected Resident getResident(String name);   // Validated
}
```

### Example Command

```java
public class TownNewCommand extends TownyCommand {
    public String getPermission() { return "towny.command.town.new"; }
    
    public boolean execute(ServerPlayer player, String[] args) {
        String name = args[0];
        
        if (TownyUniverse.getInstance().hasTown(name))
            return sendError(player, "Town already exists");
        
        TownyUniverse.getInstance().newTown(name, player);
        return sendSuccess(player, "Town created!");
    }
    
    public List<String> tabComplete(String[] args) {
        return args.length == 1 ? List.of("town1", "town2") : List.of();
    }
}
```

### Registration

```java
CommandRegistry.register("town", "new", new TownNewCommand());
CommandRegistry.register("town", "claim", new TownClaimCommand());
CommandRegistry.register("town", "spawn", new TownSpawnCommand());
// ... etc
```

---

## Event System

### NeoForge Events (Used Directly)

```java
@SubscribeEvent
public void onBlockPlace(BlockPlaceEvent event) {
    // Check protection
}
```

### Custom Towny Events

```java
// Example: TownClaimEvent
public class TownClaimEvent extends Event {
    private final Town town;
    private final WorldCoord coord;
    private final ServerPlayer player;
    private boolean cancelled;
    
    // Standard Event methods
    public boolean isCancelled();
    public void setCancelled(boolean cancelled);
}
```

### Event Flow

```
Player Action (block place)
    │
    ▼
NeoForge Event (BlockPlaceEvent)
    │
    ▼
Towny Listener (BlockListener.onBlockPlace())
    │
    ├── Check if Towny is enabled in world
    ├── Get WorldCoord from position
    ├── Get TownBlock (if claimed)
    │   ├── Wilderness → check wilderness rules
    │   └── Claimed → check permissions
    │
    ▼
Towny Action Event (TownyBuildEvent) - Cancellable
    │
    ▼
Permission Check (TownyPermission.canBuild())
    │
    ▼
Allow/Deny
```

---

## Protection System

### Permission Types

```java
public class TownyPermission {
    private boolean build;
    private boolean destroy;
    private boolean switchBlock;   // Levers, buttons
    private boolean itemUse;      // Items like buckets
    
    // Who this applies to (tracked separately)
    private boolean residentBuild;
    private boolean allyBuild;
    private boolean outsiderBuild;
    // ... same for destroy, switch, itemUse
}
```

### Permission Hierarchy

```
TownyPermission
    │
    ├── Resident personal plot → resident permissions
    │       (can be customized per-plot)
    │
    ├── Town-owned plot → town permissions
    │       (default for all town plots)
    │
    ├── TownBlockType permissions
    │       (shop, embassy, arena, etc. have defaults)
    │
    └── World (wilderness) → world permissions
```

### Protection Flow

```
onBlockPlace() → BlockListener
    │
    ├─► WorldCoord coord = WorldCoord.of(level, pos)
    │
    ├─► TownBlock tb = TownyAPI.getTownBlock(coord)
    │
    ├─► if (tb == null)  // Wilderness
    │       ├─► if (!world.isWildernessBuild()) deny
    │       └─► check wilderness permission
    │
    └─► else  // Claimed
            ├─► Town town = tb.getTown()
            ├─► Resident owner = tb.getResident()
            │
            ├─► Determine perm set:
            │   ├─► If plot has owner → resident perms
            │   └─► Else → town perms
            │
            ├─► Determine who is interacting:
            │   ├─► Same resident → RESIDENT
            │   ├─► Same town/nation → ALLY
            │   └─► Other → OUTSIDER
            │
            └─► Check appropriate permission flag
```

---

## Economy System

### Architecture

```java
public interface EconomyProvider {
    void withdraw(String accountId, double amount);
    void deposit(String accountId, double amount);
    double getBalance(String accountId);
    boolean hasAccount(String accountId);
}
```

### Lightman's Currency Integration

NeoTowny uses **Lightman's Currency** for all economy operations. This is a **required** dependency.

#### Why Lightman's Currency?

- Native NeoForge mod (not a Bukkit port)
- Supports player wallets
- Supports custom account types (perfect for Town/Nation banks)
- Active development for 1.21.1

#### Integration Pattern

```java
public class LightmansEconomyProvider implements EconomyProvider {
    private final CurrencyAPI api;
    private final boolean available;
    
    public LightmansEconomyProvider() {
        this.api = NeoForge.EVENT_BUS.add(
            new LookupModEvent<CurrencyAPI>("lightmanscurrency")
        );
        this.available = (api != null);
    }
    
    @Override
    public boolean isActive() {
        return available && api.isInitialized();
    }
    
    @Override
    public void withdraw(String accountId, double amount) {
        if (!isActive()) return;
        
        // Handle different account types
        if (accountId.startsWith("town:")) {
            // Town bank account
            UUID townUuid = UUID.fromString(accountId.substring(5));
            api.getAccount(CurrencyAccount.Type.CUSTOM)
               .orElseCreate("town_" + townUuid)
               .withdraw(amount);
        } else if (accountId.startsWith("nation:")) {
            // Nation bank account
            UUID nationUuid = UUID.fromString(accountId.substring(7));
            api.getAccount(CurrencyAccount.Type.CUSTOM)
               .orElseCreate("nation_" + nationUuid)
               .withdraw(amount);
        } else {
            // Player wallet
            api.getWallet(accountId).ifPresent(wallet -> {
                wallet.withdraw(amount);
                wallet.sync();
            });
        }
    }
    
    @Override
    public void deposit(String accountId, double amount) {
        if (!isActive()) return;
        
        if (accountId.startsWith("town:")) {
            UUID townUuid = UUID.fromString(accountId.substring(5));
            api.getAccount(CurrencyAccount.Type.CUSTOM)
               .orElseCreate("town_" + townUuid)
               .deposit(amount);
        } else if (accountId.startsWith("nation:")) {
            UUID nationUuid = UUID.fromString(accountId.substring(7));
            api.getAccount(CurrencyAccount.Type.CUSTOM)
               .orElseCreate("nation_" + nationUuid)
               .deposit(amount);
        } else {
            api.getWallet(accountId).ifPresent(wallet -> {
                wallet.deposit(amount);
                wallet.sync();
            });
        }
    }
    
    @Override
    public double getBalance(String accountId) {
        if (!isActive()) return 0.0;
        
        if (accountId.startsWith("town:") || accountId.startsWith("nation:")) {
            return api.getAccount(CurrencyAccount.Type.CUSTOM, accountId)
                      .map(acc -> acc.getBalance())
                      .orElse(0.0);
        }
        
        return api.getWallet(accountId)
                  .map(wallet -> wallet.getBalance())
                  .orElse(0.0);
    }
    
    @Override
    public boolean hasAccount(String accountId) {
        if (!isActive()) return false;
        
        if (accountId.startsWith("town:") || accountId.startsWith("nation:")) {
            return api.getAccount(CurrencyAccount.Type.CUSTOM, accountId).isPresent();
        }
        
        return api.getWallet(accountId).isPresent();
    }
}
```

#### Account ID Schema

| Account Type | ID Format | Example |
|--------------|-----------|---------|
| Player | UUID or name | `player-uuid` or `Notch` |
| Town | `town:{uuid}` | `town:550e8400-e29b-41d4-a716-446655440000` |
| Nation | `nation:{uuid}` | `nation:550e8400-e29b-41d4-a716-446655440000` |

#### Detection

```java
public class EconomyManager {
    private EconomyProvider provider;
    
    public EconomyManager() {
        // Lightman's Currency is REQUIRED
        // Throw error if not present
        if (!isModLoaded("lightmanscurrency")) {
            throw new RuntimeException(
                "NeoTowny requires Lightman's Currency! " +
                "Please install Lightman's Currency before running NeoTowny."
            );
        }
        
        this.provider = new LightmansEconomyProvider();
        
        if (!provider.isActive()) {
            throw new RuntimeException(
                "Lightman's Currency failed to initialize. " +
                "Please check your Lightman's Currency configuration."
            );
        }
        
        Towny.LOGGER.info("Economy initialized with Lightman's Currency");
    }
}
```

**Note:** Unlike Bukkit Towny which supports multiple economy backends (Vault, Reserve, or closed economy), NeoTowny **requires** Lightman's Currency. This simplifies the economy layer significantly.

#### Transaction Tracking

Lightman's Currency provides transaction history. NeoTowny should log Towny-specific transactions for audit:

```java
public class EconomyTransaction {
    private final String accountId;
    private final double amount;
    private final TransactionType type;  // TAX, PLOT_PURCHASE, DEPOSIT, WITHDRAW
    private final long timestamp;
    private final String description;
}

// Store transactions in data folder
// config/neotowny/economy/transactions/{year}/{month}/{day}.json
```

### Economy Flow

```
Daily Timer Task
    │
    ▼
For each Town:
    ├─► Calculate tax per resident
    │   ├─► Town tax (flat or %)
    │   └─► Plot tax (per owned plot)
    │
    ├─► Deduct from resident accounts
    │
    └─► Deposit to town bank
    
For each Nation:
    ├─► Calculate nation tax
    ├─► Deduct from town banks
    └─► Deposit to nation bank
```

---

## Timer Tasks

### Daily Timer (Taxes, Upkeep)

Runs once per game day (or configurable interval).

```java
public class DailyTimerTask {
    public void run() {
        for (Town town : towns) {
            collectTaxes(town);
            chargeUpkeep(town);
        }
        for (Nation nation : nations) {
            collectTaxes(nation);
        }
    }
}
```

### Health Regeneration

Players in friendly territory regenerate health faster.

```java
public class HealthRegenTask {
    public void run(ServerPlayer player) {
        WorldCoord coord = WorldCoord.of(player);
        if (isFriendlyTerritory(player, coord)) {
            // Apply bonus regeneration
        }
    }
}
```

---

## Configuration

### Main Config

```java
public class TownySettings {
    // World settings
    private static int townBlockSize = 16;
    private static int townBlockRatio = 8;  // Blocks per resident
    private static int maxTownBlocks = 0;   // 0 = unlimited
    
    // Economy
    private static double priceNewTown = 100.0;
    private static double priceClaimTownBlock = 10.0;
    private static double defaultTax = 0.0;
    private static boolean usingEconomy = true;
    
    // Features
    private static boolean wildernessBuild = false;
    private static boolean explosionsEnabled = true;
    private static boolean fireSpreadEnabled = true;
}
```

### World Settings (per-world)

```java
public class TownyWorld {
    private boolean usingTowny;
    private boolean claimable;
    private boolean pvp;
    private boolean explosion;
    private boolean fire;
    private boolean worldMobs;
    private boolean wildernessMobs;
    private boolean endermenProtect;
}
```

---

## Testing Strategy

### Unit Tests
- Test core object logic (Town.create(), Resident.setTown(), etc.)
- Test permission calculations
- Test coordinate conversions

### Integration Tests
- Spawn test server
- Create towns, claim blocks
- Verify protection works
- Test economy transactions

### Manual Testing
- Multi-player scenarios
- Edge cases (outposts, nations, war)

---

## Dependencies

### Required
- NeoForge (1.21.1)
- Minecraft (1.21.1)
- **Lightman's Currency** - Economy mod (REQUIRED)

### Optional
- LuckPerms (permissions)

---

## Performance Considerations

1. **WorldCoord caching**: Store TownBlocks in ConcurrentHashMap keyed by WorldCoord for O(1) lookups
2. **Permission caching**: Cache permission checks per player/position in PlayerCache
3. **Async saving**: Save data on async thread, don't block server
4. **Lazy loading**: Only load data when needed, not all at startup
5. **Event throttling**: Don't run heavy logic on every tick

---

## Future Considerations

- GUI screens for town/nation management
- Dynmap integration
- War system
- Chat channels (TownyChat)
- PAPI placeholders