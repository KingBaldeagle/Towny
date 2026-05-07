# Commands

All in‑game commands start with a forward slash (`/`). The mod registers the following command groups:

## Town Commands (`/town`)
| Sub‑command | Usage | Permission |
|---|---|---|
| `new <name>` | Create a new town while standing on a claimed block. | `towny.command.town.new` |
| `list` | List all towns alphabetically. | `towny.command.town.list` |
| `here` | Show which town the player is currently in. | `towny.command.town.here` |
| `add <name>` | Add a resident to your town (mayor only). | `towny.command.town.add` |
| `invite <name>` | Invite a resident (mayor only). | `towny.command.town.invite` |
| `accept` | Accept a pending town invitation. | `towny.command.town.accept` |
| `deny` | Decline a pending town invitation. | `towny.command.town.deny` |
| `delete` | Disband your town (mayor only). | `towny.command.town.delete` |
| `rank <name> <rank>` | Assign a custom rank to a resident (mayor only). | `towny.command.town.rank` |
| `set <key> <value>` | Set town properties such as `tag`, `board`, `public`, `open`. | `towny.command.town.set` |
| `spawn` | Teleport to the town’s home block. | `towny.command.town.spawn` |
| `nation join <nation>` | Join an existing nation (mayor only). | `towny.command.town.nation_join` |
| `nation leave` | Leave the nation your town belongs to (mayor only). | `towny.command.town.nation_leave` |
| `kick <name>` | Kick a resident from your town (mayor only). | `towny.command.town.kick` |
| `leave` | Leave your current town (must not be mayor). | `towny.command.town.leave` |
| `claim` | Claim the TownBlock you stand on for your town. | `towny.command.town.claim` |
| `unclaim` | Unclaim the TownBlock you stand on. | `towny.command.town.unclaim` |

## Resident Commands (`/resident` or `/res`)
| Sub‑command | Usage | Permission |
|---|---|---|
| *(no sub‑command)* | Show your own resident info. | `towny.command.resident.self` |
| `<name>` | Show info about another resident. | `towny.command.resident.show` |

## Nation Commands (`/nation`)
| Sub‑command | Usage | Permission |
|---|---|---|
| `new <name>` | Create a nation (must be mayor of a town without a nation). | `towny.command.nation.new` |
| `list` | List all nations. | `towny.command.nation.list` |
| `add <town>` | Add a town to your nation (king only). | `towny.command.nation.add` |
| `kick <town>` | Remove a town from your nation (king only). | `towny.command.nation.kick` |
| `leave` | Leave the nation your town belongs to (mayor only). | `towny.command.nation.leave` |

## Plot Commands (`/plot`)
| Sub‑command | Usage | Permission |
|---|---|---|
| `claim` | Claim a personal plot on the current TownBlock. If a plot exists and is for sale, the command attempts to purchase it. | `towny.command.plot.claim` |
| `unclaim` | Unclaim your personal plot (must be the owner). | `towny.command.plot.unclaim` |

## Chat Commands
| Command | Description |
|---|---|
| `/tc <msg>` | Send a message to the **town** chat channel. |
| `/nc <msg>` | Send a message to the **nation** chat channel. |
| `/lc <msg>` | Send a message to the **local** chat channel (nearby players). |

## Utility Commands (`/towny`)
| Sub‑command | Description |
|---|---|
| *(no sub‑command)* | Prints a short status confirming the Phase‑3 command set is active. |
| `economy` | Shows economy provider, tax intervals, delinquent resident count, recent transaction count, and a sample currency format. |
| `war status` | Displays the number of active wars. |
| `war declare <nation>` | Declare war on another nation (must belong to a nation). |
| `papi <placeholder>` | Resolve a placeholder value for the executing player (used by other mods). |
| `reload` *(admin)* | Reload the configuration file. |
| `prices` *(admin)* | Show economy‑related prices (town/nation creation cost, claim cost, etc.). |
| `map` *(admin)* | Display a simple town map (implementation placeholder). |

---

For a complete list of permission nodes, see the **Permissions** section in the main README.
