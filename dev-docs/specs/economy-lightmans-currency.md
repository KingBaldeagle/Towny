# NeoTowny Economy with LightmansCurrency

This document details how NeoTowny's economy integrates with LightmansCurrency (LC) for the 1.21.1 NeoForge port.

---

## Overview

NeoTowny uses LightmansCurrency as its sole economy backend. This means:
- All player money using LC's wallet system
- Town bank accounts use LC's Team system
- Formatting uses LC's built-in money formatting

---

## Account Types

### 1. Player Accounts

Each player has coins stored in two places:

**1. Wallet** (what player carries):
- Physical coin ItemStacks in player's inventory
- Any player can use their wallet immediately
- Coins represent actual value (copper=1, iron=10, gold=100, etc.)

**2. Bank Account** (optional, virtual storage):
- Accessed via ATM blocks
- Unlocked at Netherite wallet tier (level 6)
- Stores virtual coins separate from physical wallet

For Towny:
- **Taxes always use wallet only** - physical coins in inventory
- Bank account is for players who want to store large amounts safely
- Never tries bank account for taxes (player should use wallet)

```java
// For taxes, collect from player's WALLET (physical coins in inventory)
public boolean collectTaxes(Player player, double amount) {
    // WalletUtils handles coin selection from player's inventory
    // Takes appropriate coins to meet the amount
    return WalletUtils.tryRemoveCoins(player, amount);
}
```

**Note**: Towny doesn't interact with player's bank account at all. Players may use bank accounts for personal storage but Towny tax/transaction operations always use wallet only.

// Bank account is optional - for deposits/withdrawals larger than what wallet can hold
## 2. Town Bank Accounts

Each town gets a **Team Bank Account** in LightmansCurrency.

**Creation Requirement:**
- Mayor must CREATE a team with the **same name** as the town first (via `/lcteam create <name>`)
- Then run `/town new <name>`
- Mayor becomes team owner

```java
public boolean createTown(Resident mayor, String name) {
    // Check if team exists (must be created by mayor first)
    Team team = Team.getTeam(name);
    if (team == null) {
        mayor.sendMessage("You must create a team first: /lcteam create " + name);
        return false;
    }
    
    if (!team.getOwner().equals(mayor.getUUID())) {
        mayor.sendMessage("You must own the team to create a town!");
        return false;
    }
    
    // Create the town
    Town town = new Town(name, mayor);
    Towny.getDataSource().saveTown(town);
    town.setTeamID(team.getID());
    
    return true;
}
```

**Access Control:**
- Only the **mayor** (team owner) can deposit/withdraw
- Mayor accesses via `/lcbank <townname>` or ATM

**Access Control:**
- Only the **mayor** can deposit/withdraw from town's team account
- Residents are NOT added as team members (mayor has exclusive access)
- Mayor accesses via /lcbank or ATM

When mayor changes:

```java
public void onNewMayor(Town town, Resident newMayor) {
    // Transfer team ownership to new mayor
    Team team = Team.getTeam("town_" + town.getUUID().toString());
    if (team != null) {
        team.setOwner(newMayor.getUUID());
    }
}
```

## 3. Nation Bank Accounts

Each nation has its balance tracked in Towny data.

**Creation Requirement:**
- King must CREATE a team with the **same name** as the nation first (via `/lcteam create <nationname>`)
- Then run `/nation new <name>`
- Nation creation checks for team existence

```java
public boolean createNation(Resident king, String name) {
    // King must create team first
    Team team = Team.getTeam(name);
    if (team == null) {
        king.sendMessage("You must create a team first: /lcteam create " + name);
        return false;
    }
    
    if (!team.getOwner().equals(king.getUUID())) {
        king.sendMessage("You must own the team to create a nation!");
        return false;
    }
    
    // Create the nation
    Nation nation = new Nation(name, capitalTown);
    nation.setTeamID(team.getID());
    
    return true;
}
```

**Access Control:**
- Only the **king** (team owner) can access nation's balance
- King's town members cannot access nation funds

