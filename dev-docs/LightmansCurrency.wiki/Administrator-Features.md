This page lists various notable Admin Features that don't necessarily fit 

### Configuring Coin Minting & Melting
The `canMint` & `canMelt` options in the [Server Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#server-config) allow you disable the minting or melting of coins in their entirety. Should you wish to be more specific about which coins can be minted or melted you can edit the `coin_mint.recipes.mint` & `coin_mint.recipes.melt` sub-options to disable the minting or melting of specific coins.

### Configuring Relative Coin Values & Adding Custom Coins
Relative coin values can be defined by editing the `MasterCoinList.json` config file. You can also add custom coin types or add new items to existing coin chains. More details on how to do so can be found [https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config](here).

### Configuring Wallet Abilities
The `wallet` sub-options in the [Server Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#server-config) can be used to define at which level wallets gain each ability. Note that if a wallet has the coin pickup ability it will be automatically given the exchange ability regardless of the defined config value.

### Configuring Loot
The `loot` section of the [Common Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#common-config) allows you to edit which coin items are spawned by entities/chest, and the relevant sub-sections (`loot.entities` and `loot.chests`) allow you to disable loot for that type altogether and/or define the list of entities/loot tables that will drop certain levels of loot.

### Configuring Villagers
You can disable various LC Villager professions & tweaks from the `villagers` section of the [Common Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#common-config). You can also set up vanilla villagers to trade in coins instead of emeralds in their trades in the `villagers.modification` section of the same config.

Note: Due to the way villagers work, changing these config options will not apply to any trade offers that a villager has already generated, it will only apply to newly generated trade offers.

## Admin Mode

**Admin Mode** is an admin-only mode that can be toggled by running the `/lcadmin toggleadmin` command.

While a player is in Admin Mode, they are treated as the owner of any machine they interact with, allowing them to access the storage & settings of any and all machines and Teams added by my mod. They can also break protected blocks as though they were the owner as well, allowing them to remove traders placed in troublesome areas or traders owned by players who have since been banned and/or left the server.

## Creative Traders
While in Admin Mode, the storage screen will show a creative button in the top-right corner of the Trader Name Tab (as shown [here](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#trader-name-tab)). Clicking this button will toggle creative mode for this trader. While a trader is in creative mode, it will not require the trader to have any money or items in its internal storage in order to properly execute its portion of the trades, and will thus have infinite stock for all trades. You can also click the +/- buttons next to the creative button to increase or decrease the number of trades that trader has (limited to 1 -> 100 trades), however keep in mind that for any in-world traders it will still only render the first **X** trades it is normally capable of rendering in the world.

## Persistent Traders
Persistent Traders is a modpack tool intended to allow modpack creators to make custom Creative Traders that are always in every world loaded by their modpack. The details on how this system works can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/Persistent-Traders).