# Player Trading
Using the `/lctrade <player>` command, you can request an in-person trade with another player allowing you to trade with other players without the use of a trader. Once the `/lctrade` command is run, the player you requested a trade with will get a message in chat with a piece of text that will run the matching `/lctradeaccept <tradeID>` command required to accept your trade request. Once the trade is accepted, the Player Trade Menu will open:

![](https://i.imgur.com/9hawD6A.png)

In this menu, you can place the items you wish to give to the other player in the item slots on the left side of the menu, and even define an amount of money to give them from your wallet with the Money Value input above (the money to be given will be displayed below the players name.

Once you are content with the trade as is, both players must click the "Propose" button. The menu will note that the trade is accepted, the arrow will turn a light blue. Once both players have accepted, they must both now click the "Accept" button to finalize the trade (which will turn their arrow green). Once both players have accepted the trade, the menu will close and the items and/or money will be transferred to the other player.

If the Items or Money amount of either player is changed after the trade is Proposed/Accepted it will reset both players states to the default state (gray arrow) and they will have to go through the process of Proposing/Accepting the trade again.

![](https://i.imgur.com/DZqzl8V.png)

While in this menu, players can access a private chat screen to discuss the terms of their trade by clicking the book button on the top-right of the menu, and clicking that button again will return you to the trade screen.

**Note**: While this chat room is private, it's messages are still logged in the server logs like any other chat messages, so don't go talking about things that can get you banned in this private chat room :)


## Player Trading Configuration
In the Server Config there are options to force players to be in the same dimension and/or define a maximum distance between players that is allowed for a Player Trade to be carried out.