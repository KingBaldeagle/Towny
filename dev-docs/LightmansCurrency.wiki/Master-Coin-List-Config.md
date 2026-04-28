# Master Coin List Config
The Coin List config file allows you to fully customize what items are considered coins, as well as their relative value. This is done by editing the `MasterCoinList.json` file that can be found in the `config/lightmanscurrency/` folder.

**IMPORTANT NOTE**: In the Forge v2.2 update, the format of this config file has been changed. To view the format of the config in older forge versions (or the latest fabric versions), go [here](https://github.com/Lightman314/LightmansCurrency/wiki/Coin-List-Config-(Pre%E2%80%902.2)).

## Breakdown
The base Json file has a `Chains` array, containing an array of [Chain Entries](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config#chain-entries).

### Chain Entries
Each chain entry contains the following inputs:

`chain`: A string Chain ID of this coin chain. Required, and must be unique between entries. At least 1 entry must use the "main" chain id.

`name`: The display name of the chain. REQUIRED. Can be a simple string, or use minecraft text formatting.

`displayType`: The id of the coin chains display type. REQUIRED. More details can be found [here](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config#display-types). Depending on the type input, more inputs may be required.

`InputType`: The input type of the coin chain. REQUIRED. Allowed values are "DEFAULT" and "TEXT".

`EventChain`: Whether this chain is locked behind an event. OPTIONAL. If set to true, players will not be able to set prices from this coin chain unless unlocked by the `/lcadmin events` command.

`CoreChain`: A Json array with the main list of [Coin Entries](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config#coin-entry). MANDATORY. Must contain at least one valid entry! First entry should not include an `exchangeRate` entry.

`SideChains`: A Json array containing more json arrays of [Coin Entries](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config#coin-entry). OPTIONAL. First entry of each side-chain must contain a `ParentCoin` entry with the same id as a Coin Entry from the `CoreChain` array.

`ATMData`: A Json object containing all of the [ATM Data](https://github.com/Lightman314/LightmansCurrency/wiki/ATM-Exchange-Option-Configuration). OPTIONAL. If not present, no ATM dropdown will be available for this coin chain.

### Coin Entry
Each Coin Entry contains the following inputs:

`Coin`: The item ID of the coin. REQUIRED.

`exchangeRate`: How many of the previous coin must be exchanged into this coin. i.e. How much of the smaller coin this coin is worth. REQUIRED except for the first entry of the `CoreChain`.

`ParentCoin` The item ID of the parent coin. ONLY used by the first entry of a `SideChain`.


### Display Types
Below is a list of the included display types, and the required & optional inputs they add (and where they add them).

### Coin Display
`"displayType": "lightmanscurrency:coin"`

* Displays the coin value as a sum of the coin initials. e.g. 5 Netherite and 2 Diamond coins would display as `5n2d`.
* Displays the coin tooltips as their exchange rates. e.g. An Emerald coin will state that it's worth 10 Gold coins, and that 10 of these are worth 1 Diamond coin.

**Adds the following inputs to each Coin Entry input**:

`initial`: The display name of this coins initial, used for displaying the value. Can be a simple string, or use minecraft [json text formatting](https://minecraft.fandom.com/wiki/Raw_JSON_text_format). Should only result in a 1 or 2 character string. If not present, it will default to using the first character of the item name in lower-case.

`plural`: The name of the coin in a plural format, used for displaying the coins tooltips. Can be a simple string, or use minecraft [json text formatting](https://minecraft.fandom.com/wiki/Raw_JSON_text_format). If not present, it will default to appending an `s` to the end of the coins name (or whatever format is defined in the `item.lightmanscurrency.generic.plural` language entry for your selected language).

### Number Display
`"displayType": "lightmanscurrency:number"`

* Displays the coin value as a singular decimal value within a defined format. e.g. 5 of a coin worth 1.0 unit each would be displayed as `$5`
* Displays the coin tooltips the same as the coin value, with one line showing the value of each individual coin, and one line showing the value of the entire stack. e.g. `Worth $5|Stack is worth $15`.

**Adds the following inputs to the Chain Entry input**:

`displayFormat`: A [json text input](https://minecraft.fandom.com/wiki/Raw_JSON_text_format) of the format used to display the value. REQUIRED. 

- If the text is a string literal (i.e. `"displayFormat":{"text":"input_text"}`), it should be formatted as `text{value}more_text` where `{value}` will be replaced by the numerical value.
- If the text is translatable text (i.e. `"displayFormat":{"translate":"some.translation.key"}`), the translation for the given translation key should use a more standardized format of `text%smore_text` where `%s` will be replaced by the numerical value.

An input of `${value}` (or `$%s` for translated text) will display the values as shown in the examples above, while an input of `{value} Gold` would display it as `5 Gold`, `12.5 Gold`, etc.

`displayFormatWordy`: A [json text input](https://minecraft.fandom.com/wiki/Raw_JSON_text_format) of the format used explicitly in the Value Input widget. **OPTIONAL**. If not present, will default to the `displayFormat` already given. Follows the exact same formatting requirements as the `displayFormat` when it comes to how the literal/translation should be formatted to insert the number.

**Adds the following inputs to each Coin Entry input**:

`baseUnit`: Whether this coin considered worth exactly 1 display unit. Should be `"baseUnit": true` for only 1 coin in the entire chain, and can be ignored for all other coin entries.

## Important Warnings
* The same coin item cannot be used within multiple chains, nor can it be in the same chain twice. Doing so will result in an error to parse the config file, and the default values will be loaded instead.
* Changes to this file will not be reflected in-game until the `/lcconfig reload` command is run by an admin, or until the server has been rebooted.
* Any issues parsing this config file will be reported in the servers logs during a reload, so if something is not loading correctly **read the logs** and it will tell you exactly what input is missing and/or formatted incorrectly.