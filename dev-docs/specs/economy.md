# Economy Feature Specification

## Overview
The economy subsystem manages all monetary transactions for towns, nations, and residents. It integrates with Vault (optional) and supports configurable tax rates.

## Core Requirements
1. **Accounts**
   - Global server account, nation accounts, town accounts, and resident personal accounts.
   - Accounts are created lazily when first accessed.
2. **Transactions**
   - Deposit, withdraw, and transfer operations with atomicity.
   - Transaction logging via `TownyTransaction` for audit.
3. **Taxes**
   - Town tax collected from residents on a configurable schedule.
   - Nation tax collected from towns based on nation’s tax percentage.
   - Tax exemption flags per resident/town.
4. **Integration with Vault**
   - Provide `Economy` implementation if Vault is present.
   - Support for other economy plugins via `EconomyProvider` interface.
5. **Configuration**
   - Currency name, symbol, decimal places.
   - Default balances for new residents.
   - Interest or inflation settings (optional).
6. **Concurrency**
   - Thread‑safe handling of account balances.
   - Use synchronized blocks or concurrent maps.

## Edge Cases
- Prevent negative balances unless overdraft is enabled.
- Handle failed transactions gracefully and roll back.
- Ensure tax collection does not cause infinite loops (e.g., nation tax triggering town tax).
- Offline player transactions queued until player data loads.

## Integration Points
- `EconomyHandler` and `BankEconomyHandler` classes.
- Events: `TownTransactionEvent`, `NationTransactionEvent`, `BankTransactionEvent`.
- Commands: `/money`, `/town deposit`, `/nation tax`.

## Tests
- Deposit/withdraw correctness.
- Tax collection over multiple cycles.
- Vault integration mock tests.
- Concurrency stress test for simultaneous transactions.
