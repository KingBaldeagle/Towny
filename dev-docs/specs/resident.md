# Resident Feature Specification

## Overview
A **Resident** represents a player in the Neo‑Towny system. Residents can belong to a town, have ranks, and hold permissions.

## Core Requirements
1. **Registration**
   - On first login, a Resident object is created automatically.
   - Username is stored; UUID is primary identifier.
2. **Town Membership**
   - Residents can be mayor, assistant, or regular member.
   - Invitation flow via `/town invite` and `/town accept`.
3. **Permissions**
   - Permissions are derived from rank and town/nation settings.
   - Permissions can be overridden per resident via `/resident setperm`.
4. **Economy Interaction**
   - Residents hold personal balances in the town bank (if enabled).
   - Ability to deposit/withdraw via `/money` commands.
5. **Jail & Punishment**
   - Residents may be jailed; jail time tracked.
6. **Data Persistence**
   - Stored in `players.yml` or database; survives server restarts.

## Edge Cases
- Handle name changes (username updates) while preserving UUID.
- Prevent duplicate resident entries.
- Correctly handle offline residents during tax collection.

## Integration Points
- `Resident` class (`com.baldeagle.towny.object.resident.Resident`).
- Events: `ResidentJailEvent`, `ResidentUnjailEvent`, `ResidentToggleModeEvent`.
- Economy: `Resident.getAccount()`.

## Implementation Status (as of 2026-04-28)
- ✅ `Resident#isMayor()` uses a null-safe mayor comparison with `town.getMayor()`.
- ✅ Resident has town/nation linkage helpers (`hasTown`, `getTown`, `hasNation`).
- 🚧 Nation/king behavior remains TODO (`isKing()` still returns false).

## Tests
- Automatic creation on login.
- Invitation acceptance/rejection.
- Permission checks for rank actions.
- Jail/unjail lifecycle.
