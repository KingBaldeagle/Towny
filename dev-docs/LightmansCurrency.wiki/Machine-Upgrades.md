# Machine Upgrades

Many machines have Upgrade Slots that support various upgrade items. The most relevant of these are [Traders](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines) and [Trader Interfaces](https://github.com/Lightman314/LightmansCurrency/wiki/Trader-Interfaces). Some Upgrades can have multiple variants of the same upgrade applied to the same machine, whereas others are considered **Unique** and only one copy of said upgrade can be applied to each machine.

### Upgrade Template
![](https://i.imgur.com/jIg6ogT.png)![](https://i.imgur.com/rpBZBRB.png)

As my mods upgrades are crafted in a vanilla Smithing Table, as of MC v1.20 all upgrade recipes require a Smithing Template to craft. Unlike most Smithing Templates, however, the Smithing Template required for upgrade crafting can be crafted from a Trading Core instead of having to be found in a chest out in the wilderness before being copied by a different recipe for cheap.

### Item Capacity Upgrade
![](https://i.imgur.com/6VOGIvz.png)![](https://i.imgur.com/TKUQxIW.png)

![](https://i.imgur.com/WUOCOhm.png)![](https://i.imgur.com/od6USJ2.png)

Valid Machines:
* Item Traders
* Slot Machines
* Item Trader Interface

The Item Capacity Upgrade comes in 4 tiers, each increasing the maximum stack size of the traders Item Storage by the given amount.

### Trade Offer Upgrade
![](https://i.imgur.com/dyPisCt.png)![](https://i.imgur.com/WPEPVLS.png)![](https://i.imgur.com/aaAxSL3.png)

![](https://i.imgur.com/fpeeAwE.png)![](https://i.imgur.com/VCxVpRo.png)![](https://i.imgur.com/dUMoN2Z.png)

Valid Machines:
* Item Traders
* Fluid Traders (LC Tech)

**Unique**

The Trade Offer Upgrades increase the Trade Offer count of the given trader. Does not function on traders that have flexible Trade Offer counts already such as the [Paygate](https://github.com/Lightman314/LightmansCurrency/wiki/Paygate) and LC Techs Energy Traders, or traders that are strictly limited to a single trade such as the [Slot Machine](https://github.com/Lightman314/LightmansCurrency/wiki/Slot-Machine).

### Network Upgrade
![](https://i.imgur.com/6MQ41S8.png)

Valid Machines:
* Most Non-[Network Traders](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders)

**Unique**

The Network Upgrade can be applied to any Trader that is not already capable of being viewed on a Trading Terminal and making it accessible from said terminal just like any other [Network Trader](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders).

Note: Due to its direct world interaction, the [Paygate](https://github.com/Lightman314/LightmansCurrency/wiki/Paygate) cannot ever be turned into a Network Trader, as it requires the world to be loaded at its physical location in order to function.

### Speed Upgrade
![](https://i.imgur.com/bex4rxY.png)![](https://i.imgur.com/3BdiFnW.png)![](https://i.imgur.com/Xop51z7.png)
![](https://i.imgur.com/VJ9ba5E.png)![](https://i.imgur.com/FbIw8Ct.png)

**Unique**

Valid Machines:
* All [Trader Interfaces](https://github.com/Lightman314/LightmansCurrency/wiki/Trader-Interfaces)

The Speed Upgrade decreases the delay of the Trader Interfaces interactions. The Netherite version of the upgrade completely eliminates the delay making the Trader Interface interact with its trader every single tick.

### Hopper Upgrade
![](https://i.imgur.com/j5fqNAs.png)

**Unique**

Valid Machines:
* All [Trader Interfaces](https://github.com/Lightman314/LightmansCurrency/wiki/Trader-Interfaces)

The Hopper Upgrade allows the Trader Interface to attempt to collect or push its storage contents to neighboring blocks. For example an Item Trader Interface will automatically collect any items that it's giving to its linked trader from a chest (or other valid item handler) directly next to one of its input sides. Using the same example, it would also export any items that it's getting from the trader would be to any chests (or other valid item handler) directly next to one of its output sides.

By default the hopper-style interactions happen immediately following the trader interfaces defined interaction, and follows the same timer as said interactions.

### Money Chest Upgrades
Due to their complex nature and customizable settings, Money Chest Upgrades and how they function are detailed on the [Money Chest](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Chest#money-chest-upgrades) page.