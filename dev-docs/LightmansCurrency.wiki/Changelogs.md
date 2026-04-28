# 2.3.0.4
**Released for 1.20.1 & 1.21.1 on 1/1/2026**

**Additions**
- Added new "Transaction Register" item that allows you to easily track transactions and peform monetary math
- Added new "Trader Guide" book that will appear if Patchouli is installed
  - Still WIP and currently only has entries for Wallets and Trade Rules

**Changes**
- Increased the upper limit count for Trade Limit and Player Trade Limit rules from 100 -> 1,000,000
- Money Chest now has a custom christmas texture from Dec 24 -> Dec 26
- Tweaked money handler priorities for the trader so that money is taken **and** given to ATM cards in the money slots first before attempting to insert money into your wallet
  - If no wallet is equipped, coins should now be placed in the money slots first instead of being placed directly into your inventory

**Compatibility**
- Added new "lc_money" global to CC computers, allowing for Money Value math to be performed in a more proper manner.
- Money Value table results now include a "type" entry that includes their unique name (i.e. "lightmanscurrency:coin!main")
- Fixed various issues with existing peripherals

**Bug Fixes**
- Fixed an issue where the Command Trader didn't save the description/tooltip to json when turned into a persistent trader.
- Fixed an issue where the Money Chest Bank Upgrade didn't properly display info on a dedicated server.

**API Changes**
- Deprecated `TradeRule#createTab` and replaced it with a new `RegisterTradeRuleTabs` event to try and move client-only code out of common classes
  - Deprecated method still used as a fallback, but it's no longer abstract and will throw an exception (will be safly caught by the tab and logged) if not overridden explaining that no tab builder was registered
- Added MoneyAPI#GetPlayersMoneyHandlerUnsafe and CurrencyType#createUnsafeMoneyHandlerForPlayer
  - Used by traders to get less strict versions of money handlers for the player so that alternative handlers can be used instead.
  - If not overridden returns the normal/safe money handler.

**1.20.1 Patch A**
- Fixed an issue where the ATM Data didn't properly parse simple item icons

**Patch B**
- **1.20.1** Fixed an issue where a dedicated server would crash in a development environment
- Fixed desync issues that would happen when the client succeeds at inserting items into a traders storage, but the server denies it due to a difference in NBT data that the client doesn't know about.
- You can now quick-insert upgrades in the slot machine storage

**Patch C**
- **1.20.1** Fixed an issue where the Coin Chest Bank Upgrade did not get the selected bank account from the item stack correctly.
- Fixed an issue where the vanilla `/give` command would give you coins twice when the `mixins.giveCommandIntercept` config option was enabled.

**Patch D**
- Added an extra null-check when attempting to get variant data to prevent crashes if a null item or block is somehow registered.
- Updated Simplified Chinese translation thanks to Carrywanna

**Patch E**
- Slightly optimized trader log packets to only send the most recent notification when a traders logs are updated
- **1.20.1** No longer sends `SPacketSyncVariantChunkCap` or `SPacketSyncVariantBECap` packets if the capability doesn't have any data that needs to be synced when a player first starts tracking said chunk
- Fixed a critical issue with the Item Trader Interface's hopper export

# 2.3.0.3
**Released for 1.20.1 & 1.21.1 on 12/10/2025**

**Additions**
- You can now copy a traders settings data directly to your computers clipboard from the "Settings Clipboard" tab if no item is in the slot
  - Likewise you can now load a traders settings data directly *from* your computers clipboard if no item is in the slot
  - You can paste the results of these interactions into a text document of your choosing for proper storage and/or sharing

**Changes**
- When the Server Tax Collector is set up to only target "Network Objects", it will no longer tax trades for traders that were accessed directly instead of through the terminal (even if they are *also* accessible through the terminal)
- Can now flag Model Variants as "optional" to prevent errors from being logged when the variant is baked
  - Now applied to Card Display variants for modded wood types to prevent log spam

**Bug Fixes**
- Fixed an issue where Salary Targets weren't properly flagged as client-sided when loaded from file or created from a sync packet
- Fixed a critical issue where any Team with salaries made before v2.3.0.0 would throw an exception when loading from file and cause all team data to be wiped in the process

**API Changes**
- BankReference#flagAsClient(IClientTracker) now stores the parent client tracker so that if is flagged as client-sided later, the bank reference will change to match it
  - Primarily done so that salary data can properly load their targets from file even though the parent bank account has not yet be flagged as client-side
- Added `ITaxableContext` interface and default implementation (with static constructors in `ITaxableContext`)
- Added `ITaxable#getPossibleContexts` method that will return a Set of possible `ITaxableContext` entries for this taxable based on the results of `ITaxable#isNetworkAccessible`
- Deprecated `TaxAPI#GetTaxCollectorsFor(ITaxable)` and replaced it with `TaxAPI#GetTaxCollectorsFor(ITaxableContext)`
  - Added `ITaxCollector#ShouldTax(ITaxableContext)`

**Patch A**
- Optimized the Salary Target tab slightly so that fewer calculations need to be performed each frame
  - Bank Accounts targeted by a Custom Target will no longer appear in the direct selection list.

# 2.3.0.2
**Released for 1.20.1 & 1.21.1 on 12/05/2025**

**Additions**
- Added Server Tax Collector toggle option "Only Target Network Objects"
  - When enabled, the server-wide tax collector will only apply to network traders or traders with a network upgrade

**Changes**
- Optimized various salary price calculations to try and reduce FPS drops when on the Salary Info tab
- Improved how variant data is stored for blocks that don't need a block entity.
  - May result in data loss from some simple blocks as their placeholder block entity will be removed

**Bug Fixes**
- Fixed an issue where Salaries were not alerted when a player joins the server
- **1.21** Fixed an issue with some text input reworks that caused crashes in several places (missed some spots in 1.21-2.3.0.1c apparently)

**Compat Changes**
- Filters from FTB Filter System now function as filters in item traders

**API Changes**
- WalletDropEvent now has a "destroyWallet" field that determines if the wallet is flagged to be completely destroyed.
  - Is false in most cases, but can be true if the wallet curios slot is configured to be destroyed on death.
  - If true, the built-in event handler will not spawn any drops and it will reset the wallet to an empty item stack
- Added additional boolean argument to `SalaryData#getTotalCost,#canAffordNextSalary, and #checkForOnlinePlayers methods
  - Old methods still exist for a while, but will be removed in the next breaking update
  - New argument determines whether the total price calculation should actually be performed for 100% accuracy, or if it should depend on the cached results of this calculation
- Added additional `ITaxable#isNetworkAccessible` method so that the server tax collector can determine whether it should be targeted when "Only Target Network Objects" is enabled
- Replaced `common.blockentity.variant.IVariantSupportingBlockEntity` with new `api.variants.block.block_entity.IVariantDataStorage` interface
  - Added CapabilityVariantData which can now be used to obtain the `IVariantDataStorage` interface for optional addons
  - Deprecated `IVariantSupportingBlockEntity` and flagged it for removal, but made it extend `IVariantDataStorage` to avoid breaking addons that already used it
  - Added `api.variants.block.loot.VariantDataModifier` loot modifier that addons can use to attach variant data to the results of existing loot tables
- Removed `api.variants.block.IEasyVariantBlock` and the `GenericVariantBlockEntity` in favor of the new CapabilityVariantData registration methods used to store variant data outside of a block entity

**Patch A**
- Fixed FTB Filter System compat not actually initializing

# 2.3.0.1
**Released for 1.20.1 & 1.21.1 on 11/18/2025**

**Additions**
- You can now search item tooltips from within the search text. Preface these with `tooltip:INPUT` to **only** search for tooltips, or `item:INPUT` to **only** search for item names/ids
- Added `quality.itemRenderBlacklist` client config option that lets you blacklist certain items from being rendered in-world by item traders/auction stands
  - Blacklisted items will instead render a Trading Core as a placeholder model
- Variant Wand now opens "Item Variant" menu when used in the air or on a non-variant block
- Certain items now support item variants
  - Item variants can be created in the same way as block variants, but do require an "itemVariant:true" field in the variant json file to ensure it targets items
  - Most item variants do not require a "models" argument (using the "item" field for the item model, or textures from the "textures" field), but some special ones may
    - By default wallets require an entry in the "models" list to be used for their hip model, but a texture replacement will work as well

**Changes**
- Salary Targets tab now displays already selected accounts at the top of the list
- Salary Delay can now include a Minute input, allowing you to set up salaries that pay more often
- Bank Accounts now receive a custom notification when salaries are paid including how much was paid in total, how much was paid to each account, and which accounts actually received payment
- You can now Delete existing salaries from the Salary Settings tab
- Model Variants now inherit parent variants targets and targetSelectors and merge them with their own lists

**Bug Fixes**
- Fixed an issue where Salaries could potentially target a bank account that no longer exists, resulting in money no-longer existing.
- Fixed an issue where the server tax collector would not send sync packets until after the server reboots after it's first creation.

