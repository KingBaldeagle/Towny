# Teleportation & Warm‑up Specification (Towny)

## Overview
Towny provides several teleport commands for towns, nations, and plots. Each command can have an optional **warm‑up timer**, **cool‑down**, and **admin bypass**. Warm‑up prevents instant escapes during combat and can be cancelled by movement or taking damage.

## Teleport Commands
| Command | Description | Required Permission | Notes |
|---|---|---|---|
| `/town spawn` | Teleport to the town’s spawn point. | `towny.town.spawn` | Uses town’s stored spawn location.
| `/nation spawn` | Teleport to the nation’s capital spawn. | `towny.nation.spawn` | Only available to nation members.
| `/plot home` | Teleport to a plot’s home block (if set). | `towny.plot.home` | Plot must have a home set via `/plot sethome`.
| `/towny admin tp <player> <x> <y> <z>` | Admin direct teleport (no warm‑up). | `towny.admin` | Bypasses all checks.

## Warm‑up Mechanics
1. **Configuration** (`towny.yml`):
```yaml
tp:
  warmup-seconds: 5          # default warm‑up time for all tp commands
  cancel-on-move: true       # cancel if the player moves during warm‑up
  cancel-on-damage: true     # cancel if the player takes damage during warm‑up
  cooldown-seconds: 30       # optional cooldown after successful teleport
```
2. **Process**
   - When a player runs a teleport command, Towny checks if the player has the `towny.admin` node; if so, the teleport occurs instantly.
   - Otherwise, Towny schedules a **warm‑up task** (`TeleportWarmupTimerTask`). The player receives a chat message indicating the remaining seconds.
   - If `cancel-on-move` is true and the player moves, the task is cancelled and a cancellation message is sent.
   - If `cancel-on-damage` is true and the player receives damage, the task is cancelled.
   - Upon successful completion, the player is teleported and a cooldown timer starts (if configured).
3. **Bypass Flags**
   - Players with `towny.town.spawn.nocd` or similar nodes can skip the cooldown.
   - `towny.admin` bypasses both warm‑up and cooldown.

## Cool‑down Handling
- Cool‑down timestamps are stored per player in memory (or persisted if `use-persistence` is enabled).
- Attempting another teleport while on cooldown returns a message with the remaining time.
- Admins can reset cooldowns via `/towny admin resetcooldown <player>`.

## Edge Cases
- **Combat Logging** – If a player is in combat (`CombatUtil.isInCombat(player)`), teleportation is blocked unless they have `towny.town.spawn.combatbypass`.
- **World Boundaries** – Teleport destinations must be within the world border; otherwise the teleport is cancelled and an error is logged.
- **Offline Players** – Admin teleport commands can target offline players; the location is applied when they next log in.
- **Multi‑world** – Teleport commands respect the world of the target location; warm‑up timers are world‑agnostic.
- **Permission Overrides** – Plot‑specific `towny.plot.teleport` can allow/disallow teleportation to a plot’s home.

## Integration Points
- `TeleportWarmupTimerTask` – schedules and monitors warm‑up.
- `TownyPermissionSource` – checks permissions for each command.
- Event Hooks:
  - `TownyTeleportEvent` – fired before teleport; can be cancelled by other plugins.
  - `TownyTeleportSuccessEvent` – fired after successful teleport.
- Configuration values accessed via `TownySettings.getTpWarmupSeconds()` etc.

## Tests
- Verify warm‑up timer counts down and teleports after the correct delay.
- Confirm movement or damage cancels the warm‑up when configured.
- Ensure cooldown prevents immediate re‑teleport.
- Test admin bypass (instant teleport, no cooldown).
- Validate combat restrictions block teleport unless bypass permission is present.
- Check that teleport to invalid coordinates fails gracefully.
