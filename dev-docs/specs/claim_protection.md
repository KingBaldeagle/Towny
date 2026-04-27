# Claim Protection Specification (Towny)

## Overview
Claim protection governs which blocks, entities, and actions are restricted within a claimed plot or town area. It ensures that only authorized residents can modify or interact with protected resources.

## Protection Scope
1. **Block Placement & Breakage**
   - Only residents with the appropriate permission (`towny.plot.build`) may place or break blocks inside a claimed plot.
   - The mayor and assistants have implicit permission; other residents must be granted via the town's permission system.
2. **Entity Interaction**
   - Spawning, killing, or interacting with entities (e.g., mobs, armor stands) is limited to players with `towny.plot.interact`.
3. **Explosions & Fire**
   - Plot flags `explosions` and `fire` control whether explosions or fire can affect the plot. When disabled, any explosion (Creeper, TNT, etc.) or fire spread is cancelled within the plot bounds.
4. **Redstone & Pistons**
   - Redstone signal propagation and piston movement are blocked across plot borders unless the `redstone` flag is enabled.
5. **WorldGuard Integration**
   - If WorldGuard is present, Towny regions are synchronized with corresponding WorldGuard regions to provide cross‑mod protection.

## Configuration Options (towny.yml)
```yaml
claim-protection:
  allow-build: true            # default permission for town members
  allow-interact: true         # entity interaction permission
  explosions-enabled: false    # toggle explosion protection
  fire-enabled: false          # toggle fire spread protection
  redstone-enabled: false      # toggle redstone across borders
```

## Runtime Checks
- When a player attempts an action, Towny intercepts the event and checks:
  1. Is the target location within a claimed plot?
  2. Does the player have the required permission based on rank?
  3. Are the relevant plot flags enabled?
- If any check fails, the action is cancelled and a feedback message is sent to the player.

## Edge Cases
- **Unclaimed Wilderness** – No protection; standard Minecraft rules apply.
- **Overlapping Claims** – The system prevents overlapping claims; if a conflict occurs during claim, the claim is rejected.
- **Admin Bypass** – Players with `towny.admin` bypass all protection checks.
- **Offline Residents** – Permissions are resolved from stored data; offline status does not affect protection.

## Integration Points
- `TownBlock` methods `isPvpEnabled()`, `isFireAllowed()`, etc.
- Events: `TownBlockClaimEvent`, `TownBlockUnclaimEvent`, `TownBlockOwnerChangeEvent`.
- Permission checks via `TownyPermissionSource`.

## Tests
- Attempt block break/place with and without permission.
- Verify explosions/fire are cancelled when flags are disabled.
- Ensure redstone does not cross plot borders when disabled.
- Confirm WorldGuard region sync when a plot is claimed.
