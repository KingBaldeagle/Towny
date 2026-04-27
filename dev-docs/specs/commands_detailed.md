# Command Overview (Expanded) Specification (Towny)

## Purpose
Provide a comprehensive reference for every user‑facing Towny command, covering syntax, required permissions, side‑effects, and any configuration dependencies. This serves developers writing plugins, documentation writers, and maintainers.

## Command Categories
1. **Town Management** – creation, deletion, settings, resident handling.
2. **Nation Management** – creation, alliances, wars, taxes.
3. **Plot Management** – claim, unclaim, set type, permissions, sale.
4. **Economy Commands** – balance checks, transfers, bank interactions.
5. **Teleport Commands** – spawn, outpost, home, admin teleport.
6. **Jail & Punishment** – jail, unjail, info.
7. **Administrative** – reload, debug, force actions.
8. **Utility** – help, info, map, chat.

## Detailed Command List
| Command | Description | Permission Node | Side‑effects |
|---|---|---|---|
| `/town new <name>` | Create a new town at your current location. | `towny.town.create` | Claims the current plot, adds you as mayor, creates a town bank account.
| `/town delete` | Disband your town. | `towny.town.delete` (mayor) | Unclaims all town plots, transfers remaining funds to nation (if any) or discards them, fires `TownDeleteEvent`.
| `/town claim` | Claim the plot you stand on for your town. | `towny.town.claim` | Checks adjacency, plot limit, deducts claim cost via `TownyEconomyHandler`, fires `TownBlockClaimEvent`.
| `/town unclaim` | Release the plot you are standing on. | `towny.town.unclaim` | Removes plot ownership, may trigger plot loss penalties.
| `/town add <player>` | Invite a resident to your town. | `towny.town.invite` | Sends `TownInviteEvent`, creates a pending invitation record.
| `/town accept` | Accept a pending town invitation. | `towny.town.join` | Adds resident to town, updates resident data, fires `TownAddResidentEvent`.
| `/town setspawn` | Set the town spawn point to your location. | `towny.town.spawn.set` | Updates spawn coordinates, fires `TownSpawnChangeEvent`.
| `/town spawn` | Teleport to your town's spawn. | `towny.town.spawn` | Initiates warm‑up (see Teleportation spec), may be blocked by combat.
| `/town sethome` | Set a home block within a plot you own. | `towny.town.home.set` | Stores location in plot data.
| `/town home` | Teleport to your plot home. | `towny.town.home` | Warm‑up applies; respects plot ownership.
| `/nation new <name>` | Create a new nation and become its king. | `towny.nation.create` | Registers nation, assigns capital spawn (if set), creates nation treasury.
| `/nation invite <town>` | Invite a town to join your nation. | `towny.nation.invite` | Fires `NationInviteTownEvent`.
| `/nation accept` | Accept a nation invitation. | `towny.nation.join` | Adds town to nation, updates town’s nation reference, fires `NationAddTownEvent`.
| `/nation setspawn` | Set the nation spawn location. | `towny.nation.spawn.set` | Updates nation spawn, fires `NationSpawnChangeEvent`.
| `/nation spawn` | Teleport to the nation spawn. | `towny.nation.spawn` | Warm‑up applies; can be used by any nation member.
| `/nation ally <nation>` | Propose an alliance with another nation. | `towny.nation.alliance` | Fires `NationInviteAllyEvent`.
| `/nation ally accept` | Accept an alliance invitation. | `towny.nation.ally.accept` | Updates alliance sets, fires `NationAllyAcceptEvent`.
| `/nation war <nation>` | Declare war on another nation. | `towny.nation.war` | Fires `NationWarEvent`; enables PvP between members.
| `/nation peace <nation>` | End a war / cease‑fire. | `towny.nation.peace` | Fires `NationPeaceEvent`.
| `/plot set <flag> <true|false>` | Toggle a plot flag (e.g., `pvp`, `fire`). | `towny.plot.set` | Updates `TownBlock` permissions, triggers `TownBlockPermissionChangeEvent`.
| `/plot sell <price>` | Put the current plot up for sale at the given price (copper units). | `towny.plot.sell` | Marks plot as for‑sale; price stored as copper.
| `/plot buy` | Purchase the plot you are standing on (if for sale). | `towny.plot.buy` | Deducts price using Lightmans Currency conversion rules, transfers ownership, fires `TownBlockOwnerChangeEvent`.
| `/money` | Open personal wallet GUI (Lightmans Currency). | `towny.economy.access` | Shows balance, allows deposits/withdrawals.
| `/towny admin reload` | Reload all Towny configuration files. | `towny.admin.reload` | Calls `Towny.reload()`, may temporarily pause scheduled tasks.
| `/towny admin tp <player> <x> <y> <z>` | Admin teleport without warm‑up. | `towny.admin` | Instant teleport, bypasses all checks.
| `/jail <player> <time> [-f <amount>]` | Jail a resident for a duration with optional fine. | `towny.jail` | Moves resident to jail location, starts timer, deducts fine.
| `/unjail <player>` | Release a resident from jail. | `towny.unjail` | Cancels timer, teleports back to previous location.
| `/jail setlocation` | Set the current block as the town's jail location. | `towny.jail.set` | Stores location in town data.
| `/help towny` | Show help menu with paginated command list. | `towny.command.help` | Reads from `HelpMenu` class.
| `/towny map` | Display an ASCII map of the surrounding area. | `towny.map` | Generates map via `TownyAsciiMap`.
| `/towny version` | Show plugin version and Git commit. | `towny.command.version` | Reads from `plugin.yml`.

## Command Execution Flow
1. **Permission Check** – Before any command logic runs, Towny queries `TownyPermissionSource.testPermission(Player, node)`. Admins (`towny.admin`) bypass all checks.
2. **Argument Validation** – Commands parse arguments using the built‑in command framework; invalid syntax returns a usage message.
3. **State Verification** – Commands verify contextual state (e.g., player must be in a town to run `/town spawn`).
4. **Event Firing** – Most state‑changing commands fire a corresponding event (e.g., `TownAddResidentEvent`). Plugins can cancel these events to block the action.
5. **Economy Interaction** – Commands that involve money call `TownyEconomyHandler` which delegates to the active `EconomyProvider` (Lightmans Currency or Vault).
6. **Feedback** – Successful commands send a chat message summarizing the outcome; failures explain the reason (permission denied, insufficient funds, invalid location, etc.).

## Configuration Influence
- Many commands respect settings in `towny.yml` such as `command-aliases`, `enable-teleport-warmup`, and `max-plot-claim-per-town`.
- The `admin-bypass` flag in the config can allow certain privileged commands to ignore warm‑up timers.

## Edge Cases
- **Offline Targets** – Commands targeting offline players (e.g., `/jail <player>`) store the action and apply it when the player logs in.
- **Cross‑World Teleports** – Teleport commands automatically handle dimension changes; world borders are respected.
- **Concurrent Modifications** – Commands that modify the same town/nation (e.g., two admins adding residents simultaneously) rely on synchronized data structures within Towny to avoid race conditions.
- **Permission Overrides** – Plot‑specific permission overrides can affect whether a player is allowed to run certain plot commands.

## Testing Recommendations
- Unit tests for each command’s permission node, argument parsing, and side‑effects.
- Integration tests covering command sequences (e.g., create town → claim plot → set for sale → buy).
- Mock `EconomyProvider` to verify correct copper deductions.
- Test admin bypass behavior for commands that normally have warm‑up.
- Verify that events are fired and can be cancelled appropriately.
- Ensure help pagination works with a large number of commands.
