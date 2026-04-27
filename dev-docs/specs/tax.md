# Tax System Specification (Towny)

## Overview
The tax subsystem governs regular financial levies applied to residents, towns, and nations. Taxes are collected automatically at configurable intervals and are routed to the appropriate treasury accounts.

## Tax Types
1. **Resident Tax**
   - Charged to every resident on a scheduled basis (daily by default).
   - Amount is defined in the town’s configuration (`town.tax.resident-amount`).
   - Collected from the resident’s personal wallet (using Lightmans Currency denominations).
2. **Town Plot Tax**
   - Applied per claimed plot as described in the Plot specification.
   - Calculated as `town.plot-tax-rate * numberOfPlots` and drawn from the town’s bank account.
3. **Town Treasury Tax**
   - An optional flat or percentage tax that the town may impose on residents for services (e.g., upgrades).
4. **Nation Tax**
   - Each nation defines a tax percentage (`nation.tax.percent`).
   - At the end of the tax cycle, every member town contributes `town.balance * (nation.tax.percent / 100)` to the nation’s account.

## Collection Cycle
- **Interval** – Configurable (`tax.collection-interval`), default is once per in‑game day.
- **Order of Operations**
  1. Resident taxes are deducted first; if a resident lacks sufficient funds, the transaction fails and the resident is marked as *delinquent*.
  2. Town plot taxes are then withdrawn from the town account.
  3. Nation tax is calculated from the remaining town balance and transferred to the nation account.
- **Failure Handling**
  - If a town cannot pay its plot tax, the town enters *tax arrears*; subsequent tax cycles may trigger penalties such as reduced town level or forced plot unclaim.
  - Delinquent residents may be restricted from certain actions (e.g., plot purchase) until they settle.

## Configuration Keys (example)

All tax amounts are expressed in **copper units**. The system will automatically convert the required amount into the highest possible Lightmans Currency denominations (netherite → diamond → emerald → gold → iron → copper) when performing deductions.

```yaml
tax:
  collection-interval: 1d  # measured in in‑game days (Minecraft days)
  resident:
    amount: 10  # copper units per cycle
  town:
    plot-tax-rate: 5  # copper per plot per cycle
    flat-tax: 0
  nation:
    percent: 3  # % of town balance
```

*Note: Nation tax percentage is applied to the town's total balance expressed in copper units (the lowest denomination).*

```yaml
tax:
  collection-interval: 1d  # measured in in‑game days (Minecraft days)
  resident:
    amount: 10  # copper units per cycle
  town:
    plot-tax-rate: 5  # copper per plot per cycle
    flat-tax: 0
  nation:
    percent: 3  # % of town balance
```

## Integration Points
- **TownyEconomyHandler** – Handles balance checks and transfers.
- **Events**
  - `TownTransactionEvent` – emitted when town pays plot tax.
  - `NationTransactionEvent` – emitted for nation tax contributions.
  - `ResidentJailEvent` – may be triggered for chronic tax delinquency.
- **Commands**
  - `/town tax set <amount>` – adjust town tax amount.
  - `/nation tax <percent>` – set nation tax percentage.

## Edge Cases
- Tax collection must respect Lightmans Currency conversion rules (highest denomination first).
- When a resident is offline, the tax is applied when they next log in; if insufficient funds persist, they become delinquent.
- Nations with no towns should not collect tax.
- Tax percentages are clamped to 0‑100.

## Tests Required
- Successful resident tax deduction.
- Plot tax correctly proportional to number of plots.
- Nation tax correctly calculated from town balances.
- Failure scenarios: insufficient resident funds, town arrears handling, delinquent flagging.
