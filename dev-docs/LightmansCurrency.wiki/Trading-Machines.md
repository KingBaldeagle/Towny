## The Trading Core
In order to craft any type of trader, you must first craft (or purchase) a trading core.

![](https://i.imgur.com/K5QDtta.png)

The trading core is the core part of every trader that handles the automatic distribution of money & items.

## Trading Machine List
### Display Case
![](https://i.imgur.com/UKyaf9l.png)
* Trade Count: 1
### Shelves
![](https://i.imgur.com/CowWGfu.png)
* Trade Count: 1
* Special Notes: Can be crafted using any supported wood type
### Double Shelves
![](https://i.imgur.com/tmwgCzY.png)
* Trade Count: 4
* Special Notes: Can be crafted using any supported wood type
### Card Display
![](https://i.imgur.com/hamWqWD.png)
* Trade Count: 4
* Special Notes: Can be crafted using any supported wood type and any wool color
### Vending Machine
![](https://i.imgur.com/aMBYem3.png)
* Trade Count: 6
* Special Notes: Can be dyed to all 16 vanilla colors in a crafting table, and washed white in a crafting table with a bucket of water
### Freezer
![](https://i.imgur.com/WAqNH5N.png)
* Trade Count: 8
* Special Notes: The freezer door opens and closes like a chest when being used by a player. Color of the freezer depends on the color of concrete powder used to craft it
### Large Vending Machine
![](https://i.imgur.com/RM11AXJ.png)
* Trade Count: 12
* Special Notes: Can be dyed to all 16 vanilla colors in a crafting table, and washed white in a crafting table with a bucket of water

## Specialty Trader list
Specialty traders behave similarly to your standard item trader, but have certain sale item restrictions and behavior
### Armor Display
![](https://i.imgur.com/tgikaUp.png)
* Trade Count: 4
* Special Notes: Can only sell/purchase for 1 of each type of armor piece (Head/Body/Legs/Feet).

### Bookshelf
![](https://i.imgur.com/UCMY9LX.png)
* Trade Count: 10
* Special Notes: Can be crafted using any supported wood type. Can only sell/purchase books, written/writable books, and enchanted books by default
* Customization: Modded books can be added to the tradable books list by giving them the `lightmanscurrency:tradeable/book` item tag via a data pack

## Other Traders
These traders are completely different from your Standard or Special Traders, but still use variations of the same menus, and are compatible with the Cash Register.

* [Auction House](https://github.com/Lightman314/LightmansCurrency/wiki/Auction-House)
* [Paygate](https://github.com/Lightman314/LightmansCurrency/wiki/Paygate-&-Tickets)
* [Slot Machine](https://github.com/Lightman314/LightmansCurrency/wiki/Slot-Machine)
* [Ticket Kiosk](https://github.com/Lightman314/LightmansCurrency/wiki/Tickets#ticket-kiosk)

## Trader Features
* Traders are owner-protected, meaning that only the traders owner can access the traders storage or break the trader.
* Traders log activity so that the owners can see who their biggest customers are, and what items are in the highest demand.
* Traders can store any item(s) that are being either sold or purchased. Default item limit is 576 (9x 64), but this can be upgraded by giving the trader some item storage capacity upgrades.
* Traders have varying types of trades, and can be set to sell an item, buy an item, or barter one item for another. Each trade slot on a trader can be a different trade type, such that one trader can sell a bucket of lava and buy the empty bucket back.
* Trader owners can flag other players as allies so that they can access the trader storage for assistance with restocking & keeping track of the logs.
* Traders can be owned by a [team](https://github.com/Lightman314/LightmansCurrency/wiki/Teams), allowing all members of the team to manage the trader as though they were on the ally list, and all team admins to manage the trader as though they were the traders owner.
* Traders can have defined **[Trade Rules](https://github.com/Lightman314/LightmansCurrency/wiki/Trade-Rules)** that can change how players can interact with the trades. Trade rules can be applied to the trader as a whole, or only applied to a specific trade.
* Traders can be linked to their owners bank account in the Trader Settings. Full details can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#traders).
* Traders can be turned into a [Network Trader](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders) by giving it a Network Upgrade.
* When broken, traders will drop all items & coins in storage.
* When broken illegally (through another mods mining machine, setblock commands, etc.) the trader and its contents will be placed into an emergency ejection inventory that you can access through your inventory.

## Trade Types
### Sales
Sales are the default trade type that consist of the item(s) you're selling, and the amount of money you are selling it for. You can sell up two item stacks in one trade (either two stacks of the same item, or two different items), and the price can be set as high as you wish. In order for the trade to go through, you must have enough of the item being sold in storage to actually give to the customer, and once bought the money from the sale will go into the traders coin storage, and the item(s) sold will be removed from storage and given to the customer.
### Purchases
Purchases are the opposite of a sale that consists of the item(s) you're buying and the amount of money you wish to pay for it. In order for the trade to go through, you must have enough money in the traders coin storage to actually pay for the item, as well as enough room in storage to store the purchased item(s). Once bought, the purchased item(s) will go into the traders storage and the money paid will be removed from the traders coin storage and given to the customer.
### Barters
Barters are the most straightforward trade type that consists of the item(s) you wish to receive, and the item(s) you with to give in return. In order for the trade to go through you must have enough of the sell item(s) in storage and enough room in storage to store the item(s) you are receiving. Once bartered, the item(s) you are receiving are put into storage, and the sold item(s) are removed from storage and given to the customer.

## The Trading Screen
The trading screen appears when a user interacts with a trading machine as is what the customers will see when attempting to make a trade.

![](https://i.imgur.com/676YkAQ.png)

* Above the players inventory to the right are 5 coin slots where the player can insert coins should they not have a wallet on their person.
* Below the coin slots is where the currently available money from both the players wallet & any money in the coin slots are displayed.
* At the very top is where the list of trades are displayed. Trades are generally displayed as **Input** -> **Output**, where the input is any money/items that the customer are expected to pay, and the output is what the customer will receive after paying. Hovering over an item with your mouse will show the items details, as well as various information about the trade such as how many trades are in stock, and the items original name should a custom one be defined.
* Note that any items surrounded by a purple square are not NBT enforced, which allows customers to sell an item with any NBT data (such as a shulker box with random junk in it, an enchanted book with any enchantment, etc), or has them buy a random item from the traders inventory (such as a shulker box with random contents).
* For owners & allies, a chest button appears in the bottom-left corner allowing them to open the storage screen for the trader.
* For the owner & allies with coin collection permissions, a coin collection button appears above the storage screen button. Hovering over the button will show how much money is in the traders coin storage, and clicking on it will automatically collect the coins and either place them in the players wallet, or in the traders coin slots.
* If the trader is a Network Trader (either via upgrade or natural process), a "Back to Terminal" button will appear below the Storage Button (if applicable), pushing the buttons above it upwards slightly.

## The Trader Storage Screen
The trading storage GUI is where owners & allies can edit & define a traders trades, add coins to storage for purchases, and restock items for sales/barters. It consists of several tabs detailed below.

On every tab the following buttons are above the screen.
* The button at the bottom left will return you to the Trading Screen.
* The 2nd button at the bottom left will show and collect coins from the traders coin storage same as the coin collection button on the trading screen.

There will also be 5 coin slots above the players inventory to the right, and when coins are placed in there a button will appear to the left of them that allows you to deposit those coins in the traders storage (to be used as payment for any purchase trades).

### Basic Trade Edit Tab
![](https://i.imgur.com/jke9BoI.png)

This tab allows you to quickly edit the basics of your trades, such as what items they're selling, etc. If you click on any empty item slot (usually given a placeholder [], but may change depending on a traders restrictions) while holding an item, you can define the item being sold/purchased. Right-clicking with an empty cursor will decrease the quantity by 1, while right-clicking with a matching item will increase it by 1. Clicking on an empty slot with an empty cursor, clicking in the price area, or simply clicking in an empty portion of the button will open the [Advanced Trade Edit Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#advanced-trade-edit-tab) while automatically selecting the relevant portion of the trade.

### Trader Storage Tab
![](https://i.imgur.com/qYPUUE0.png)

This tab allows you to put items into or out of storage. You can shift-click items in/out of storage, or simply move them with your mouse. By default you can hold 9x64 items of each type in storage (ignoring stack limits of items that normally cannot be stacked such as tools/weapons), but your storage space can be upgraded by crafting an Item Capacity Upgrade and placing it in one of the 5 upgrade slots to the right. Note that the traders storage can only accept items that are being purchased or sold in one of the defined trades.

The two arrow buttons are quick interaction buttons that allow you to quickly collect all items in storage, or quickly restock the trader with items in your inventory. Use caution when using the quick-collect, as it does not account for your inventory space and as such any excess items will be dropped onto the floor (or deleted if you are in creative mode).

### Advanced Trade Edit Tab
![](https://i.imgur.com/YEgSoFn.png)
![](https://i.imgur.com/fFf0za3.png)

This tab allows you to change the trades type, set the price (if applicable), or set a trades item to an item that you don't have in your inventory (if said item is an input). The current trade type is displayed in the button on the top-right, and clicking it will cycle the trade through the valid trade types (sale -> purchase -> barter). The currently selected portion of the trade will have a small arrow above it. If the price is selected, a price selection widget will appear that you can use to define the trades price. If an item is selected, a Custom Name input will appear (if the selected item is an output), or an item selection widget will appear where you can search for items and define the trade with an item from that list (if the selected item is an input).

When this tab is selected, a Trade Rule button will appear in the top-right of the screen, allowing you to edit trade rules that only apply to this trade. See the [Trade Rules](https://github.com/Lightman314/LightmansCurrency/wiki/Trade-Rules) page for more details on how Trade Rules work.

### Trader Logs Tab
![](https://i.imgur.com/8UN9xJ2.png)

The Trader Logs Tab allows you to view the interaction logs stored by the trader. All customer interactions will be logged here, as well as any settings changes made within the Trader Settings Tab. Note that the logs stored here behave the same as [Notifications](https://github.com/Lightman314/LightmansCurrency/wiki/Notifications)!

### The Trader Settings Tab
The trader settings tab (formerly the trader settings screen), consists of several sub-tabs displayed on the right edge of the screen, each of which includes a different setting or customization option for you to edit.

#### Trader Name Tab
![](https://i.imgur.com/03vSBio.png)

On this tab you can change and/or reset the traders custom display name, as would appear in the title of the storage/trader screen.

If the trader is a [Network Trader](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders) or has a [Network Upgrade](https://github.com/Lightman314/LightmansCurrency/wiki/Machine-Upgrades#network-upgrade), a phantom slot will also be drawn and placing any item within that slot will allow you to customize the traders icon on the Trading Terminal to that item instead.

Towards the bottom, a "Pickup Entire Trader" button may be shown. Clicking on this allows to to pick up and move the trader elsewhere without losing the traders data/storage.

Note: Even if the trader is a network trader, the trader cannot be interacted with while it's still in item form. If the item is somehow lost or destroyed while you're moving it, an admin can use the `/lcadmin traderdata recover <traderID>` command to re-create the item.

If in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), the Creative toggle button will also appear on this screen in the bottom-right corner. You can get a better idea of the features of a creative trader [here](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#creative-traders).

While in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), you can also forcibly add/remove trades (between 1 & 100) by clicking the +/- buttons that appear next to the creative toggle button.

#### Persistent Trader Tab
![](https://i.imgur.com/HTblAqK.png)

This tab will only appear while in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode) while interacting with a trader that can be made persistent (which is all except for the Paygate and Auction House traders).
More details on how this works can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/Persistent-Traders#creating-a-persistent-trader-in-game)!

#### Trader Allies Tab
![](https://i.imgur.com/rRWADyg.png)

On this tab you can add/remove other players from the list of allies. Simply type the account name into the text box, and click the Add button to add them to the list, or click the Remove button to remove them from the list.

#### Ally Permissions Tab
![](https://i.imgur.com/d0m7GWX.png)

On this tab you can define what permissions players on the ally list have. Note that some permissions require other permissions to work (i.e. they must have permissions to Access Settings in order to be capable of changing the trader's name, etc.)
The permissions available for edit may/will depend on the traders type (e.g. only item traders will have the "Change Item Settings" permission)

#### Misc Settings Tab
![](https://i.imgur.com/dIO6hZp.png)

This tab has miscellaneous options that don't really warrant an entire tab for themselves alone.

###### Enable Search Box
This option adds a search box to the customers interaction screen, allowing them to search for trades containing a specific item, etc. Recommended if your trader has a lot of trade offers for your customers to look through (especially if you utilize admin abilities to increase the traders offer count to a large amount)

###### Link to Bank Account
This option disables the traders local money storage, and makes it so that all money earned and/or paid through the trades is instead sourced from the owners bank account directly. Useful if you have a lot of traders you are trying to manage, and don't want to have to constantly walk around to all of your traders and collect the money manually.

**Note**: This option will not appear if the traders current owner does not have a bank account to be linked to. For security reasons, this option is automatically disabled whenever the traders owner changes.

###### Notifications
Activating the "Notifications Enabled" option will make it so that the owner will be notified of all trades and other such notifications in the personal [Notifications](https://github.com/Lightman314/LightmansCurrency/wiki/Notifications) whenever a player interacts with a trade. The notifications received are the same as those displayed in the Trader Logs tab.

If "Notify in Chat" is enabled, you will also receive a push notification in your in-game chat whenever you receive a notification from this trader.

If the trader is owned by a Team, you can also customize which members of the team receive the notifications (only the owner, only the admins, or all members).

#### Tax Settings Tab
![](https://i.imgur.com/Sw0ojAt.png)

This tab allows you to edit any settings related to the [Tax Collectors](https://github.com/Lightman314/LightmansCurrency/wiki/Taxes). At the present moment, the only relevant option is the acceptable tax rate which can be increased or lowered by clicking on the +/- buttons next to the label. If the Tax Rate applied by all relevant Tax Collectors exceeds this tax rate the trader will lock itself from further interaction until this issue is resolved.

#### External Input/Output Tab
![](https://i.imgur.com/vaLZh2M.png)

This tab is unique to Item Traders, though it also appears in the Fluid & Energy traders added by LC Tech. It allows you to define sides from which items can be added or extracted from the traders storage. If the green square is shown on that side, input/output is permitted from that side.

Items input are limited by what items are allowed in the Traders Storage. Items output are limited by items that are NOT being sold by any trades (even if the trade is not fully defined).

**Note**: Since the Display Case does not rotate based on your placement angle, it's "back" will always be north, and "left" will always be west.

#### Ownership Tab
![](https://i.imgur.com/KnMOP3N.png)

On this tab you can transfer the traders ownership to another Player or to a Team. Please do so with caution, as transferring ownership cannot be undone without the new owner transfers the traders ownership back to you.

By default this screen will show a list of all known potential owners, with the current owner being placed at the very top, then showing any teams your a member of, followed by a list of known players. You can search through the list by typing in a player/teams name in the search box above the list, and if a player is not on the list, you can click the button on the top right to switch to manual input mode, upon which a text box will appear allowing you to manually type in a players username.

As mentioned above, transferring ownership to another player/team will automatically disable the bank account link for security reasons.

For more details on how to create a team, and how they interact with traders go [here](https://github.com/Lightman314/LightmansCurrency/wiki/Teams).

### Statistics Tab
![](https://i.imgur.com/DT08BRA.png)

This tab allows you to view various statistics about the trader such as how much money it's paid, earned, how many times it's been interacted with, etc.

Players with Settings permissions can wipe the stats clean for a clean slate.

### Tax Information Tab
![](https://i.imgur.com/3rnAIRu.png)

This tab displays useful details about which [Tax Collectors](https://github.com/Lightman314/LightmansCurrency/wiki/Taxes) are currently applying taxes to this trader, and what tax rate they're applying. The Tax Collectors name will change color depending on it's current status:
* Green: Means that this Tax Collector is currently taxing this Trader
* Gray: Means that this Tax Collector _can_ tax this Trader, but does not yet have your consent to do so
* Red: Means that this Tax Collector has been intentionally blocked from taxing this trader by an Admin

If a Tax Collector _wishes_ to apply taxes to this trader, but does not currently (which will occur if a Tax Collector is not activated until after your trader is placed), there will be a button to the right of it's Tax Rate asking you if you "Accept" its taxes, making it so that your trader will be taxed by it in the future.

If you are in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), you will see an [X] button to the left of each Tax Collectors tax rate as well. Clicking this will toggle whether this trader should ignore all taxes from this Tax Collector (and thus change it's name to Red as noted above)

### Trader Rules Tab

This tab allows you to edit the Trade Rules that apply to the entire trader. See the [Trade Rules](https://github.com/Lightman314/LightmansCurrency/wiki/Trade-Rules) page for more details on how Trade Rules work.

## Network Traders
There are also network traders, which behave the same but can be accessed from any location via a Trading Terminal, details of which can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders).