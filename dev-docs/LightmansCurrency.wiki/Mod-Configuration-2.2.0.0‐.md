# Mod Configuration

**IMPORTANT NOTE** This page lists how things are configured in LC Forge v2.2.0.0 and older. To view documentation for the latest versions of LC Forge please go [here](https://github.com/Lightman314/LightmansCurrency/wiki/Mod-Configuration).

As a Currency/Economy mod, Lightman's Currency has a lot of configuration options to help you balance your economy in any way you wish. Below is a more detailed list of all configuration options and what they change.

## Client Config
Found in `FOLDER/config/lightmanscurrency-client.toml`

### quality
`itemTraderRenderLimit` The maximum number of items (per trade) that an Item Trader is allowed to render in-world. Setting to 0 will disable, but setting to 1 should be enough to reduce lag in trader-heavy areas.

### time
`timeFormatting` The format of the Time Stamps displayed in this mod. Mostly used for Notifications. Follows [SimpleDateFormatting](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)

### wallet_slot
Various Inputs that let you customize the location of the wallet slot in your inventory menu. Useful if another mod places their own item slots or buttons where the wallet slot is placed.

`buttonX/Y` The X & Y offset of the "Open Wallet" button (pixels from the top-left corner of the wallet slot).

`slotX/Y` the X & Y position of the wallet slot in the inventory screen (pixels from the top-left corner of the menu).

`creativeSlotX/Y` the X & Y position of the wallet slot in the creative inventory (pixels from the top-left corner of the menu).

### wallet_hud
Various Inputs that let you customize the location of the Wallet HUD.

`enabled` Whether the Wallet HUD will show itself.

`displayType` The display method of the Wallet HUD.
* `ITEMS_WIDE` Draws the Wallet followed by its contained coins.
* `ITEMS_NARROW` Same as `ITEMS_WIDE` but the contained coins will be closer together and may/will overlap.
* `TEXT` Draws the value text of the wallets contents in the format defined in the `coin_value_display` section of the server config.

`displayCorner` The corner that the Wallet HUD will display at. Accepted values are `TOP_LEFT`,`TOP_RIGHT`,`BOTTOM_LEFT`, and `BOTTOM_RIGHT`

`displayOffsetX/Y` The X/Y offset of the Wallet HUD (pixels from the corner)

### inventory_buttons
Various inputs that let you customize the location of the notification & team manager buttons in your inventory menu. Useful if another mod places their own buttons where my buttons are placed.

`buttonX/Y` the X position of the left-most button (pixels from the top-left corner of the menu).

`buttonCreativeX/Y` the X & Y position of the left-most button in the creative inventory (pixels from the top-left corner of the menu).


### chest_buttons
Settings related to the "Move Coins into Wallet" button that can appear in the chest screen.

`enabled` Whether the "Move Coins into Wallet" button will appear in the top-right corner of a Chest Screen (if there are coins to collect)

`allowHiddenCollection` Whether the button will also collect coins that are flagged as `hidden` (by default these would be the Coin Pile and Coin Block items)

### notification
`notificationsInChat` Whether notifications are allows to push a notification in your in-game chat box. Notifications will still be visible in the notification screen accessible from your inventory.

### slot_machine
Settings related to the Slot Machine and its animation.

`animationDuration` The duration of the slot machine animation in Minecraft Ticks (20 ticks = 1s)

`animationRestDuration` The duration of the slot machine animations "rest" state (only used when the 5x or 10x buttons were pressed)

### sounds
`moneyMendingClick` Whether money mending will trigger a coin sound when repairing a tool.


## Common Config
Found in `FOLDER/config/lightmanscurrency-common.toml`

`debugLevel` The debug level of the mod. Useful if you don't want to see log messages from me in the server console. Note that even if disabled, logs will still appear in the debug.log file.

### crafting
Various config options to disable the recipes of certain blocks/items. There are quite a lot of sub-options, but I'm not going to go into details here, as they're fairly self-explanatory.

Note: Disabling the crafting recipe does **NOT** disable their use for players who already have them, and will not prevent them from being obtained via command, creative mode. or admin trader. If you wish to disable a recipe that isn't listed in the config, I highly recommend the use of [CraftTweaker](https://www.curseforge.com/minecraft/mc-mods/crafttweaker).

### villagers
Various config options to customize how my mod interacts with Vanilla and/or Modded Villagers.
Note: Due to how villagers work, any changes made here **CANNOT** apply to any existing villager trades.

`addCustomWanderTrades` Whether my mod will insert some custom trades into the Wandering Traders trade pool.

`addBanker/Cashier` Whether the Banker or Cashier villagers added by my mod will have any trades registered.

#### other_traders
`changeVanillaTrades` Whether villagers added by vanilla Minecraft will have their Emeralds replaced with coins.

`changeModdedTrades` Whether villagers added by other mods will have their Emeralds replaced with coins.

`changeWanderingTrades` Whether the Wandering Trader will have their Emeralds replaced with coins.

`defaultTraderCoin` The default coin that villagers will have their Emeralds replaced by.

`traderOverrides` A **list** of override entries that will replace specific villager types with a coin other than the default coin.

Entries should be formatted as "`villager_id`-`coin_id`".

For example, replacing the Butcher's emeralds with a Gold Coin would be
"minecraft:butcher-lightmanscurrency:coin_gold"

### loot_customization
Various config options allowing you to define the coins used for Entity and Chest loot.

`lootItemT?` The item defined as Tier ? loot.

### entity_loot
Various config options allowing you to define what entities will drop what value of coins (if any at all)

`enableEntityDrops` Whether entities will drop coins at all. Does not replace any loot table modifications done via a data pack.

`enableSpawnerEntityDrops` Whether entities spawned by a vanilla spawner will drop coins. Enable only if you wish players to be able to infinitely farm coins.

`allowFakePlayerTrigger` Whether modded machines that emulate player behavior can trigger coin drops. Enable only if you wish players to be able to infinitely farm coins from mobs.

`entityListT?` A list of entity ids that should spawn loot of the given Tier (and lower tiers as well).

Note: If an entity is on multiple lists, only the lowest tier of loot will be triggered.

`bossEntityListT?` A list of entity ids that should spawn boss-level loot of the given Tier (and lower tiers as well).

Note: Boss tier loot does not require a player kill to trigger, and spawns a much larger coin quantity than the non-boss equivalent.

### chest_loot
Various config options allowing you to define what chests loot tables will spawn what value of coins (if any at all)

`enableChestLoot` Whether coins will be inserted into any chests loot tables at all. Does not block loot tables added or edited by a data pack.

`chestListT?` List of loot tables (e.g. "minecraft:chests/ruined_portal") that will generate the given tier of loot in them.

Note: If a loot table is on multiple lists, only the lowest tier of loot will be triggered.

## Server Config
Found in `WORLD/serverconfig/lightmanscurrency-server.toml`
For Single-Player worlds, it's found in `FOLDER/saves/WORLD/serverconfig/lightmanscurrency-server.toml`

`notificationLimit` The maximum number of player notifications allowed before old entries are deleted. Required to keep notification sync packets from getting too large.

`ejectIllegalBreaks` Whether protected blocks (such as traders, trader interfaces, etc.) broken by non-players (such as Create Drills, or other modded blocks that don't call Forges Block Break Events) will have their contents safely ejected into a temporary storage that will only be accessible by the protected blocks owner/admins. If disabled, the protected blocks contents will be thrown on the ground as though the owner had broken it.

`canMintCoins` Whether coin mint recipes of mintType `MINT` will be craftable in the Coin Mint. Set to false to disable the minting of coins (unless added by a datapack using mintType `OTHER`).

`canMeltCoins` Whether coin mint recipes of mintType `MELT` will be craftable in the Coin Mint. Set to false to disable the melting of coins (unless added by a datapack using mintType `OTHER`).

`defaultMintDuration` The default time period it will take for the Coin Mint to mint/melt a coin. Ignored if the recipe has a defined "duration".

### coin_minting
Config options that allow you to disable the minting of a specific default coin.
Note: Only affects recipes with a mintType of `MINT`

### coin_melting
Config options that allow you to disable the melting of a specific default coin.
Note: Only affects recipes with a mintType of `MELT`

### wallet
Config options that allow you to determine which Wallet Tiers have which abilities. The Wallet Tiers are as follows:
0. Copper Wallet
1. Iron Wallet
2. Gold Wallet
3. Emerald Wallet
4. Diamond Wallet
5. Netherite Wallet

`convertLevel` The Wallet Tier that gains the ability to exchange coins via a button in the Wallet Menu.

`pickupLevel` The Wallet Tier that gains the ability to automatically collect coins (while equipped). This also determines which Wallet Tier can be enchanted with the Coin Magnet enchantment.

`bankLevel` The Wallet Tier that gains the ability to interact with their bank account.

### coin_value_display
Various Config options that allow you to customize how coin values are displayed and input.

`coinTooltipType` The display method used for displaying a registered coins value tooltip.
* `DEFAULT`: Tooltip displays this coins default exchange values. Example: Worth 1 copper coin/10 of these are worth 1 gold coin
* `VALUE`: Tooltip displays this coins value text. Example: Worth $10 (Format based on the `valueFormat` config input)
* `NONE`: No tooltip will be displayed.

`coinValueType` The display method used for display a coin value
* `DEFAULT`: Coin Count & Initial text. Example: 5g1i2c
* `VALUE`: Value format defined in the `valueFormat` config.

`coinValueInputType` The input method used for defining a Coin Value in various menus.
* `DEFAULT`: List of coins with arrows above and below to increase/decrease the coin value.
* `VALUE`: A text input where you simply type in the value number (e.g. type 10 to set a trades price as $10)

`baseValueCoin` The coin defined as 1 value unit for `VALUE` display purposes. Any coins worth less than this will have a decimal value.

`valueFormat` The format that any `VALUE` text will be displayed in.

Note: Must include `{value}` within the input or it will merely display the exact string given for all value text displays.

### upgrades
Config values to define the power of certain upgrades

`upgradeCapacity1` The number of items added to a machines item storage when given an Item Capacity Upgrade (Iron)

`upgradeCapacity2` The number of items added to a machines item storage when given an Item Capacity Upgrade (Gold)

`upgradeCapacity3` The number of items added to a machines item storage when given an Item Capacity Upgrade (Diamond)

### money_chest_upgrades
Config values to define the power of certain Money Chest upgrades

`magnetRange1` The radius (in meters) of the Money Chest Magnet Upgrade (Copper)

`magnetRange2` The radius (in meters) of the Money Chest Magnet Upgrade (Iron)

`magnetRange3` The radius (in meters) of the Money Chest Magnet Upgrade (Gold)

`magnetRange3` The radius (in meters) of the Money Chest Magnet Upgrade (Emerald)

### enchantments
Config values to define the power of certain enchantments

`moneyMendingCost` A Coin Value input to define the cost of repairing a single unit of durability with the Money Mending enchantment.

Coin Value inputs should be formatted as follows: "`count`-`coin_id`,`count`-`coin_id`"

**Examples:**
Cost of 1 gold coin and 5 iron coins would be: `"1-lightmanscurrency:coin_gold,5-lightmanscurrency:coin_iron"`

Cost of 1 copper coin would be: "1-lightmanscurrency:coin_copper"

`coinMagnetBaseRange` The radius (in meters) of the Level 1 Coin Magnet enchantment

`coinMagnetRangeLevel` The increase in radius (in meters) added by each additional level of the Coin Magnet enchantment.

### auction_house

`enabled` Whether the Auction House will be automatically generated and accessible.

Note: Disabling after players have used the Auction House will result in any items/money within the Auction Houses storage being trapped permanently.

`visibleOnTerminal` Whether the Auction House will appear in the Trading Terminal. If disabled, the only way to access the Auction House is by interacting with an Auction Stand.

`minDuration` The minimum number of days an auction can define as its duration.

Note: Even when set as 0 days, an auction can still be no less than 1 hour long.

`maxDuration` The maximum number of days an auction can define as its duration.

### player_trading

`maxPlayerDistance` The maximum distance allowed between players in order for a player trader to persist.

Note: Input of -1 will always allow a player trade regardless of position or dimension, while an input of 0 will allow a player trade regardless of position but will enforce that they be in the same dimension.

### discord
Config options related to the Lightman's Discord Integration "Trade Bot" addon. Does nothing if Lightman's Discord Integration is not installed.

`channel` The channel ID of the Trade Bot channel.

`prefix` The prefix used for Trade Bot commands.

`limitSearchToNetwork` Whether the search command should limit it search result to only Network Traders.

#### notifications
Config options to determine if the Trade Bot should announce notifications in its channel when certain events happen.

`networkTraderBuilt` Whether a notification will appear when a new Network Trader is created.

Note: This notification will have a 60 second delay to allow the owner to customize the traders name, assign it to a team, etc.

`auctionHouseCreated` Whether a notification will appear when an Auction is created by a player in the Auction House.

`auctionHousePersistentCreations` Whether a notification will appear when a Persistent Auction is created/renewed.

`auctionHouseCancelled` Whether a notification will appear when an Auction is cancelled.

`auctionHouseWon` Whether a notification will appear when an Auction is completed and had a valid bidder.

## Other

The [Master Coin List](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config) and [Persistent Traders](https://github.com/Lightman314/LightmansCurrency/wiki/Persistent-Traders) config files have their own page.