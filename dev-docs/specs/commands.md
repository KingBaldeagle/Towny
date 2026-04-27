# Command Specification Overview

This document enumerates all user‑facing commands for Neo‑Towny, their syntax, permissions, and side‑effects.

| Command | Description | Permission | Notes |
|---|---|---|---|
| `/town new <name>` | Create a new town. | `towny.town.create` | Requires a claim at location.
| `/town invite <player>` | Invite a player to the town. | `towny.town.invite` | Player must be online.
| `/town accept` | Accept a pending town invitation. | `towny.town.join` | 
| `/town set <key> <value>` | Modify town settings (e.g., `home`, `spawn`). | `towny.town.manage` | Valid keys listed in town.yml.
| `/town claim` | Claim the current plot for the town. | `towny.town.claim` | Adjacent rule enforced.
| `/town delete` | Disband the town. | `towny.town.delete` | Only mayor or admin.
| `/nation new <name>` | Create a new nation. | `towny.nation.create` | 
| `/nation invite <town>` | Invite a town to the nation. | `towny.nation.invite` | 
| `/nation accept` | Accept nation invitation. | `towny.nation.join` |
| `/nation tax <percent>` | Set nation tax rate. | `towny.nation.manage` |
| `/plot set <flag> <true|false>` | Toggle plot flags (pvp, fire, mobs, explosions). | `towny.plot.manage` |
| `/money` | Open personal wallet GUI. | `towny.economy.access` |
| `/towny reload` | Reload configuration files. | `towny.admin.reload` | Admin only.

*Each command fires corresponding events (e.g., `TownAddResidentEvent`). Implementations must validate arguments and provide informative error messages.*
