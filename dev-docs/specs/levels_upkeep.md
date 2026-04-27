# Nation & Town Level / Upkeep Specification (Towny)

## Overview
Towny assigns **levels** to both Nations and Towns based on population, land ownership, and economic activity. Levels affect tax rates, plot limits, and other gameplay modifiers. Upkeep (maintenance costs) is periodically deducted from town/nation accounts and may trigger penalties if unpaid.

## Level Calculation
### Town Level
- **Base Formula** (configurable via `towny.yml`):
  ```
  town.level = floor( sqrt( residentCount ) ) + floor( plotCount / 10 )
  ```
- **Modifiers**:
  - **Bonus from Economy**: If the town’s bank balance exceeds a threshold, add `+1` level.
  - **Penalty**: If the town is delinquent on taxes, subtract `-1` level.
- Levels are recomputed **daily** during the `NewDayTimerTask`.

### Nation Level
- **Base Formula**:
  ```
  nation.level = floor( sqrt( totalResidentCount ) ) + floor( totalTownCount / 5 )
  ```
- **Modifiers**:
  - **Allied Nations Bonus**: Each allied nation adds `+0.5` (rounded down at final calculation).
  - **War Penalty**: If the nation is at war, subtract `-1` level.
- Recalculated **daily** similar to towns.

## Upkeep Mechanics
1. **Upkeep Cost Determination**
   - Configurable per level in `towny.yml`:
     ```yaml
     upkeep:
       town:
         base: 10            # copper per level per day
         multiplier: 1.2    # scaling factor per additional level
       nation:
         base: 100           # copper per level per day
         multiplier: 1.5
     ```
   - Final cost = `base * (multiplier ^ (level‑1))` rounded to nearest copper.
2. **Deduction Process**
   - Executed during the **tax collection cycle** (see `tax.md`).
   - Towny calls `TownyEconomyHandler.withdraw(townAccount, amount)`.
   - The `amount` is expressed in **copper units** (the base denomination of Lightmans Currency). When the withdrawal is performed, the Lightmans Currency provider converts the copper total into the highest possible coin denominations (netherite → diamond → emerald → gold → iron → copper) before deducting from the account.
   - If the account lacks sufficient funds, the town/nation enters **upkeep arrears**.
3. **Arrears & Penalties**
   - First missed payment: town level decreases by 1 at next recalculation.
   - Second consecutive miss: town is marked **inactive**; residents cannot claim new plots.
   - Third consecutive miss: town is **ruined** and may be deleted automatically.
   - Nations follow analogous penalties but affect all member towns.

## Configuration Options
```yaml
level:
  town:
    calc-residents: true   # include resident count in level calc
    calc-plots: true       # include plot count in level calc
  nation:
    calc-residents: true
    calc-towns: true
upkeep:
  town:
    base: 10
    multiplier: 1.2
    max-misses: 3
  nation:
    base: 100
    multiplier: 1.5
    max-misses: 2
```

## Events
- `TownLevelChangeEvent` – fired after a town’s level is recalculated.
- `NationLevelChangeEvent` – analogous for nations.
- `TownUpkeepFailureEvent` / `NationUpkeepFailureEvent` – triggered when a payment cannot be made.
- Listeners may cancel these events to prevent penalties (e.g., a custom plugin grants a grace period).

## Interaction with Other Specs
- **Tax Spec (`tax.md`)**: Upkeep deductions are part of the overall tax collection cycle.
- **Economy Provider (`economy_provider.md`)**: Handles the actual withdrawal of copper from accounts.
- **Jail Spec (`jail.md`)**: Delinquent residents (e.g., unable to pay fines) may affect town level via tax delinquency.
- **Outpost & Spawn Specs**: Town level can influence the maximum number of outposts allowed (`town.outpost.max-per-level`).

## Edge Cases
- **Zero Balance** – If a town’s balance is zero, the upkeep cost is still attempted; failure triggers arrears.
- **Negative Level** – Levels are clamped to a minimum of 1.
- **World Unload** – Level recalculation is paused if the world containing the town/nation is unloaded; it resumes on world load.
- **Offline Towns/Nations** – Calculations run server‑side; offline status does not affect level updates.
- **Concurrent Modifications** – When a resident joins/leaves a town during the daily recalculation, the level used for that day is based on the pre‑recalculation state to avoid race conditions.

## Tests Required
- Verify correct level calculation for various resident/plot counts.
- Test upkeep deductions and arrears progression across multiple days.
- Ensure events fire with accurate old/new level values.
- Simulate insufficient funds and confirm penalty application.
- Confirm configuration overrides (e.g., custom base/multiplier) affect results correctly.
- Check interaction with tax collection (upkeep deducted after taxes).
