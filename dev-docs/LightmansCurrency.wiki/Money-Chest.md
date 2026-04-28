# Money Chest
![](https://i.imgur.com/uyeRrGr.png)

The Money Chest is a special type of chest that is specifically designed to store coins. By itself it doesn't do much special, and like most chests it can have items inserted or removed from it by hoppers and other modded forms of item transportation.

### Money Chest Menu
![](https://i.imgur.com/G7yZasd.png)

The Money Chest has a standard 27 slot item storage that can hold any registered coins. In addition it has 3 Upgrade Slots in the top-right that can take any upgrades compatible with the Money Chest. Most of these upgrades will add more tabs to the left edge of the screen allowing you to configure them, and you can also click the toggle switches next to the slot to disable the upgrade entirely.

## Money Chest Upgrades

### Coin Exchange Upgrade
![](https://i.imgur.com/Aiz2bQh.png)

![](https://i.imgur.com/wC0Y6NZ.png)

The Coin Exchange Upgrade allows the Money Chest to exchange the coins inside the chest automatically. The tab for this upgrades settings will display a similar screen as shown on the ATM's [Coin Exchange Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#coin-exchange-tab), however when a button is clicked it will become "selected" and that exchange will then automatically be triggered whenever an item is inserted or removed from the chest.

The button at the bottom toggles whether the Coin Exchange is allowed to happen when a player is interacting with the chest. Set it to "Block When Open" if you don't want items being moved around while you're interacting with the chest.

### Coin Magnet Upgrade
![](https://i.imgur.com/5KcOcDL.png)![](https://i.imgur.com/tHn9MXC.png)

![](https://i.imgur.com/fhqdHLu.png)![](https://i.imgur.com/vqxk0fl.png)

The Coin Magnet Upgrade has 4 levels with increasing range. While equipped and active, the Money Chest will automatically collect coins within the given radius around the chest.

### Bank Upgrade
![](https://i.imgur.com/mGvE36e.png)

![](https://i.imgur.com/hbH3IE8.png)

The Bank Upgrade allows the Money Chest to automatically deposit or withdraw money from a defined bank account. This upgrade adds two new tabs to the chest, one strictly for selecting the bank account, and another for more advanced settings.

![](https://i.imgur.com/crSAPe6.png)

The large button at the top of this tab allows you to toggle the upgrades mode from Deposit mode to Withdraw mode, etc.

The Money Value input defines the target value. This value is used differently depending on the mode.

While in Deposit Mode, the upgrade will deposit all contents of the chest once the stored contents hit the target value
* If no target value is defined, it will simply deposit all contents regardless of their type or value
* If a target value is defined, it will only deposit coins of the same value type as the target, and it will not attempt a deposit until the requisite amount of money is contained within the chest

While in Withdraw Mode, the upgrade with withdraw money from the bank account until the chest contains coins of the defined value
* If no target value is defined, no money will be withdrawn

In either mode, if the withdraw/deposit interaction results in the chest containing more items than it can hold, the overflow items will be stored within the upgrade and the upgrade will cease to function until said overflow items are removed (a chest button will appear in the top-right corner what you can click to collect the overflow items into your inventory).

### Security Upgrade
![](https://i.imgur.com/Ugbx4oG.png)

![](https://i.imgur.com/uF2mmqI.png)

The Security Upgrade allows you to limit who can access the chest to either only yourself, or members of your team. The tab for this upgrade is a simple [Ownership Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ownership-tab) tab similar to the one on various other machines.

The main things of note are as follows:
* By default, the upgrade will NOT have a defined owner. You will need to select yourself as the owner manually for the protection to start taking effect.
* The Security Upgrade does not in any way prevent or protect the chest from having items removed via item automation (hoppers, etc.)
* Only the Team Admins can access the Security Upgrade's tab when a Money Chest is owned by a team, however they can still remove the upgrade from the upgrade slot
* The Security Upgrade cannot be disabled (may not apply to older LC versions)