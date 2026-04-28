# FAQ

### My game is lagging when I'm around a lot of traders? How can I fix this?
Often time the game lags when you're in an area with a large amount of traders due to the large amount of display items being rendered. You can lower the amount that get rendered by editing the [Client Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#client-config) and changing the `itemTraderRenderLimit` option to a lower number, such as 1 or 0.

### How do I make a trader with infinite stock?
Traders with infinite stock (Creative Traders) can be made from any existing trader by activating [LC Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode) and accessing the traders settings. You'll find a yellow "C" button in the bottom corner of the [Trader Name Settings Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#trader-name-tab), and clicking that will thus make the trader creative. Note that while creative traders have infinite stock, they also don't store any money or items gained during the transactions, and they're purely intended to be used for admin shops (beginner equipment, buying resources from players to push money into the economy, etc.)

### You state that coins can be melted, but I can't figure out the recipe!
For balancing purposes, the melting of coins back into materials is disabled as having both coin minting & melting enabled by default allows the coins to take the part of a horribly balanced Equivalent Exchange process for basic materials. If you want the coin melting ability, you can go into the [Server Config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#server-config) and enable it.

### I can't do X because the buttons are off of the screen!
Unfortunately, some screens require a lot of vertical screen space. I've added the scrolling capabilities to the trader screens to help correct that, but not all screens can be easily shrunk down to fit in a pretty looking 256px tall area (namely some of the ATM screens). To fix this you will need to lower your GUI Scale (can be found in Minecraft's video settings). I've tested everything to fit on GUI Scale 3 on a 1080p monitor, and that should fix any screen size issues you encounter.

**Note**: These issues should be fixed for the ATM as of v1.1.5.3 or newer, fixed for the Wallet as of v1.1.6.0 or newer, and fixed for the trader as of v1.2.0.0.

### What if I don't want monsters to drop coins/so many coins?
All loot options can be found in the [common config](https://github.com/Lightman314/LightmansCurrency/wiki/Config-Files#common-config), although I can understand that it can be confusing. In the `loot.entities` section there is a simple option to disable all entity coin drops, but should you simply want to tweak the values to help balance your economy you can simply move an entity into a different level of loot table. For example, if you want zombies to drop less coins (only drop copper instead of the default of dropping iron & copper), you simply need to delete the "minecraft:zombie" entry from the "loot.entities.lists.T2" list and add it to the "loot.entities.lists.T1" list. You can also add modded entities to the loot tables simply by placing their entity id into the appropriate loot table list.

### I broke an Armor Display but the Armor Stand stuck around and cannot be broken!
This is sadly the side-effect of a bug, so if the Armor Display was built before v1.1.1.0, this will likely happen. Fortunately you can use the /kill @e[type=minecraft:armor_stand,distance=0..10] to kill all nearby armor stands to get them out of your hair. If this is a repeating issue, please update to v1.1.1.0 or newer and clear any old armor stands using the kill command before reporting this as an issue/bug.

### Please Make a Fabric Port!!!!!
The link to the fabric port can be found at the top of the Lightman's Currency mod description!