# Trader Interfaces
![](https://i.imgur.com/4duYdaB.png)

A Trader Interface is a utility tool to allow the automation of Trader interactions from both the customer and owner sides. By default the only Trader Interface is the Item Trader Interface, but add-ons like LC Tech may add more for various new trader types.

The Trader Interface is linked to a singular [Network Trader](https://github.com/Lightman314/LightmansCurrency/wiki/Network-Traders) and automate an interaction with it based on it's selected **Mode**. The Trader Interface has 4 modes which do the following:

#### Trade Mode
This mode is the default mode. In this mode, you can link the Trader Interface to any Network Trader, and then further select a single trade from said trader to automate. The Trader Interface will then take money from its owners bank account and/or items from its storage to attempt to carry out the trade, and store any earned items into its storage (and any gained money into the owners bank account, etc.)

#### Restock Mode
This mode allows you to link the Trader Interface to any Network Trader that you have [permissions](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ally-permissions-tab) for and once linked the Trader Interface will take items from its storage and attempt to transfer them to the Traders item storage.

Note: It will only attempt to place items that the Trader is actively selling

#### Drain Mode
This mode allows you to link the Trader Interface to any Network Trader that you have [permissions](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ally-permissions-tab) for and once linked the Trader Interface will take try to take items from the Traders storage and place them into its own storage.

Note: It will only attempt to take items that the Trader is not actively selling, so this mode is mainly useful for collecting items you're purchasing from your customers.

#### Restock & Drain Mode
This mode is a combination of both the Restock Mode and the Drain Mode noted above. As also noted above, the Drain Mode doesn't attempt to drain items that are being sold by the Trader, therefore these two modes will not conflict.

### Status Tab
![](https://i.imgur.com/XQKXp7w.png)

The default tab of the Trader Interface gives an overview of it's current status, and allows you to change it's current **Mode**.

At the top, it displays the name of the trader interface, and then directly below that the name of the Trader that it's currently linked to.

If in **Trade Mode**, the linked Trade will be displayed beneath. If the linked trade has been changed in any way, a list of warnings and notes will be listed below as well as a separate instance of the newer version of the trade, as well as a button off to the side allowing you accept the trades changes and clearing the warnings.

Another important thing of note is the "Active Mode" toggle button in the top-right corner. The Trader Interface has 4 "active states" that clicking this button will toggle through:
* **Disabled**: Trader Interface is disabled and will not do anything. Default state allowing you to set up your desired settings without accidentally doing something unwanted.
* **No Redstone Signal**: Trader Interface will be active while **NO** redstone signal is being received by the block.
* **Redstone Signal**: Trader Interface will be active while a redstone signal is being received by the block.
* **Always Active**: Trader Interface will be active regardless of any redstone signals being received.

There is also another button directly below the "Active Mode" toggle button that toggles whether the Trader Interface will be active if the owner is offline. Note: If the trader is owned by a team, then any member of said team being on the server will allow the Trader Interface to be active when requiring an online owner.

Both of these side buttons are visible from every tab so you can easily and quickly disable/enable the Trader Interface from anywhere in its menu.

### Storage Tab
![](https://i.imgur.com/57oRigT.png)

The Storage Tab is a smaller version of a normal traders [Storage Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#trader-storage-tab), and their [Input/Output Settings](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#external-inputoutput-tab) tab, and functions the exact same as them all.

Just like a normal Item Trader, the Trader Interfaces storage only allows relevant items to be placed within, so while in **Restock Mode** it'll only allow items sold by the linked Trader, and in **Trade Mode** it'll only allow items being purchased/bartered by the linked trade, etc.

This tab is also where you access the Upgrade Slots for the Trader Interface. See the [Machine Upgrades](https://github.com/Lightman314/LightmansCurrency/wiki/Machine-Upgrades) page for full details on what upgrades can be equipped to a Trader Interface.

### Trader Select Tab
![](https://i.imgur.com/ZPiXX1n.png)

This tab is where you select which Network Trader you wish to link this Trader Interface to. Which traders show up on the list will vary depending on which **Mode** the Trader Interface is in (all traders if in Trade Mode, but only your traders if in Restock/Drain Mode).

### Trade Select Tab
![](https://i.imgur.com/q96oEe6.png)

This tab is where you select with Trade to specifically interact with if your Trader Interface is in **Trade Mode**. The selected trade will be grayed out to show that it is already selected.

### Ownership Tab
![](https://i.imgur.com/hOjldm7.png)

The Ownership Tab allows you to change the Trader Interfaces owner. The inputs behave exactly like those of a Traders [Ownership Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#ownership-tab).

### Statistics Tab
![](https://i.imgur.com/E6J5uS2.png)

The Trader Interface will collect statistics such as how money was paid/earned, how many trade interactions happened, etc. and they can all be viewed from this tab. This tab will not have any particularly helpful info if the Trader Interface is in Restock/Drain Mode.

### Additional Notes:
* By default the Trader Interface only attempts an interaction once every 20 ticks (or 1 second). You may lower this interaction delay by equipping a [Speed Upgrade](https://github.com/Lightman314/LightmansCurrency/wiki/Machine-Upgrades#speed-upgrade) to it's upgrade slots from the Storage Tab