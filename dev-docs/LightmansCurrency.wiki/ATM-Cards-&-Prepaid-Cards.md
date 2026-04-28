As of LC v2.2.3.2, two new experimental features were added to Lightman's Currency to test out and make sure my API is properly handling items with the `IMoneyHandler` item capability.

## ATM Cards
![](https://i.imgur.com/PUz3xz9.png)

*Note: Recipe is disabled by default*

ATM Cards are an item that you can interact with to link with any Bank Account you have access too.

![](https://i.imgur.com/bGMk4jO.png)

The Menu opened by interacting with it pulls up a simple Bank Account Selection screen with a dummy slot in the top-left to view your selected ATM Card, and a large button at the bottom to lock the card. Once the card is locked it's bank account cannot be changed and the menu cannot be accessed except by a player in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode).

Which Bank Account the ATM Card is linked to, as well as said Bank Accounts current balance can be viewed in the items tooltip.

Once the ATM Card is linked to a bank account, it can be placed in the Money Slot of a Trader and used to pay for items/trades instead of the money in your wallet. If you lose an ATM Card and it's at risk of being used by someone not authorized to, you can reset the validation of the Bank Account to forcibly unlink all ATM Cards linked to that Bank Account from the ATM's [Balance Notification Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#balance-notification-tab).

## Prepaid Cards
Prepaid Cards behave similarly to an ATM Card, except they only have a finite amount of money stored within themselves and they will disappear once that money has been consumed.

Prepaid Cards can only be created using the `/lcadmin makePrepaidCard <players> <amount> [color]` where the `amount` argument is a valid [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments).

The amount of money left in the card can be seen from its tooltip in addition to a small reminder that it will disappear once the money is consumed.