```java
public class Nation extends Government {
    private double balance;  // Stored in Towny data
    
    public double getBalance() {
        return this.balance;
    }
    
    public void deposit(double amount) {
        this.balance += amount;
    }
    
    public boolean withdraw(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }
    
    // Only king can access
    public boolean canAccess(Resident resident) {
        return resident == this.getKing();
    }
}
```

When king changes (new capital):

```java
public void onNewKing(Nation nation, Town newCapital) {
    // Nation balance stays with nation
    // New king (capital town's mayor) now has access
}

---

## Core Economy Operations

### Creating a Town (/town new)

```java
public boolean createTown(Resident mayor, String name) {
    // 1. Check creation cost
    double cost = TownySettings.getPriceNewTown();
    if (!economy.withdraw(mayor.getName(), cost)) {
        mayor.sendMessage("Not enough money. Cost: " + economy.format(cost));
        return false;
    }
    
    // 2. Create Town object
    Town town = new Town(name, mayor);
    Towny.getDataSource().saveTown(town);
    
    // 3. Create LC team bank account
    economy.createTeamBankAccount(town);
    
    return true;
}
```

### Claiming Land (/town claim)

```java
public boolean claimTownBlock(TownBlock block, Player player) {
    double cost = block.getPrice();
    
    // Withdraw from player's WALLET (coins in inventory)
    if (!economy.withdraw(player.getName(), cost)) {
        player.sendMessage("Not enough money: " + economy.format(cost));
        return false;
    }
    
    // Deposit to town's team account
    Town town = block.getTown();
    String accountName = "town_" + town.getUUID().toString();
    economy.deposit(accountName, cost);
    
    Towny.getDataSource().saveTownBlock(block);
    return true;
}
```

### Selling a Plot (/plot forsale)

```java
public boolean buyPlot(Player buyer, TownBlock block) {
    double price = block.getPrice();
    Resident buyerRes = TownyAPI.getResident(buyer);
    
    // Withdraw from buyer
    if (!economy.withdraw(buyerRes.getName(), price)) {
        buyer.sendMessage("Not enough money: " + economy.format(price));
        return false;
    }
    
    // Deposit to previous owner (if plot owner exists)
    Resident plotOwner = block.getResident();
    if (plotOwner != null) {
        economy.deposit(plotOwner.getName(), price);
    } else {
        // Go to town account if no plot owner
        String townAccount = "town_" + block.getTown().getUUID().toString();
        economy.deposit(townAccount, price);
    }
    
    return true;
}
```

### Daily Taxes

Tax flow:

| Town Status | Tax Goes To |
|-------------|-------------|
| No nation | Server bank account (admins only) |
| Has nation | Nation bank account |

#### Server Bank Account

For towns without a nation, taxes go to a server-wide LC Team (admins only).

**Creation:**
- Admin must CREATE a team named "server" after world creation: `/lcteam create server`
- Team should have NO owner (managed programmatically)
- Admin does `/lcteam deleteowner` to remove owner after creation

```java
public class ServerEconomy {
    private static Team serverTeam;
    
    public static void initServerTeam() {
        serverTeam = Team.getTeam("server");
        if (serverTeam == null) {
            // Team doesn't exist yet - admins must create it
            Towny.getLogger().warn("Server team 'server' not found! Admin must run: /lcteam create server");
        }
    }
    
    public static boolean canAccess(Player player) {
        return player.hasPermission("towny.admin");
    }
    
