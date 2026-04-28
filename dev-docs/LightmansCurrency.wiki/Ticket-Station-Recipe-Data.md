The Ticket Station has two different recipe types, each with their own inputs & fields.

## Master Ticket Recipes

The Master Ticket recipe type has only 1 ingredient and 1 output, and manually modifies the NBT of the output item to match the next random color or the color of the dye in the modifier slot, as well as assigning the output item the next ticket id.

This recipe also consumes the item in the modifier slot.

**Recipe Type**: `"lightmanscurrency:ticket_master"`

#### Fields:

`ingredient`: A **required** ingredient field defining what item(s) can go in the materials slot of the Ticket Station to craft this item.

`result`: A **required** item id field defining what item is crafted with this recipe.

## Normal Ticket Recipes

This recipe has 2 ingredient inputs and 1 output, and manually modifies the NBT of the output item to match the same display color and ticket id of the item in the modifier slot.

This recipe does **not** consume the item in the modifier slot when crafting.

**Recipe Type**: `"lightmanscurrency:ticket"`

#### Fields:

`ingredient`: A **required** ingredient field defining what item(s) can go in the materials slot of the Ticket Station to craft this item.

`masterTicket`: A **required** ingredient field defining what item(s) can go in the modifier slot of the Ticket Station to craft this item.

`result`: A **required** item id field defining what item is crafted with this recipe.

### Other Notes
Since the built-in recipes conflict with each other by default in regards to the ticket/pass recipes there is no need to worry about creating conflicting recipes as players will be able to select their result before committing to crafting the item.