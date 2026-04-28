# Item Position Data

Various machines from my mod will render items in the world for various reasons. The main two being the various Item Traders displaying the items being sold and/or purchased, as well as the Auction Stand displaying a random set of item(s) being auctioned off.

Now for a long time the position and scale of these items were hard-coded, but since my mod has become popular enough that people have started making resource packs for it (many of which intend to make many of my machines take a more medieval theme) I decided to give them the freedom to re-position where the items are drawn via a new .json file that you can include in your resource pack.

Said data is defined in two separate files:

### Item Position Data
These files are located in the `assets/NAMESPACE/lightmanscurrency/item_position_data` folder of your resource pack, and define the position & rotations of the items being displayed by the blocks that utilize it. The data itself has the following fields

#### Entries
This is a list of entry values, which contains a singular `Position` field which contains the x/y/z float values that define the position where the item will be displayed relative to the bottom-left corner of the block. Other things of note are as follows:

- The position is not affected by any `Scale` inputs
- The position defines the center point of the item being drawn, not the bottom-left corner as some might assume
- The X position moves the items left/right, Y up/down, and Z towards the back
- A block is exactly 1 meter tall, wide, deep, etc. so unless you're displaying the item above the block all of your position inputs should be less than 1 (unless you're setting the position for a tall and/or wide block like the vending machines)
- The number of values within the Entries list defines how many items can be displayed by the block, and they will always be displayed in order as defined by the block displaying the items (i.e. item traders will always display the items of the first trade at the first entry position on this list, etc.

**IMPORTANT NOTE**: All fields other than the `Entries` field can be defined in **either** the entry field, or in the root json element. If any field is defined in both, the value in the root json element will be ignored and replaced by the value defined in that particular entry. Some fields are optional depending on the value of another field.

#### Scale
This is a float field that defines the size of the items being drawn. If not provided it will default to 1, but it's not recommended to leave it as such as an item drawn with a scale of 1 will be exactly 1 meter tall and take up the entire block.

#### ExtraCount
This is a positive integer field that defines how many additional items can be displayed in a position relative to this item. If not present it will default to 0.

#### offsetX/Y/Z
This is three float fields that define the x/y/z offset where any additional items will be displayed relative to the previously displayed item from that entry. Optional if ExtraCount is 0. Cannot be 0,0,0. To prevent Z-fighting when drawing thicker items near each other I recommend setting all three values to *at least* 1.0E-4

#### RotationType
This is a enum field that defines what rotation handling the items being displayed should use. At the moment there are three valid Rotation Types:

- `SPINNING`
  - Spinning rotation means that the item will casually rotate 360 degrees around its center regardless of any block rotation. Typically used for blocks that don't have a facing such as the Display Case & Auction Stands.
- `FACING`
  - Facing rotation means that the item will be rendered facing towards the front of the block. Only functions on rotatable blocks such as Card Displays or Vending Machines.
- `FACING_UP`
  - Facing up rotation behaves similar to the `FACING` rotation, except the items will be facing upwards with the bottom of the item facing towards the front of the block. Only functions well on rotatable blocks, but will still lay the item flat if used on a non-rotatable block.

### Item Position Blocks

These files are located in the `assets/NAMESPACE/lightmanscurrency/item_position_blocks` folder of your resource pack, and behave similarly to block tags in that it's basically just takes in a list of block ids. The data itself has two fields.

#### values
This contains a list of string fields which should be either the blocks id (i.e. `"lightmanscurrency:shelf_oak"`), or a block tag (i.e. `"#lightmanscurrency:shelf"`).

I should note that this is pretty much the same as the vanilla tag value field except it does not support optional inputs. That said, optional inputs are completely unnecessary as it also doesn't fail to parse or throw any errors when the id of a missing block is given.

#### target
This is an optional string field that defines the resource location of which Item Position Data the blocks listed in this data will have applied to them. If not defined it will use the Item Position Data at the same resource location of this positioning data.

Note: Do not get confused about the extra "lightmanscurrency" folder to the path of the item position data folder, the resource location of an Item Position Data file at `assets/example/lightmanscurrency/item_position_data/file.json` will still only be `"example:file"`