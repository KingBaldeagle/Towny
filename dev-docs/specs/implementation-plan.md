# NeoTowny Implementation Plan

This document outlines the implementation of Towny features into NeoForge 1.21.1. Towny is a complex Bukkit plugin for managing Resident-Town-Nation hierarchies with land protection, economy, permissions, and more.

## Overview

Towny (Bukkit/Paper) manages a **Resident -> Town -> Nation** hierarchy with:
- Grid-based land claiming (TownBlocks, default 16x16)
- Per-plot permissions (build, destroy, switch, itemuse)
- Economy (taxes, plot sales, upkeep)
- Multi-world settings
- Optional: War, Chat, Jailing

**Porting Strategy**: Phase-based implementation starting with core data models, then commands, then listeners, then optional features.

---

## Phase 1: Core Data Models

### 1.1 Core Interfaces and Base Classes

```
src/main/java/com/baldeagle/towny/
├── object/
│   ├── TownyObject.java          # Base class with UUID, name
│   ├── Government.java          # Town/Nation base (bank, permissions)
│   ├── Resident.java           # Player representation
│   ├── Town.java              # Town with residents, blocks, mayor
│   ├── Nation.java            # Nation with towns, king
│   ├── TownBlock.java         # Claimed plot (16x16)
│   ├── TownyWorld.java       # Per-world settings
│   ├── Coord.java             # (x, z) coordinate
│   ├── WorldCoord.java        # world + Coord
│   ├── Position.java           # world + x, y, z
│   ├── TownBlockType.java      # Plot type (normal, shop, embassy, etc.)
│   ├── PlotGroup.java         # Grouped plots
│   ├── District.java         # Named neighborhoods
│   ├── EconomyAccount.java   # Bank account (Town/Nation)
│   ├── TownyPermission.java # Permissions (build, destroy, etc.)
│   └── metadata/
│       └── CustomDataField.java # Extensible metadata
```

**Key Decisions**:
- Use NeoForge registries for TownBlockType
- Use Minecraft's built-in permission system or create TownyPerms
- Store data in JSON files (modern, simpler than YAML)
- Use NeoForge's built-in config system for settings

### 1.2 TownyObject (Base)

```java
public abstract class TownyObject {
    private final UUID uuid;
    private final String name;
    private Map<String, CustomDataField<?>> metadata = new HashMap<>();

    public UUID getUUID();
    public String getName();
    public void addMetadata(CustomDataField);
    public void removeMetadata(key);
}
```

### 1.3 Resident

```java
public class Resident extends TownyObject {
    private final Player player; // Can be null if offline
    private Town town;
    private List<Resident> friends;
    private List<TownBlock> townBlocks;
    private TownyPermission permissions;
    private String title; // Mayoral/nation prefix
    private String surname;

    public boolean isMayor();
    public boolean isKing();
    public boolean hasTown();
    public Town getTown();
    public boolean hasNation();
    public Nation getNation();
}
```

### 1.4 Town

```java
public class Town extends Government {
    private final List<Resident> residents;
    private Resident mayor;
    private Nation nation;
    private Map<WorldCoord, TownBlock> townBlocks;
    private TownyWorld world;
    private TownBlock homeBlock; // Spawn location
    private Position spawn; // Spawn point
    private boolean pvp, explosion, fire, mobs;
    private double taxes, plotTax, commercialTax, embassyTax;
    private boolean isPublic, isOpen, isNeutral;

    public Resident getMayor();
    public List<Resident> getResidents();
    public int getNumResidents();
    public boolean hasNation();
    public Nation getNation();
    public boolean hasHomeBlock();
    public TownBlock getHomeBlock();
    public int getMaxTownBlocks(); // Based on residents
    public int availableTownBlocks();
    public int getNationZoneSize();
}
```

### 1.5 Nation

```java
public class Nation extends Government {
    private final List<Town> towns;
    private Town capital;
    private Map<UUID, Nation> allies; // UUID -> Nation
    private Map<UUID, Nation> enemies;
    private Position spawn;

    public Town getCapital();
    public List<Town> getTowns();
    public int getNumResidents(); // Total across all towns
    public boolean isCapital(town);
}
```

