# Money Value Arguments

Money Value arguments are used in both Commands and Config files to allow a player input of a monetary value.

They all should start with a `prefix` followed by a `;` semicolon.

The following Money Value parsing types are included by default:

## Empty/Free
* Prefix: `null;`

Rarely necessary, but technically functional, inputting `null;empty` or `null:free` will be parsed as an empty (no value, but not flagged as free) or free value. Free input can technically be used to make the purchase of certain configurable things free.

## Coin Value
* Prefix: `coin;`

Coin Values are input in the following format:

`coin;count-coin_id,count2-coin_id2,...`

Such that inputting

`coin;6-lightmanscurrency:coin_netherite,4-lightmanscurrency:coin_emerald,3-lightmanscurrency:coin_copper`

will result in a coin value of 6 netherite coins, 4 emerald coins, and 3 copper coins.

Also of note is that the `count-` portion is optional, an if excluded will default to a count of 1 such that inputting

`coin;lightmanscurrency:coin_copper`

will result in a coin value of 1 copper coin.

**IMPORTANT NOTE**
In LC v2.1 and older, the empty/free inputs are not accepted, and only the Coin Value inputs can be parsed correctly without the `coin;` prefix.