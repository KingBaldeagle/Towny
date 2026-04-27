# Economy Provider Integration Specification (Towny)

## Overview
Towny abstracts all monetary operations through an **Economy Provider** interface. This allows the core plugin to work with any economy mod (Vault, Reserve, or custom implementations) without hard‑coding currency logic. The Neo‑Towny re‑implementation integrates **Lightmans Currency** as the default provider.

## Core Interfaces
1. **`EconomyProvider`** (package `com.baldeagle.towny.object.economy.provider`)
   - Methods: `deposit(Resident, long amount)`, `withdraw(Resident, long amount)`, `getBalance(Resident)`, `transfer(Resident, Resident, long amount)`.
   - All amounts are expressed in **copper units** (the lowest denomination) to keep a consistent base across providers.
2. **`EconomyAdapter`** (package `com.baldeagle.towny.object.economy.adapter`)
   - Bridges the provider to Towny’s internal transaction system (`TownyTransaction`).
   - Handles conversion between provider‑specific currency objects and the long‑based copper value.
3. **`TownyEconomyHandler`**
   - Central façade used by Towny code (taxes, plot sales, command handling). Delegates to the active `EconomyProvider`.

## Lightmans Currency Integration
- **Provider Implementation**: `LightmansCurrencyProvider` implements `EconomyProvider`.
  - Uses Lightmans Currency’s API to create, query, and modify coin stacks.
  - Converts coin stacks to a **copper total** using the denomination hierarchy (netherite → diamond → emerald → gold → iron → copper).
  - When depositing, the provider attempts to mint the highest possible denominations first (e.g., 1 gold instead of 100 copper) following the conversion rules defined in `lightmans_currency.md`.
- **Configuration** (`towny.yml`)
  ```yaml
  economy:
    provider: "lightmanscurrency"   # other options: "vault", "reserve"
    use-legacy: false                # if true, fallback to legacy integer balances
  ```
- **Fallback Path**: If Lightmans Currency is not present, Towny falls back to a simple integer‑based economy (stored in `TownyDataSource`).

## Provider Selection Flow
1. During plugin enable, Towny reads `towny.yml` to determine the provider name.
2. It attempts to instantiate the corresponding class via reflection (`Class.forName`).
3. If instantiation fails, Towny logs a warning and loads the **LegacyProvider** which stores balances as plain long values.
4. The selected provider is stored in `TownyEconomyHandler.setProvider(EconomyProvider)` and used globally.

## Transaction Lifecycle
- **Create Transaction**: `TransactionBuilder` builds a `Transaction` object with source, destination, amount (copper), and a description.
- **Execute**: `TownyEconomyHandler.performTransaction(Transaction)` calls the provider’s `withdraw`/`deposit` methods. On failure, the transaction is aborted and the appropriate `TownyTransactionEvent` is not fired.
- **Event Emission**:
  - `TownTransactionEvent` – for town‑level transactions.
  - `NationTransactionEvent` – for nation‑level transactions.
  - `BankTransactionEvent` – for bank‑related operations.
  - Listeners can cancel these events to prevent the underlying provider call.

## Interaction with Existing Specs
- **Economy Spec (`economy.md`)**: Defines tax collection, account structures, and periodic deductions. The provider is the concrete implementation that moves money between accounts.
- **Lightmans Currency Spec (`lightmans_currency.md`)**: Provides the denomination hierarchy and conversion rules. The provider must respect these rules when converting copper totals to actual coin items.
- **Tax Spec (`tax.md`)**: Calls `TownyEconomyHandler` to deduct taxes; the provider ensures correct currency handling.

## Edge Cases & Validation
- **Insufficient Funds**: Provider returns `false` on withdraw; the calling code (e.g., tax collection) logs the failure and may mark the resident/town as delinquent.
- **Overflow**: Amounts are stored as signed 64‑bit longs; checks are performed before any addition to prevent overflow.
- **Concurrency**: Provider methods must be thread‑safe. Lightmans Currency’s API uses synchronized handlers; custom providers should use `synchronized` blocks or atomic operations.
- **Offline Residents**: Balances are persisted in the resident data file; provider must support loading/saving without requiring the player to be online.
- **Currency Change**: If the server switches providers at runtime (e.g., from Lightmans to Vault), Towny does **not** auto‑migrate balances. An admin tool (`/towny economy migrate <from> <to>`) can be implemented to convert stored copper totals.

## Testing Guidelines
- **Provider Unit Tests** – Mock Lightmans Currency API to verify correct copper‑to‑coin conversion and vice versa.
- **Integration Tests** – Run tax collection, plot sale, and command‑based transactions with Lightmans Currency enabled; assert final balances match expected copper totals.
- **Fallback Tests** – Disable Lightmans Currency and ensure the LegacyProvider correctly handles deposits/withdrawals.
- **Concurrency Tests** – Simulate multiple simultaneous deposits/withdrawals on the same account to ensure thread safety.

## Future Extensions
- Support for multiple simultaneous providers (e.g., a hybrid system where towns use Lightmans Currency and nations use Vault) via a **provider registry**.
- Expose provider selection via a command (`/towny economy setprovider <name>`) with runtime reload support.
- Add hooks for custom plugins to implement their own `EconomyProvider` and register it through the Towny API.
