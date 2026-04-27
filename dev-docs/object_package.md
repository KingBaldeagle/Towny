# NeoTowny Object Package

This document details the core data objects in NeoTowny - the foundation everything else builds upon.

---

## Overview

The `object` package contains all the core data models in NeoTowny. These are the classes that represent:
- Players (Resident)
- Towns and Nations
- Claimed land (TownBlock)
- World settings
- Permissions

**Total: ~16,000 lines** (the largest package)

---

## Class Hierarchy

```
TownyObject (abstract base)
    │
    ├── Resident
    │
    ├── Government (abstract - bank/tax base)
    │   ├── Town
    │   └── Nation
    │
    ├── TownBlock
    │
    ├── TownyWorld
    │
    ├── PlotGroup
    │
    └── District
```

---

## TownyObject

**Purpose:** Base class for all Towny entities. Provides UUID, name, and metadata.

```java
public abstract class TownyObject {
    private final UUID uuid;
    private final String name;
    private final Map<String, CustomDataField<?>> metadata = new HashMap<>();
    private long registered;      // Unix timestamp
    private long lastOnline;    // Unix timestamp
    
    // Methods
    public UUID getUUID();
    public String getName();
    public boolean hasUUID();
    public long getRegistered();
    
    // Metadata
    public void addMetaData(CustomDataField<?> field);
    public void removeMetaData(String key);
    public <T> T getMetaData(String key, Class<T> type);
    public boolean hasMetaData(String key);
}
```

**Why it exists:**
- Every Towny object needs a UUID and name
- Provides extensible metadata for plugins to attach data
- Tracks registration/login times

---

## Resident

**Purpose:** Represents a player who has joined the server.

```java
public class Resident extends TownyObject {
    // Relationships
    private Town town;                               // Current town (null if none)
    private List<Resident> friends = new ArrayList<>();  // Friend list
    
    // Personal plots
    private List<TownBlock> townBlocks = new ArrayList<>();  // Owned plots
    
    // Display
    private String title = "";    // e.g., "Mayor "
    private String surname = "";  // e.g., " the Brave"
    private String about = "";    // Bio text
    
    // Ranks
    private List<String> townRanks = new ArrayList<>();
    private List<String> nationRanks = new ArrayList<>();
    
    // Permissions
    private TownyPermission permissions = new TownyPermission();
    
    // Jail
    private JailData jailData;
    
    // Jumping/Levitation
    private Position lastStablePosition;
    
    // === Methods ===
    
    // Relationships
    public boolean hasTown();
    public boolean hasNation();
    public Town getTown();              // throws if not in town
    public Town getTownOrNull();        // returns null if not in town
    public Nation getNation();          // throws if not in nation
    public Nation getNationOrNull();
    
    public boolean isMayor();
    public boolean isKing();
    public boolean isOnline();
    
    // Friends
    public void addFriend(Resident resident);
    public void removeFriend(Resident resident);
    public boolean hasFriend(Resident resident);
    public List<Resident> getFriends();
    
    // Town blocks
    public void addTownBlock(TownBlock tb);
    public void removeTownBlock(TownBlock tb);
    public boolean hasTownBlock(TownBlock tb);
    public List<TownBlock> getTownBlocks();
    
    // Title/Surname
    public String getTitle();
    public void setTitle(String title);
    public boolean hasTitle();
    public String getSurname();
    public void setSurname(String surname);
    public boolean hasSurname();
    
    // Formatted name (title + name + surname)
    public String getFormattedName();
    
    // Permissions
    public TownyPermission getPermissions();
    public void setPermissions(String line);
    
    // Ranks
    public boolean hasTownRank(String rank);
    public void addTownRank(String rank);
    public void removeTownRank(String rank);
    public List<String> getTownRanks();
    
    public boolean hasNationRank(String rank);
    public void addNationRank(String rank);
    public void removeNationRank(String rank);
    public List<String> getNationRanks();
    
    // Jumping
    public Position getLastStablePosition();
    public void setLastStablePosition(Position pos);
    
    // NPC detection
    public boolean isNPC();
}
```

