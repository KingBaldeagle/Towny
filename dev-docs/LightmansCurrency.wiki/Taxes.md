**NOTE** This page is still a work in progress!

## The Tax Collector
![](https://i.imgur.com/umSdfrE.png)

 The Tax Collector is the only method of applying any form of Taxes on the economy. It does so by applying a percentage-based sales tax on all [Trading Machines](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines) within a defined area. Admins may create/access a Server-wide Tax Collector via the `/lcadmin taxes openServerTax` command.

Like most ownable machines, the Tax Collector is mine-protected, and can only be broken by the owner (or team admin).

**NOTE**: Crafting recipe can be disabled in the [Common Config](https://github.com/Lightman314/LightmansCurrency/wiki/Mod-Configuration#crafting) if you don't want these to be accessible to every player (although there are safety measures to prevent players from taxing areas they aren't allowed to).


## Tax Collector Setup
![](https://i.imgur.com/4tilPIT.png)

When a Tax Collector is placed it will start displaying the Area around it to its current owner, and it will not yet be activated. If the owner right-clicks the Tax Collector block it will open the Tax Collectors settings menu.

Notes about the Area display:
* Color changes depending on the Tax Collectors active status (red for inactive, green for active).
* Non-owners of the Tax Collector will see the area in yellow if active (and cannot see the area at all if it's inactive)
* Players in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode) will always see the area in the same colors the owner would see.

### Main Settings Tab
![](https://i.imgur.com/9DIDWLq.png)

This tab gives you access to most of the Tax Collectors important settings.

You can collect the Tax Collectors stored money by clicking the "Collect Money" button in the top-right of the menu. Note: This button is visible from all tabs, and will be hidden if the Tax Collector is linked to a bank account.

The "Active" text in the top-left has a different color depending on if it is currently active (red for inactive, green for active). Click the checkbox to its left to Activate the Tax Collector. Admins can make it so that only players in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode) can activate Tax Collectors, so if the checkbox is not there contact your server admins to request permission.

Just below that is the Tax Rate setting. Press the +/- button to increase/decrease the Tax Rate by 1% (hold shift to change by 10%). This will determine what percentage of money you will collect.

To the right of both of those is the "Area Visible" selection. This determines who will be show the transparent Area outline. The options are as follows
* `None`: Nobody will see the outline (still visible to admins)
* `Members`: Only the owner (or members of the team if the Tax Collector is owned by a Team) will see the outline
* `All`: All players will see the outline.

Below the Tax Rate setting is a checkbox labeled "Link to Bank Account". Checking this will result in the Tax Collector depositing all collected money into the Owners bank account instead of storing the money in the Tax Collector itself.

The large Text Box is where you can define a custom name for the Tax Collector. Simply type in the name you wish it to be displayed as and click the "Change Name" button and your tax collector will be displayed by that name for log and info purposes.

Below the text box are 3 sets of +/- buttons with appropriate labels. These are what allow you to customize the Tax Collectors Area. The area has 3 settings you can change.
* Radius: The radius is how many meters north/south/east/west of the central Tax Collector block that will be taxed.
* Height: How tall the taxed area is.
* Y-Offset: How far above/below the taxed area the bottom is. A value of 0 will not tax any blocks below it.

### Logs Tab
![](https://i.imgur.com/EQfLb5r.png)

Like most machines in my mod, the Tax Collector keeps a log of its interactions. You can view them from this tab.

### Statistics Tab
![](https://i.imgur.com/zQt5wJW.png)

This tab allows you to view some more detailed info about the Tax Collector and what it has done. At present it displays the following:

* The total amount of money it has collected as taxes.
* The total number of unique machines it has collected taxes from.
* The most taxed machine (note: this data is slightly buggy at the moment, will hopefully be fixed in a future update)

You can clear the data by clicking the "Clear Stats" button in the top-right of the menu.

### Ownership Tab
[](https://i.imgur.com/nFrfBJg.png)

This tab allows the you to change the Tax Collectors owner. Behaves exactly like the Traders [Ownership Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ownership-tab)

### Admin Options
![](https://i.imgur.com/K44mwDn.png)

This tab only appears to players who are in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), and contains the following options.

* `Force Acceptance`: When enabled, all machines within the area will be taxed regardless of whether they were placed before the Tax Collector was. Does not prevent the Traders "Acceptable Tax Rate" setting from locking the trader.
* `Infinite Range`: When enabled this Tax Collector will attempt to tax all traders within that dimension.

## Taxes on Traders

Traders have their own settings and behaviour around Tax Collectors. See their pages about their respective [Tax Information Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#tax-information-tab) and [Tax Settings Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#tax-settings-tab).

In addition to the info listed there, whenever a Trader is placed within a taxable area it will send a message to the player in chat informing them that taxes may apply.

## Additional Notes:
* The Area of a Tax Collector is always centered around the Tax Collector block, so make sure you place your Tax Collector in the approximate center of the area you wish to tax (you may take advantage of the vertical offset to hide it underground so that only staff can see the block).
* The taxes collected will always be rounded down (e.g. A 1% sales tax applied to a 99 copper coin purchase will round down to 0 money collected as 99/100 < 1)
* Only monetary exchanges will be taxed. If a player wishes to avoid sales tax from a Tax Collector, they **can** use barter trades to avoid being taxed, and this will remain a possibility until the end of time.

## Tax Collector Configuration
The Tax Collector has various config options to limit the use of Tax Collectors in the [Server Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#server-config) such as setting a maximum tax rate and making it so that only admins can activate a Tax Collector.