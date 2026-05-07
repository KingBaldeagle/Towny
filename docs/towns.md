# Towns

## Core Model (`Town`)
- Extends `Government` and stores:
  - **Mayor** – the town creator (`Resident`). Only the mayor can execute most administrative `/town` commands.
  - **Residents** – a `HashSet<Resident>` containing the mayor and all town members.
  - **TownBlocks** – a concurrent map of `WorldCoord → TownBlock` representing claimed 16×16 chunks.
  - **Home Block** – a special `WorldCoord` used as the spawn point for `/town spawn`.
  - **Jail Block** – optional location where jailed players are sent.
  - **Nation** – reference to a `Nation` if the town has joined one.

## Creating a Town
- Command: `/town new <name>`
- Preconditions:
  - Player must be standing on a *claimed* `TownBlock` (the claim can be created with `/t claim` first).
  - Player must have enough copper (`Config.PRICE_NEW_TOWN_IN_COPPER`).
- Process:
  1. Withdraw the creation cost from the player's LightmansCurrency account.
  2. Instantiate a new `Town` and set the player as mayor.
  3. Deposit the cost into the newly created town team account (handled by `TownyEconomyHandler`).

## Managing Residents
- **Add** – `/town add <player>` (mayor only). Calls `Town#addResident`.
- **Invite** – `/town invite <player>` (mayor only). Stores an invite in `TownInviteRegistry`.
- **Accept** – `/town accept` – joins the pending invitation.
- **Deny** – `/town deny` – declines a pending invitation.
- **Kick** – `/town kick <player>` (mayor only). Uses `Town#removeResident`.
- **Leave** – `/town leave` – resident leaves their current town (cannot be the mayor).

## Town Properties (`/town set`)
| Key | Type | Effect |
|---|---|---|
| `tag` | String | Town tag shown on HUD and chat.
| `board` | String | Message of the day for the town.
| `public` | Boolean | If true, anyone can view town info without being a member.
| `open` | Boolean | If true, players can join the town without an invitation (future feature).

## Home & Jail Blocks
- **Home Block** – set via a future command (`/town set home`). Used by `/town spawn`. The HUD displays the home block coordinates.
- **Jail Block** – set via a future command (`/town set jail`). Jailed residents are teleported here until their sentence expires. Jailing is performed by the `/towny jail` command (implemented in `TownyCommands`).

## Claiming & Unclaiming Land
- `/town claim` – claims the `TownBlock` under the player for the town. Only the mayor can claim on behalf of the town.
- `/town unclaim` – releases the block. It is removed from the town’s `townBlocks` map.

## Outposts
Town blocks can be marked as **outposts** (`TownBlock#setOutpost(boolean)`). This flag is stored in persistence but currently has no dedicated command; admins can toggle it via server‑side scripts or future extensions. See the dedicated *Outposts* document.

## HUD Integration
The HUD displays the name of the town (or "Wilderness") the player is standing in. See `hud.md` for details.

---