**Key fields explained:**
| Field | Purpose |
|-------|---------|
| `town` | The town this resident belongs to |
| `friends` | Friend list for building permissions |
| `townBlocks` | Plots this resident personally owns |
| `title/surname` | Custom display prefixes/suffixes |
| `townRanks/nationRanks` | Rank permissions within town/nation |
| `jailData` | If jailed, stores jail location and time |

---

## Government (Abstract)

**Purpose:** Base class for Town and Nation. Provides common bank and tax functionality.

```java
public abstract class Government extends TownyObject {
    // Economy
    protected double balance = 0.0;
    protected double taxes = 0.0;           // Daily tax amount
    protected boolean taxPercent = false;  // true = percentage, false = flat
    
    // Spawn
    protected Position spawn;              // Spawn point
    
    // Permissions
    protected TownyPermission permissions = new TownyPermission();
    // flags: build, destroy, switch, itemUse, pvp, explosion, fire, mobs
    
    // Metadata
    protected String board = "";              // Welcome message
    protected String tag = "";               // 4-char tag
    protected boolean isPublic = true;     // Can /town spawn
    protected boolean isOpen = false;        // Anyone can join
    protected boolean isNeutral = false;   // Cannot be attacked in war
    
    // === Methods ===
    
    // Economy
    public double getBalance();
    public void setBalance(double amount);
    public void collect(double amount);     // Deposit to bank
    
    public double getTaxes();
    public void setTaxes(double amount);
    public boolean isTaxPercent();
    public void setTaxPercent(boolean percent);
    
    // Spawn
    public Position getSpawn();
    public void setSpawn(Position spawn);
    public boolean hasSpawn();
    
    // Permissions
    public TownyPermission getPermissions();
    public void setPermissions(String line);
    
    // Display
    public String getBoard();
    public void setBoard(String board);
    public String getTag();
    public void setTag(String tag);
    
    public boolean isPublic();
    public void setPublic(boolean isPublic);
    public boolean isOpen();
    public void setOpen(boolean isOpen);
}
```

---

## Town

**Purpose:** A collection of residents with claimed land and a bank account.