### 1.6 TownBlock (Claimed Plot)

```java
public class TownBlock extends TownyObject {
    private WorldCoord worldCoord; // Immutable
    private Town town;
    private Resident resident; // Plot owner (optional)
    private TownBlockType type;
    private boolean outpost, forSale;
    private double plotPrice;
    private TownyPermission permissions;

    public WorldCoord getWorldCoord();
    public Coord getCoord(); // Within-town coordinates
    public Town getTown();
    public boolean hasResident();
    public Resident getResident();
    public boolean isOutpost();
    public TownyPermission getPermissions();
}
```

### 1.7 TownyWorld (Per-World Settings)

```java
public class TownyWorld extends TownyObject {
    private String worldName;
    private boolean usingTowny;
    private boolean claimable;
    private boolean pvp, forcePvp;
    private boolean friendlyFire;
    private boolean explosion, forceExplosion;
    private boolean fire, forceFire;
    private boolean worldMobs, wildernessMobs, townMobs;
    private boolean endermenProtect;
    private boolean creatureTrample;
}
```

### 1.8 WorldCoord and Coord

```java
public record Coord(int x, int z) {
    public static Coord of(int x, int z);
    public static Coord parseCoord(String input);
}

public record WorldCoord(String worldName, Coord coord) {
    public static WorldCoord of(Level world, int x, int z);
    public static WorldCoord of(Level world, BlockPos pos);
    public static WorldCoord parseWorldCoord(Level world, String input);
}
```

---

## Phase 2: Data Persistence

### 2.1 DataSource Interface

```java
public interface TownyDataSource {
    void loadAll();
    void saveAll();
    void saveTown(Town town);
    void saveNation(Nation nation);
    void saveResident(Resident resident);
    void saveTownBlock(TownBlock townBlock);
    void saveTownyWorld(TownyWorld world);
    void savePlotGroup(PlotGroup group);
    void saveDistrict(District district);
}
```

### 2.2 JSON Data Source (Default)

Store data in `config/towny/` directory:
- `residents/{uuid}.json`
- `towns/{uuid}.json`
- `nations/{uuid}.json`
- `townblocks/{world}/{x,z}.json`
- `worlds/{name}.json`
- `plotgroups/{uuid}.json`
- `districts/{uuid}.json`

**Load Order**:
1. Towns -> Nations (because Town needs Nation reference)
2. Residents (because Resident needs Town reference)
3. TownBlocks (because TownBlock needs Town reference)
4. PlotGroups -> Districts
5. Towns' TownBlocks (link to PlotGroups/Districts)

### 2.3 TownySettings

```java
public class TownySettings {
    private static int townBlockSize = 16;
    private static int townBlockRatio = 8; // Blocks per resident
    private static int maxTownBlocks = 0; // 0 = unlimited
    private static double priceNewTown = 100.0;
    private static double priceNewNation = 1000.0;
    private static double priceClaimTownBlock = 10.0;
    private static double defaultTax = 0.0;
    private static boolean usingEconomy = true;
    // ... and many more
}
```

---

## Phase 3: Core Commands

### 3.1 Command Structure

NeoForge uses `ClientChatCommand` and `ServerCommand` for commands. Towny needs server commands only.

```
/resident <name>       - Resident status
/resident friend add <name>
/res friend list
/resident plotlist
/resident toggle pvp|map|plotborder
/res town claim        - Claim plot for town
/res town toggle pvp|fire|explosion|mobs
/res town set board <msg>
/res town set mayor <resident>
/res town set homeblock
/res town set spawn
/res town set taxes <amount>
/res town set perm (build/destroy/switch/item on/off)
/t new <townname>      - Create town
/t here              - Show town's status
/t spawn             - Teleport to town
/t list             - List all towns
/t add <resident>
/t kick <resident>
/t leave
/t claim [radius|auto|outpost]
/t unclaim [all|radius]
/n new <nationname>   - Create nation
/n here              - Nation status
/n spawn            - Teleport to capital
/n list             - List nations
/n add <town>
/n kick <town>
/n leave
/n ally add|remove <nation>
/n enemy add|remove <nation>
/plot claim          - Claim personal plot
/plot unclaim
/plot fs [price]     - For sale
/plot set reset|shop|embassy|arena|inn|jail|farm|wilds
/plot set perm
/plot set name <name>
/plot trust add|remove <resident>
/towny              - Main command
/towny prices
/towny map
/towny top residents|land
/towny reload
/townyadmin ...      - Admin commands
```

