## How To Craft A Wallet
There are 8 types of wallets, 7 of which can be crafted.
### Copper Wallet
![](https://i.imgur.com/tkMfpMY.png)
* Default Capacity: 6 stacks.
* Coin Exchanging: NO
* Coin Pickup: NO
* Bank Account: NO
### Iron Wallet
![](https://i.imgur.com/h5DYdFC.png)
* Default Capacity: 12 stacks.
* Coin Exchanging: YES
* Coin Pickup: NO
* Bank Account: NO
### Gold Wallet
![](https://i.imgur.com/c9hhc5N.png)
* Default Capacity: 18 stacks.
* Coin Exchanging: YES
* Coin Pickup:YES
* Bank Account: NO
### Emerald Wallet
![](https://i.imgur.com/fZzjlrG.png)
* Default Capacity: 24 stacks.
* Coin Exchanging: YES
* Coin Pickup: YES
* Bank Account: NO
### Diamond Wallet
![](https://i.imgur.com/tKg6EGk.png)
* Default Capacity: 30 stacks.
* Coin Exchanging: YES
* Coin Pickup: YES
* Bank Account: NO
### Netherite Wallet
![](https://i.imgur.com/bfIQkEs.png)
* Default Capacity: 36 stacks.
* Coin Exchanging: YES
* Coin Pickup: YES
* Bank Account: YES
* Fireproof: YES
### Nether Star Wallet
![](https://i.imgur.com/IwtFucE.png)
* Default Capacity: 54 Stacks.
* Coin Exchanging: YES
* Coin Pickup: YES
* Bank Account: YES
* Indestructible: YES
* +1 to the Coin Magnet Enchantment level
### Ender Dragon Wallet
![](https://i.imgur.com/AbW0PuN.png)
* Default Capacity: 42 Stacks
* Coin Exchanging: YES
* Coin Pickup: YES
* Bank Account: YES
* Fireproof: YES
* Can upgraded 2 additional times
* +3 to the Coin Magnet Enchantment level
* 10% chance to spawn in End City chests

**Special Note**:
Due to the changes in system, the bonus Coin Magnet levels will not appear in MC 1.21 or newer, however they will still be reflected in the wallets tooltip where it details its current collection radius.

In addition to the above crafting recipes, wallets can also be upgraded to higher level wallets by crafting them with a higher level material. Doing so will maintain the wallets contents, custom name, and any applied enchantments when upgraded. The Ender Dragon Wallet cannot be crafted, but has a 10% chance to spawn in all end city chests.

## Wallet Features
* Wallets can be opened and their contents accessed by using them from your hotbar

## Equipping your wallet
Most Wallet Features will not function if the wallet is not equipped to the appropriate slot. By default a wallet can be equipped to a new Wallet Slot that will appear in your inventory screen, however if Curios is installed that slot will not appear and you must instead equip the wallet into the Curios wallet slot.

If Curios is not installed, you can quick-equip your wallet by using the Wallet from your hotbar while crouching

Equipped wallets have the following abilities:
* Can be opened by pressing the key-bind noted in the Wallets Tooltip (`V` by default)
  * Tooltip may not show in older versions of LC
* Wallets with the "Coin Pickup" ability will automatically take any coin items you personally pick up and place them in your wallet instead of your inventory
* Money stored within equipped wallets can be used for any in-person monetary interaction such as purchasing or selling items from a trader.
* Coin Magnet enchantment can be added to all wallets capable of coin pickup, and will increase the range from which coins will be collected.

## Wallet UI
![](https://i.imgur.com/qezpuNE.png)

The wallet UI allows you to add/remove coins from your wallet.

Wallets with Coin Exchanging capabilities will have a button in the top-left allowing you to exchange the coins within the wallet for the highest value coin when pressed. Doing so will allow you to consolidate your coins to consume less space within the wallet.

Wallets with Coin Pickup capabilities will have another button below the exchange button that toggles the Auto-Exchange feature. When enabled (default) the contents of the wallet with automatically be exchanged for the highest coin value whenever a coin is automatically collected by the wallet (allowing you to collect large amounts of small coins without the wallet overflowing).

Wallets with Bank Account capabilities (Netherite Wallets by default) will have an ATM button in the top-left, allowing you to access the Wallet Bank Account UI.

## Wallet Bank Account UI
![](https://i.imgur.com/iPHWy37.png)

The Wallet Bank Account UI, allows you to deposit/withdraw coins to/from your currently selected bank account, and/or change which bank account is selected.

Behavior is the same as the ATM's [Account Management Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#account-management-tab) & [Account Selection Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#account-selection-tab), with the exception that and Admin only features will not work. View those tabs for more details on how Bank Account interactions work.

## Wallet Enchantments

### Coin Magnet
Coin magnet is an enchantment that increases the coin collection range of the wallet, the range of which increases with every level of the enchantment.
This enchantment can only be applied to wallets that are capable of collecting coins.
This enchantment can be put on a wallet by enchanting it in an enchanting table.

### Money Mending
Money mending isn't an enchantment that goes on the wallet itself, but instead goes on any damageable tool (same as the vanilla Mending enchantment).
When an item with Money Mending is equipped (main-hand, off-hand, or armor slots) it will be automatically repaired to full durability every second, at a defined money cost that will be taken from your equipped wallet.
The default repair cost is 1 copper coin per unit of durability, but this can be changed in the [server config](https://github.com/Lightman314/LightmansCurrency/wiki/Mod-Configuration#server-config).
If you have no wallet equipped in your wallet slot (or curios wallet slot), or your equipped wallet does not have enough money to repair the tool, no repairs will happen until your wallet is given some money.

## Wallet Configuration

What abilities each wallet type has can be configured in the wallet section of the [Server Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#server-config).