```java
public class Town extends Government {
    // Residents
    private final List<Resident> residents = new ArrayList<>();
    private Resident mayor;
    
    // Nation
    private Nation nation;
    
    // Claims
    private final Map<WorldCoord, TownBlock> townBlocks = new ConcurrentHashMap<>();
    private TownBlock homeBlock;     // First claim - spawn location
    private List<Position> outpostSpawns = new ArrayList<>();
    
    // World
    private TownyWorld world;
    
    // Economy (extended from Government)
    private double plotTax = 0.0;
    private double commercialPlotTax = 0.0;  // Shops
    private double embassyTax = 0.0;
    private double plotPrice = 0.0;
    private double commercialPlotPrice = 0.0;
    private double embassyPlotPrice = 0.0;
    
    // Features
    private boolean isRuined = false;
    private long ruinedAt = 0;
    private boolean isForSale = false;
    private double forSalePrice = 0;
    
    // Allies/Enemies
    private final Map<UUID, Town> allies = new LinkedHashMap<>();
    private final Map<UUID, Town> enemies = new LinkedHashMap<>();
    
    // Trusted
    private final Set<Resident> trustedResidents = new HashSet<>();
    private final Map<UUID, Town> trustedTowns = new LinkedHashMap<>();
    
    // Plot groups
    private Map<String, PlotGroup> plotGroups;
    
    // Jails
    private List<Jail> jails = new ArrayList<>();
    private Jail primaryJail;
    
    // Outlaws (residents banned from town)
    private final List<Resident> outlaws = new ArrayList<>();
    
    // === Methods ===
    
    // Residents
    public List<Resident> getResidents();
    public int getNumResidents();
    public Resident getMayor();
    public void setMayor(Resident mayor);
    public void addResident(Resident resident);
    public void removeResident(Resident resident);
    public boolean hasResident(Resident resident);
    public boolean hasResident(String name);
    
    // Mayor succession
    public void findNewMayor();  // When mayor leaves
    
    // Nation
    public boolean hasNation();
    public Nation getNation();
    public void setNation(Nation nation);
    public void removeNation();
    public boolean isCapital();  // Is capital of nation?
    
    // Claims
    public Map<WorldCoord, TownBlock> getTownBlocks();
    public int getNumTownBlocks();
    public TownBlock getTownBlock(WorldCoord coord);
    public boolean hasTownBlock(WorldCoord coord);
    public void addTownBlock(TownBlock tb);
    public void removeTownBlock(TownBlock tb);
    
    public boolean hasHomeBlock();
    public TownBlock getHomeBlock();
    public void setHomeBlock(TownBlock tb);
    
    // Outposts
    public List<Position> getOutpostSpawns();
    public void addOutpostSpawn(Position pos);
    public int getMaxOutpostSpawn();
    public boolean isOutpost(WorldCoord coord);
    
    // Town block limits
    public int getMaxTownBlocks();        // Based on residents
    public int getBonusBlocks();          // Purchased extra
    public int availableTownBlocks();
    
    // Economy
    public double getPlotTax();
    public void setPlotTax(double tax);
    public double getPlotPrice();
    public void setPlotPrice(double price);
    
    public double getTaxOwing(Resident resident);  // Calculate tax for resident
    
    // Plot groups
    public Map<String, PlotGroup> getPlotGroups();
    public void addPlotGroup(PlotGroup group);
    public PlotGroup getPlotGroup(String name);
    
    // Allies/Enemies
    public Map<UUID, Town> getAllies();
    public Map<UUID, Town> getEnemies();
    public void addAlly(Town town);
    public void removeAlly(Town town);
    public boolean hasAlly(Town town);
    public void addEnemy(Town town);
    public void removeEnemy(Town town);
    public boolean hasEnemy(Town town);
    
    // Trusted
    public Set<Resident> getTrustedResidents();
    public void addTrustedResident(Resident resident);
    public void removeTrustedResident(Resident resident);
    public boolean hasTrustedResident(Resident resident);
    
    // Jails
    public List<Jail> getJails();
    public void addJail(Jail jail);
    public Jail getPrimaryJail();
    public void setPrimaryJail(Jail jail);
    
    // Outlaws
    public List<Resident> getOutlaws();
    public void addOutlaw(Resident resident);
    public void removeOutlaw(Resident resident);
    public boolean hasOutlaw(Resident resident);
    
    // Ruined towns
    public boolean isRuined();
    public void setRuined(boolean ruined);
    public boolean isForSale();
    public void setForSale(boolean forSale, double price);
}
```

**Town sizes (levels):**
| Level | Residents | Name | TownBlocks |
|-------|-----------|------|------------|
| 0 | 0 | Ruins | 1 |
| 1 | 1 | Settlement | 16 |
| 2 | 2 | Hamlet | 32 |
| 3 | 6 | Village | 96 |
| 4 | 10 | Town | 160 |
| 5 | 14 | Large Town | 224 |
| 6 | 20 | City | 320 |
| ... | ... | ... | ... |

---

## Nation

**Purpose:** A collection of towns, led by the capital town's mayor.

```java
public class Nation extends Government {
    // Towns
    private final List<Town> towns = new ArrayList<>();
    private Town capital;
    
    // Relationships
    private final Map<UUID, Nation> allies = new LinkedHashMap<>();
    private final Map<UUID, Nation> enemies = new LinkedHashMap<>();
    
    // King (mayor of capital)
    private String kingName;
    
    // === Methods ===
    
    // Towns
    public List<Town> getTowns();
    public int getNumTowns();
    public void addTown(Town town);
    public void removeTown(Town town);
    public boolean hasTown(Town town);
    
    // Capital
    public Town getCapital();
    public void setCapital(Town capital);
    public boolean isCapital(Town town);
    
    // King
    public String getKing();  // Name of king
    public Resident getKingResident();  // Resident object
    
    // Total residents (sum of all towns)
    public int getTotalResidents();
    
    // Allies/Enemies
    public Map<UUID, Nation> getAllies();
    public Map<UUID, Nation> getEnemies();
    public void addAlly(Nation nation);
    public void removeAlly(Nation nation);
    public boolean hasAlly(Nation nation);
    public void addEnemy(Nation nation);
    public void removeEnemy(Nation nation);
    public boolean hasEnemy(Nation nation);
    
    // Nation zones (protected wilderness around towns)
    public int getNationZoneSize();  // Blocks around town that only nation can build
}
```

