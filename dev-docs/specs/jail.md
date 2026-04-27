# Jail & Punishment System Specification (Towny)

## Overview
The jail system allows towns and nations to detain residents as a form of punishment. Jailed residents are moved to a designated jail location, lose certain permissions, and may incur fines or tax penalties.

## Core Concepts
1. **Jail Definition**
   - Each **Town** can define a jail location via `/town setjail` (may be a world coordinate or a specific block).
   - Nations can optionally inherit a town’s jail location or define their own via `/nation setjail`.
   - **Jail Removal on Nation Join** – When a town becomes part of a nation, the town’s jail location is automatically cleared unless the nation explicitly defines its own jail. This prevents duplicate or conflicting jail locations. The removal is handled by the `TownPreJoinNationEvent` listener, which calls `Town.removeJailLocation()` if the nation does not provide a jail.
2. **Resident State**
   - A resident has a *jail status* (`Resident.isJailed()`) and a *jail timer* indicating remaining jail time.
   - While jailed, the resident is considered a **prisoner** and cannot perform most Towny actions.
3. **Punishment Types**
   - **Time‑based** – a fixed number of in‑game minutes/days.
   - **Fine‑based** – a monetary penalty deducted from the resident’s wallet.
   - **Combination** – both time and fine can be applied simultaneously.

## Commands
| Command | Description | Permission | Notes |
|---|---|---|---|
| `/jail <player> <time>` | Jails a resident for the specified time (e.g., `10m`, `2h`). | `towny.jail` | Optional fine can be added with `-f <amount>`.
| `/unjail <player>` | Releases a resident from jail early. | `towny.unjail` | Removes timer and restores permissions.
| `/jail setlocation` | Sets the current block as the jail location for the town. | `towny.jail.set` | Must be run by mayor or authorized rank.
| `/jail info <player>` | Shows remaining time, fine amount, and location. | `towny.jail.info` | Read‑only.

## Permission Changes While Jailed
- **Disabled Actions**: `towny.town.claim`, `towny.town.sethome`, `towny.town.spawn`, `towny.nation.setspawn`, plot build/destroy, PvP, and any command requiring `towny.resident.*` permissions.
- **Enabled Actions**: `towny.jail.info`, `towny.unjail` (if permitted), chat, and basic movement within the jail area.
- Permissions are enforced via `ResidentToggleModeEvent` and the general permission checking pipeline.

## Jail Timer Mechanics
- Time strings are parsed using the same format as the scheduler (`10m`, `2h`, `1d`).
- The timer ticks down each server tick; when it reaches zero, the resident is automatically released (`ResidentUnjailEvent`).
- Warm‑up does not apply to jail release; the resident is teleported instantly to their previous location or a town spawn.

## Fines & Financial Penalties
- Fines are specified in **copper units** and deducted from the resident’s wallet at the moment of jailing.
- The system uses Lightmans Currency conversion rules (highest denomination first).
- If a resident lacks sufficient funds, the jail command fails and an error is returned.

## Integration with Tax System
- Jailed residents may be marked as **delinquent** for tax purposes, preventing them from paying resident taxes until released.
- Optional config (`jail.tax.exempt`) can exempt jailed residents from tax collection.

## Configuration (`towny.yml`)
```yaml
jail:
  default-time: 10m            # default jail time if not specified
  default-fine: 0              # default fine in copper units
  tax-exempt-while-jailed: true
  max-jail-time: 7d            # hard cap on jail duration
```

## Edge Cases
- **Offline Residents** – If a resident is offline when jailed, they are marked as jailed and will be teleported to the jail location upon next login.
- **World Unload** – If the jail world unloads, the resident remains jailed; teleport occurs when the world reloads.
- **Admin Override** – Players with `towny.admin` can jail/unjail without restrictions and bypass fines.
- **Multiple Jails** – A resident can only be jailed once; attempting to jail an already jailed resident returns an error.
- **Escape Attempts** – Movement outside the jail radius is blocked. If a player uses illegal teleport commands, the attempt is cancelled and a warning is sent.

## Events
- `ResidentJailEvent` – fired when a resident is placed in jail.
- `ResidentUnjailEvent` – fired on release.
- `ResidentToggleModeEvent` – used to switch resident mode to *jailed* and back.
- `JailDurationExtendEvent` – optional extension of existing jail time.

## Tests Required
- Jailing a resident with time and fine, verify wallet deduction and teleport.
- Automatic release after timer expires.
- Permission restrictions while jailed (attempt to claim a plot, set home, etc.).
- Admin jail bypass and forced release.
- Handling of offline resident jail and subsequent login.
- Configuration limits (max jail time, default values) enforced.