### 3.2 Command Implementation Pattern

```java
@Mod(Towny.MODID)
public class TownyMod {
    private void registerCommands() {
        CommandDispatcher<CommandSourceStack> dispatcher =
            manager.getDispatcher();

        // /town new
        dispatcher.register(literal("town")
            .then(literal("new")
                .argument("name", StringArgumentType.word())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    Resident resident = TownyAPI.getInstance().getResident(player);
                    String name = StringArgumentType.getString(context, "name");
                    // Validate and create
                    return Command.SINGLE_SUCCESS;
                })
            )
        );
    }
}
```

---

## Phase 4: Event Listeners

### 4.1 Block Listener (Protection)

```java
@SubscribeEvent
public void onBlockPlace(BlockPlaceEvent event) {
    if (!TownySettings.isUsingTowny(world)) return;

    Player player = event.getEntity();
    WorldCoord worldCoord = WorldCoord.of(player.level(), event.getPos());
    TownBlock townBlock = TownyAPI.getInstance().getTownBlockOrNull(worldCoord);

    if (townBlock == null) {
        // Wilderness
        if (!TownySettings.isWildernessBuild()) {
            event.setCanceled(true);
        }
        return;
    }

    TownyPermission perms = townBlock.getPermissions();
    if (!perms.build(player, townBlock)) {
        event.setCanceled(true);
    }
}
```

**Protect**: Block place, block break, block interact, item use, switch use.

### 4.2 Entity Listener (Combat, Mobs)

```java
@SubscribeEvent
public void onEntityDamage(DamageEvent event) {
    // Check PVP settings
    // Check friendly fire
    // Check outlaw
}
```

**Protect**: Entity damage, entity spawn, mob grief.

### 4.3 Player Listener (Join/Leave, Spawn, Chat)

```java
@SubscribeEvent
public void onPlayerJoin(PlayerLoggedInEvent event) {
    // Create/get Resident
    // Send town board if applicable
    // Send notifications
}

@SubscribeEvent
public void onPlayerLeave(PlayerLoggedOutEvent event) {
    // Update last online
    // Save resident
}

@SubscribeEvent
public void onPlayerRespawn(PlayerRespawnEvent event) {
    // Check bed spawn
    // Check jail
    // Town spawn on death
}
```

### 4.4 Explosion Listener

```java
@SubscribeEvent
public void onExplosion(BlockExplosionEvent event) {
    // Check if explosion should be protected
    // Revert blocks if enabled
}
```

---

## Phase 5: Economy Integration

Towny uses an abstraction layer (`TownyEconomy`) that supports multiple economy plugins. This implementation uses **LightmansCurrency**.

### 5.1 Dependency Setup

In `build.gradle`:
```gradle
dependencies {
    compileOnly "com.lightman314:lightmanscurrency:${project.lc_version}"
}
```

In `gradle.properties`:
```properties
lc_version=1.21-2.3.0
```

### 5.2 Player Wallets

LightmansCurrency provides every player with a personal wallet containing coins. Players can also access bank accounts via ATM/team system.

- **Wallet access**: Get player's coin inventory from their wallet
- **Bank accounts**: Player bank accounts are available at level 6 (Netherite wallet by default)

### 5.3 Town Bank Accounts

LightmansCurrency's **Team Bank Account** system maps perfectly to Towns:

- When a Town is created, create a team with the Town name
- The mayor is the team owner, controls deposits/withdrawals
- Residents can be added as team members (optional, based on town settings)

```java
// Creating town bank account
Team townTeam = Team.create(town.getName(), town.getUUID().toString());
townTeam.setOwner(town.getMayor().getUUID());
// Add residents as team members if town is public/open
for (Resident res : town.getResidents()) {
    if (res != town.getMayor()) {
        townTeam.addMember(res.getUUID());
    }
}
```