    public static void deposit(double amount) {
        if (serverTeam != null) {
            serverTeam.tryAddCoins(amount);
        }
    }
}
```

**Note**: Admin must create team "server" before Towny economy works for nationless towns.

#### Tax Collection

```java
public void collectDailyTaxes() {
    for (Town town : Towny.getDataSource().getTowns()) {
        if (!town.hasTaxes()) continue;
        
        double tax = town.getTaxes();
        Team townTeam = Team.getTeam("town_" + town.getUUID().toString());
        
        // Determine tax destination
        boolean hasNation = town.hasNation();
        
        for (Resident resident : town.getResidents()) {
            Player player = resident.getPlayer();
            if (player == null) continue;
            
            // Collect from player wallet
            boolean collected = collectTaxes(player, tax);
            
            if (!collected) {
                handleTaxDebtor(resident, town, tax);
                continue;
            }
            
            // Deposit to town's team account
            if (townTeam != null) {
                townTeam.tryAddCoins(tax);
            }
        }
        
        // After player taxes collected, distribute to nation or server
        if (hasNation) {
            // Town has nation - nation takes tax from town
            Nation nation = town.getNation();
            double nationTax = nation.getTax();
            
            if (nationTax > 0 && townTeam != null) {
                if (townTeam.tryRemoveCoins(nationTax)) {
                    nation.deposit(nationTax);
                }
            }
        } else {
            // Town has no nation - server takes excess/tax from town
            // Optional: configurable amount goes to server
            double serverTax = town.getServerTax();
            if (serverTax > 0 && townTeam != null) {
                townTeam.tryRemoveCoins(serverTax);
                ServerEconomy.deposit(serverTax);
            }
        }
    }
}
}

private void handleTaxDebtor(Resident resident, Town town, double tax) {
    if (town.isTaxingNonResidents() && !resident.isMayor()) {
        // Kick for non-payment if configured
        town.removeResident(resident);
        resident.sendMessage("Kicked from " + town.getName() + " for unpaid taxes");
    } else if (town.isJailingTaxDebtors()) {
        // Move to jail if town has jail plot
        jailResident(resident, town);
    }
}
}

---

## Tax Settings

| Setting | Default | Description |
|---------|---------|------------|
| `taxes` | 0.0 | Daily tax per resident |
| `plotTax` | 0.0 | Tax per owned plot |
| `commercialTax` | 0.0 | Additional tax for commercial plots |
| `embassyTax` | 0.0 | Additional tax for embassy plots |

```java
public class Town extends Government {
    private double taxes = 0.0;
    private double plotTax = 0.0;
    private double commercialTax = 0.0;
    private double embassyTax = 0.0;
    
    public double getTotalTaxFor(Resident resident) {
        double total = taxes;
        total += plotTax * resident.getOwnedPlots().size();
        
        for (TownBlock block : resident.getOwnedPlots()) {
            switch (block.getType()) {
                case COMMERCIAL -> total += commercialTax;
                case EMBASSY -> total += embassyTax;
            }
        }
        return total;
    }
}
```

---

## Plot Pricing

### Setting Plot for Sale

```java
public void setPlotForSale(TownBlock block, double price) {
    block.setForSale(true);
    block.setPrice(price);
    Towny.getDataSource().saveTownBlock(block);
}

public void removePlotSale(TownBlock block) {
    block.setForSale(false);
    block.setPrice(0.0);
    Towny.getDataSource().saveTownBlock(block);
}
```

### Plot Price Calculation

```java
public double calculatePlotPrice(TownBlock block) {
    double base = TownySettings.getClaimPrice();
    Town town = block.getTown();
    
    // Apply multipliers based on plot type
    switch (block.getType()) {
        case SHOP -> base *= TownySettings.getCommercialMultiplier();
        case EMBASSY -> base *= TownySettings.getEmbassyMultiplier();
        case ARENA -> base *= TownySettings.getArenaMultiplier();
        case INN -> base *= TownySettings.getInnMultiplier();
        default -> { /* normal */ }
    }
    
    // Apply town bonus (number of residents)
    double residentBonus = 1.0 + (town.getResidents().size() * 0.01);
    
    return base * residentBonus;
}
```

---

## Economy Checks (Enable/Disable)

```java
public class TownySettings {
    private static boolean useEconomy = true;  // Default: enabled
    
    public static boolean isUsingEconomy() {
        return useEconomy && ModList.get().isLoaded("lightmanscurrency");
    }
}
```

When LC is not present:
- All economy operations fail gracefully
- Towns can still be created (free)
- Land can still be claimed (free)
- Taxes default to 0 and are never collected

---

## Command Integration

### /towny prices