---

## TownBlock

**Purpose:** A single claimed plot (default 16x16).

```java
public class TownBlock extends TownyObject {
    // Location (immutable)
    private final WorldCoord worldCoord;
    
    // Owners
    private Town town;           // Town that owns this block
    private Resident resident;  // Personal owner (optional)
    
    // Type
    private TownBlockType type = TownBlockType.NORMAL;
    
    // Flags
    private boolean outpost = false;
    private boolean forSale = false;
    private double plotPrice = 0;
    
    // Permissions (overrides town permissions)
    private TownyPermission permissions;
    
    // Plot group
    private UUID plotGroupId;
    
    // District
    private UUID districtId;
    
    // === Methods ===
    
    // Location
    public WorldCoord getWorldCoord();
    public Coord getCoord();  // x,z within town
    public Level getLevel();  // Minecraft level
    
    // Owners
    public Town getTown();
    public boolean hasTown();
    public boolean hasResident();
    public Resident getResident();
    public boolean isOwned(Resident resident);
    public boolean isOwnedBy(Resident resident);  // Or their townmate
    
    // Type
    public TownBlockType getType();
    public void setType(TownBlockType type);
    
    // Flags
    public boolean isOutpost();
    public void setOutpost(boolean outpost);
    public boolean isForSale();
    public double getPlotPrice();
    
    // Permissions
    public TownyPermission getPermissions();
    public void setPermissions(String line);
}
```

**TownBlock types:**
| Type | Purpose | Allows Players |
|-----|---------|----------------|
| NORMAL | Default | Build/destroy based on town perms |
| SHOP | Player shops | Shop plugins only |
| EMBASSY | Other towns' plots | Anyone can buy |
| ARENA | PVP combat | Always PVP, friendly fire |
| INNS | Beds for spawn | Use beds anywhere |
| JAILS | Jail cells | Jailing players |
| FARMS | Farming | Farm-related items only |
| WILDS | Wilderness | Harvest wilds only |

---

## TownyWorld

**Purpose:** Per-world settings for Towny.

```java
public class TownyWorld extends TownyObject {
    private String worldName;
    
    // World settings
    private boolean usingTowny = true;
    private boolean claimable = true;
    
    // PVP
    private boolean pvp = true;
    private boolean forcePvp = false;
    private boolean friendlyFire = false;
    
    // Explosions
    private boolean explosion = true;
    private boolean forceExplosion = false;
    
    // Fire
    private boolean fire = true;
    private boolean forceFire = false;
    
    // Mobs
    private boolean worldMobs = true;
    private boolean wildernessMobs = true;
    private boolean townMobs = false;
    
    // Protection
    private boolean endermenProtect = true;
    private boolean creatureTrample = true;
    
    // Regeneration
    private boolean revertUnclaim = true;
    private boolean revertEntityExpl = true;
    private boolean revertBlockExpl = true;
    
    // Jailing
    private boolean jailing = true;
    
    // === Methods ===
    
    public String getWorldName();
    public Level getLevel();
    
    // Settings getters/setters for each flag above...
}
```

---

## WorldCoord

**Purpose:** Identifies a location in Towny's grid system.

