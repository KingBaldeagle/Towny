# Lightmans Currency Integration

## 1. Overview

**Lightmans Currency (LC)** is a modern, lightweight economy core for Fabric/NeoForge that exposes a clean API for wallets, team bank accounts, and simple coin‑based transactions.  Neo‑Towny leverages LC to provide a *drop‑in* economy solution without the need to bundle a full-fledged banking plugin.  This document explains how LC is integrated, where the integration lives in the source tree, and how developers can extend or troubleshoot the behaviour.

Key take‑aways:

* **Dependency** – LC is added as a `compileOnly` dependency.  The game does not bundle it, so it is expected to be present on the server.
* **Objects mapped** – **Players** → *Wallet*, **Towns** → *Team (bank account)*, **Nations** → *internal pseudo‑account*.
* **Basic flow** – When a town is created a `Team` is created, the mayor is made the owner.  Residents are added as *members* if the town is public or open.
* **Taxes/plot sales** – Withdrawals from a resident’s wallet are attempted; successful asserts are deposited into the town’s team, failing withdrawals result in a loss/penalty.

The sections below walk through the build setup, the LC API, how the integration is wired into Towny, and examples of core features.

---

## 2. Prerequisites

1. **NeoForge** build of Towny (see root `build.gradle`).  The mod currently targets Java 17.
2. **Lightmans Currency** (LC) must be installed on the server.  The current implementation targets LC `1.21-2.3.0` – the manifest must match the required version.
3. The LC jar is typically found in your server mods folder, or you can fetch the artifact from the official repository.
4. Optional: the `Guilds` or `Teams` APIs of LC.  The example uses the `Team` class for town accounts; `WalletUtils` and `MoneyAPI` are used for wallet interactions.

---

## 3. Gradle Integration

Add the following snippet to `build.gradle` under `dependencies`:

```gradle
dependencies {
    compileOnly "com.lightman314:lightmanscurrency:${project.lc_version}"
}
```

> **Tip** – Keep the LC version in `gradle.properties` to make it easier to bump:
> ```properties
> lc_version=1.21-2.3.0
> ```

The `compileOnly` keyword ensures that LC’s classes are available at compile time but are not bundled into your jar, respecting the plugin‑mod separation.

---

## 4. LC API Fundamentals

