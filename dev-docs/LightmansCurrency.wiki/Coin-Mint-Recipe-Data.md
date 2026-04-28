Coin Mint Recipes are fairly simple compared to most other recipes as they only have 1 input and 1 output.

**Recipe Type**: `"lightmanscurrency:coin_mint"`

#### Fields

`duration`: An **optional** integer field that determines how many ticks it takes to perform the recipe. If not defined or defined as 0, it will use the configured value from 

`ingredient`: A **required** ingredient field that defines the input item.

`count`: An **optional** count that determines how many input items are required and consumed to perform the crafting.

`result`: A **required** item stack field that determines the item crafted. May contain bonus NBT data as well as an item count if you wish for more than 1 item to be crafted.