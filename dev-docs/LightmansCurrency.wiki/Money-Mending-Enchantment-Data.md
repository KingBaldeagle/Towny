# Money Mending Enchantment

**IMPORTANT NOTE**: This page details features only available in the 1.21+ version of Lightman's Currrency, and does not apply to any older Minecraft versions.

As Minecraft 1.21 made enchantments data-driven, I have decided to add the possibility open for users to make customized Money Mending enchantments with prices that differ from the configurable and built-in Money Mending enchantment. The main functionality for this is the new `lightmanscurrency:repair_with_money` enchantment effect that can be added to an enchantments `effects` entry to trigger the code that will result in the repairs being triggered. An example of this "enchantment effect" in the enchantments json file can be seen below:

```
"effects": {
    "lightmanscurrency:repair_with_money":{
        "baseCost":"coin;1-lightmanscurrency:coin_chocolate_copper",
	"enchantmentExtraCost": [
            {
                "bonusCost": "coin;4-lightmanscurrency:coin_chocolate_copper",
                "enchantment": "minecraft:infinity"
            }
        ],
        "itemOverrides": [
            {
                "baseCost":"coin;1-lightmanscurrency:coin_chocolate_gold",
                "items":["create:super_glue","#minecraft:axes"]
            }
        ]
    }
}
```

As you can see, the enchantment effect has 3 inputs. Each one functions as follows:

### baseCost
This is a [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) field which defines the base cost to repair each unit of durability.

### enchantmentExtraCost
This is a JsonArray containing a list of entries that define an additional cost to be added to the base cost whenever a given enchantment is present.

Each entry in the list contains the following fields:

`bonusCost`: A [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) that defines a price that will be added to the base cost whenever the enchantment is present on the item.

`enchantment`: The id of the enchantment (i.e. "minecraft:sharpness") that must be on the item for this additional cost to be applied/added.

`maxLevelCalculation`: An optional integer field that defines the enchantment level that limits how many times the price is added. Defaults to 1 if not present, meaning that it will only check if the enchantment is present and not care about the enchantments level.

Notes:
- If maxLevelCalculation is more than 1, the `bonusCost` will be added to the base cost once for each level of the enchantment that is present.
- The `bonusCost` value must be the same type as the `baseCost` defined in the root of this data in order for the additional cost to be applied correctly. If it is a different money type, it will simply not be added to the base cost and be effectively ignored.

### itemOverrides
This is another JsonArray that contains a list of entries that allow you to override the base cost for certain items.

Each entry in the list contains the following fields:

`baseCost`: A [Money Value](https://github.com/Lightman314/LightmansCurrency/wiki/Money-Value-Arguments) field that defines what the new base cost will be if the item matches the list.

`items`: A String List containing the items (i.e. "minecraft:stick") or item tags (i.e. "#minecraft:sticks") that will trigger this override.

Notes:
- The base cost defined by an override *may* be a different money type as the default base cost, however if the new base cost is not the same type as any extra enchantment costs that also apply to the item, it will not trigger correctly.
  - There is nothing that prevents you from adding multiple `enchantmentExtraCost` entries for the same enchantment with different bonus cost fields, potentially with different money types, to work around this limitation
- The base cost overrides are applied in order, so if an item is accepted by multiple entries only the last one on the list will be applied as that is the last one that was checked.

## Other Info
The other enchantment fields can be easily copied from the vanilla mending enchantment file (or from the built-in Money Mending enchantment in my mods jar file), but here's a few other tidbits of helpful information that should help you more easily set up a customized Money Mending enchantment:

#### Exclusive Sets
There's no exclusive set for mending in vanilla MC as there is only one mending enchantment, so I took the liberty of creating the `c:exclusive_set/mending` enchantment tag that includes the vanilla & money mending enchantments.

#### Money Mending Tag
For convenience, I created a `lightmanscurrency:money_mending` tag that you can add your custom enchantment to if you wish, and doing so will easily add your new enchantment to the various vanilla enchantment tags required to make it available to a player in the same way vanilla mending is.

It also adds your enchantment to the aforementioned `c:exclusive_set/mending` tag so that you won't get multiple mending/money mending enchantments on the same item, etc.

#### Translation/Description
Since Money Mending repair prices are listed in the tooltip, there's no true requirement for each variant to have a different enchantment name so using the default `enchantment.lightmanscurrency.money_mending` translation key for the enchantments name is a perfectly valid option.