# Plot Feature Specification

**Size**: A Plot (TownBlock) occupies exactly one Minecraft chunk (16 × 16 blocks) on the X‑Z plane. The height spans the entire world height (Y‑axis).

## Overview
A **Plot** is a land parcel within a town. Plots have owners, types, and permissions (e.g., PvP, mobs, explosions).

## Core Requirements

1. **Claiming**
   - `/town claim` claims the plot the player stands on.
   - Must be adjacent to an existing town plot unless town is open.
   - Enforce maximum plot count per town (config).
   - **Taxation**: Each claimed plot is subject to the town's plot tax rate (configurable). Tax is deducted from the town account periodically.
   - **Sale**: Plots can be put up for sale by the town (`/plot sell <price>`). Price uses Lightmans Currency denominations (copper, iron, gold). When a player purchases, the highest denomination available is deducted (e.g., 1 gold > 10 iron > 100 copper) and transferred to the town account.
2. **Ownership & Transfer**
   - Plots can be owned by the town or an individual resident.
   - Owner can transfer via `/plot transfer <player>`.
3. **Plot Types**
   - Residential, Commercial, Agricultural, etc., each with default settings.
   - Plot type set by mayor (`/plot type <type>`).
4. **Permissions**
   - Toggle flags: PvP, Fire, Mobs, Explosions, Taxable.
   - Commands: `/plot set <flag> <true|false>`.
5. **Pricing & Taxes**
   - Claim cost may depend on plot type and town size.
   - Taxable flag determines whether town tax is applied.
6. **Deletion**
   - Unclaimed plots become wilderness.
   - Plot deletion via `/plot unclaim` (may require mayor permission).

## Edge Cases
- Prevent claim on protected regions (WorldGuard integration).
- Ensure plot boundaries align to chunk/grids as defined by config.
- Handle world unload – persist plot data.

## Integration Points
- `TownBlock` class (`com.baldeagle.towny.object.TownBlock`).
- Events: `TownBlockClaimEvent`, `TownBlockUnclaimEvent`, `TownBlockOwnerChangeEvent`.
- Economy: claim cost deducted from town account.

## Tests
- Claim flow success and failure (adjacent, max limit).
- Flag toggling persistence.
- Transfer ownership validation.
