# Town Feature Specification

## Overview
A **Town** is a collection of plots owned by a single resident (the mayor) within a world. Towns belong to a **Nation** (optional) and have their own economy, permissions, and settings.

## Core Requirements
1. **Creation**
   - Command: `/town new <name>`
   - Must have a claimed plot at the player’s location.
   - The creator becomes the mayor and automatically a resident of the town.
   - Validation: name uniqueness, length limits, allowed characters.
2. **Resident Management**
   - Residents can be invited (`/town invite <player>`), accepted, or revoked.
   - Mayor can promote/demote ranks (`/town rank <player> <rank>`). Ranks affect permissions (e.g., plot claim, tax payment).
3. **Plot Claiming**
   - Town members may claim additional plots (`/town claim`) respecting town size limits (configurable).
   - Each plot belongs to exactly one town; cannot overlap.
4. **Economy**
   - Town has a bank account. Taxes are collected from residents periodically (configurable interval).
   - Town can set home block, spawn, and outpost locations.
5. **Nation Association**
   - Optional joining of a nation (`/town nation join <nation>`). Leaving with `/town nation leave`.
   - Nation tax contribution is calculated as a percentage of town’s balance.
6. **Permissions & Settings**
   - Town settings are stored in `towns.yml` (or equivalent) and can be edited via GUI or `/town set <key> <value>`.
   - Permissions are hierarchical: Mayor > Assistants > Residents.
7. **Deletion & Ruin**
   - Town can be deleted by mayor or admin (`/town delete`).
   - On deletion, all plots become wilderness; funds are transferred to nation or discarded based on config.

## Edge Cases & Validation
- Prevent creation if the player already owns a town.
- Disallow duplicate town names across worlds.
- Ensure tax collection does not underflow town balance.
- Handle nation removal: towns become nation‑less automatically.
- Graceful handling of world unload/reload – persist town data.

## Integration Points
- **TownyAPI**: `TownyUniverse.getTown(String name)` for lookups.
- **EconomyHandler**: `TownyEconomyHandler` for account operations.
- **Event System**: fire `TownAddResidentEvent`, `TownRemoveResidentEvent`, `TownDeleteEvent`.

## Implementation Status (as of 2026-04-28)
- ✅ `Town` now lives at `com.baldeagle.towny.object.town.Town` and extends `Government`.
- ✅ `Town` tracks a mayor and optional nation membership (`getMayor`, `hasNation`, `getNation`).
- ✅ Resident add/remove operations synchronize resident-town linkage.
- ✅ Legacy aliases `getOwner` / `setOwner` remain for backward compatibility while migrating code paths.

## Tests Required
- Creation flow success and failure cases.
- Resident invitation acceptance and denial.
- Plot claim limits enforcement.
- Tax collection over multiple days.
- Nation join/leave persistence.
