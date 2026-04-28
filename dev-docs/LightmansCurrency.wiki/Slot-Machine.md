# The Slot Machine
![](https://i.imgur.com/CKVSd0I.png)

Slot Machines are a somewhat major offshoot of the normal Item Traders, however instead of customers buying a pre-determined item, they allow the owner to set up weighted loot entries from which the customer will receive a single randomly selected entry from the list.

## The Trading Screen
### Interact Mode
![](https://i.imgur.com/Z1BqpXA.png)

The Trading Screen for Slot Machines is a lot different from the normal screen (although it does have a fallback trade button should a Slot Machine be linked to a Cash Register). The last reward obtained will show in the center slot machine slots, while random possible items will be slightly visible above and below.

To interact with the Slot Machine you may hit any of the 3 "Try your luck" buttons to the left of the coin slots. The right-most one will roll once, while the other two will roll 5 and 10 times respectively. The price of each roll will be displayed in the tooltip while hovering any of these buttons. (If you cannot afford to roll 5/10 times, it will roll as many times as you can afford to, and if the Slot Machine runs out of stock midway through it will also cancel the multi-roll prematurely).

You can access Info Mode by clicking on the white [i] icon in the top-right of the menu. Clicking on it again will return to the Interaction Mode.

### Info Mode
![](https://i.imgur.com/FInMqRo.png)

Info Mode shows the available results/winnings you can get from the Slot Machine as well as your approximate chances of obtaining them (only shows decimal points if the chance is less than 1%, only shows the first 2 decimal points, etc.)

## The Storage Screen
The general layout of the Slot Machine's storage screen works the same as any other trader. See the original [Trader Storage Screen](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#the-trader-storage-screen) section for details on any tab not detailed here.

Note: Due to the single-trade nature of the Slot Machine, you cannot access or modify any trade-specific Trade Rules, as the Traders Trade Rules will be more than sufficient.

### Entry Edit Tab
![](https://i.imgur.com/mj5h84y.png)

This tab allows you to edit the entries and their rewards. By default only 1 entry will appear here, but you can add more by clicking the + button in the top-right corner. You may remove an entry you no longer want by clicking the - button to the left of the entries name ("Entry #1", etc.).

Each entry will have a defined weight that can be set to any number from 1 <--> 1000 by typing it into the text box to the right of the "Weight:" label. An entries odds of being selected will be its personal weight/the sum of all entries weights. (e.g. an entry with a weight of 5 in a slot machine with a total weight of 50 will be 5/50 or 10%).

Below the Weight input is the item input slots. You can place up to 4 stacks of items in these slots (either 4 different items, or 4 stacks of the same item), and these will define the items to be given to the customer when that entry is randomly selected.

Note: If all items defined in the item input slots for an entry are coins, the trader will instead give money to the player from its stored money when that entry is randomly selected, instead of trying to remove coin items from storage. Because of this, the Slot Machine will be considered "Out Of Stock" if it doesn't have enough money in storage to pay out these rewards, nor will it allow coins to be placed into its item storage.

### Price Edit Tab
![](https://i.imgur.com/Ng9YOMn.png)

This tab allows you to define the price that the customer will need to pay for each interaction.

### Trader Storage Tab
The Trader Storage of a Slot Machine behaves the same as that of an Item Trader. See original [Trader Storage Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#trader-storage-tab) page for details.

Note: Just like Item Traders, Slot Machines will only allow items to be placed in storage if one of its entries has that item defined as a reward.
Slot Machines can accept both Network Upgrades and Item Capacity Upgrades to increase its storage capacity.