**Compat Changes**
- Added ATM Peripheral for CC compat
- Terminal Peripheral now has it's own independent type, and can listen to various trade events for all network traders in existence
- Added `TraderPeripheral#getTrades()` method to traders who implement the `getTrade(int)` method
- LC Peripherals now detect and transform UUID IntArrayTag's into UUID Strings when turning them into LuaTable's
  - Will attempt to transform UUID strings back into IntArrayTag data if it successfully parses as a UUID when turning LuaTable's into CompoundTag's
- Fixed various issues with my system that turned LuaTable's into CompoundTag data and vice-versa

**API Changes**
- Added `api.variants` package with various publicly accessible classes for use by the Model Variant system
  - Moved `IVariantBlock` to `api.variants.block` package. Left deprecated interface in old location to avoid breaking addon mods.
  - Added `IVariantItem` interface to support "item" variants. Item variants will mostly ignore model array input, but some complex items (like wallets) may still require "block" model data
  - Added `VariantProvider` class to allow registering custom `IVariantBlock` and `IVariantItem` sources (`Function<Block,IVariantBlock>` or `Function<Item,IVariantItem>`) so that other mods may support model variants as an "optional" dependency (i.e. no longer hard-coded to require the Block class itself to implement IVariantBlock)
    - Variant Providers should be registered in your mods init sequence, as the client setup event happens after assets are loaded
    - Added `VariantBlockWrapper` and `VariantItemWrapper` helper classes to make it easier to wrap a block/item with their relevant `IVariantBlock`/`IVariantItem` interface.
	  - `VariantBlockWrapper` is abstract as Block Variants require special data depending on the block state, etc.
	  - `VariantItemWrapper` is *not* abstract and can be used as-is to wrap normal 1-model items, but *can* be extended to implement additional model requirements
- Various numerical parsers used in TextInputUtil text box builders now support "null" parsing where a string is accepted but not pushed to the value handler
  - Added TextInputUtil.resourceBuilder()
  - TextBoxWrapper now has an additional builder argument to allow blocking handler messages when the value is changed with #setValue or new #setString methods

**Patch A**
- Fixed an issue where **all** model variants were added to creative tabs and displayed in JEI
- Fixed an issue where variant item models didn't render the correct model

**Patch B**
- Fixed an issue where variant target selectors still looked for matches even if none were present
- Optimized the target selector lookup so that results are cached if duplicate lookups are loaded in the same reload

**Patch C**
- Fixed an issue with some text input reworks that caused crashes in several places

**Patch D**
- Removed Create Wrench from the "variant wand" tag since some people apparently find it annoying that is can open the variant menus.

# 2.3.0.0
**Released for 1.20.1 & 1.21.1 on 11/2/2025**

**Additions**
- Added Salary tab to the ATM
  - Functions similar to the Team Salary system from previous verions, except it targets specific bank accounts
  - Bank Accounts can have up to 100 different salaries, each with its own independent targets, timers, and payments
  - Old Team Salaries will be copied over to their bank accounts salary system
    - Team Bank Accounts will have custom target options allowing them to target the teams members or admins
	- The Teams Bank Account Settings tab now has an additional option to determine who on the team is allowed to edit the teams salaries
- Added wallet abilities to Create's item attribute system
- Create List Filters and Attribute Filters now function as filter items for item traders (in the same way as the "Custom Item Trade Filter")
- You can now configure the client, common, and server config files from my mod through the Mods -> Config buttons provided by forge/neoforge
- Added 2 new "Corner" Model Variants for the Card Displays
  - Should make rounding corners look much nicer when utilized correctly

**Changes**
- Slot Machine Rework
  - Removed Entry "Weights". You must now define each entries "odds" directly
    - Has both a text box input for precise inputs, as well as a slider that can select exact numbers
  - Total odds of all entries cannot exceed 100%
  - If total odds is less than 100%, that missing portion is considered the odds of failure (upon which the customer will not win anything)
    - Failure chance is displayed at the top of the edit screen, as well as at the top of the customers details tab
  - Each entry can now define a custom set of icons to display when winning that prize
    - If no custom icons are defined, the randomly generated icons are still used
    - Randomly generated icons no longer display item counts
    - Icons for the "failure" result will always be a full set of X icons
- You can now drag-and-drop ghost items from JEI and REI into various locations
  - Item Trade sell/barter item slots (basic trade edit tab and item trade edit tabs)
  - Slot Machine entry item slots
  - Trader Display Settings custom icon slot
- Trader Category tab in the notification screen now matches your traders custom icon if it has one
- Coin Magnet enchantment now properly updates the wallet items that it can be applied to match wallets with the "Pickup" ability as defined in the server config
  - Known Issue: JEI will not be updated with the new list of wallets that can have the Coin Magnet enchant if the config is changed while you are already connected to the server, however all functional applications will still work as intended
- Item Trade Filters now collect the purchase display items (and random sell items if out of stock but trader is creative) from the creative tabs
- All colors of the Sus Jar should now show in JEI
- Updated config reload command code. Files and other reloadables now load in a particular order so that config options reloaded to the chocolcate coins reload before the master coin list does
  - The `/lcconfig reload` command now allows for an additional configID argument that allows you to reload only the given config file
- Wallet ability config options are now a *list* of wallets with the ability instead of a "minimum level"
- Updated and finished most of the custom ComputerCraft peripherals
- Opening the ATM or Terminal with the item version of the ATM/Terminal now closes the menu if the item is removed from your inventory
- Minor rework to the Item Position Data's "RotationType" fields
  - Now registers "RotationHandlerTypes" which parse the RotationHandler from a JsonObject
  - Allows rotation handlers to have custom input fields
    - "lightmanscurrency:spinning" now has an optional "speed" field that allows you to increase or decrease the speed of the rotation (default of 2)
	- "lightmanscurrency:facing_up" now has an optional "offset" field that allows you to rotate the item around the y-axis the given amount (in degrees)
  - Backwards compatible with old built-in rotation type string inputs

**Bug Fixes**
- Fixed an issue where creative traders didn't properly display the items being sold from a filter if no items matching that filter were actually in the traders storage
- Fixed an issue where the Slot Machine's single-attempt button displayed the roll count instead of the actual price when hovering over it
- Fixed an issue where Ticket Kiosk crafting trades no longer printed tickets as intended
- Fixed an issue where you couldn't deselect nodes in the Settings Clipboard tab
  - Also fixed an issue where the trade rule sub-nodes for the main "Trades" node did not give themselves unique ids for each trade index
  - You can now also select/target sub-nodes to only write/read certain trades from the settings data
- Fixed various vulnerabilities with some various custom packets that could result in hacked clients making changes server-side without proper authority/checks on the logical server.
- Fixed an issue where the list config parser still acknowledged commas as a list seperator even if the comma was within quotes

**API Changes**
- Added new GhostSlot class and IGhostSlotProvider to allow for widgets to provide their own cross-mod ghost slots for JEI and REI
- Added TradeRenderManager#collectGhostSlots method so that trades may have their own customized ghost slots
- Removed wallet ability tags and replaced them with new custom holder sets targeting ItemListOption config values
  - `"#lightmanscurrency:wallet/exchange"` -> `{"type":"lightmanscurrency:item_list_config","fileID":"lightmanscurrency:server","option":"wallet.exchangeAbility"}`
  - `"#lightmanscurrency:wallet/pickup"` -> `{"type":"lightmanscurrency:item_list_config","fileID":"lightmanscurrency:server","option":"wallet.pickupAbility"}`
  - `"#lightmanscurrency:wallet/bank"` -> `{"type":"lightmanscurrency:item_list_config","fileID":"lightmanscurrency:server","option":"wallet.bankAbility"}`
- Added various Salary related methods to the IBankAccount interface
- Removed various Salary related methods from the `ITeam` interface
- Moved IconUtil and IconData classes to `api.misc.icons`
  - Removed IconData.of methods. Each IconData type now has a seperate class with its own constructors
  - Created new IconIcon IconData type for direct 16x16 image icons
  - Updated constants in IconUtil class to utilize the new texture locations and icon types
- Removed Sprite class (primarily used by the PlainButton), and removed old IconAndButtonUtil
  - Added various new Sprite classes to the api.misc.client.sprites package
  - Added new "SpriteUtil" class to replace the old IconAndButtonUtil sprite constants (including new constants for commonly used assets)
  - Created "ButtonUtil" class to keep various common button builders available (namely the coin collection button builder methods)
- All `Notification` methods now return `Component` instead of `MutableComponent`, as text should not need to be edited at this point
- Made various data from config options public for config screen usage
- Added ConfigAPI class that lets you register custom ConfigReloadable instances
  - Config Files are automatically registered, and can now define a priority to determine which order they should be reloaded in by overriding `ConfigReloadable#getDelayPriority`
  - Allows custom config files (such as json files) to be reloaded in a particular order when /lcconfig reload is called
    - Used internally to make sure certain configs reload before the MasterCoinList is reloaded, while others (such as the server config and persistent traders) is reloaded after
