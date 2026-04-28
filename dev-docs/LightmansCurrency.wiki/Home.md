Welcome to the Lightman's Currency wiki!

Here you will find the most up-to-date tutorials on how to use Lightman's Currency!

This wiki is in the process of being updated to include features & changes from v2.2.3.5!

To view a list of all helpful pages, please take a look at the [Table of Contents](https://github.com/Lightman314/LightmansCurrency/wiki/Table-Of-Contents)

### Overview & Features
* 6 different types of coins to the game (copper, iron, gold, emerald, diamond, netherite), that can be obtained in various ways.
* Each coin has a server-defined comparative value. Defaults to each coin being worth 10 of the coin valued below it. Will be show in a tooltip when hovering over each coin.
* Coins can be minted and melted back into their base components at a Coin Mint machine. _Can be disabled/restricted in config_
* Coins can be converted between different types easily by crafting and using an ATM machine.
* Adds 6 levels of wallets with various features being unlocked such as auto-collecting coins that are picked up, and converting coins to higher coin values manually and/or automatically.
* Adds craft-able local and universal traders that can be used to trade items with other players in a safe manner. All traders are owner-protected, meaning that only the owner of the trader can break their trader and access the storage area.
* Trader Team creation, allowing a group of players to manage their traders cooperatively. Comes with 3 permission levels: Member, Admin, and Owner.
* Personal & Team bank accounts, for easy & protected money storage. Traders can be linked to its owners bank account for automatic coin collection/payment.
* Netherite wallets, coins, coin piles, and coin blocks are all fireproof like normal netherite.

### Client Configuration
If you're having FPS issues in trader-heavy areas, you can limit the amount of items being rendered in a trader by editing the lightmanscurrency-client.toml config file's 'traderRenderType' option from FULL to either PARTIAL (only 1 item will render per trade) or NONE (no items will be rendered).

### Ways to get Money
* Mint coins in the Coin Mint
* Coins can spawn in loot chests throughout the world
* Kill monsters
* Sell items to other players

**Note**: Some of the above options can be disabled by the server owner.

#### 1.16 Copper Coins
By default copper coins cannot be minted on the 1.16 version of the mod. If you are using a mod that adds copper to the game, please check [here](https://github.com/Lightman314/LightmansCurrency/tree/LC-1.16/Recipe%20Datapacks) for a datapack that adds the copper coin mint & melt recipes for that mod.

### Translations
You can find more details about what translations are available, and how to provide them [here](https://github.com/Lightman314/LightmansCurrency/wiki/Translation-Info).

**Disclaimer**: _These translations are provided on a volunteer basis, and as an English speaking person, I cannot in any way validate their contents. Please report any inaccurate or inappropriate translations in the Issues tab of this github._

### Mod Ports
This mod is designed for Forge, however if you are a mod developer interested in porting the mod to another mod loader such as Fabric, you can find more details [here](https://github.com/Lightman314/LightmansCurrency/wiki/Mod-Ports).