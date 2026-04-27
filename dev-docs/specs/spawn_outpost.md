# Spawn & Outpost Management Specification (Towny)

## Overview
Towns and Nations can define **spawn points** (default teleport destinations) and **outpost spawns** (additional safe locations). These locations are stored per town/nation and can be set, queried, and used by teleport commands.

## Spawn Types
1. **Town Spawn**
   - Each town has a primary spawn location (`Town.getSpawn()`), usually set by the mayor with `/town setspawn`.
   - Used by `/town spawn` and as the fallback location for town‑related teleportation.
2. **Nation Spawn**
   - Nations have a capital spawn (`Nation.getSpawn()`) set by the king via `/nation setspawn`.
   - Accessed by `/nation spawn` for all nation members.
3. **Outpost Spawn**
   - Towns can define additional outpost locations (`Town.addOutpostLocation(Location)`). Outposts serve as secondary safe zones, often used for remote bases.
   - Accessed via `/town outpost <index>` where `<index>` selects which outpost to teleport to.
   - **Outpost Area** – Each outpost defines a protected **radius** (default 8 blocks) around its location. Within this radius, the outpost inherits the town’s plot permissions (build, destroy, PvP, etc.) but can have its own flag overrides set via `/plot set` commands when standing inside the outpost area.
   - **Protection** – Mobs, fire, and explosions respect the outpost’s plot flags. The outpost area is considered a single plot for permission checks, even if it spans multiple chunk boundaries.
   - **Visualization** – When a player stands within an outpost radius, a particle halo (configurable) is displayed to indicate the protected zone.


## Commands
| Command | Description | Permission | Notes |
|---|---|---|---|
| `/town setspawn` | Set the town’s primary spawn to the player’s current location. | `towny.town.spawn.set` | Stores coordinates and world.
| `/town spawn` | Teleport to the town’s spawn point. | `towny.town.spawn` | Warm‑up applies (see Teleportation spec).
| `/town addoutpost` | Add a new outpost at the player’s location. | `towny.town.outpost.add` | Outposts are indexed in the order added.
| `/town outpost <index>` | Teleport to the specified outpost. | `towny.town.outpost.teleport` | Index must be valid; warm‑up applies.
| `/nation setspawn` | Set the nation’s spawn location. | `towny.nation.spawn.set` | Only king or ministers with permission.
| `/nation spawn` | Teleport to the nation’s spawn. | `towny.nation.spawn` | Warm‑up applies.

## Data Storage
- **Town Spawn** – Stored in `TownyUniverse` as part of the `Town` object (`Town.setSpawn(Location)`). Persisted to `towns.yml`.
- **Nation Spawn** – Stored in the `Nation` object (`Nation.setSpawn(Location)`). Persisted to `nations.yml`.
- **Outposts** – Each town maintains a list of `Location` objects (`Town.getOutpostLocations()`). Serialized as a list of coordinate strings in `towns.yml`.

## Validation Rules
- The target location must be within the world border and not inside a protected region (WorldGuard check if present).
- Players must have the required permission to set spawns; otherwise the command is denied.
- Outpost index validation: if an invalid index is provided, the command returns an error listing the valid range.

## Edge Cases
- **World Unload** – If the world containing a spawn/outpost is unloaded, Towny marks the location as **invalid** and prevents teleport until the world reloads.
- **Dimension Changes** – Spawns can be in any dimension (Overworld, Nether, End). Teleport commands will handle cross‑dimension travel automatically.
- **Permission Overrides** – Nations can set a flag (`nation.spawn.allow-outside-territory`) to allow members to teleport to the nation spawn even if they are outside nation territory.
- **Outpost Limits** – Configurable maximum number of outposts per town (`town.outpost.max`). Attempting to add beyond this limit returns an informative message.

## Configuration (`towny.yml`)
```yaml
town:
  spawn:
    allow-outside-territory: false   # can members teleport to town spawn from any location?
  outpost:
    max-per-town: 5                 # maximum outposts a town may have
nation:
  spawn:
    allow-outside-territory: true    # nation members can use nation spawn from anywhere
```

## Integration Points
- `Town` and `Nation` classes hold spawn/outpost data.
- `TownCommand` and `NationCommand` handle the respective sub‑commands.
- Teleportation system (`TeleportWarmupTimerTask`) is reused for all spawn/outpost teleports.
- Events:
  - `TownSpawnChangeEvent` – fired when a town’s spawn is set.
  - `NationSpawnChangeEvent` – fired on nation spawn change.
  - `TownOutpostAddEvent` / `TownOutpostRemoveEvent` – for outpost management.

## Tests
- Setting and teleporting to town and nation spawns.
- Adding, listing, and teleporting to outposts; enforce max limit.
- Validation that spawns cannot be set outside world borders.
- Ensure warm‑up and cooldown behavior matches Teleportation spec.
- Test outpost teleportation after world reload.