- Reworked RotationHandler registration, parsing, and writing code
  - Each built-in rotation handler type has been seperated into their own class, and some now have custom input fields (but still have a default constructor as well)

**Texture Changes**
- Split "gui/icons.png" into seperate icon textures in the "gui/icons/" folder.
  - Should make it less breaking for future resource pack updates, as new icons will no longer be undefined in out of date resource packs
- Split many gui files (item_edit.png, buttons.png, etc.) into seperate sprites within the "gui/common/widgets" or "gui/common/container" folders
  - Similar to the icon split, intended to make the addition of new sprites less breaking for resource packs targeting older LC versions as the new widgets will still have their default sprite available
- Removed many re-used assets from various screen/gui/menu textures and moved them into a common folder

**Patch A**
- Fixed a typo in the wallet visibility toggle button resulting it in not having a proper sprite

**Patch B**
- Fixed an issue where the GhostSlot builder for fluids used the wrong FluidStack class
- Fixed various issues with the CC Tweaked peripherals

# 2.2.6.4
**Released for 1.20.1 & 1.21.1 on 9/13/2025**

**Additions**
- Terminal Screen now includes a dropdown widget on the top-right (in-between the "Open All" button and the search box) that allows you to change how traders are sorted
  - Last selection is remembered throughout your entire game session, but will be forgotten if/when you close your game
  - Added `network_terminal.defaultSortMode` config option to the client config that defines the default sort mode when first opening the screen
	- Added small "save" button next to the dropdown that will update this config option with your currently selected sort type
- Added "lc_trade" and "lc_trade_pre" events to all Trader Peripherals
- MultiPrice edit tab now also allows you to edit the selected trades type/direction (sale,purchase,barter, etc.)
  - Button tooltip for accessing this tab has been changed to state "Change Basic Settings..." instead of "Change Price..." as this is no longer a price-exclusive event
- Added new "Custom Item Trade Filter" item that can be inserted into most item traders to sell/purchase/barter with any item from the item list and/or item tag list
  - Since this list only contains item ids and item tags, no form of NBT enforcement will happen when a filter is being utilized
  - **MAJOR CHANGE** All Trades that are not fully strict on what item they're selling (including normal NBT not enforced trades) no longer require that they sell multiple of the exact same item, and will now randomize every item individually instead of grabbing a random stack and taking the required amount from them.
  - Disclaimer: Because of these changes, it's now possible to randomly sell a customer up to 128 different/unique items in the same trade. Please be responsible with this new power.

**Changes**
- Dropdown Widget buttons now block tooltips from being rendered by widgets behind them
- Added `getPlayerPermissionLevel(playerName,permission)` method to the base "lc_trader" peripheral
- Slot Machine now returns the input trader peripheral instead of the base trader peripheral
- Updated Traditional Chinese translation thanks to CF0741

**Bug Fixes**
- Fixed an issue where Item Trader Interfaces only accepted the exact sale/purchase/barter items of the trader instead of any item that the trader is allowed to store/sell/purchase/barter.

**Other**
- Placeholder "Trading Guide" patchouli book has now been registered, however it will not include any contents yet as it is still in development.

**API Changes**
- Added new `TerminalSortType` class so that new selectable sort types can be registered
  - Added `TraderAPI#RegisterSortType`, `TraderAPI#GetSortType`, `TraderAPI#GetAllSortTypes` and `TraderAPI#GetAllSortTypeKeys` methods for registering and accessing trader sort types
- Added `TerminalSorter#getDefaultSorter` and `TerminalSorter#getSorter` overrides that now include a `TerminalSortType` argument
  - Original methods still exist, but will now default to sorting by trader id as a tie-breaker
- ITooltipSource#renderTooltip now returns a boolean value to indicate whether it successfully rendered a tooltip and should block further tooltips from being rendered this frame
- ConfigBuilder#comment now accepts Object values instead of strings, and will successfully parse text components, suppliers, and lists as comments
- Added TraderData#validDirectionOptions and TradeData#setTradeDirection methods so that you can supply and consume the valid trade directions that your trader/trades support
- Added `FilterUtil` api class so that filters from other mods can be used instead of my mods item trade filter item.

**Texture Changes**
- Added "save" icon to the Network Terminal texture
- New "item_trade_filter" and "trader_guide" item sprites
- New container/item_filter gui texture

# 2.2.6.3
**Released for 1.20.1 & 1.21.1 on 8/24/2025**

**Additions**
- Added four new config options to the Auction House portion of the Server Config
  - `auctionFeeRate` option allows you to define a percentage-based auction fee to be collected from the highest bid as a tax when an auction is completed
  - `auctionSubmitPrice` option allows you to define a flat price that must be paid by the player before they can submit an auction (will be collected when they hit the "Create Auction" button)
  - `storeFeeInServerTax` option allows you to determine whether the auction fee should be safely stored in the server tax for stat tracking, etc.
  - `playerLimit` option allows you to limit all players to only be able to submit a finite number of auctions at any given time (players in admin mode can ignore this limit)

**Changes**
- TraderPeripheral#getLogs now appends "[#] " text in front of any stacked logs so that the count is noticable to the computer
- Moved the "Create Auction" button to the bottom right, and changed it into a smaller "+" icon button
  - Has "Create Auction" tooltip when hovered over and active, and displays warnings about any relevant auction fees or limits
- Text Money Value inputs now factor in the relevant decimal points when setting the starting string
- Coupons and Passes can now have a finite number of "Uses" defined when crafted.
  - Coupons lose a use when a discount is granted because of its presence
  - Passes lose a use when used on a Paygate's ticket trade
  - Uses Remaining are noted in the items tooltip. If no tooltip is present, the Pass/Coupon has infinite uses (old behaviour)
  - Durability Data can now be provided in a ticket/coupons crafting recipe allowing you to define the min/max use counts as well as whether infinite uses can be defined
  - Resized the Ticket Station background image to more easily fit the code and durability input fields for the coupons
  - Reworked the Ticket Station JEI/REI plugins to include more details if a code/durability input is accepted
- Added a new button/tab to the default Trader Screen that allows you to directly type Discount Codes into the trader without the need for a coupon
  - Codes are not remembered after closing and re-opening the menu, and must be typed in each time you interact with a trader
  - Due to their special screens, this cannot be done on a Slot Machine or Gacha Machine unless you interact with them through a Cash Register

**Bug Fixes**
- **1.20.1** Fixed an issue with my JEI plugin that caused issues when REI's built-in JEI integration was attempting to use it
  - Requires a somewhat up-to-date version of JEI to function correctly, and may cause crashes if using a particularly old version of JEI
- Fixed an issue with the SusBlockColor code that caused a crash when attempting to render in certain circumstances

**API Changes**
- Added `TicketItem#damageTicket` utility method for easily damaging ticket/coupon items with the ticket use data attached.
- **1.21** Deprecated `lightmanscurrency:ticket_data` data component. Tickets now use the vanilla dyed color data for color, and a new `lightmanscurrency:ticket_id` data component for their id.
- **1.21** Deprecated `lightmanscurrency:coupon_data` data component. Coupons now use the vanilla dyed color data for color, and a new `lightmanscurrency:coupon_code` data component for their id.

**Texture Changes**
- Resized Ticket Station texture

**1.20.1 Patch A**
- Fixed an issue where the Coupon and Ticket recipes didn't pass their Durability Data to the relevant places

**Patch B**
- Fixed an issue that resulted in Coupons not losing their durability/uses when giving a discount
- Tweaked the `ConfiguredItemListing` offer code to no longer freak out if the parent listing returns a null offer

**Patch B**
- Fixed an issue that resulted in Coupons not losing their durability/uses when giving a discount

# 2.2.6.2
**Released for 1.20.1 & 1.21.1 on 8/9/2025**

**Additions**
- Added new "tradeStock" int range filter. Unlike most filters this one has dual functionality
  - Only passes this filter if a trade is present with a stock count that matches the given range (not factoring in trade rules that only apply to certain customers)
  - Blocks other filters from passing because of trades that do not also pass this filters stock count
  - Example: `tradeStock>0 nether_star` will limit the search results to traders selling or buying nether star(s) AND that the trade buying or selling nether star(s) has 1 or more trade in stock
  - Only applies to the Terminal Search by default (will not work from the search bar within the trader UI)
- Added new Clipboard Settings node for Item Trades
- Added two new config options to the "auction_house" section of the server config
  - "ownerBidding" config option allows you to prevent an auctions owner from submitting a bid on their own auction
  - "doubleBidding" config option allows you to prevent the same player from submitting a bid twice in a row

**Changes**
- Item Trader Interfaces should now properly save and load the new ticket kiosk trade data, making it properly compatible with Ticket Kiosk traders
- The Basic Trade Edit tab now remembers the previous scroll and search text inputs after closing and re-opening the tab
- The Item Trades "Trade Type" selection has been change from its old toggle button to a drop-down widget to make it easier to understand and select your desired trade type.

