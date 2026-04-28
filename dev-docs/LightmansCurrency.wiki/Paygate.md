## The Paygate
![](https://i.imgur.com/tGU3Gyt.png)

The Paygate is a unique trading machine in that it doesn't give an item in return for the payment, but it instead activates a redstone signal of the duration defined by the trade. It's also is unique in that it allows the owner to link a master ticket to the trade, and let customers use a matching ticket (or pass) to activate it instead of money. The redstone signal it outputs behaves the same as a redstone block, and redstone will automatically connect to it. While it's outputting a redstone signal, another trade cannot be triggered.

If a player is holding a valid ticket when right-clicking on the paygate, the paygate will automatically trigger a trade using that ticket as payment instead of opening the UI, allowing customers to quickly interact with the paygate.

If a player has a valid Pass when triggering a ticket trade, the trade will be activated but the pass **will not** be consumed allowing a customer to utilize a ticket trade multiple times without having to buy a new ticket every time.

### The Trading Screen
The trading screen works the same as any other trader. See the original [Trading Screen](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#the-trading-screen) section for details.

One difference of note, is that there will be an additional button above the "Collect Money" button on both the Trading Screen and the Trader Storage Screen that allows you to collect any stored ticket stubs.

### The Paygate Storage Screen
The general layout of the Paygate's storage screen works the same as any other trader. See the original [Trader Storage Screen](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#the-trader-storage-screen) section for details.

### The Basic Trade Edit Tab
![](https://i.imgur.com/rDdDpbR.png)

This tab allows you to view your trades, and make the most basic of edits. If you click on the price slot (left of the arrow) with an empty hand you will open the [Advanced Trade Edit Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Paygate-&-Tickets#advanced-trade-edit-tab) for this trade. Clicking on the duration slot will do the same. If you click on the price slot with a valid **Master Ticket**, it will switch the trade to ticket mode, making the trade accept any tickets with said master tickets ID as payment instead of money.

You can take advantage of the +/- buttons in the top-right corner of the screen to add up to 4 trades to the Paygate, allowing you mix & match trade payments and output durations to let players spend a different amount of money for a different duration of redstone, pay with multiple ticket ID's, or give them the option to pay with either a ticket or money.


### Advanced Trade Edit Tab
![](https://i.imgur.com/tr1NLdE.png)

This tab allows you to change the price of the trade (if not in ticket mode), and/or change the duration of the Redstone output (in ticks). Note that if the trade is in ticket mode, you cannot set the price as it will only accept tickets, but clicking on the input slot with an empty hand while on this tab will clear the ticket and revert the trade to once again require money. Like the Basic Trade Edit Tab, you can also pair a ticket by clicking on the input slot with a valid Master Ticket.

![](https://i.imgur.com/DBOrkqL.png)

If the trade is in ticket mode, an additional option will appear below the Duration text, allowing you to toggle its ticket stub handling. Your two options are to either store the Ticket Stubs in the Paygate (to be collected in the same manner that you would collect the stored money), or to return the ticket stub to the customers inventory.

Note: The Paygate limits the redstone duration to be no longer than 1 minute (1200 ticks) by default, but this can be changed in the server config.