# NeoTowny

A NeoForge 1.21.1 port of Towny, adding Resident-Town-Nation hierarchy management to Minecraft servers.

## What is NeoTowny?

NeoTowny adds a complete towny system to your Minecraft server:

- **Resident → Town → Nation** hierarchy
- Grid-based land claiming (16x16 blocks per claim)
- Per-plot permissions (build, destroy, switch, item use)
- Economy integration with LightmansCurrency
- Tax collection and upkeep
- Nation alliances and wars (future)

## Features

- Create towns and claim land
- Form nations with multiple towns
- Set town spawn points
- Configure plot types (normal, shop, embassy, arena, etc.)
- Tax collection from residents
- Nation taxes from member towns
- Player permissions per plot

## Installation

### Prerequisites

- Minecraft 1.21.1
- NeoForge 21.1.228+
- **LightmansCurrency** 1.21-2.3.0+

### Server Setup

1. Install NeoForge server
2. Place `neo-towny-<version>.jar` in the `mods` folder
3. **Required**: Install LightmansCurrency mod
4. Start server to generate config

### LightmansCurrency Setup

NeoTowny uses LightmansCurrency's team system for bank accounts:

1. **Server bank account**: An admin must create a team named "server":
   ```
   /lcteam create server
   /lcteam deleteowner
   ```
   (This is for towns without a nation - taxes go here)

2. **Town bank accounts**: When creating a town, the mayor must first create a team with the town name:
   ```
   /lcteam create <townname>
   /town new <townname>
   ```

3. **Nation bank accounts**: When forming a nation, the king must first create a team:
   ```
   /lcteam create <nationname>
   /nation new <nationname>
   ```

## Economy Flow

```
Player Wallet → Town Team Account
                          ↓
              [Has Nation?] → Nation Team Account
                  ↓ No
            Server Team Account (admins only)
```

- Player pays tax → town's team account (mayor accesses)
- Town in nation → pays nation tax → nation's tracked balance
- Town without nation → server team (admins only)

## Commands

### Player Commands

```
/resident, /res       - Resident status and settings
/res plotlist        - List your owned plots
/town new <name>      - Create a town (must create team first)
/t town <name>       - Show town status
/t spawn             - Teleport to town spawn
/t claim             - Claim land
/t list              - List all towns
/nation new <name>   - Create a nation (must create team first)
/nation <name>       - Show nation status
/n spawn             - Teleport to nation capital
/plot claim          - Claim personal plot
/plot set <type>     - Set plot type (shop, embassy, etc.)
/plot fs <price>     - Set plot for sale
```

### Admin Commands

```
/towny reload         - Reload config
/towny prices       - Show economy prices
/towny map          - Show town map
```

## Permissions

| Permission | Description |
|-----------|-------------|
| towny.resident.create | Create a resident |
| towny.town.create | Create a town |
| towny.town.claim | Claim land |
| towny.nation.create | Create a nation |
| towny.admin | Admin access |

## Configuration

Config file: `config/towny/common.toml`

```toml
[economy]
useEconomy = true
priceNewTown = 100.0
priceNewNation = 1000.0
priceClaimTownBlock = 10.0

[town]
defaultTax = 0.0
townBlockSize = 16

[nation]
defaultTax = 0.0
```

## Building from Source

```bash
./gradlew build
```

Output: `build/libs/neo-towny-<version>.jar`

## Credits

- Original Towny: https://github.com/TownyAdvanced/Towny
- LightmansCurrency: https://github.com/Lightman314/LightmansCurrency

## License

All Rights Reserved