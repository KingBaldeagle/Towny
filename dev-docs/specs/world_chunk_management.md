# World & Chunk Management Specification (Towny)

## Overview
Towny manages land ownership and protection at the **chunk** level. Each claimed chunk (a 16×16 block area) is represented by a `TownBlock` object that stores the owning town, plot permissions, and related metadata. Proper handling of world loading/unloading, chunk events, and data persistence is essential for consistent behavior.

## Core Concepts
- **TownBlock** – Represents a single claimed chunk. Contains:
  - Owning town reference.
  - Permission flags (build, destroy, pvp, fire, explosions, etc.).
  - Optional custom data (e.g., outpost designation).
- **World Registry** – A global map (`WorldData`) that tracks all `TownBlock` instances per world (identified by its name/UUID).
- **Chunk Listener** – Listens to Bukkit/Spigot chunk load/unload events to attach or detach `TownBlock` data.
- **Persistence** – `TownBlock` data is serialized to `towns.yml` (or a dedicated `townblocks.yml`) and reloaded on server start or world load.

## Claim Lifecycle
1. **Claim Request** (`/town claim` command)
   - Verify the player is standing in a loaded chunk.
   - Ensure the chunk is adjacent to an existing claim of the same town (unless the town is open).
   - Check town plot limits and claim cost.
   - Fire a **cancelable** event `TownBlockClaimEvent`. Plugins can cancel or modify the claim.
   - On success, create a `TownBlock` instance, set the owner, and store it in the world registry.
   - Persist the new claim data immediately.
2. **Unclaim** (`/town unclaim`)
   - Similar validation flow, firing `TownBlockUnclaimEvent`.
   - Remove the `TownBlock` from the registry and update the town’s plot count.
   - Persist changes.

## Chunk Integration
- **Chunk Load** – When a chunk loads, the plugin checks the world registry for a matching `TownBlock`. If found, it attaches the `TownBlock` data to the chunk (e.g., via a metadata map or a custom wrapper).
- **Chunk Unload** – On unload, the `TownBlock` data is detached and any runtime state (e.g., cached permissions) is saved back to the registry.
- **World Load/Unload** – On world load, a `WorldData` instance is created and populated from the persisted file. On world unload, all pending `TownBlock` changes are flushed to disk.

## Permission Checks
Whenever a block‑place, block‑break, entity‑interaction, or explosion event occurs, Towny performs:
1. Resolve the chunk’s `TownBlock` (if any).
2. Determine the owning town and retrieve its permission set.
3. Apply plot‑specific flags (build, destroy, pvp, fire, explosions).
4. If the action is disallowed, cancel the original event and optionally send a feedback message.

## Configuration (`towny.yml`)
```yaml
world-management:
  claim-adjacent-required: true   # require new claims to be adjacent
  max-plots-per-town: 500          # global limit per town
  plot-size: 1                     # number of chunks per plot (default 1)
  allow-cross-dimension-claims: false
  chunk-save-interval: 200         # ticks between automatic chunk data saves
```

## Edge Cases
- **World Unload** – If a world unloads while a claim is pending, the claim is aborted and the player receives an error.
- **Chunk Not Loaded During Teleport** – Teleport commands force the target chunk to load before moving the player.
- **Dimension Switch** – Claims in other dimensions are validated against the correct `WorldData` entry.
- **Corrupt Data** – On load, malformed `TownBlock` entries are logged and ignored, leaving the chunk as wilderness.
- **Concurrent Claims** – Since Bukkit runs on a single thread for world modifications, two players cannot claim the same chunk simultaneously; the second attempt receives a “already claimed” message.

## Integration Points
- `TownBlock` class (`com.baldeagle.towny.object.townblock.TownBlock`).
- Event listeners: `TownyBlockListener` registers for `ChunkLoadEvent`, `ChunkUnloadEvent`, `WorldLoadEvent`, and `WorldUnloadEvent`.
- Permissions: `TownBlock.getPermissions()` returns a `TownyPermission` object used by the permission system.
- Persistence helpers: `TownyDataSource` handles reading/writing of claim data.

## Implementation Status (as of 2026-04-28)
- ✅ Added core world/claim value objects: `Coord`, `WorldCoord`, and `TownyWorld`.
- ✅ Added `TownBlock` + `TownBlockType` models and basic claim registration via `TownyUniverse#claimTownBlock`.
- 🚧 Event listeners, protection checks, and persisted claim storage are still pending.

## Events
- `TownBlockClaimEvent` – fired before a claim is finalized; cancelable.
- `TownBlockUnclaimEvent` – fired before a claim is removed; cancelable.
- `TownBlockOwnerChangeEvent` – after ownership changes.
- `TownBlockPermissionChangeEvent` – when plot flags are modified.

## Testing Guidelines
- Unit tests for claim adjacency validation and plot limit enforcement.
- Integration tests that load a world, claim several chunks, unload the world, reload, and verify claim persistence.
- Tests for permission enforcement on block break/place inside claimed chunks.
- Simulate world unload during a claim attempt and verify proper abort handling.
- Validate that cross‑dimension claim attempts respect the `allow-cross-dimension-claims` flag.
