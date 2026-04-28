# Lightmans Currency Specification

## Denominations
The currency hierarchy is based on a decimal‑like system where each higher tier is worth **10** of the immediate lower tier:

- **Copper** – base unit
- **Iron** – 10 copper
- **Gold** – 10 iron (100 copper)
- **Emerald** – 10 gold (1 000 copper)
- **Diamond** – 10 emerald (10 000 copper)
- **Netherite** – 10 diamond (100 000 copper)

## Conversion Rules
When a transaction occurs, the system attempts to satisfy the amount using the **highest denomination available** in the payer’s wallet. The algorithm works as follows:
1. Determine the total price in copper units.
2. Starting from the highest denomination (netherite) down to copper, take as many whole coins of that denomination as possible without exceeding the required amount.
3. Subtract the value covered by those coins and continue with the next lower tier.
4. If the payer lacks sufficient total value, the transaction fails.

### Example
- Price: **100 copper** (equivalent to 1 gold).
- Player’s wallet contains: 0 netherite, 0 diamond, 0 emerald, **1 gold**, 5 iron, 50 copper.
- The system uses the 1 gold coin (the highest denomination that can cover the price) and deducts it, leaving the remaining balance unchanged.

If the player only had **15 iron** and **0 gold**, the system would convert 10 iron to 1 gold internally, then use the remaining 5 iron (50 copper) plus any copper to reach the required amount.

## Pricing in Specifications
All prices in Neo‑Towny specifications are expressed in **copper units** to avoid ambiguity. For example, a plot priced at "1 gold" should be written as **100 copper** in the spec. The implementation will handle conversion to the appropriate coin items when displaying or processing the transaction.

## Implementation Status (as of 2026-04-28)
- ✅ Economy abstraction now uses copper-unit values (`long`) for balances and transfers.
- ✅ Denomination breakdown/formatting helpers are implemented in `LightmansCurrencyProvider`.
- 🚧 Real wallet/team API calls to Lightman's Currency are still pending; current provider uses in-memory balances as an integration-safe fallback.