### 5.3.1 Nation Bank Accounts

**Design Decision**: Nations do NOT use LC Team accounts directly. Instead:

- Each town pays nation taxes/upkeep to their town's team account
- Nation draws revenue from member towns
- This keeps LC Teams as player-focused (which they are designed for)

Alternative (if direct nation accounts needed): Store nation balance in Towny data, manage independently.

```java
// Nation revenue collection
public double getNationBalance(Nation nation) {
    double total = 0.0;
    for (Town town : nation.getTowns()) {
        Team townTeam = Team.getTeam("town_" + town.getUUID());
        if (townTeam != null) {
            // Optional: take portion for nation
            total += townTeam.getCoinValue();
        }
    }
    return total;
}
```

### 5.4 LightmansEconomy Implementation

The `LightmansEconomy` class wraps LightmansCurrency's API to provide Towny with economy functionality. Here's how it handles the different account types:

#### 5.4.1 Account Types

Towny has three types of accounts that map to LightmansCurrency:

| Towny Account | LC Equivalent | Account Name Format |
|---------------|--------------|-------------------|
| Player | Wallet + Bank Account | Player UUID |
| Town | Team Bank Account | `town_{town_uuid}` |
| Nation | Team Bank Account | `nation_{nation_uuid}` |

#### 5.4.2 Class Structure

```java
public class LightmansEconomy implements TownyEconomy {
    
    private boolean active = false;
    
    @Override
    public void initialize() {
        // Check if LightmansCurrency is present
        if (!ModList.get().isLoaded("lightmanscurrency")) {
            TownyEconomy.LOGGER.warn("LightmansCurrency not found - economy disabled");
            this.active = false;
            return;
        }
        this.active = true;
    }
    
    @Override
    public boolean isActive() {
        return this.active && ModList.get().isLoaded("lightmanscurrency");
    }
    
    @Override
    public boolean withdraw(String accountName, double amount) {
        if (accountName.startsWith("town_")) {
            // Town account - use team bank account
            Team team = Team.getTeam(accountName);
            if (team == null) return false;
            return team.tryRemoveCoins(amount);
        } else if (accountName.startsWith("nation_")) {
            // Nation account - tracked internally (not LC team)
            return NationEconomy.withdraw(accountName, amount);
        } else {
            // Player account - WALLET only (physical coins in inventory)
            ServerPlayer player = getPlayerByName(accountName);
            if (player == null) return false;
            return WalletUtils.tryRemoveCoins(player, amount);
        }
    }
    
    @Override
    public boolean deposit(String accountName, double amount) {
        if (accountName.startsWith("town_")) {
            Team team = Team.getTeam(accountName);
            if (team == null) return false;
            return team.tryAddCoins(amount);
        } else if (accountName.startsWith("nation_")) {
            // Nation - tracked internally
            return NationEconomy.deposit(accountName, amount);
        } else {
            // Player - WALLET only
            ServerPlayer player = getPlayerByName(accountName);
            if (player == null) return false;
            return WalletUtils.tryAddCoins(player, amount);
        }
    }
    
    @Override
    public double getBalance(String accountName) {
        if (accountName.startsWith("town_") || accountName.startsWith("nation_")) {
            Team team = Team.getTeam(accountName);
            if (team == null) return 0.0;
            return team.getCoinValue();
        } else {
            // Player - just check WALLET (physical coins in inventory)
            ServerPlayer player = getPlayerByName(accountName);
            if (player == null) return 0.0;
            
            Wallet wallet = WalletUtils.getWallet(player);
            return wallet != null ? wallet.getCoinValue() : 0.0;
        }
    }
    
    @Override
    public String format(double amount) {
        // Use LightmansCurrency's built-in formatting
        return MoneyAPI.formatValue(amount);
    }
    
    // Helper methods
    private ServerPlayer getPlayerByName(String name) {
        // Look up player by name or UUID
        return ...;
    }
}
```

**Key API classes/methods from LightmansCurrency:**

