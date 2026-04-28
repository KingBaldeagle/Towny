# Config Files

**IMPORTANT NOTE** This page lists how things are configured in LC Forge v2.2.0.1 or newer. To view documentation for older versions of LC Forge, or for LC Fabric, please go [here](https://github.com/Lightman314/LightmansCurrency/wiki/Mod-Configuration-2.2.0.0%E2%80%90).

As a Currency/Economy mod, Lightman's Currency has a lot of configuration options to help you balance your economy in any way you wish. Below is a more detailed list of all configuration options and what they change.

Note, unlike forge configs, changes to these files will not be automatically loaded. To reload them you must run the `/lcconfig reload` command. Running this command will reload commands on the server if you are an admin, as well as reload all configs on your client.

## Client Config
Found in `FOLDER/config/lightmanscurrency-client.txt`

### quality
`itemTraderRenderLimit` The maximum number of items (per trade) that an Item Trader is allowed to render in-world. Setting to 0 will disable, but setting to 1 should be enough to reduce lag in trader-heavy areas.

### time
`timeFormatting` The format of the Time Stamps displayed in this mod. Mostly used for Notifications. Follows [SimpleDateFormatting](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html)

### wallet_slot
Various Inputs that let you customize the location of the wallet slot in your inventory menu. Useful if another mod places their own item slots or buttons where the wallet slot is placed.

`slot` the X & Y position of the wallet slot in the inventory screen (pixels from the top-left corner of the menu). Formatted as `X,Y`.

`creativeSlot` the X & Y position of the wallet slot in the creative inventory (pixels from the top-left corner of the menu). Formatted as `X,Y`.

`button` The X & Y offset of the "Open Wallet" button (pixels from the top-left corner of the wallet slot). Formatted as `X,Y`.

### wallet_hud
Various Inputs that let you customize the location of the Wallet HUD.

`enabled` Whether the Wallet HUD will show itself.

`displayCorner` The corner that the Wallet HUD will display at. Accepted values are `TOP_LEFT`,`TOP_RIGHT`,`BOTTOM_LEFT`, and `BOTTOM_RIGHT`

`displayOffsetX/Y` The X/Y offset of the Wallet HUD (pixels from the corner). Note, negative values will be necessary for any corner that isn't the TOP_LEFT.

`displayType` The display method of the Wallet HUD.
* `ITEMS_WIDE` Draws the Wallet followed by its contained coins.
* `ITEMS_NARROW` Same as `ITEMS_WIDE` but the contained coins will be closer together and may/will overlap.
* `TEXT` Draws the value text of the wallets contents as defined by the money type/coin chain.

### network_terminal
Various inputs that let you customize the size and filters on the [Network Terminal](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders#trading-terminal)

`columnLimit` The maximum number of button columns the terminal will display. Actually displayed amount may be less than this depending on your screen size and gui scale.

`rowLimit` The maximum number of button rows the terminal will display. Actually displayed amount may be less than this depending on your screen size and gui scale.

`searchFilter` A default search filter to be added to whatever search text you type into the search bar. Make it blank (`searchFilter=""`) to disable.

### inventory_buttons
Various inputs that let you customize the location of the notification & team manager buttons in your inventory menu. Useful if another mod places their own buttons where my buttons are placed.

`button` The X & Y position of the left-most button (pixels from the top-left corner of the menu). Formatted as `X,Y`

`buttonCreative` The X & Y position of the left-most button in the creative inventory (pixels from the top-left corner of the menu). Formatted as `X,Y`

### chest_buttons
Settings related to the "Move Coins into Wallet" button that can appear in the chest screen.

`enabled` Whether the "Move Coins into Wallet" button will appear in the top-right corner of a Chest Screen (if there are coins to collect)

`allowSideChainCollection` Whether the button will also collect coins from a side-chain (by default these would be the Coin Pile and Coin Block items)

### notification
`notificationsInChat` Whether notification sources are allows to push a notification in your in-game chat box. Notifications will still be visible in the notification screen accessible from your inventory regardless of this setting.

### slot_machine
Settings related to the Slot Machine and its animation.

`animationDuration` The duration of the slot machine animation in Minecraft Ticks (20 ticks = 1s)

`animationRestDuration` The duration of the slot machine animations "rest" state (only used when the 5x or 10x buttons were pressed)

### sounds

`moneyMendingClink` Whether money mending will trigger a coin sound when repairing a tool.

### debug
`debugScreenBG` Naturally intended for debug purposes, enabling this will make all screens in my mod draw a solid white background behind it. I mostly added this to make it easier to take screenshots for this wiki 😄 

## Common Config
Found in `FOLDER/config/lightmanscurrency-common.txt`

`debugLevel` The debug level of the mod. Useful if you don't want to see log messages from me in the server console. Note that even if disabled, logs will still appear in the debug.log file.

### crafting
Various config options to disable the recipes of certain blocks/items. There are quite a lot of sub-options, but I'm not going to go into details here, as they're fairly self-explanatory.

Note: Disabling the crafting recipe does **NOT** disable their use for players who already have them, and will not prevent them from being obtained via command, creative mode. or admin trader. If you wish to disable a recipe that isn't listed in the config, I highly recommend the use of [CraftTweaker](https://www.curseforge.com/minecraft/mc-mods/crafttweaker).

Note #2: As of v2.2.4.3, all coin mint recipe config options have been moved into this section as well.

### events

`advancementRewards` Whether the various event advancements will give you chocolate coins when you first obtain them.

`chocolate` Whether chocolate coins will be manually inserted into the master coin list as their own independent coin chain.

`chocolateDrops` Whether chocolate coins will partially replace normal coin drops during seasonal events (currently only valentines & christmas, but may change in the future)

`chocolateRate` The chance of a normal coin being replaced by a chocolate coin when dropped by an entity or spawned in a chest.

Note: Chocolate coin drops will replace coins of the corresponding tier as configured in the loot section, so chocolate copper coins will replace the T1 loot item, etc.

### villagers
Various config options to customize how my mod interacts with Vanilla and/or Modded Villagers.
Note: Due to how villagers work, any changes made here **CANNOT** apply to any existing villager trades.

`addCustomWanderingTrades` Whether my mod will insert some custom trades into the Wandering Traders trade pool.

`addBanker/addCashier` Whether the Banker or Cashier villagers added by my mod will have any trades registered. Unfortunately it's not possible to disable their ability to obtain that profession though.

#### modification
`changeVanillaTrades` Whether villagers added by vanilla Minecraft will have their Emeralds replaced with coins.

`changeModdedTrades` Whether villagers added by other mods will have their Emeralds replaced with coins.

`changeWanderingTrades` Whether the Wandering Trader will have their Emeralds replaced with coins.

`defaultReplacementCoin` The default coin that villagers will have their Emeralds replaced by.

`replacementCoinOverrides` A **list** of override entries that will replace specific villager types with a coin other than the default coin.

Entries should be formatted as "`villager_id`-`coin_id`".

For example, replacing the Butcher's emeralds with a Gold Coin would be
"minecraft:butcher-lightmanscurrency:coin_gold"

You can also add additional regional replacement coins for a given profession by appending a `r;region_id;coin_id` with each regional section seperated by an additional `-`
i.e. "minecraft:butcher-lightmanscurrency:coin_gold-r;minecraft:snow;lightmanscurrency:coin_chocolate_gold" will replace emeralds with gold coins, but if they're a snowy villager it'll instead replace emeralds with chocolate gold coins.

### loot
Various config options allowing you to define the coins used for Entity and Chest loot.

`lootItemT?` The item defined as Tier ? loot.

#### entities
Various config options allowing you to define what entities will drop what value of coins (if any at all)

`enabled` Whether entities will drop coins at all. Does not replace any loot table modifications done via a data pack.

`allowSpawnedDrops` Whether entities spawned by a vanilla spawner will drop coins. Enable only if you wish players to be able to infinitely farm coins.

`allowFakePlayerDrops` Whether modded machines that emulate player behavior can trigger coin drops. Enable only if you wish players to be able to infinitely farm coins from mobs.

`lists.T?` A list of entity ids that should spawn loot of the given Tier (and lower tiers as well).

Note: If an entity is on multiple lists, only the lowest tier of loot will be triggered.

`lists.BossT?` A list of entity ids that should spawn boss-level loot of the given Tier (and lower tiers as well).

Note: Boss tier loot does not require a player kill to trigger, and spawns a much larger coin quantity than the non-boss equivalent.

All entity list entries can accept the following inputs:
* Entity ID: Input of "minecraft:zombie" will make the zombie entity drop coins of that tier.
* EntityType Tag: Input of "#minecraft:skeletons" will make all entities with the "minecraft:skeletons" tag drop coins of that tier.
* ModId Filter: Input if "example:*" will make all entities from the "example" mod drop coins of that tier.

#### chests
Various config options allowing you to define what chests loot tables will spawn what value of coins (if any at all)

`enabled` Whether coins will be inserted into any chests loot tables at all. Does not block loot tables added or edited by a data pack.

`lists.T?` List of loot tables (e.g. "minecraft:chests/ruined_portal") that will generate the given tier of loot in them.

Note: If a loot table is on multiple lists, only the lowest tier of loot will be triggered.

### structures
Various config options that allow you to disable certain structures that my mod adds

`villageHouses` Whether custom banker & cashier houses will have a chance to spawn in vanilla villages

`ancientCity` Whether a small archaeology site will have a chance to spawn in ancient cities

`idasStructures` (1.20.1 only) Whether certain larger structures will have a chance to spawn in vanilla villages if [IDAS](https://www.curseforge.com/minecraft/mc-mods/idas) is also installed

## Server Config
Found in `FOLDER/config/lightmanscurrency-server.txt`
Will not be generated client-side when connecting to a dedicated server.

### notifications
`limit` The maximum number of player and/or machine notifications allowed before old entries are deleted. Required to keep notification sync packets from getting too large.

### machine_protection

`safeEjection` Whether protected blocks (such as traders, trader interfaces, etc.) broken by non-players (such as Create Drills, or other modded blocks that don't call Forges Block Break Events) will have their contents safely ejected into a temporary storage that will only be accessible by the protected blocks owner/admins. If disabled, the protected blocks contents will be thrown on the ground as though the owner had broken it.

`anarchyMode` Whether any protection is provided at all. Enable at your own risk as this makes it so that all machines that normally have protection no longer do, and can be broken by any player unless protected by some other mod (such as a land claiming mod).

`quarantinedDimensions` A list of dimension ids (i.e. "minecraft:overworld") that are banned from using any form of network trading and/or bank account interactions. Intended to allow the existence of "Creative Dimensions" where players can cheat items in without being able to cheat in an ATM and deposit a bunch of cheated money into their personal bank accounts. Does still allow in-person trading, so you can get creative with this if you'd like to create the concept of "the end is beyond the range of our network satelites" or something silly and/or cool like that.

### coin_mint

`defaultMintDuration` The default time period it will take for the Coin Mint to mint/melt a coin. Ignored if the recipe has a defined "duration".

`soundVolume` Changes the volume of the coin mints **clang** noise that is played after each successful mint. Feel free to lower this if you have mint recipes with a low duration (either via custom recipe or by lowering the default duration)

### wallet
Config options that allow you to determine which Wallet Tiers have which abilities. The Wallet Tiers are as follows:
0. Copper Wallet
1. Iron Wallet
2. Gold Wallet
3. Emerald Wallet
4. Diamond Wallet
5. Netherite Wallet
6. Nether Star & Ender Dragon Wallet
7. No Wallet (i.e. disable)

`exchangeLevel` The Wallet Tier that gains the ability to exchange coins via a button in the Wallet Menu.

`pickupLevel` The Wallet Tier that gains the ability to automatically collect coins (while equipped). This also determines which Wallet Tier can be enchanted with the Coin Magnet enchantment.

`bankLevel` The Wallet Tier that gains the ability to interact with their bank account.

`allowCapacityUpgrade` Whether you can upgrade your wallets capacity by right-clicking on it with an upgrade item (defaults to diamonds, but accepts any item with the "lightmanscurrency:wallet_upgrade_material" item tag)

`manualDropOverride` Changes the behavior of the wallet drops upon your death, forcing the game to manually spawn the item entity instead of passing the dropped items along to the LivingDropsEvent. Enable if you are having problems with a grave mod collecting wallet drops if you don't want it to (i.e. if you have keepWallet on, but want the money dropped from the `coinDropPercent` rule to spawn in the world where it can be lost/stolen instead of going in the gravestone where it's protected)

### upgrades.*
Config values to define the power of certain upgrades

Contains several sections for each upgrade type that I won't get into here as they should be fairly self-explanatory base on each options comment. Long story short though is that any upgrade with a number in it's tooltip can have said number configured (i.e. change how many bonus items an item capacity upgrade provides, etc.)

### enchantments
Config values to define the power of certain enchantments

`tickDelay` Defines how often the Money Mending & Coin Magnet enchantments trigger their effects. Defaults to 20 ticks or once per second.

`coinMagnetBaseRange` The coin collection range (in meters) of the Coin Magnet I enchantment.

`coinMagnetLeveledRange` The range (in meters) added by each extra level of the Coin Magnet enchantment.

`coinMagnetCalculationLevelCap` The maximum level of Coin Magnet enchantment that will have its range calculated.

`moneyMendingRepairCost` A [Money Value Input](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) to define the cost of repairing a single unit of durability with the Money Mending enchantment.

**1.21+ ONLY**
`moneyMendingInfinityCost` A [Money Value Input](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) to define the additional cost of repairing an item with the Infinity Enchantment

**1.20.1 ONLY**
`moneyMendingBonusForEnchantment` A list of bonus costs for each given enchantment. Formatted as "cost|enchantment_id|max_level" where `cost` is a [Money Value Input](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments), `enchantment_id` is a valid enchantment id (i.e. "minecraft:infinity", and `max_level` is an integer defining how many times this bonus cost can/will be applied. Functions the same as the [Enchantment Extra Cost](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Mending-Enchantment-Data#enchantmentextracost) portion of the 1.21 enchantment data.

`moneyMendingItemOverrides` A list of base cost overrides for each given item. Formatted as "cost|item_input1,item_input2,..." where `cost` is a [Money Value Input](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments), and everything else is a list of either item ids (i.e. "minecraft:wooden_sword") or item tags (i.e. "#example:expensive_repairs"). Functions the same as the [Item Overrides](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Mending-Enchantment-Data#itemoverrides) portion of the 1.21 enchantment data.

### auction_house

`enabled` Whether the Auction House will be automatically generated and accessible.

Note: Disabling after players have used the Auction House will result in any items/money within the Auction Houses storage being trapped permanently.

`visibleOnTerminal` Whether the Auction House will appear in the Trading Terminal. If disabled, the only way to access the Auction House is by interacting with an Auction Stand.

`minimumDuration` The minimum number of days an auction can define as its duration.

Note: Even when set as 0 days, an auction can still be no less than 1 hour long.

`maximumDuration` The maximum number of days an auction can define as its duration.

### bank_accounts
Various bank account options mostly to allow server-generated interest on money deposited in a bank account

`interest` a number between 0.0 and 1.0 to define the interest rate applied to all bank accounts. An interest rate of 1.0 will result in the money doubling on each interest payment.

`forceInterest` Whether players should always receive *something* from their interest even if their interest rounds to less than a single coin

`interestNotification` Whether players should receive an interest notification to their personal notifications whenever interest is given. Regardless of whether this is enabled, the interest will still always appear in their bank accounts private logs.

`interestUpperLimits` A list of [Money Value Inputs](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) that define the largest amount of money a player can receive for each given coin chain/money type. For example an input of `"coin;1-lightmanscurrency:coin_netherite"` will mean that no matter how much money that player has they can never receive more than 1n worth of interest for the default coins.

### terminal

`lcterminalCommand` Whether the `/lcterminal` command will be accessible to all players, allowing them to access the Trading Terminal menu regardless of whether they have crafted and/or have a terminal item.

`sortUnnamedTradersToBottom` Whether traders with no custom name will be sorted to the bottom of the Network Terminal list. Enable to discourage players leaving a bunch of "Item Network Terminal T4" traders on the trader list.

### paygate

`maxRedstoneDuration` The maximum number of ticks that the paygate is allowed to send a redstone signal for.

### command_trader

`maxPermissionLevel` The maximum permission level that a Command Trader is allowed to run commands at.

### player_trading

`maxPlayerDistance` The maximum distance allowed between players in order for a player trader to persist.

Note: Input of -1 will always allow a player trade regardless of position or dimension, while an input of 0 will allow a player trade regardless of position but will require that both players be in the same dimension.

### taxes

`adminOnlyActivation` Whether only players in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#moderation) can activate a Tax Collector. Enable to require administrator authorization in order to tax an area.

`maxTaxRate` The maximum tax rate that a Tax Collector is allowed to enforce.

`maxRadius` The maximum horizontal radius around the Tax Collector that it is allowed to tax.

`maxHeight` The maximum vertical area around the Tax Collector that it is allowed to tax.

`maxVertOffset` The maximum vertical offset from the Tax Collector that the area can be set in.

### chocolate_coins

`chocolateEffects` Whether the chocolate coins will give players potion effects when eaten by a player.

### compat
Various config options about my compatibility features with other mods.

#### claim_purchasing
Config options around my mods Claim & Forceload purchasing.

Compatible with FTB Chunks, Cadmus, & Flan.

`allowClaimPurchase` Whether players can buy claims via the `/lcclaims buy claim` command.

`claimPrice` The [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) price to be paid per claim chunk purchased.

`maxClaimCount` The maximum number of bonus claims players are allowed to buy.

`allowForceloadPurchase` Whether players can buy claims via the `/lcclaims buy forceload` command.

`forceloadPrice` The [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) price to be paid per forceload chunk purchased.

`maxForceloadCount` The maximum number of bonus forceload chunks players are allowed to buy.

#### ldi
Config options related to the Lightman's Discord Integration "Trade Bot" addon. Does nothing if Lightman's Discord Integration is not installed.

`channel` The channel ID of the Trade Bot channel.

`prefix` The prefix used for Trade Bot commands.

`limitSearchToNetwork` Whether the search command should limit its search result to only Network Traders (or normal trades with network upgrades)

#### ldi.notifications
Config options to determine if the Trade Bot should announce notifications in its channel when certain events happen.

`networkTraderBuilt` Whether a notification will appear when a new Network Trader is created.

Note: This notification will have a 60 second delay to allow the owner to customize the traders name, assign it to a team, etc.

`auctionCreated` Whether a notification will appear when an Auction is created by a player in the Auction House.

`auctionPersistentCreations` Whether a notification will appear when a Persistent Auction is created/renewed.

`auctionCancelled` Whether a notification will appear when an Auction is cancelled.

`auctionWon` Whether a notification will appear when an Auction is completed and had a valid bidder.

## Other

The [Master Coin List](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config) and [Persistent Traders](https://github.com/Lightman314/LightmansCurrency/wiki/Persistent-Traders) config files have their own page.