**Big Fixes**
- Fixed an issue where the Out Of Stock notification was pushed before the normal trade notification was pushed, potentially preventing said notification from stacking
- Fixed an issue where the Item Trade notification didn't properly stack
- Fixed an issue with how Ancient Coins performed additions/subtractions that could result in crashes when calculating the price
- Fixed an issue where item stack match could be done horribly wrong if the amount of items in storage was larger than the amount allowed when attempting to insert items from a hopper-like machine

**API Changes**
- Added PendingSearch#setupFilter/setupStrictFilter/setupUnfiltered methods that all have `Function<String,Supplier<Boolean>>` arguments instead of the normal `Predicate<String>` argument
  - Allows you to only parse the string once, but inform the search result of its pass/fail status at a later time. Useful for filters that may be applied to multiple values
- FilterUtils now has several variations of the `intRange`/`longRange`/`floatRange` methods for utility purposes
  - The new variant that returns a `Predicate<Integer>` utilizes the new `setupUnfiltered` method for the PendingSearch allowing you to easily test if a number matches the range and automatically pass the pending search entry if it passes
- Added `FilterUtils#getTradeFilter(TradeData,PendingSearch)` method that allows you to manually implement the new "tradeStock" filter
- Added `ItemTradeData#registerCustomItemTrade` and `ItemTradeData#loadOfUnknownType` methods so that custom Item Trades can still be parsed properly by the Item Trader Interface
- Removed `ItemTrade#getSellItemInternal` in favor of the new `ItemTrade#getActualItem` method
- Added empty `ItemTrade#save/loadAdditionalSettings` methods so that custom item trades can also save/load additional data to the settings

**Patch A**
- Fixed various issues with the search box if no search filter was given
- Fixed an issue where the new "tradeStock" filter didn't function as intended
- Added a safety check so that a trades stock cannot go above the max integer value no mattter how much money you have available

**Patch B**
- Fixed an issue where collecting coins from a Stock Ticker would delete the coins if you didn't have a wallet equipped
- Fixed an issue where certain trade offer logs were posted to the error/warning channels instead of the debug channel

# 2.2.6.1
**Released for 1.20.1 & 1.21.1 on 7/13/2025**

**Additions**
- Added new Clipboard Settings Nodes for Tax Settings and Trader Rules to all traders
- Added new `command_trader.placementPermissionLevel` server config option allowing you to prevent players from even placing the trader block if they don't have valid operator permissions

**Changes**
- Added new "Coupon" item and crafting recipe
  - Crafted from paper ticket materials in the Ticket Station using the same recipe as the Master Ticket
  - Crafting requires a player input for the Coupon's secret "code" that will be matched with the discount code rule
    - Code input field will appear when the Coupon recipe is selected
- Internal Rework of the Ticket Kiosk trader
  - Ticket Kiosk now allows you to select the trade of choice from the Advanced Trade Edit tab in a similar manner to how it's selected in the Ticket Station
  - TicketStationRecipe's now have specialized match/assemble methods for use by the Ticket Kiosk
- Auctions now have a tooltip naming the player who submitted the items for auction when you hover over the arrow
  

**Bug Fixes**
- Fixed an issue where you could quick move books into the clipboard slot even if that tab wasn't open
- **1.21** Fixed an issue where the Money Mending enchantment didn't have the tradeable tag, preventing it from being spawned as a valid cashier/librarian trade
- **1.20** Fixed an issue where a traders display settings weren't saved to file...

**API Changes**
- Added `ICopySupportingRule` interface that trade rules can implement to allow their data to be copied like other trader settings

**Patch A**
- Updated Russian and Simplified Chinese languages

**Patch B**
- Fixed missing translation for the new Tax Settings node
- Fixed issue with Discount Codes limit info translation resulting in it displaying the total limit incorrectly

**Patch C**
- **1.20.1** Fixed an issue where the coupon recipe was generated as the wrong recipe type
- Removed the REI click area for the Ticket Station while the code input is visible

**1.20.1 Patch D**
- Fixed the hinge position for the Freezers #Inverted variants

** 1.20.1 Patch E**
- Fixed a missing null check with the CapabilityBlockEntity's getCapability method

# 2.2.6.0
**Released for 1.20.1 & 1.21.1 on 6/27/2025**

**Additions**
- Added new "freezer_door" property that allows you to customize the hinge position and rotation angle
  - Rotation can be inverted by defining the rotation angle as a negative value
- Added two new config options to the client config, allowing you to disable the tax collector warnings from server-wide tax collectors and from player owned tax collectors
  - Clicking on the tax collector warnings will suggest a `/lcconfig edit` command to disable said warning
- Added WIP trader peripherals with generic methods for all traders and specialized methods for item, paygate, and gacha traders
  - Also includes peripherals for the Cash Register, Terminal, and Auction Stand (which just gives direct access to the auction house peripheral)
  - Slot Machine peripheral is pending a minor slot machine rework and will be included whenever that happens
  - Ticket Kiosk peripheral is subject to change as there's also a pending ticket kiosk rework as well
- Added new "Settings Clipboard" tab where you can insert various writable items and copy/paste the traders settings to/from said item
  - Only certain basic settings are available at the moment, but more complex settings will become available in future updates
  - Current writable objects are paper, books (normal, book and quill, and written), and create's clipboard
  - Certain items such as books & clipboards will have a summary of the data written to the book itself in a readable format

**Changes**
- Combined several tabs into a a singular Trader Info tab
  - Trader Logs has been divided into two separate tabs (one for regular logs, and one for settings logs) and are now sub-tabs of the Trader Info tab
  - Trader Stats tab is now a sub-tab of the Trader Info tab
  - Tax Info tab is now a sub-tab of the Trader Info tab
- Changed Variant Locked tooltip style
  - Variant Locked message no longer appears by default in jade (configurable in jades config system)
- Variant is Locked message displayed when attempting to interact with a variant wand now appears above the action bar instead of in chat
- Players in creative now ignore the variant blacklist and hidden flags while selecting variants
- The gold coin can now be used for piglin bartering
  - Added new `villagers.piglinsBarterCoins` config option to the common config that lets you disable this new behaviour
- `model_variants.variantBlacklist` now accepts wildcards (i.e. "lightmanscurrency:freezer/inverted/*" will blacklist all of the new inverted freezer door variants)

**Bug Fixes**
- Fixed an issue where non-trader blocks lost their "Variant Lock" state when mined
- Fixed an issue where Wallet coin pickup code is still run if a previous event listener flagged the ItemPickupEvent as not being allowed to pick up the item
  - This is mostly a non-issue on 1.20.1 as the event is cancellable, but this can apparently cause issues in 1.21 as that version of the event is not

**API Changes**
- Added `TraderData#addInfoTabs` allowing you to add/remove info sub-tabs
- Added `TraderData#registerNodes` allowing you to register custom settings nodes for your traders
- Added `SettingsNode` class, and `TraderData#registerNodes` method so that custom traders can register their own settings nodes to be written or loaded from items
- Added `PrettyTextWriter` class allowing you to customize how the setting summaries are written on a given item type
- `lightmanscurrency:settings/writable` and `lightmanscurrency:settings/readable` item tags are now actually utilized and items can be added to these tags to flag them as items that can have settings written/loaded from them

**Patch A**
- Fixed an issue with how the variant blacklist config option was parsed and written to file
- **1.20.1** Fixed an issue where the default variant would be sorted last instead of first
- Added Model Variant attributes to Create's attribute filter system

**Patch B**
- Fixed a crash that would happen if CC Tweaked wasn't installed

# 2.2.5.4
**Released for 1.20.1 & 1.21.1 on 6/6/2025**

**Additions**
- Added new `quality.gachaMachineFancyGraphics` config option that results in the Gacha Machine not rendering the gacha balls and instead drawing a more simple representation of them.
  - Removed `quality.gachaMachineFullRender` config option as it is now obsolete
- Added new `model_variants.variantBlacklist` server config option that prevents certain model variants from being selected in the Variant Menu
- Added new "hidden" flag to Model Variant json data that prevents them from showing up in the Variant Menu
- Added new "VariantLock" (1.20.1)/`lightmanscurrency:variant_lock`(1.21) flag for items that can be defined to prevent the blocks variant from being changed in the variant menu
- Added new "show_in_creative" property to the Model Variant json data that allows that variant to appear in the creative menu and the relevant recipe viewers
- Added new `/lcadmin emptyWallet <entities>` command that removes all coins from the selected players wallets
- Added Search Box to the Bank Account selection widget, so that you can more easily find the bank account you are looking for
- Added new `bank_accounts.interestBlacklist` config option that allows you to blacklist certain money types from gaining interest
- Added new Herobrine Armor Display variant (creative-mode only)