| Class/Method | Purpose |
|--------------|---------|
| `Team.create(name, id)` | Create a team bank account |
| `Team.getTeam(id)` | Get team by ID |
| `Team.tryAddCoins(amount)` | Deposit money to team |
| `Team.tryRemoveCoins(amount)` | Withdraw money from team |
| `Team.getCoinValue()` | Get total balance in "coin value" |
| `WalletUtils.getWallet(player)` | Get player's wallet |
| `WalletUtils.tryAddCoins(player, amount)` | Add coins to wallet |
| `WalletUtils.tryRemoveCoins(player, amount)` | Remove coins from wallet |
| `Wallet.getCoinValue()` | Get wallet balance as value |
| `MoneyAPI.formatValue(amount)` | Format amount as display string |

### 5.5 Tax Collection Flow

```java
// DailyTimerTask - collect town taxes
for (Town town : towns) {
    Team townTeam = Team.getTeam(town.getUUID().toString());
    if (townTeam == null) continue;
    
    for (Resident resident : town.getResidents()) {
        double tax = town.getTaxes();
        
        // Try to collect from player's WALLET (physical coins in inventory)
        if (!WalletUtils.tryRemoveCoins(resident.getPlayer(), tax)) {
            // Player can't pay - handle (kick, jail, etc.)
            handleTaxOwer(resident, town, tax);
            continue;
        }
        
        // Deposit to town team account
        townTeam.tryAddCoins(tax);
    }
}
```

### 5.6 Plot Sales

```java
// Player buys plot from town
Town town = plot.getTown();
Team townTeam = Team.getTeam(town.getUUID().toString());
Player buyer = ResidentUtil.getPlayer(buyerResident);

// Remove from buyer
if (!PlayerUtilities.tryRemoveCoins(buyer, price)) {
    return false; // Not enough money
}

// Add to town account
townTeam.tryAddCoins(price);
```

### 5.7 No LightmansCurrency Fallback

If LC is not present, fall back to "Closed Economy" mode (no economy, or limited custom implementation).

---

## Phase 6: Timer Tasks

### 6.1 Daily Timer (Taxes, Upkeep)

```java
@SubscribeEvent
public void onDailyTimer(TickEvent.ServerTickEvent event) {
    if (shouldRunDaily()) {
        for (Town town : TownyUniverse.getTowns()) {
            collectTownTaxes(town);
        }
        for (Nation nation : TownyUniverse.getNations()) {
            collectNationTaxes(nation);
        }
    }
}
```

### 6.2 Health Regeneration

Players in their town/nation ally regenerate health faster.

### 6.3 Plot Regeneration

If enabled, unclaimed plots regenerate to pre-claim state over time.

---

## Phase 7: Optional Features (Later Phases)

### 7.1 War System

Towny has optional war plugins that integrate with Towny.

### 7.2 Chat System

TownyChat provides channels: local, town, nation, ally, admin, global.

### 7.3 Jailing

Jail plots, jail residents, bail payment.

### 7.4 HUD (Heads-Up Display)

Map HUD, permission HUD.

### 7.5 PAPI Hooks

PlaceholderAPI integration for plugins.

---

## Project Structure

