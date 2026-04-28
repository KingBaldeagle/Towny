## What is a Trade Rule?
Trade rules are specially applied rules that can be applied to either a specific trade, or to the trader as a whole. They can either limit who is allowed to interact with the trade, or modify the trade's price. You can edit a trader or trades trade rule via the Trade Rule Tab on the Trader Storage Screen and the Trade Rule button on the Advance Trade Edit Tab.

Note: Not all Trade Rules will support all Trade Types. For example, discount/price modifying rules such as the Discount & Free Sample rule will not function or be a valid option for a Trade that is in Barter mode. Some Trade Rules can also only apply to a singular Trade and not to the Trader as a whole, such as the Demand-Based Pricing rule.

## Trade Rules on a Trader vs. Trade Rules on a Trade
When a trade rule is applied to a trader, it will apply to every trade that the trader has and/or will ever have. When a trade rule is applied to a trade, it will only be applied that specific trade, and can be used for more unique trades from the trader (such as limiting the trade so that players can only buy 1 beacon per customer while still allowing them to buy as much bread as they wish)

## Trade Rule Tab
![](https://i.imgur.com/kSAhVFy.png)

The trade rule tab, allows you to enable/disable trade rules for this trade/trader
* You can click on the check box to enable/disable the relevant Trade Rule
* The tabs along the right side allows you to edit that trade rule's specific settings
* The back button on the right side returns you to the [Advanced Trade Edit Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#advanced-trade-edit-tab) if you're editing Trade-Specific rules

## Trade Rule List
### Whitelist/Blacklist
![](https://i.imgur.com/eyLSWqv.png)

The Whitelist/Blacklist rule has two modes (Whitelist & Blacklist) whose behavior should be fairly self explanatory. When in Whitelist mode only players on the list are allowed to interact with the trade, and when in Blacklist mode any player not on the list can interact with the trade while those on the list cannot do so.

* Click the large "Mode:Whitelist/Blacklist" button at the top to change the trade rules mode
* Type a players name into the Text Box and click either the "Add" or "Remove" button to add/remove them from the discount list

Note: This Trade Rules icon will change depending on whether it's in Whitelist mode or Blacklist mode

### Player Trade Limit
![](https://i.imgur.com/Forq2gD.png)

The Player Trade Limit rule limits how many trades each user can interact with before they're locked out of that trade/trader.
* The top most input lets you define how many trades each player is allowed to do. Don't forget to hit the **Set** button to set the value.
* The **Clear Memory** button will make the trade rule forget the trade history, allowing players to once again interact with the trade/trader anew.
* The time input at the bottom allows you to define a duration after which the trader will ignore previous trade memory. Useful for if you want to limit a trade to 1 time per player per day.

### Trade Limit
![](https://i.imgur.com/UEJxv6h.png)

The Trade Limit rule behaves similarly to the Player Trade Limit rule, however it does not discriminate on player and once the defined amount of interactions have been done it will lock all players out of that trade/trader.
* The top most input lets you define how many trades each player is allowed to do. Don't forget to hit the **Set** button to set the value.
* The **Clear Memory** button will make the trade rule forget the trade history, allowing players to once again interact with the trade/trader anew.

### Discount
![](https://i.imgur.com/ryRLQha.png)

The Discount rule allows you to give a percentage based discount to friends that you add to the discount list. Note that when applied to purchases, the discount rule will instead increase the payout price by the given percentage instead
* The top most input lets you define the discount percentage. Don't forget to hit the **Set** button to set the value
* Type a players name into the Text Box and click either the "Add" or "Remove" button to add/remove them from the discount list

### Sale
![](https://i.imgur.com/nLBO4UW.png)

The Sale rule works similarly to the Discount rule, except that it gives a percentage based discount to all users for a limited period of time.
* The top most input lets you define the discount percentage. Don't forget to hit the **Set** button to set the value
* The time input at the bottom allows you to define the duration of the sale once activated
* Click on the "Start" button to start the sale. While active, it will display the time remaining above the button, and you can end the sale early by hitting the stop button

### Free Sample
![](https://i.imgur.com/zMOTmfe.png)

The Free Sample rule makes the first defined number interaction from each person free for any sale trade

* The top most input lets you define how many Free Samples each player can claim before the price returns to normal. Don't forget to hit the **Set** button to set the value
* Click on the "Reset" button to reset each players free samples, allowing them to claim them all anew
* The time input at the bottom allows you to define a duration after which the trader will ignore previous free sample memory. Useful for if you want to limit a trade to 1 free sample per player per day.

### Price Fluctuation
![](https://i.imgur.com/PY3FuGG.png)

The Price Fluctuation rule will randomly change the price by the given percentage amount more or less than the trades normal price once every given time duration.

* The top most input lets you define the fluctuation range as a percentage. Don't forget to hit the **Set** button to set the value
* The time input at the bottom allows you to define how often the price fluctuates

### Demand-Based Pricing
![](https://i.imgur.com/IB2i5lq.png)

The Demand-Based Pricing rule is probably the most complicated Trade Rule out there, and can only be applied directly to a trade. This rule effectively takes the two price inputs (the one built in to the trade itself, and one additional one that can be edited in the rule itself), and make the price change in a linear manner between these two price points based on the trades current stock count. If the stock count is above the Upper Stock Limit the price will always be the cheaper of the two prices, and if it's below the Lower Stock Limit the price will always be the more expensive of the two, and if it's somewhere in-between it will be somewhere in-between those two values.

* The price input is used to define the other price for this trade (can be the upper price or the lower price, it doesn't matter the rule will automatically determine which is which). Must be the same money type as the price defined in the trade itself, and cannot be the exact same as the other price, but Free is a supported input for one of the two prices.
* The Lower Stock and Upper Stock text fields allow you to change at what stock counts the price starts changing based on demand.

The rules status is displayed at the top informing you of any invalid inputs (prices are different types, stock range invalid, etc.) or when set up correctly giving you a summary of what the inputs will result in.