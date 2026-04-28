## Ticket Machine
![](https://i.imgur.com/mOw7IDw.png)

The ticket machine is used to craft Master Tickets, normal Tickets, and Passes.

### Crafting Master Tickets
![](https://i.imgur.com/nn7obQX.png)

You can craft a master ticket by putting any valid ticket material in the middle-left slot and clicking on the arrow. This master ticket will be unique to all other tickets, and you can view the master ticket's id by holding Shift while hovering over it. Master tickets can only be used to pair paygate(s) to their ticket id, or to craft matching tickets to be used as payment.

You can customize the color of the resulting Master Ticket by placing any dye in the left slot before the Master Ticket is crafted. Once a Master Ticket has been crafted its color cannot be changed, and all resulting Tickets & Passes crafted from this Master Ticket will also be the same color.

### Crafting Tickets & Passes
![](https://i.imgur.com/4YvOYVE.png)

Once you've crafted a master ticket, you can place it in the far-left slot and then tickets crafted by the ticket machine will have matching IDs to the master ticket. Tickets crafted this way will be usable with any paygate that's been paired to the master ticket used to craft them.

Note: Tickets & Passes have the same recipe, however a preview of the resulting item is always shown in the phantom slot in the top-right corner of the menu. Using the scroll wheel while hovering over that slot will change the item you will craft, and it will be noted in the tooltip that there are multiple possible results and that you can use the scroll wheel to switch recipe targets.

### Ticket Crafting Materials
There are 2 types of tickets you can make, Paper Tickets, and Golden Tickets. Both are functionally the same, however one is crafted from paper, while the other from gold.

Note: New Tickets can also be crafted by recycling old & unwanted Master Tickets, Tickets, Passes, and/or Ticket Stubs of the same variety.

All machines that directly handle & consume tickets (only the [Paygate](https://github.com/Lightman314/LightmansCurrency/wiki/Paygate) at this point) will create a Ticket Stub in its place allowing for all tickets to be recycled after use.

## Ticket Kiosk
![](https://i.imgur.com/Q52zpmA.png)

The ticket kiosk is a unique trader that can handle 4 trade slots, but can only sell or purchase ticket-related items. However it has the advantage of having a built-in ticket machine, such that you can define a ticket sale with a master ticket and it will be able to print and sell child tickets so long as ticket crafting materials are in stock. It's also unique in that when it prints a ticket, said ticket will be named to the name defined in the Display Name field in the Price Screen.

## Ticket Commands

Two ticket-related commands are available for convenience:

* `/tickets create <color>` will create a new Master Ticket with the given color
* `/tickets changeColor <color>` will change the color of any ticket held in the players hand to the given color

Color arguments above can be either a color name (YELLOW,RED, etc.) or a hex code (0xFFFFFF, etc.)

## Ticket Recipes

The Ticket Station can have customized recipes loaded from a data pack. See the [Ticket Station Recipe Data](https://github.com/Lightman314/LightmansCurrency/wiki/Ticket-Station-Recipe-Data) page for details on how these recipes are formatted if you wish to add your own.