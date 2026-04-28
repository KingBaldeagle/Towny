## Recipes
![](https://i.imgur.com/UVMbM2i.png)

The ATM block is used to exchange coins for other coins of the same value, as well as accessing your bank account(s) to deposit or withdraw coins. The ATM also has an item version that can be interchanged at will in the crafting grid for easier access by using it from your hotbar, or (while Curios is installed) with a key-bind while equipped.

### Coin Exchange Tab
![](https://i.imgur.com/Bef6S0M.png)

To exchange coins, simply place the coins you wish to exchange in one of the ATM's 9 coin slots, and then click one of the exchange buttons above. The two large buttons at the top will convert all coins to the largest or lowest value coin respectively. Should the ATM run out of space in the coin slots the exchange will be stopped until space is cleared.

This page is fully configurable so that modpack makers can change the coin exchange options to match the coins that their mod has registered. Details on how to do so can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/ATM-Exchange-Option-Configuration).

### Account Selection Tab
![](https://i.imgur.com/nA3iP98.png)

This tab allows you to select which bank account you wish to interact with on the Account Management Tab. By default your currently selected account will appear on the list, and all other accounts you have access to will be listed below.

Note that the bank account you select here will remain selected until you either select another account, or lose access to a selected team bank account.

If you are in [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode), a Command Block button will appear in the top-right allowing you to access the Account Admin Selection where you can gain admin access to other players personal bank accounts by typing their name in the Text Box

#### Account Admin Selection
![](https://i.imgur.com/uz1rjke.png)

This version of the Account Selection Tab allows you to type in a players name, and thusly access their bank account on the other tabs as though you were them. After typing in the players name and clicking the button, an appropriate response will be printed below (error if there's no player with that name, or a success message if you select their account) once you receive the response from the server (and thus lag may cause delays in the response).

### Account Management Tab
![](https://i.imgur.com/YwBAiyh.png)

This tab allows you to deposit or withdraw coins from the selected bank account. The accounts current balance is displayed below the Deposit & Withdraw buttons. You can deposit coins by placing them in the 9 coin slots above your inventory and clicking the Deposit button, and you can also use the Money Value input at the top to define a specific amount of coins to deposit should should you wish to do so, although if no amount is defined it will simply deposit every coin in the coin slots. You can withdraw coins from the account by selecting the amount to Withdraw in the Money Value input at the top, and clicking the Withdraw button. If you ever attempt to deposit or withdraw more coins than available, it will automatically deposit or withdraw the maximum amount allowed.

_Friendly Reminder_: You can hold down the Shift key to increase the coin difference when pressing an arrow button on the Coin Value widget, and hold the control key to multiply that amount by 10x.

### Balance Notification Tab
![](https://i.imgur.com/0gvRXwO.png)

This tab allows you to select a balance level on which you wish to be notified of your bank accounts balance via a [Notification](https://github.com/Lightman314/LightmansCurrency/wiki/Notifications). It doesn't have much application for everyday use, but is intended as a tool to let you know you need to deposit more money into your bank account if you have a Trader or a Trader Interface linked to your bank account and it's used up all (or most) of its available funds.

If no value is defined, then you will not receive any notifications about your accounts balance.
If this is defined for a Team-Owned bank account, the notification will be sent all members of the team that are capable of accessing the bank account.
You may define a single notification limit per money type (i.e. get a notification when under 10n OR when there's less than 100CC in your bank account)

This tab also has a button allowing you to reset all [ATM Cards](https://github.com/Lightman314/LightmansCurrency/wiki/ATM-Cards-&-Prepaid-Cards) linked to this bank account, making all ATM Cards unusable until they're re-linked by a player who has access to this account.

### Account Log Tab
![](https://i.imgur.com/42q7UKo.png)

This tab allows you to view a list of your bank accounts interactions (Deposits, Withdraws, and Transfers). This should be very useful for keeping track of how much money your Traders have been earning, or to ensure that your Team Members aren't embezzling team funds, etc.

### Account Transfer Tab
![](https://i.imgur.com/N3wOa6r.png)

This tab allows you to transfer money from your currently selected bank account to another player or teams bank account. To transfer to a player, simply type their name in and click the "Transfer to Player" button after defining a Transfer Amount from the value widget above. To transfer to a Team, click the button in the top-right to change to "Team Selection" mode, and click the team you wish to transfer to from the list (if a team doesn't appear on the list, it does not have a bank account) and then click the "Transfer to Team" button after selecting a transfer amount from the value widget above.

## Wallets
Some wallets (netherite by default, but customizable in the config) are capable of automatically interfacing with your personal bank account, and will get their own variants of the Account Selection and Management tabs accessible via a button on the corner of the Wallet screen.

## Traders
Traders can be linked to their owners bank account in their [Settings](https://github.com/Lightman314/LightmansCurrency/wiki/Trading-Machines#trader-name-tab). While linked, all profits will be deposited directly into their bank account, and all payments for purchases will be withdrawn directly from their bank account. For safety, a traders bank account link will be reset whenever the owner is changed.