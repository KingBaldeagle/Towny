# Permission System Specification (Towny)

## Overview
Towny’s permission system governs what actions a player can perform on the world, within towns, nations, and plots. Permissions are hierarchical and can be granted at the **global**, **nation**, **town**, **plot**, and **resident** levels, with overrides possible for individual residents on a per‑plot basis.

## Permission Hierarchy
1. **Global (Admin)** – Players with the `towny.admin` node bypass all checks.
2. **Nation Level** – Permissions that apply to all towns and residents of a nation (e.g., `towny.nation.invite`).
3. **Town Level** – Permissions granted to all members of a town (e.g., `towny.town.claim`).
4. **Plot Level** – Granular permissions for a specific plot (`TownBlock`). Flags such as `build`, `destroy`, `switch`, `item_use`, `pvp`, `mobs`, `fire`, `explosions` are stored in the plot’s `TownyPermission` object.
5. **Resident Overrides** – Individual residents can have custom overrides on a plot, stored as `PermissionData`. Overrides can **SET**, **NEGATE**, or be **UNSET** (default to inherited permission).

## Permission Nodes
Permission nodes are defined in `PermissionNodes.java` and follow the pattern `towny.<category>.<action>`.
Typical categories:
- `towny.town.*` – town management (create, delete, set, invite, claim).
- `towny.nation.*` – nation management (create, invite, tax).
- `towny.plot.*` – plot specific actions (build, destroy, switch, item_use).
- `towny.resident.*` – resident actions (jail, toggle mode).
- `towny.admin.*` – admin bypasses.

## Evaluation Flow (PlayerCacheUtil)
When an action occurs, Towny follows this order:
1. **Admin Bypass** – If the player has `towny.admin`, allow.
2. **Wild Override** – Global world‑wide overrides (e.g., server config) are checked.
3. **Resident Override** – If the resident has a specific `PermissionData` for the plot, use its setting.
4. **Plot Permission** – Use the plot’s `TownyPermission` flags for the action.
5. **Town Permission** – If the plot is town‑owned, fall back to town‑level permissions.
6. **Nation Permission** – If the town belongs to a nation, apply nation‑level permissions.
7. **Default Deny** – If none grant the action, it is denied.

## Permission Types (ActionType)
`TownyPermission.ActionType` enumerates the actions:
- `BUILD` – placing blocks.
- `DESTROY` – breaking blocks.
- `SWITCH` – using levers/buttons.
- `ITEM_USE` – using items (e.g., flint & steel).
- `PVP` – attacking other players.
- `MOBS` – harming mobs.
- `FIRE` – fire spread.
- `EXPLOSIONS` – explosions.

Each action maps to a boolean flag in `TownyPermission` and can be overridden per resident.

## GUI Editing (PermissionGUI)
Towny provides a GUI (`PermissionGUI`) for editing a resident’s permission overrides on a specific plot.

### How to Open the GUI
- **Command**: `/towny permission <player> <plot>` – opens the permission editor for the specified resident on the given plot (the plot can be identified by coordinates or by looking at the block and using the current location).
- **In‑Game Interaction**: Residents with the `towny.town.permission.edit` node can right‑click a plot while holding a **book** (or any item defined in the config) to open the GUI for the nearest resident (typically themselves).
- **Admin Shortcut**: Players with `towny.admin` can run `/towny permission admin <x> <y> <z>` to edit any resident’s permissions on that plot.

### What the GUI Looks Like
- **Header** – Shows the plot coordinates, town name, and resident name.
- **Permission Grid** – A 2‑column grid listing each `ActionType` (Build, Destroy, Switch, Item Use, PvP, Mobs, Fire, Explosions). Each cell displays a colored wool block:
  - **Green Wool** – Permission **SET** (allowed).
  - **Red Wool** – Permission **NEGATED** (denied).
  - **Gray Wool** – Permission **UNSET** (inherits from plot/town/nation).
- **Tooltips** – Hovering over a wool block shows a tooltip with the permission name and current state.
- **Controls** – Clicking a wool block cycles its state (Unset → Set → Negated → Unset). A **Save** button at the bottom writes changes to the plot’s `PermissionData`. A **Cancel** button discards changes.
- **Navigation** – If multiple residents have overrides on the same plot, a dropdown at the top allows switching between them.

Changes made in the GUI are persisted immediately after pressing **Save**, and the system refreshes any cached permission data for the affected resident.


## Configuration
Relevant config entries (`towny.yml`):
```yaml
permissions:
  default:
    town:
      build: true
      destroy: true
      switch: true
      item_use: true
    nation:
      build: true
      destroy: true
    plot:
      pvp: false
      fire: false
      explosions: false
``` 
These defaults are loaded when a new town/nation/plot is created and can be overridden per instance.

## Edge Cases
- **Overlapping Permissions** – Resident overrides take precedence over plot flags, which take precedence over town/nation defaults.
- **Admin Bypass** – Only users with the explicit admin node may bypass; other high‑rank nodes (e.g., `towny.town.mayor`) do not bypass.
- **Offline Residents** – Overrides are stored in the plot data and applied when the resident logs in; offline status does not affect permission checks.
- **WorldGuard Sync** – If WorldGuard is installed, Towny creates matching regions; permission checks are delegated to WorldGuard when compatible.

## Integration Points
- `TownBlock.getPermissions()` – returns the `TownyPermission` for a plot.
- `TownyPermissionSource` – central interface for testing permissions (`testPermission(Player, node)`).
- `PermissionGUIUtil` – opens the permission editing interface.
- Events: `TownBlockClaimEvent`, `TownBlockOwnerChangeEvent` (used to recalculate permissions on ownership changes).

## Tests Required
- Verify that admin bypass grants all actions.
- Test resident override precedence for each `ActionType`.
- Confirm plot flag toggles affect only that plot.
- Ensure nation and town defaults are inherited correctly when no overrides exist.
- Validate GUI changes persist after server reload.