Below is a quick reference of the LC classes and their most commonly used methods.  For a complete reference, consult the [LC source repository](https://github.com/LightmansCurrency/LightmansCurrency) and the generated Javadoc.

| Class | Responsibility | Typical Methods | Notes |
|-------|----------------|-----------------|-------|
| `com.lightmanscurrency.api.team.Team` | Represents a paid‑account (bank). | `create`, `getTeam`, `tryAddCoins`, `tryRemoveCoins`, `getCoinValue`, `setOwner` | Town accounts are created as a `Team` with a unique ID (`town_uuid`). |
| `com.lightmanscurrency.api.wallet.WalletUtils` | Helpers for player wallets. | `getWallet`, `tryAddCoins`, `tryRemoveCoins` | Wallet holds physical coin items. |
| `com.lightmanscurrency.api.money.MoneyAPI` | Formatting and conversion. | `formatValue` | Provides string representation with locale‑aware formatting. |
| `com.lightmanscurrency.api.team.Team` | Interacts with team bank accounts. | `getTeam`, `setOwner`, `tryAddCoins`, `tryRemoveCoins`, `getCoinValue` | Handles bankroll operations. |
| `com.lightmanscurrency.api.server.ClaimData` | (Optional) Used if you want to integrate claim‑based payments. | – | Not used in the core integration. |

### 4.1 Wallet Interaction

```java
ServerPlayer player = …; // Retrieved from Bukkit or NeoForge classes
Wallet wallet = WalletUtils.getWallet(player);
if (wallet != null) {
    double coins = wallet.getCoinValue();
    // Withdraw 10 coins
    boolean success = WalletUtils.tryRemoveCoins(player, 10.0);
    // Deposit 5 coins back
    WalletUtils.tryAddCoins(player, 5.0);
}
```

### 4.2 Team (Bank) Interaction

```java
// Create a new account for a town
Team townTeam = Team.create(townName, townUUID.toString());
// Set mayor as the owner
townTeam.setOwner(mayorUUID);
// Add a resident
townTeam.addMember(memberUUID);
// Deposit coins
townTeam.tryAddCoins(500.0);
// Withdraw
boolean ok = townTeam.tryRemoveCoins(200.0);
```

---

## 5. Towny Integration Points

All the integration logic lives inside the `TownyEconomy` class.  A minimal example is shown in `TownyEconomy.java` in the root `neo-towny/src/main/java/com/baldeagle/towny/economy`.

### 5.1 Initialization

When Towny starts, it calls `TownyEconomy.initialize()`.  The method checks whether LC is present and toggles `active` accordingly.

```java
public void initialize() {
    if (!ModList.get().isLoaded("lightmanscurrency")) {
        TownyEconomy.LOGGER.warn("LightmansCurrency not found - economy disabled");
        this.active = false;
        return;
    }
    this.active = true;
}
```

### 5.2 Town Account Creation

During town creation (`TownNewCommand`), after the user pays the creation cost, we invoke:

```java
Team townTeam = Team.create(town.getName(), town.getUUID().toString());
// Make mayor the owner
townTeam.setOwner(town.getMayor().getUUID());
// Optionally add residents
for (Resident res : town.getResidents()) {
    if (!res.isMayor()) townTeam.addMember(res.getUUID());
}
```

The `Team` ID follows the convention `town_UUID` which allows us to retrieve it later with `Team.getTeam(id)`.

### 5.3 Nation Revenue

Nations do **not** use LC `Team` accounts directly. Instead, each town contributes a portion of its tax collection to a *pseudo‑nation account* maintained locally.  When a town pays its daily tax, the code checks an internal map:

```java
double nationShare = town.getStocks().percentShare();
if (nationShare > 0) {
    NationBiztion nationBiz = treasury.get(nationId);
    nationBiz.addRevenue(taxAmount * nationShare);
}
```

This keeps LC focused on player‑centric wallets/banks while keeping nation balances internally.

### 5.4 Tax Collection Flow

Collect taxes on a scheduled event (e.g., daily ticker):

```java
for (Town town : TownyUniverse.getTowns()) {
    Team townTeam = Team.getTeam(town.getUUID().toString());
    if (townTeam == null) continue; // Defensive
    for (Resident res : town.getResidents()) {
        double tax = town.getTaxes();
        if (!WalletUtils.tryRemoveCoins(res.getPlayer(), tax)) {
            // Equal to : handle tax arrears – e.g., kick, jail, or apply penalty
            handleTaxOwer(res, town, tax);
            continue;
        }
        townTeam.tryAddCoins(tax); // Deposit into town account
    }
}
```

When a resident cannot pay, the helper `handleTaxOwer` can trigger an event or switch the resident to a “poverty” state.

### 5.5 Plot Sales

When a town claims a plot for a resident, the resident pays the sale price from their wallet and the town’s bank account receives the funds.

```java
if (!WalletUtils.tryRemoveCoins(buyer, plot.getPrice())) return false;
Team townTeam = Team.getTeam(town.getUUID().toString());
if (townTeam != null) townTeam.tryAddCoins(plot.getPrice());
```

### 5.6 Handling Missing LC

If the mod is missing, the `isActive()` method returns `false`.  All economy calls become no‑ops, leaving the game in a *closed‑economy* state; units simply see a 0 balance and transactions are denied.  Logging informs the admin which allows quick detection.

---

## 6. Configuration & Customisation

The core logic uses a naming convention and a small set of hard‑coded values.  If you wish to change how coins are taxed or how the team names are generated, you can do so in the following files.

| File | What to edit |
|------|--------------|
| `TownyEconomy.java` | Naming constants, fallback logic |
| `TownyCommands.java` | Argument parsing for `/town set balance` (if you add a command) |
| `dev-docs/specs/` | Document new config values |

| Config | Default value |
|--------|---------------|
| `econ.town.prefix` | `town_` |
| `econ.nation.prefix` | `nation_` |
| `economy.enabled` | `true` |
| `economy.fallback_mode` | `closed` |

Entries can be added to `config/towny.yml` (or the new `config/towny/dev.yml` if you use a dev config).  The mod does not read custom properties at runtime currently, but you can extend the `TownyConfig` class in the future.

---

## 7. Testing the Integration

### 7.1 Unit Tests

* `IntegrationTestableBankAccountTest` – verifies that deposit, withdrawal, and balance behave correctly.
* `TaxCollectionTest` – ensures each resident’s wallet results in a proper transfer.
* `PlotSaleTest` – checks that the resident’s wallet is debited and the town team receives the funds.

Add the tests to the `src/test/java/...` directory and run them with `./gradlew test`.

### 7.2 Manual Play‑testing

1. Start a local server with the mods: Towny + LC.
2. Create a town that requires a creation cost; confirm a team is created in the LC UI.
3. Claim a plot, ensure the resident’s wallet is decreased and the town’s team increased.
4. Wait for the daily tax event (or trigger it manually by calling `TownyEconomy.collectTaxes()` from the console). Verify residents lose money and the team account is credited.
5. Remove LC from the mods folder, restart the server. Observe warnings and that all economy calls now are no‑ops.

---

## 8. Common Issues & FAQs

**Q1. I see a warning about “LightmansCurrency not found – economy disabled”. Why?**

> Ensure the LC jar is present in the same `mods` directory as Towny. The version number must match the one referenced in `gradle.properties`.

**Q2. My team account is missing a balance after a purchase.**

> Check that the team ID matches the pattern used at creation (`town_UUID`).  If not, the fetch `Team.getTeam(id)` will return `null`.

**Q3. Residents can still see a balance of 0 even after depositing coins.**

> The game stores wallets in the player’s inventory. If the player has no coins in the container, the balance will be 0. Use `/lc inventory` to inspect.

**Q4. How do I add a custom tax multiplier for a specific town?**

> Extend `Town.getTaxes()` to consider a custom field or add a configuration entry that overrides the default multiplier.

---

## 9. Further Reading

* **Lightmans Currency API Javadoc** – https://www.lightmanscurrency.com/api/java
* **Towny Economy Design Docs** – `dev-docs/specs/economy.md` (to be created) – outlines internal pseudo‑account handling.
* **NeoForge Modding Guide – Gradle** – https://docs.neoforged.net/docs/guide/building-mods/distribution/#dependencies

---

## 10. Summary

The LC integration is fully reactive: it adds a robust economy with minimal friction.  By mapping towns to LC `Team` banks and using player `Wallet`s, we keep the public API lean while exposing the power of LC’s robust transaction system.  Careful naming conventions, documentation, and fallback logic ensure the mod is resilient when LC is missing or misconfigured.
