Lightman's Currency adds two new game rules that allow for easy customization & modification of certain aspects of the game.

### keepWallet
* Default Value: false
* Type: boolean (true/false)

The `keepWallet` Game Rule determines whether the player will drop their wallet upon death, or keep it in their inventory. If `keepInventory` is true, `keepWallet` will be ignored in favor of the `keepInventory` rule.

### coinDropPercent
* Default Value: 0
* Type: integer
* Range: 0 -> 100

The `coinDropPercent` Game Rule determines what percentage of the players wallet contents will be dropped upon death. If both `keepWallet` & `keepInventory` are false, this value will be ignored as the entire wallet is being dropped regardless. A value of 100 will drop the entirety of the wallets contents, and a value of 0 (default) will not drop any coins whatsoever.