```java
public record WorldCoord(String worldName, int x, int z) {
    // Convert from BlockPos
    public static WorldCoord of(Level level, BlockPos pos) {
        int townBlockSize = TownySettings.getTownBlockSize();
        return new WorldCoord(
            level.getName().asString(),
            pos.getX() / townBlockSize,
            pos.getZ() / townBlockSize
        );
    }
    
    // Convert to BlockPos range
    public BlockPos getMinBlock();
    public BlockPos getMaxBlock();
    
    // Neighbors
    public WorldCoord north();
    public WorldCoord south();
    public WorldCoord east();
    public WorldCoord west();
    
    // Serialize
    public String toString();  // "world: x, z"
    public static WorldCoord parse(String input);
}
```

**Example:**
- With `townBlockSize = 16`:
- Block at (32, 64, 32) → WorldCoord("minecraft:overworld", 2, 2)
- This represents a 16x16 area from (32,0,32) to (47,0,47)

---

## Position

**Purpose:** Represents a spawn point location.

```java
public class Position {
    private final String worldName;
    private final double x, y, z;
    private final float yaw, pitch;
    
    public static Position of(Level level, Vec3 pos, float yaw, float pitch) {
        return new Position(level.getName(), pos.x, pos.y, pos.z, yaw, pitch);
    }
    
    public static Position of(Level level, BlockPos pos) {
        return new Position(level.getName(), pos.getX(), pos.getY(), pos.getZ(), 0, 0);
    }
    
    public Level getLevel();
    public Vec3 vec3();
    public BlockPos blockPos();
}
```

---

## TownyPermission

**Purpose:** Stores permission flags for a location.

```java
public class TownyPermission {
    private boolean build = true;
    private boolean destroy = true;
    private boolean switchBlock = true;
    private boolean itemUse = true;
    
    // For individual groups
    private boolean residentBuild = true;
    private boolean allyBuild = true;
    private boolean outsiderBuild = true;
    // ... same for destroy, switch, itemUse
    
    // World flags
    private boolean pvp = false;
    private boolean explosion = false;
    private boolean fire = false;
    private boolean mobs = false;
}
```

**Permission check:**
```
Location (TownBlock)
    │
    ├─► Has resident owner?
    │       └─► Use resident permissions
    │
    └─► Otherwise
            └─► Use town permissions

Then check: RESIDENT / ALLY / OUTSIDER
            └─► Check appropriate flag
```

---

## PlotGroup

**Purpose:** Groups multiple TownBlocks for bulk operations.

```java
public class PlotGroup extends TownyObject {
    private Town town;
    private List<TownBlock> townBlocks = new ArrayList<>();
    private TownBlockType type;
    private TownyPermission permissions;
    private boolean forSale;
    private double price;
}
```

---

## District

**Purpose:** Named neighborhoods within a town.

```java
public class District extends TownyObject {
    private Town town;
    private List<TownBlock> townBlocks = new ArrayList<>();
    private String name;
}
```

---

## CustomDataField

**Purpose:** Extensible metadata for Towny objects.

```java
public class CustomDataField<T> {
    private String key;
    private T value;
    private Class<T> type;
    
    // Static factory methods
    public static CustomDataField<Boolean> of(String key, boolean value);
    public static CustomDataField<Integer> of(String key, int value);
    public static CustomDataField<Double> of(String key, double value);
    public static CustomDataField<String> of(String key, String value);
    public static CustomDataField<List<String>> of(String key, List<String> value);
}
```

**Usage:**
```java
resident.addMetaData(CustomDataField.of("lastHome", worldName));
resident.getMetaData("lastHome", String.class);
```

---

## Summary Table

| Class | Lines | Purpose |
|-------|-------|---------|
| Town | 2,026 | Everything about towns |
| Resident | 1,141 | Player data |
| TownyWorld | 1,067 | World settings |
| TownBlock | 759 | Claimed plot |
| Nation | 719 | Nation data |
| Government | 393 | Town/Nation base |
| WorldCoord | 350 | Position in grid |
| TownyPermission | 348 | Permission flags |
| PlotGroup | 249 | Plot grouping |
| Position | 166 | Spawn location |
| District | 133 | Neighborhoods |
| +35 other files | ~5,000 | Utilities, interfaces, etc. |

**Total: ~16,089 lines**