**Changes**
- Paygate now has a new setting (found in the misc settings tab) that allows you to select how Paygate Trade handles conflicting redstone outputs
- Paygate trades are now considered *invalid* and cannot be activated or viewed by customers if no output side is defined
- The Command Traders "Permission Level" setting has been moved to the Misc Settings tab
- Create Compat mixins no longer load if it detects that creates major version number is <6, allowing you to use the latest LC version with older versions of create
- You can now search a paygate or command trades description or tooltip from the search bar
- Skinned Armor Displays now appear in the creative menu by default (glassless skinned armor displays do not)
- Reworked many variant names/tooltips

**Bug Fixes**
- Fixed various visual issues with how the scroll bar and trade button background is drawn on the Trading Terminal screen
- Fixed an issue where coin price entries did not space the coins correctly
- Fixed an issue where the first Slot Machine entry was randomized with 1 more weight than intended

**API Changes**
- Removed deprecated ITraderSearchFilter & ITradeSearchFilter methods
  - Made new methods no longer have an empty default
- Added `IDescriptionTrade` interface that trades can inherit to allow for easy searching from player-input trade descriptions/tooltips
- `AlertData#of(MutableComponent,AlertType)` private method is now public for use in situations where the AlertType may vary
- Added `MiscTabAddon` class and `TraderData#getMiscTabAddons` method allowing traders to add options to the misc settings tab
- Deprecated all `DisplayEntry#of` constructors
  - All built-in display entry classes are now public within the `*.lightmanscurrency.client.gui.widget.button.trade.display` package
  - All built-in display entry classes now have their own public static constructors that should be called directly
  - ItemEntry and ItemWithBackgroundEntry constructors no longer accept a count input as this is irrelevant and never really used (use `ItemStack#copyWithCount` instead if you must override the items count)

# 2.2.5.3
**Released for 1.20.1 & 1.21.1 on 5/20/2025**

**Changes**
- Completely reworked the Model Variant system and how it forces models to be loaded.
  - **Should** fix any incompatibilities with ModernFix and it's Dynamic Resources system
- Model Variant properties no longer require a namespace be provided if it is using the "lightmanscurrency" namespace
- Added new "targetSelector" field to the Model Variant data allowing you to define a search phrase
  - Accepts both single string or a list of strings.
  - Should be formatted as either "namespace:partial_id_*", "*back_end_of_id", or "\*center_of_id\*"
  - Use with caution as it may increase load times in modpacks with large block counts as it has to manually search all block ids to find matches
- Added new "input_display_offset" property that allows you to offset that variants rendering in the DirectionalSettingsWidget
- Added new "tooltip_info" property that allows you to define a tooltip that will be displayed in various places
  - Contains 3 boolean options, one for being displayed in the item tooltip, in the selection screens tooltip, and in jades tooltip.
- Glassless Display Case variants now have custom item position data ("lightmanscurrency:display_case_open") that draws the items slightly higher & larger since we no longer need to be contained within the glass

**Bug Fixes**
- Fixed an issue where Variant Properties were not registered before the resource packs were first loaded
- **1.20.1** Fixed an issue where items with variant data didn't properly display the variant when placed until a neighboring block was updated
  
**API Changes**
- Completely changed how Variant Properties are registered. You must now listen to the `RegisterVariantPropertiesEvent` in the mod bus to register your custom variant properties

**Patch A**
- Reworked how I listen to the texture atlases. Should hopefully fix any issues related to textures being assigned randomly.

**Patch B**
- **1.20.1** Fixed some refmap issues with the Create 6.0 compat that resulted in it not functioning correctly after being compiled.
- Wallet contents are now properly compressed after making a purchase at a Stock Ticker shop
- Master Coin List now includes the item id of the duplicate coin when logging that particular error

# 2.2.5.2
**Released for 1.21.1 on 4/28/2025**

**Released for 1.20.1 on 5/12/2025**

**Additions**
- Added new `quality.itemScaleOverrides` config option that allows you to forcibly scale items or item tags that are too large when drawn by item traders, etc.
  - By default includes items with the `lightmanscurrency:display_half_size` item tag
