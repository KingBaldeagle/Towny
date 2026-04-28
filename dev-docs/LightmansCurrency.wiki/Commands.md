# Commands

Lightman's Currency adds a few commands to the game, and this page lists them all out for your convenience.

Notes: Arguments surrounded in <> are required, while arguments surround in [] are optional

### /lcbaltop
`/lcbaltop [page]` Lists the top bank account balances of all players on the server. Only displays 10 entries per page.

Can be run by all players

### /lctrade

Command used to initialize a [Player Trade](https://github.com/Lightman314/LightmansCurrency/wiki/Player-Trading)

`/lctrade <player>` request a trade with the given player

`/lctradeaccept <tradeID>` accept the player trade with the given trade id (typically auto-filled by clicking on the "Accept" button from the request message)

### /lcconfig

`/lcconfig reload` reloads all config files added by my mod (including the Master Coin List and other such configs)

If run by an Admin, all config files will be reloaded on the server

If run by a non-Admin, only their personal client config will be reloaded

`/lcconfig view <fileName> <configOption>` allows you to view the value of the given config option

`/lcconfig edit <fileName> <configOption> <newValue>` allows you to edit the value of the given config option

`/lcconfig edit <fileName> <listConfigOption> add <newValue>` allows you to add a new entry to the given config option

`/lcconfig edit <fileName> <listConfigOption> replace <index> <newValue>` allows you to replace the list entry at the given index with the new value

`/lcconfig edit <fileName> <listConfigOption> remove <index>` allows you to remove the list entry at the given index (starting at 0)

Note: The `newValue` inputs must be formatted exactly as expected by the config file including quotation marks, square brackets, etc.

`/lcconfig reset <fileName> <configOption>` allows you to reset the value of the given config option back to its default value

Note: All editing/resetting commands can only be run by Admins for Server & Common config files. All such commands run for Client config files will only edit the players local client configs.

### /lcadmin

`/lcadmin toggleadmin` Enables or Disables [Admin Mode](https://github.com/Lightman314/LightmansCurrency/wiki/Administrator-Features#admin-mode)

`/lcadmin traderdata list` Lists helpful info on all traders in existence, such as world position, owner, etc.

`/lcadmin traderdata search <searchText>` Similar to `/lcadmin traderdata list`, but it filters the traders based on the searchText (useful for finding all traders owned by a player, or with an inappropriate name, etc.)

`/lcadmin traderdata delete <traderID>` Allows you to forcibly delete a trader. Note that doing so will forfeit all items/money within the trader, so manually locating and breaking the traders block is preferred. You may use `/lcadmin traderdata list/search` to get a traders ID for deletion.

`/lcadmin traderdata debug <traderID>` Allows you to see a traders raw NBT tag for debug purposes. Also sends a debug packet to the client to be printed in the logs. Useful for testing any server-client de syncs with the trader data.

`/lcadmin traderdata addToWhitelist <traderID> <player>` Allows you to add the given player(s) to the traders [whitelist trade rule](https://github.com/Lightman314/LightmansCurrency/wiki/Trade-Rules#whitelistblacklist). Requires that the whitelist rule be enabled by the traders owner. TraderID field accepts persistent trader ids, and this command is mostly intended for modpack devs to grant players access to said persistent trader(s).

`/lcadmin traderdata recover <traderID>` Allows you to recover the "Pickup Item" of any trader block that was picked up and/or destroyed illegally. TraderID field will auto suggest any trader ids that are currently in a recoverable state.

`/lcadmin prepareForStructure <traderPos>` Allows you to save a trader blocks current trader state into the trader blocks NBT data. When used on a trader, it allows it to be safely copied to a different location via tools such as vanilla Structures, World Edit, and even NBT copying via middle click (only if used on a traders core block, which would be the bottom-left block for multi-block traders).

`/lcadmin taxes list` Same functionality as the `/lcadmin traderdata list` command, but specifically for Tax Collectors.

`/lcadmin taxes delete` Allows you to forcibly delete a Tax Collector. Just like with Traders, however, locating them and destroying the block manually is preferred.

`/lcadmin taxes openServerTax` Allows you to access the cross-dimensional Server Tax Collector. Once opened, you can edit the settings as you wish.

`/lcadmin taxes forceDisableTaxCollectors` Forces all Tax Collectors to be disabled, but does not delete or otherwise interfere with them. Mostly useful if you've decided to switch on the "Admin Only Activation" config option for tax collectors after players have already crafted and enabled Tax Collectors of their own.

`/lcadmin events <player> list` Lists all event flags that the player has unlocked

`/lcadmin events <player> unlock <event>` Adds the given event flag to the player so that they can use the given coin chain

`/lcadmin events <player> lock <event>` Removes the given event flag from the player so that they can no-longer use the given coin chain

`/lcadmin events <player> reward <item> <count>` Gives the given item to the player, same as the vanilla `/give` command. Used by my event advancements to give players their seasonal rewards, however the command no longer functions if the "advancement rewards" config option is disabled.

`/lcadmin makePrepaidCard <players> <value> [color]` Gives a [Prepaid Card](https://github.com/Lightman314/LightmansCurrency/wiki/ATM-Cards-&-Prepaid-Cards#prepaid-cards) to each player with the given stored money value & item color. If the color is not given, it will default to white. Value uses standard [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) inputs

### /tickets

`/tickets changeColor <color>` Changes the color of the ticket item currently held in your hand.

`/tickets create [color]` Crafts a new Master Ticket with the given color. If no color is given, it will default to the next color in the cycle.

### /lcbank

`/lcbank give <all|allPlayers|allTeams> <amount> [notifyPlayers]` Gives the given amount of money to all bank accounts (allPlayers and allTeams specifically target private player accounts & team bank accounts respectively)

`/lcbank give players <players> <amount> [notifyPlayers]` Gives the given amount of money to the given players personal bank accounts

`/lcbank give team <teamID> <amount> [notifyPlayers]` Gives the given amount of money to the given teams bank account. Team ID can be found at the bottom of the Teams [Name & Ownership](https://github.com/Lightman314/LightmansCurrency/wiki/Teams#name--ownership-tab) tab.

`/lcbank take <all|allPlayers|allTeams> <amount> [notifyPlayers]` Takes the given amount of money from all bank accounts (allPlayers and allTeams specifically target private player accounts & team bank accounts respectively)

`/lcbank take players <players> <amount> [notifyPlayers]` Takes the given amount of money from the given players personal bank accounts

`/lcbank take team <teamID> <amount> [notifyPlayers]` Takes the given amount of money from the given teams bank account. Team ID can be found at the bottom of the Teams [Name & Ownership](https://github.com/Lightman314/LightmansCurrency/wiki/Teams#name--ownership-tab) tab.

`/lcbank delete player online <player>` Clears the contents of the given players personal bank account. Also resets their currently selected account back to their personal bank account.

`/lcbank delete player offline <nameOrID>` Completely deletes the players personal bank account of the given offline player. Input can be either their name, or their UUID. Also resets their currently selected account.