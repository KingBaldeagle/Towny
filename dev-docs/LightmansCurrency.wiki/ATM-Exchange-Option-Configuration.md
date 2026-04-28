# ATM Data Configuration

In v2.2.0.0 or newer, this data is located in the `ATMData` section of the Master Coin List's [Chain Entry](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config#chain-entries), and the `ATMData.json` file will be ignored.

In older versions (Forge 2.1 and Fabric), this data will be loaded from the `ATMData.json` file located within the `config/lightmanscurrency/` folder.

The ATM Data is responsible for changing what coin exchange buttons are displayed in the [ATM Exchange Tab](https://github.com/Lightman314/LightmansCurrency/wiki/Bank-Accounts-&-ATM#coin-exchange-tab). By default changing this should not be needed, but if you are adding or replacing the default currency by editing the [MasterCoinList](https://github.com/Lightman314/LightmansCurrency/wiki/Master-Coin-List-Config) you will likely want to edit this as well so that the ATM is capable of exchanging the new currency types.

## Breakdown
The base Json object has an `ExchangeButtons` (`ConversionButtons` if pre-2.1.1.4) array tag with several coin data entries. Each Coin Data entry has the following inputs:

`x` & `y`: The x and y position of the button. Position is the number of Minecraft pixels from the top-left corner of the screen.

`width`: The width of the button in Minecraft pixels. Note that there is no height input, as the height is set at 18 pixels tall.

`command`: The Exchange Command to be executed when the button is clicked. Details on the commands and there syntax are listed below.

`icons`: A list entry of icons used to decorate the button. An empty list will simply display a blank button. Details on the icon types available is listed below.

### Exchange Commands
**IMPORTANT NOTE:** These commands were changed in v2.1.1.4, so if you're inputting these commands for an older version, replace every instance of `exchange...` with `convert...`. Note that 2.1.1.4+ will still process commands using the `convert` text, but older versions will not process commands using the `exchange` text.

`exchangeAllUp`: By default the button at the top-left. Exchanges all coins upwards into the highest value coin regardless of what coin or chain the coins are in.

`exchangeAllDown`: By default the button at the top-right. Exchanges all coins downwards into the lowest value coin regardless of what coin or chain the coins are in.

`exchangeUp-namespace:coin_item_id`: Exchanges the `namespace:coin_item_id` coin into the coin valued directly above it within the same chain. Note: Does not function if this coin is the highest value coin in the given chain.

`exchangeDown-namespace:coin_item_id`: Exchanges the `namespace:coin_item_id` coin into the coin valued directly below it within the same chain.
Note: Does not function if this coin is the lowest value coin in the given chain.

### Button Icon Types

All Button Icons have the following inputs:
`type`: The icon type. Used to determine which icon to load internally, as well as what additional inputs are required.
`x` & `y`: The x and y positions of the icon. Position is the number of Minecraft pixels from the top-left corner of the button.

#### Item Icon
`"type":"lightmanscurrency:item"`

Item icons render a given item on the button, and require the following inputs:

`item`: The item to be rendered. Accepts both item ids and ItemStack tags.

Examples:
* `"item":"lightmanscurrency:coin_copper"`
* `"item":{"ID":"minecraft:enchanted_book","Tag":"{Enchantments:[{id:\"minecraft:sharpness\",lvl:5}]}"}`

#### Small Arrow
`"type":"lightmanscurrency:small_arrow"`

This icon type renders a small arrow pointed in the given direction. Arrow is only 6x6 pixels, so a larger y position would be required compared to a normal item icon. Arrow icons require the following inputs:

`direction`: The direction for the arrow to point. Accepts `UP`,`DOWN`,`LEFT`, and `RIGHT`.

#### Custom Sprite
`"type":"lightmanscurrency:sprite"`

This icon type can render any 256x256 texture file from Minecrafts built-in resources or those added by any other mods (or one included in a resource pack). Since it's more direct in it's approach, it naturally requires more inputs:

`texture`: The resource location of the texture, including it's `.png` extension. For example, an input of `"texture":"lightmanscurrency:gui/icons.png"` would allow you to draw an icon from my included icon texture.

`u` & `v`: The uv location defining the top-left position of the sprite to draw within the texture file. Distance is (naturally) from the top-left corner of the texture file, and a value of 0,0 would be the exact top-left of the texture.

`width` & `height`: The size of the sprite to render.