- Added the ability to add `Model Variants` with a Resource Pack.
  - Model Variant data can be placed in the `assets/namespace/lightmanscurrency/model_variants/` directory of your resource pack
  - Added new Glassless variants to the Armor Display & Display Case blocks
  - Added 5 new "Skinned" variants to the Armor Display with matching Glassless variants
  - Added new Footless variants to the Vending Machine & Large Vending Machine blocks
  - Variants can be selected by using the new Variant Wand on any block that supports it
  - Possible to define custom ItemPositionData for relevant blocks by adding a "lightmanscurrency:item_position_data" property
    - Allows either a resource location of an existing ItemPositionData file, or the raw ItemPositionData json data
  - See the Trader Skins suggestion post on my discord for a list of unsupported blocks (https://discord.com/channels/718724106404757616/1351422592871039048)
- Updated French translations thanks to Dowar

**Changes**
- Updated Ticket textures thanks to Dowar
- Updated Vending Machine models and textures thanks to Dowar
  - The interior of Vending Machines now glow in the dark to a certain extent emulating interior lighting
- You can now define a "MinLight" property to the ItemPositionData entries to define a minimum light level the items will be rendered at

**Bug Fixes**
- Fixed an issue where you could set the Price Fluctuation trade rule to fluctuate the price every second
- Fixed an issue where a trade sync packet wouldn't be sent when editing the items from within the Item Trade Edit tab
- **1.21** Fixed an issue with the Exchange Upgrade's stream codec, causing a disconnect whenever a menu attempted to syncronize the items data.

**Patch A (1.21 exclusive)**
- Optimized the VariantBlockModel so that certain calculations are only done once per reload
- Added `quality.variantBlockModels` config option to the client config allowing you to disable the `VariantBlockModel` class and other relevant block state injects so that LC can still be used when a mod conflict is present.
  - Still loads Model Variants and `Item` models from file and you can still use the Variant Wand/Selection Menu to define Model Variants for other players to see.
- Fixed a minor UV issue with the Footless Large Vending Machine model
- Removed refmap data from lightmanscurrency.mixins.json as this is no longer required in MC 1.21+

**Patch B (1.21 exclusive)**
- Fixed a crash that would happen when launched on a dedicated server

**Patch C (1.21 exclusive)**
- Fixed a couple of crashes that would happen when a model variant inherits from another
- Model Variants now support texture overrides
  - Tweaked Model Variant validation rules to be slightly more strict now that all available features are working as intended
- Blocks broken while a variant is applied will now keep their variant in item form
  - Crouching while selecting a block with a variant applied will attempt to select an item with that variant already applied

**Patch D (1.21 exclusive)**
- Fixed some issues with the Armor Display skin variants
- Improved key-pad lighting for the vending machine models
- Directional Settings Input widget now displays the blocks current variant
- JEI should now display all ancient coin types
- Jade now properly displays the variant icon & variant name in the tooltip

**Patch E (1.21 exclusive)**
- Reworked name formatting for certain Model Variants
- Added animated Paygate face when powered thanks to Dowar
- Added Jade plugin for providing proper icons for variant blocks, as well as including the variant name in the tooltip
- Added Jade plugin that displays the time left on the Paygates activation
- Fixed an issue where the Model Variant preview would be drawn in front of tooltips, etc.
- Fixed an issue where the Paygate powered model would not work correctly for model variants
- Fixed a small typo with the Ticket Station tooltip
- Fixed an issue where the Auction Trades didn't display the full tooltip of the items being auctioned

**Patch F (1.21 exclusive)**
- Updated Jade plugin for the Paygate to be more consistent with vanilla plugin timers
- Cleaned up some unecessary/unused code
- Added two new quality options to the client config allowing you to disable the items rendered in the Gacha Balls drawn by the Gacha Machine, and from all Gacha Balls

**1.20.1 Patch A & 1.21 Patch G**
- Fixed various potential mixin conflicts, as apparently flagging methods as @Unique isn't enough in some cases...

# 2.2.5.1
**Released for 1.21.1 & 1.20.1 on 4/8/2025**

**Additions**
- Added a new Money Bag cosmetic block that allows you to store more coins than a Coin Jar
  - Can store up to 576 miscellaneous coin items
  - Bag gets larger and heavier the more coins are inside
  - When mined, contents will still be stored within the bag in item form
  - Can be used as a weapon in a pinch, and it will deal more damage the larger the bag is
  - Added `money_bag` config options to the server config allowing you to define the money bags damage, attack speed, and/or give it a chance to drop coins when used as a weapon
- Added Translation Keys to various settings values, names, and categories in preparation for an upcoming change.
- Display Cases now come in all 16 vanilla colors
- Added new `/lcbank view <player|team>` command allowing admins to more easily view a bank accounts current balance
- Added new `/lcadmin viewWallet <player>` command allowing admins to see a players currently equipped wallet
  - In order to see the wallets contents you must hover over the message to see the wallets tooltips, etc.
- Added Japanese translation thanks to Meatwo310

**Changes**
- You can now extract items from the Gacha Machine if an output side is defined
  - Note: The item extraction has no filter unlike normal item traders, so do this with caution as it will indescriminately remove items otherwise
- Added Output Sides option to each individual Paygate Trade, allowing you to have a little more precision on where the redstone output is sent
  - Also increased the maximum paygate trade count from 8 to 16 as it's now plausible for that many trades to be relevant
- Various QoL changes to serveral text input fields that were used for number fields
- Seasonal Events now log the starting reward message in your personal notifications just in case you missed the chat message
- Wallets will no longer pick up coins reserved for other targets
- The Coin Jar item now requires that you hit Ctrl to view contents so that you can view your jars contents without also seeing the items instructions
- The Coin Exchange Upgrade now allows you to select up to 10 exchange actions to be executed in the order that you selected them.
  - You must now click on the originally selected exchange button in order to de-select it
- The Paygate now allows you to define a tooltip as well.
  - Click the small toggle switch next to the description input to switch between the description & tooltip inputs.
- Updated various textures & models courtesy of Dowar

**Compatibility**
- Added compatibility with Impactors economy API
  - Impactors money can now be used to set prices for trades
  - Impactors money can now be taken directly from your personal account and used for payment or stored in LC bank accounts
  - Added "compat.impactorModule" option to the common config allowing you to disable this compatibility if there are issues with it as I was unable to properly test this compat.
  - Note: As Impactors money is not item-based, it will be automatically be deposited in the players Impactor account when a trader is destroyed (if owned by a team, it will go into the teams owners account)
  - Note: As Impactors economy system is incompatible with mine in various ways, this compatibility only functions in one direction meaning that LC money cannot be used with Impactor compatible economy systems, etc.
  - Note: As Impactors uses a non-vanilla text system, translations may not be properly utilized from LC commands that display money values (such as the /lcbaltop command)

**Bug Fixes**
- Fixed a couple of display issues with the new permissions tab
- Fixed a couple of minor issues where the Ancient Coin input wouldn't lock itself if the price is currently free
- **1.21**: Fixed an issue where the Trader Interface input/output settings would not be saved/loaded to file correctly

**API Changes**
- Moved and reworked the TextInputUtil utility class (not an API class, but it's possible some of you were using this)
  - Instead of various utility functions, it instead has a custom EditBox builder class that you can use to more easily accept numerical inputs.
  - Comes paired with the new Int/Float/Long/Double Parser classes that you can use to define the ranges.
  - More direct IsInteger/GetIntegerValue functions were properly moved to the NumberUtil class
- Various Trade Rule changes to allow various functions to confirm the player is allowed to make the changes
  - Intended to make it possible for an Admin-Only trade rule, etc.

**Patch A**
- Fixed an issue where the Paygate didn't properly provide a redstone signal
- Editing a Traders Creative setting now uses the original CreativeToggleNotification
- Editing a Traders Icon is now logged

**Patch B**
- Fixed an issue where Book Traders didn't draw their books with the correct lighting
- Fixed an issue where Wallets could no longer pick up coins properly
- **1.21:** Fixed an issue where the Team Selection widget was extremely small on the Team Management screen

**Patch C**
- Added new CustomModelData data loader that lets you create or apply custom models for items being drawn by any Item Trader
- LC Config files now have and use a file ID (resource location) instead of the file name for various commands and crafting conditions
- Fixed an issue where the Teams Salary Input widgets didn't properly reset after a different team was selected, making it appear as though it had the same settings as the previously selected team

# 2.2.5.0
**Released for 1.21 & 1.20.1 on 3/27/2025**

**New Features**
- Added new SeasonalEvents.json config file to the `config/lightmanscurrency` folder allowing you to modify, add, and/or remove seasonal loot events to the game
  - Removed the `events.chocolateDrops` and `events.chocolateRate` config options.
  - Added the `events.startingRewards` & `events.lootReplacements` config options to allow you to more easily disable portions of this new system without having to manually edit the json file.
  - Removed the `/lcadmin events reward` command as it is no longer needed
  - Added the `/lcadmin events clearRewardCache <eventID>` command to allow you to manually clear the data storing which players have received that events reward

**Changes**
- Completely reworked and upgraded the Directional Input/Output widgets.
  - Input/Output widgets are now combined into a single widget, with different colors (and the tooltip) declaring different states (input,output,both,etc.)
  - Widget now actually shows the actual block sides instead of using the card display as a placeholder block
- Trade Rule selection/toggle tab has been reworked.
  - Instead of a checkmark, you instead must click a toggle slider to activate/deactivate
  - The Trade Rules icon is now drawn to the left of the rules name
  - The Trade Rules name no longer changes color based on the rules active/deactive state
  - You must now scroll up/down to view all trade rules, which means that this tab can now support more than 9 available rules
- Tweaked various Trade Rule tabs to make them more consistent across rules with similar widgets, as well as recolored all gui text to the vanilla dark-gray color.
- Trade Rule & Settings sub-tabs now use smaller tab buttons for their sub-tabs
- Ally Permissions tab has been reworked. They are now scrollable, but display fewer options on the screen.
- You can no longer craft other items using traders that are linked to an already existing machine and were simply picked up
  - Dev Note: This limitation only properly applies to vanilla shaped/shapeless recipes. Any modded recipes beyond this scope may be able to circumvent this limitation.
- You can no longer switch money input types in various places where they're required to match an existing money value
- True/False values from the settings change notifications now have a proper translation
  - Existing notifications will not be updated, this will only be fixed for newly created notifications

**Compatibility**
- Added Create 6.0 Compat allowing you to pay for Stock Ticker purchases with coins from your wallet.

**Bug Fixes**
- Fixed an issue with certain multi-line notifications not displaying their tooltip correctly.
- Fixed an issue where tooltips drawn on the Slot Machine & Gacha Machine screens sometimes ignored resource packs that change the tooltip background.
- Fixed an issue where you could not correctly define or clear low balance notification limits for ancient coins
- Fixed an issue where the Timed Sale rule didn't properly display after the timer has been completed
- Fixed an issue where the game could crash when certain inputs resulted in more widgets being added or removed at the same time that the rendering methods were being processed in another thread

**API Changes**
- Added `TraderData#supportsMultiPriceEditing` allowing certain trader types to opt-out of the multi-price editing options in the Basic Trade Edit Tab
- `Notification#getMessage` has been removed in favor of `Notification#getMessageLines` (`Notification#getGeneralMessage` and `Notification#getChatMessage` now also return lists instead of a single MutableComponent)
  - Extend `SingleLineNotification` to more simply extend the previously normal Notification methods.
- `MoneyValueHandler` now have various protected functions allowing you to check whether we're allowed the change handler, and to notify the listener when a handler has been changed internally within this handler. Useful for single handlers that change through different money types.

# 2.2.4.5
**Released for 1.21 & 1.20.1 on 3/16/2025**

**New Features**
- Added a new built-in Fancy Icons resource pack, thanks to Dowar!

**Changes**
- Gacha Machines can now have items inserted via hopper or other item movement techniques
- Gacha Balls can now only be placed within another gacha ball up to 16 times before it will refuse to place the item inside another gacha ball
- Gacha Balls can no longer be placed inside shulker boxes to avoid infinite shulker to gacha inception
- Ancient Money Values will now be combined into fewer lines when displaying a wallets contents or your available funds on the trader menu
- Various QoL changes to the Paygate Trades:
  - Can now define the redstone power level from 1 -> 15 for each given trade
  - Can now define a text description input to replace the duration display on the trade
    - Paygate Trades with a defined description will now be significantly wider
- Descriptions for all built-in Resource Packs now utilize translation keys and can now be translated into other languages

**Bug Fixes**
- Fixed an issue that prevented you from creating Persistent Traders/Auctions from the in-game systems
- Fixed an issue where the Gacha Machine didn't run the pre/post trade events resulting in various trade rules not working as intended
- Fixed an issue where the Gacha Machine Screen didn't display the inventory label, nor your available funds
- Fixed an issue where Ancient Coins could not be placed in the traders Money Slots
- Fixed an issue where the Wallet Tooltip didn't mention any Ancient Coins that were contained within the wallet

**API Changes**
- Added CurrencyType#getGroupTooltip to allow you to group the tooltip lines for similar money values
- Added MoneyView#getAllText to easily get the grouped tooltip lines for money storage contents
- Added MoneyView#getRandomValueLine so that you can get a random line of value text instead of the text from a random money value

# 2.2.4.4
**Released for 1.21 & 1.20.1 on 3/9/2025**

**New Features**
- Added new Gacha Machine trader type
  - Comes in all 16 vanilla colors
  - Has a singular trade that sells a single random item from the machines inventory for a predefined price (similar to the slot machine)
  - Will not be considered out of stock until the machines storage is completely empty
  - Items given to the customer will be in a "gacha ball" of a random color. Using the gacha ball item will release the item inside.

**Changes**
- Added new tab to the Trader Storage menu, allowing you to collect and/or store a specific amount of money to/from the Traders internal money storage.
  - Now supports storing money directly from your wallet without needing to manually place the coins in the coin slots
  - Coin Slots for storing money in the trader are no longer visible unless this tab is open
  - Various tabs have had their contents resized to utilize the newly available screen area
- Due to the large number of tabs, the "Return to Trades" button has been moved to the top-right of the screen.
  - The button will now be hidden when a tab that uses the right edge of the screen is open such as the settings or trade rule tabs
  - To match this new positioning, the "Open Storage" buttons are also now moved to the top-right of the normal Trader Menu
- Paygate Trades now have an additional tooltip explaining how to set a ticket trade while in the traders storage in addition to the normal "edit price" tooltip
- Added two new replacers for Command Trader commands
  - `%PLAYER%` within a command will now be replaced with the players game profile name (i.e. actual account name which should be unmodified)
  - `%PLAYER_NAME%` within a command will now be replaced with the players display name (name displayed above their head, may be modified by other mods)
- Gave all owner-protected blocks the `create:non_breakable` block tag so that they cannot be broken by create machines in Create 6.0 or newer

**Bug Fixes**
- Fixed an issue where the Command Trader didn't have the owner-protected tag, nor could it be mined more quickly by a pickaxe.
- Fixed a critical issue where deposit amounts could be overflown into negative values often resulting in the game crashing.

** 1.21 Only**
- Fixed an issue where wallets in the Curios slot would not intercept normal coin collection.

# 2.2.4.3
**Released for 1.21 & 1.20.1 on 2/16/2025**

**New Features**
- Added new Command Trader that allows you to purchase a single commands execution.
  - Trader has no recipe by default and is intended for admin-use only
- Added `command_trader.maxPermissionLevel` server config option to allow limiting the maximum permission level allowed for command traders.

**QoL Changes**
- Reworked trader/trade search system to allow for filters to be provided in the search text (i.e. `type:item` or `stock:>1`, etc.)
- Network Terminal search input now scales with the width of the screen.
- Added client config option to set default search filters that will always be applied regardless of your search input
- ATM Card & Prepaid Card now have an additional tooltip to explain how they're intended to be used

**Technical Changes**
- Changed file extension for custom config files from `*.lcconfig` to `*.txt` to make it easier for the system to determine what to open them with.
- Moved Coin Mint Minting/Melting options from the server config's `coin_mint` section to the common configs `crafting.coin_mint` section.
  - Recipes are now disabled using forges recipe conditions instead of manually disabled after being loaded
  - A coins mint/melt recipes now require a full datapack reload to be applied
  - Existing config options from the server config will NOT be copied over to their new location
  - Coin mint options that aren't recipe related remain in their current location
- Removed `mintType` field from Coin Mint recipes as they are now irrelevant
- Added config option to disable the ability to craft the coin mint itself to the crafting section of the common config

**Bug Fixes**
- Fixed an issue where toggling the "Free" state on the money value widget didn't properly inform consumers resulting in various issues
- Fixed an issue where traders never got properly deleted from the trader list on the client after being deleted server-side

**API Notes:**
- Added new `IKeyboardListener` interface for EasyScreen children to make it easier to listen to keyboard events directly
- Original `ITraderSearchFilter#filter` and `ITradeSearchFilter#filterTrade` functions are now deprecated in favor of the new methods
- Added various config events to allow you to listen in to when my config files are loaded and/or reloaded more easily.

**1.20.1 Patch A**
- Fixed an issue where the "command_trader" section of the config didn't end itself properly, resulting in it having all future config sections as sub-sections of itself.

**1.20.1 Patch B & 1.21 Patch A**
- Renamed `IKeyboardListener#keyPressed` function to prevent an issue where it was being obfuscated under the assumption that is was the vanilla `keyPressed` function.
- Admins can now access the `/lcterminal` command regardless of whether it's enabled in the config.

**1.20.1 Patch C & 1.21 Patch B**
- Fixed an issue where the Notification Settings Tab could sometimes cause a crash.

**1.20.1 Patch D & 1.21 Patch C**
- Fixed a couple issues with the trade-specific trade rule tab, one of which could sometimes result in the game crashing upon opening the tab.
- Fixed a translation error with the Free Sample tooltips.

# 2.2.4.2
**Released for 1.21 on 1/21/2025**

**Released for 1.20.1 on 1/31/2025**

- Added 16 new Ancient Coins of 8 different types.
  - New coins can be placed in your wallet
  - Once discovered, new coins can be set as a price for trades
  - Ancient Coins do not have relative values or exchange rates
  - Prices using Ancient Coins can only select a singular ancient coin type to accept (and a matching quantity)
- Increase input character limit for the Text Money Input from 10 characters to 32 characters (default vanilla character limit)
- Added 2 new Village structures for each village type (one banker house and one cashier shop)
- Added 1 new Ancient City structure
- Added `structures` section to the common config, allowing you to disable the addition of these new structures to the structure template pools
- The next Ticket ID is now synced to clients for more accurate Ticket Station recipe output peeking.
- Various QoL improvements to how the `lightmanscurrency:number` coin chain display type loads.
  - If no `baseUnit` flag is present the chain will still load, and it will now default to the lowest value coin.
  - If multiple `baseUnit` flags are given, it will only use the first one found, and give a warning announcing that there are duplicate flags.
- Interacting with a trader block whose trader is in the `EJECTED` state will reset the state back to normal and move the traders data to it's new position.
  - A warning will be logged on the server whenever this happens as traders *should* not be moved in this manner.

**Texture Changes**
- ATMData buttons can now define a "height" input to change the buttons height away from the default height of 18px
- Tab Buttons can no longer change color, and now utilize different sprites for the yellow/red variants used by notifications/disabled coin chest tabs.

**1.21 ONLY**
- Updated Curios integration to utilize the new release of the official Curios API port to 1.21, as they have decided to continue official development.
- Re-added Immersive Engineering rotation blacklist for LC multi-blocks
- Reworked Money Mending json parsing.
  - Removed `infinityExtraCost` field.
  - Added `enchantmentExtraCost` array field. Each entry has a `bonusCost` field with the added price, an `enchantment` field with the enchantment id, and an optional `maxLevelCalculation` field (default of 1) to determine the maximum number of times the price will be increased by this enchantment
  - Added `itemOverrides` array field. Each entry has a `baseCost` field with the replacement base repair cost, and a string array `items` field with a list of item ids/item tags for items to apply the override on
  - Added ability to parse Money Value config options as `baseCost`/`bonusCost` fields in the format of `config;config_file;option.path.in.file` and blank input fields will no longer default to the configured values.
- Fixed an issue with how my Villager Trade Offer modifications worked resulting in an error whenever attempting to replace an emerald in the "cost" portion of a trade offer.

**1.20.1 ONLY**
- Added 2 new [IDAS](https://www.curseforge.com/minecraft/mc-mods/idas) village structures that can be spawned in villages with an additional config option in the `structures` section of the common config allowing you to disable their generation.
- Removed the `server.enchantments.moneyMendingInfinityCost` server config option
- Added the `server.enchantments.moneyMendingItemOverrides` server config option allowing you to replace the base repair cost for different items.
  - Formatted as `"baseCost|namespace:item_id,#namespace:item_tag,..."` where `baseCost` is a standard Money Value config input
- Added the `server.enchantments.moneyMendingBonusForEnchantments` server config option allowing you to increase the repair cost depending on what enchantments the item has (replaces the functionality of the old `moneyMendingInfinityCost` config option)
  - Formatted as `"bonusCost|namespace:some_enchantment|maxLevelCalculation"` OR `"bonusCost|namespace:some_enchantment"` where `bonusCost` is a standard Money Value config input, and `maxLevelCalculation` is the maximum number of times the bonus cost will be applied (bonus cost is applied 1 time for each level of the enchantment)

**1.21 Patch A**
- Fixed an issue where the sus jar would not properly copy the color from the item to the block.

**1.21 Patch B**
- Fixed an issue where the wallet HUD would display "Nothing" when no money was in your wallet.

**1.21 Patch C**
- Fixed an issue where various data wasn't properly flagged as being on the logical client resulting in many instances where the client attempted to get server data that isn't available when connected to a dedicated server.
- Fixed an issue where the registry access wasn't always available when attempting to write/load the custom villager trade files from json.

**1.21 Patch D**
- Fixed a crash that would happen when attempting to get the repair cost for an item with money mending

**1.20.1 Patch A**
- Fixed an issue where the mod would crash while loading on a dedicated server.

**API Notes:**
- Added `MoneyValue#allowInterest`. If it returns false, Bank Accounts will not attempt to calculate interest for this money type
- Added `MoneyInputHandler#isForValue(MoneyValue)`. Used as a backup to allow a singular MoneyInputHandler to be used to generate MoneyValue's that have several `MoneyValue#getUniqueName` results
- Added CustomData class and various internal classes (such as TraderSaveData, etc.) are now utilizing this new system and have been moved & renamed as a result.
  - **Should** not break compatibility as all add-ons should be utilizing the available API functions instead of calling these internal classes directly, but I'm making note of it just to be safe.

# v2.2.4.1
**Released for 1.20.1 & 1.21 on 12/8/2024**

- Added new PlayerListWidget that can be used to edit lists of players
  - New widget is now used for the Traders Ally Settings, Discount & Whitelist/Blacklist Trade Rules, and Team Member Management
- Removed Team Members display-only tab
- Team Owner is now listed in the Team Member Management tab, though they cannot be interacted with from that tab
- Team Member Management Tab icon now displays the member count
- You can now Ctrl+Click on trades to select them, allowing you to edit the price of multiple trades at once
  - Button to select all trades, and the button to actually open the tab that edits all trades prices can be found on the right edge of the Basic Trade Edit tab.
  - Tooltip has been added to all trades to inform you that they can be selected in this manner.
- Added 3 new "Interaction Upgrades". These can be used to increase the number of traders or trades a Trader Interface can select and interact with.
  - Selected Trades & Traders on the Trade/Trader Selection Tabs now draw green backgrounds.
  - You must now de-select your selected trade/trader in order to select a different one if you've reached your selection limit, even when the limit is only 1.
  - You can still only select 1 trader if the Trader Interface is in Trade Mode.
  - Functionally it will interact with each trade (or trader if in restock/drain mode) in the order that they were selected during each interaction tick.
- Added new "Daily Trades" Trader-only Trade Rule that allows you to force players to interact with your trades in order and after a predefined delay.
- Simple Config Options now list their default value in their comment (not shown for complex options such as lists, etc.)
- Added new `coin_mint.soundVolume` config option allowing you to increase/decrease the volume of the Coin Mints anvil sound effects upon the completion of an items crafting.
- REI Coin Mint plugin now uses the coin mint arrow textures instead of the built-in REI arrow
- Certain Trade Tooltips no longer display in storage if you don't have the permissions required to perform the action.
- Fixed an issue where the Owner Selection widget did not offset the owner buttons off of the search box.
- Fixed a crash that could occur when opening the Paygates Storage Menu.
- Fixed an issue where Money Mending would not confirm that curios items are actually damaged before selecting it a possible repair target.

**Patch A**
- Fixed a crash that could occur if a Trade Button is clicked on when it has no trade context, which can sometimes happen in the Trader Interface Info tab.

**1.20.1 Patch B**
- Fixed an error that would happen when attempting to create the server-wide Tax Collector when the `/lcadmin taxes openServerTax` command was run for the first time.

**1.20.1 Patch C**
- Fixed an issue that prevented the Ender Dragon Wallets from being upgraded to their maximum slot capacity

**API Notes:**
- Ctrl + Interaction Clicks for TradeData input/output/other handling will no longer be passed on to the relevant trade data's interaction functions as they're overwritten by the Basic Trade Edit Tabs selection capabilities
- Added LazyPacketData#setList and LazyPacketData#getList functions which allow for the easy reading/writing of lists to a packet. An empty flag will be set at the given key to confirm the writing of the list, whereas the actual values will be written to KEY_INDEX
- Added TradeRenderManager#lazyPriceDisplay & TradeRenderManager#lazyPriceDisplayList utility functions to make it easier to define the price displays with built-in tooltip for setting the price if in storage mode with permissions.
- Added TradeRenderManager#hasPermission, so that various storage mode trade tooltips can now more easily check for permissions
- Added TradeButton$Builder#extraTooltips & TradeButtonArea$Builder#extraTooltips capabilities so that you can define additional tooltips to be drawn when the trade is hovered

# v2.2.4.0
**Released for 1.21 on 11/19/2024**
**Released for 1.20.1 on 11/20/2024**

- Added new Basic Wallet
  - Is functionally the same as a Copper Wallet but is slightly cheaper and can be colored with dyes like leather armor.
  - Can be upgraded to an Iron Wallet in a crafting table, but it will lose its dyed color.
- Added new Ender Dragon wallet that can only be found in end city loot chests.Has less storage than the Nether Star wallet, but comes with 3 bonus levels to the Coin Magnet enchantment and can still be upgraded to match it's max capacity.
  - Starts with 42 item slots, but can be upgraded more than other wallets allowing it to reach the same max item capacity as the Nether Star wallet
  - Has 3 free bonus levels of the Coin Magnet enchantment
- Added `machine_protection.quarantinedDimensions` string list config option to the server config to allow dimensions to block off all network trader & bank account functionality.
  - Intended for either "Creative Dimensions" where players have access to infinite items/money in a seperate inventory from their normal inventory
  - Disables all Bank Account & Network Trader access, including the creation of Network Traders, etc.
- Updated Tax Area bounds rendering. Now uses vanilla world border textures for improved visibility
- Bank Account Selection buttons will now auto-scroll the account name if it is too large for the buttons available space.
- The ATM's Transfer Tab now defaults to selecting the target bank account from a list similar to the Account Selection tab.
  - Transfer to Player mode is still an option so that you can still transfer money to players who have not yet joined the server.
  - Input Mode toggle & the Transfer Button itself are now off of the right-edge of the screen near the list selection.
  - Transfer Button now displays a description of the full transfer details, and will glow orange instead of green if transferring to a player who does not yet have a bank account (in player name input mode)
- Improved and automated REI exclusion zone calculations, allowing REI to utilize all available space until an off-screen button attempts to use it.
- JEI Plugin now also registers exclusion zones for my screens.
- Added API to custom screens allowing them to give hovered item/fluid info to REI/JEI for items in abnormal slots
  - You can now look up recipes and/or usages for items in a traders item storage
  - LC Tech will be updated to allow recipe lookups for fluids in a fluid traders tank
- Optimization changes to the Trader Storage Screen. Will break any custom traders added by an addon mod.
- Added /lcterminal command allowing players to open the network terminal with a command. Only available if a new `terminal.lcterminalCommand` server config option is enabled (disabled by default).
  - Cannot be used if a player is in a "quarantined" dimension
- Internal optimizations of various widget constructors
- Wallets in a cosmetic Curios wallet slot will now be rendered on your hip instead of the actually equipped wallet.
  - Wallet Slot now has a cosmetic slot by default
- Indestructible Wallets (Nether Star) will now bounce out of the void if dropped below a dimensions lower build limit
- Game Clients will now communicate with the server and collect/cache player names for player ids.
  - Should result in more accurate player names & skulls for bank account & owner widgets, as well as the potential for future upgrades to other player name input fields such as Team Member selections, etc.
- Fixed an issue where non-owners could not access a Network Traders customer screen by interacting with the block directly
- Fixed a possible crash that could happen if the overworld wasn't loaded during a server tick
- Fixed an issue where all trader settings sub-tabs would be visible for the first frame after selecting the tab
- Fixed an issue where a notifications tooltip would still display when hovering over the delete notification button.

**1.21 Exclusive Changes**
- Wallet Data (numerical level, storage capacity, model id, etc) is now all handled via Data Components
- Re-added Supplementary's villager & trade offer manipulation compatibility as it has now updated to MC 1.21

**1.21 Patch A**
- Fixed a crash with getting the name from a PlayerReference
- Made Curios Datagen only load if curios is installed to not force any addons to have curios installed when running their own datagen

**1.20.1 Patch A**
- Fixed an issue where scrolling text (normal buttons & Bank Account buttons) would render at the wrong place.

**1.21 Patch B**
- Fixed an issue where the prefix text for Text Money Value Inputs would render in the wrong position (fixed in 1.20.1 at initial release)
- Fixed an issue where the Slot Machine Screen placed the preview widgets at the wrong y positions
- Fixed an issue where the Auction House did not block interaction if used within a quarantined dimension
- Fixed an issue where the initial wallet recipes (not the upgrade versions) were generated at the wrong location

**1.20.1 Patch B**
- Fixed an issue where the ATM's Coin Slots would always render regardless of which tab you had selected
- Fixed an issue where Trader Interfaces don't properly collect statistics on their interactions

**1.20.1 Patch C**
- Fixed an issue where EasyWidget#renderTick was not being called, resulting in various widgets not hiding or moving themselves when appropriate

**API Notes:**
* Added new `ClientPlayerNameCache` class with various methods for getting player names on the logical client. If a result is not found instantly, it will return null but send a packet to the server requesting more data, so the result is subject to change.
* `TraderStorageScreen`/`TraderStorageMenu` classes now utilize the formerly experimental `EasyTabbedMenu` and `EasyTabbedMenuScreen` classes for a more uniform tabbed screen experience. Very little has changed functionally, however the ChangeTab functions are different and EasyMenuTab#createClientTab has different returns/inputs, so an update will be needed to be made compatible with these changes.
* All `EasyWidget` widgets added by my mod now utilize a Builder sub-class to remove the need for several redundant constructors for various common widgets, as well as removing the need for certain variables to be public (such as MoneyValueWidget#drawBG).