```
neo-towny/
├── build.gradle
├── src/main/java/com/baldeagle/towny/
│   ├── Towny.java                    # Mod entry point
│   ├── TownyClient.java              # Client-only code
│   ├── Config.java                  # Config file
│   ├── object/
│   │   ├── TownyObject.java
│   │   ├── Government.java
│   │   ├── Resident.java
│   │   ├── Town.java
│   │   ├── Nation.java
│   │   ├─�� TownBlock.java
│   │   ├── TownyWorld.java
│   │   ├── WorldCoord.java
│   │   ├── Coord.java
│   │   ├── Position.java
│   │   ├── TownBlockType.java
│   │   ├── TownyPermission.java
│   │   ├── TownyWorldSettings.java
│   │   └── metadata/
│   │       └── CustomDataField.java
│   ├── db/
│   │   ├── TownyDataSource.java
│   │   └── JSONDataSource.java
│   ├── settings/
│   │   ├── TownySettings.java
│   │   └── TownyPermissions.java
│   ├── api/
│   │   └── TownyAPI.java
│   ├── command/
│   │   ├── ResidentCommand.java
│   │   ├── TownCommand.java
│   │   ├── NationCommand.java
│   │   ├── PlotCommand.java
│   │   ├── TownyCommand.java
│   │   └── TownyAdminCommand.java
│   ├── event/
│   │   ├── TownyBlockListener.java
│   │   ├── TownyEntityListener.java
│   │   ├── TownyPlayerListener.java
│   │   ├── TownyWorldListener.java
│   │   └── TownyServerListener.java
│   ├── economy/
│   │   ├── TownyEconomy.java
│   │   └── ClosedEconomy.java
│   ├── task/
│   │   ├── DailyTimerTask.java
│   │   ├── HealthRegenTask.java
│   │   └── PlotRegenTask.java
│   └── registry/
│       └── TownBlockTypes.java       # Registry for TownBlockType
├── src/main/resources/
│   ├── assets/towny/
│   │   ├── lang/en_us.json
│   │   └── townyperms.yml
│   └── data/
│       └── config.yml             # Default settings
└── src/generated/
    └── resources/                 # Generated by data gen
```

---

## Implementation Order

### Phase 1: Foundation (Week 1-2)
1. [x] Create project structure
2. [ ] Implement core objects (Resident, Town, Nation, TownBlock, TownyWorld)
3. [ ] Implement WorldCoord, Coord, Position
4. [ ] Basic JSON data source
5. [ ] TownySettings with config

### Phase 2: Core Mechanics (Week 3-4)
1. [ ] TownBlock claiming (grid-based)
2. [ ] Basic commands (/town new, /town claim, /t spawn)
3. [ ] Resident commands (/res)
4. [ ] Nation commands (/n new, /n add)
5. [ ] Block protection listener

### Phase 3: Economy (Week 5)
1. [ ] Economy integration
2. [ ] Taxes
3. [ ] Plot buying/selling
4. [ ] Nation upkeeps

### Phase 4: Polish (Week 6)
1. [ ] Entity protection (PVP, mobs)
2. [ ] World settings
3. [ ] Permissions (plot perms)
4. [ ] /towny command and map

### Phase 5: Optional (Later)
1. [ ] Chat channels
2. [ ] Jailing
3. [ ] HUD
4. [ ] War support

---

## Key NeoForge Considerations

### 1.1 Registries

TownBlockType needs to be a NeoForge registry:

```java
public class TownBlockTypes {
    public static final DeferredRegister<BlockType> BLOCK_TYPES =
        DeferredRegister.create(Registries.BLOCK_TYPE, Towny.MOD_ID);

    public static final RegistryObject<TownBlockType> NORMAL =
        BLOCK_TYPES.register("normal", () -> new TownBlockType("normal", 0.0, false));
    // ...
}
```

### 1.2 Commands

NeoForge uses `Commands` class with `CommandDispatcher`:

```java
manager.getDispatcher().register(
    Commands.literal("town")
        .then(Commands.literal("new")
            .then(Commands.argument("name", WordArgumentType.word())
                .executes(context -> {
                    // Implementation
                })
            )
        )
)
```

### 1.3 Events

Use NeoForge event bus:

```java
@SubscribeEvent
public static void onPlayerJoin(PlayerLoggedInEvent event) {
    // Implementation
}
```

### 1.4 Config

Use NeoForge config system or create custom YAML/JSON config.

### 1.5 Permissions

NeoForge doesn't have a built-in permission system. Options:
1. Create TownyPerms (custom)
2. Integrate with LuckPerms API if available
3. Use server OP as admin fallback

### 1.6 Player Communication

Use `ServerPlayer.sendSystemMessage()` or Component-based messages.

---

## Testing Strategy

1. Create integration tests for core mechanics
2. Test town creation, claiming, taxes
3. Test block protection in various scenarios
4. Test commands
5. Manual testing with multiple clients

---

## References

- Original Towny: https://github.com/TownyAdvanced/Towny
- Towny Wiki: https://github.com/TownyAdvanced/Towny/wiki
- NeoForge Modding Docs: https://docs.neoforged.net/