```java
public void pricesCommand(Player player) {
    player.sendMessage("=== Towny Prices ===");
    player.sendMessage("New Town: " + format(getPriceNewTown()));
    player.sendMessage("New Nation: " + format(getPriceNewNation()));
    player.sendMessage("Claim Block: " + format(getClaimPrice()));
    player.sendMessage("Outpost: " + format(getOutpostPrice()));
}
```

### /t deposit / /t withdraw

```java
public void townDepositCommand(Player player, double amount) {
    Town town = player.getTown();
    if (town == null) {
        player.sendMessage("You don't have a town!");
        return;
    }
    
    String account = "town_" + town.getUUID().toString();
    if (!economy.deposit(account, amount)) {
        player.sendMessage("Deposit failed!");
        return;
    }
    
    player.sendMessage("Deposited " + format(amount) + " to town");
}

public void townWithdrawCommand(Player player, double amount) {
    Town town = player.getTown();
    if (town == null) {
        player.sendMessage("You don't have a town!");
        return;
    }
    
    if (!player.hasPermission("towny.town.withdraw")) {
        player.sendMessage("You can't withdraw from town funds!");
        return;
    }
    
    String account = "town_" + town.getUUID().toString();
    if (!economy.withdraw(account, amount)) {
        player.sendMessage("Insufficient funds!");
        return;
    }
    
    // Deposit to player's wallet
    economy.deposit(player.getName(), amount);
    player.sendMessage("Withdrew " + format(amount) + " from town");
}
```

---

## Data Persistence

Town bank account references stored in Towny data:

```json
// towns/uuid.json
{
  "uuid": "...",
  "name": "Example",
  "mayor": "player-uuid",
  "teamID": "town_player-uuid",  // LC Team ID
  "lastTaxCollection": 123456789,
  ...
}
```

On town deletion:

```java
public void deleteTown(Town town) {
    // Move remaining balance somewhere? (configurable)
    
    // Delete LC team
    Team team = Team.getTeam("town_" + town.getUUID().toString());
    if (team != null) {
        team.delete();
    }
    
    // Delete town data
    Towny.getDataSource().removeTown(town);
}
```

---

## Error Handling

| Scenario | Behavior |
|----------|---------|
| LC not loaded | Economy disabled, all operations free |
| Team doesn't exist | Create on first deposit, or fail gracefully |
| Withdraw > balance | Return false, show error to player |
| Player offline, tax due | Skip, try next day |
| Team deleted externally | Recreate on next town access |

---

## Configuration Options

```java
public class TownySettings {
    // Economy
    public static double getPriceNewTown() default 100.0
    public static double getPriceNewNation() default 1000.0
    public static double getClaimPrice() default 10.0
    public static double getOutpostPrice() default 20.0
    
    // Tax defaults
    public static double getDefaultTax() default 0.0
    public static double getDefaultPlotTax() default 0.0
    
    // World options
    public static boolean isUsingEconomy() default true
    public static boolean isTaxing() default true
}
```

Config file: `config/towny/common.toml`

```toml
[economy]
# Enable economy system (requires LightmansCurrency)
useEconomy = true

# Prices
priceNewTown = 100.0
priceNewNation = 1000.0
priceClaimTownBlock = 10.0

# Taxes
defaultTax = 0.0
defaultPlotTax = 0.0

# Account behavior
deleteTownBalances = "keep"  # "keep", "distribute", "delete"
```

---

## Dependencies

**Required Mod:**
- LightmansCurrency 1.21-2.3.0+

**Optional (for PAPI placeholders):**
- PlaceholderAPI (if available)

---

## PlaceholderAPI Integration (Optional)

If PAPI is present:

```
%towny_town_balance%     - Current town's bank balance
%towny_nation_balance%   - Current nation's balance
%towny_personal_wallet% - Player's wallet value
%towny_plot_price%      - Price of plot standing on
```

```java
public class TownyExpansion extends PlaceholderExpansion {
    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        switch (identifier) {
            case "town_balance":
                Town town = TownyAPI.getTown(player);
                if (town == null) return "N/A";
                return format(getTownBalance(town));
            
            case "personal_wallet":
                return format(getPlayerBalance(player));
            
            default:
                return null;
        }
    }
}
```