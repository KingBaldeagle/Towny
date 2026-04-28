# Master Coin List Config
In v1.0.5.0, the Coin Value config options were removed, and instead coins are now registered in the generated MasterCoinList.json file that can be found in the `config/lightmanscurrency/` folder.

As of version 2.2.0.0, the format of the Master Coin List has been changed. Go [here](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config) to see the new page on the subject.

## Breakdown
The base json file has a "CoinEntries" list tag with several coin data entries. Each Coin Data entry has the following inputs:

`coinitem`: The string Item ID (e.g. `lightmanscurrency:coin_copper`) of the coin. Only one coin can be registered for each item. MANDATORY

`chain`: The chain id of this coin. The "main" chain is the chain that will be used for displaying coin values, and giving change. MANDATORY

`worth`: Sub-tag with the coin ID and count of how many of another coin this coin is worth. (e.g. `"worth":{"coin":"lightmanscurrency:coin_copper","count":4`}). If this is missing, this coins value will be defined as 1 (copper coins by default).

`initial`: Translation key for this coins initial. Only required for coins in the "main" chain, that are also not hidden.

`hidden`: true/false flag to make the coin hidden. If a coin is flagged as hidden, other coins in the chain cannot exchange upwards to this coin, and this coin will not be automatically collected by wallets on pickup.

## Important Warnings
* While side-chains are allowed, and will be exchangeable within their chain, each coin can only have 1 downward exchange, and 1 upward exchange within their chain (hidden coins not counted as a valid upward exchange).
* The "main" chain MUST have a coin with no `worth` value