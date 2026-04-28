# Model Variants

Model Variants are a way I made in v2.2.5.2 that allows you to change the model and/or textures of nearly any block added by Lightman's Currency without removing or replacing the default model/texture.

### How they work
Model Variants are 99% client-side (with the only server interaction being the saving & synchronizing of the variants id), and can be used without any action from the server owner. If one player uses a resource pack that provides a Model Variant that other players do not have, they will simply miss out on your cool looking model and see that blocks default model instead.

### Changing a variant in-game
In order to change a blocks variant in-game you must first craft a "Variant Wand" (see JEI for recipe). Once you've made one of those, you can now right-click on any supported block and it will open up the Variant Selection Menu. The variant highlighted in green is the current variant, and you can click on any variant in the item slots below to get a large preview of the variant in the top-left corner of the screen. Clicking the "Select Variant" button on the top-right of the screen will officially select the Variant that you are currently viewing and will close the menu automatically. With that the block will now have a different appearance, but otherwise function exactly the same.

Other things of note:
- As of v2.2.5.2c, any block that is broken with a Variant applied will now maintain that variant in item form until it is placed back into the world.
- While crouching and attempting to select the block with the middle mouse, you will instead select the item form of that blocks current variant (useful for creative shenanigans)

### Making your own Custom Variant
Model Variants are a custom json-based data file that can be included in any resource pack in the `assets/namespace/lightmanscurrency/model_variant/` directory. They also support inheritance in a similar manner to vanilla block models. Each Model Variant json file contains the following properties:

#### parent
Type: Resource Location (e.g. `"parent":"examplemod:test_variant"`)

This defines the parent variant for this variant, which it will copy any missing properties from. This parameter is 100% **OPTIONAL**.

#### target
Type: Resource Location (e.g. `"target":"examplemod:test_block"`) **OR** Resource Location List (e.g. ["examplemod:test_block","examplemod:test_block_2"])

This defines the block(s) that are targeted by this variant. This parameter is 100% **REQUIRED**, but may be inherited from a parent variant. If present in both the parent and the child, only the child's target(s) will be used.

Note: If an unsupported block is targeted the variant will fail to load, even if other valid targets are present.

#### name
Type: Text Component (see [Minecraft Wiki](https://minecraft.wiki/w/Text_component_format) for details)

This defines the variants display name for tooltip purposes. While technically **OPTIONAL** it is highly recommended as otherwise it will simply display as "Unnamed". May be inherited from a parent variant.

#### item
Type: Resource Location (e.g. `"item":"examplemod:block/test_block_variant"`)

This defines the variants item model. This parameter is **CONDITIONALLY REQUIRED**, and must be present if the `models` parameter is present **OR** if no `textures` parameter is present.

#### models
Type: Resource Location List (e.g. `"models":["examplemod:block/test_block_bottom","examplemod:block/test_block_top"]`

This defines the variants block model(s). This parameter is **CONDITIONALLY REQUIRED**, and must be present if the `item` parameter is present **OR** if no `textures` parameter is present.

The number of models provided must match the number of models that are relevant for the targeted block(s). Tall blocks (Vending Machine, ATM, etc.) require 2 models, Large Vending Machines require 4 models, etc.

Models cycle through the list in the following order:
- Left -> Right
- Front -> Back (no deep blocks in the mod, but technically supported should this ever be relevant)
- Bottom -> Top

For example the Normal Tall blocks are expected in the order of `[BOTTOM_MODEL,TOP_MODEL]`, and the Large Vending Machines are expected in the order of `[BOTTOM_LEFT,BOTTOM_RIGHT,TOP_LEFT,TOP_RIGHT]`, etc.

Some special blocks can have additional model requirements in addition to the normal top/bottom models. Currently these include the Freezer which requires a third model for the freezer door, and the Slot Machine which has a separate texture for its lights.

May be inherited from a parent variant, however the presence of any models in a child variant will completely replace the parents model list.

**Important Note:** If the model count does not match the expected model count of any targeted block, the variant will fail to load properly and will not function.

#### textures
Type: Texture Map (e.g. `"textures":{"top":"examplemod:block/example_texture","bottom":"examplemod:block/example_texture"}`)

This parameter defined the models textures using very similar methods to how vanilla block models have their textures defined (with the primary difference being that Model Variants do not allow referencing other textures from the same model with "#top", etc.). This parameter is **CONDITIONALLY OPTIONAL**, and only needs to be present if he `models` and `item` parameters are not present.

May be inherited from a parent variant and all entries will be merged from parent to child. The child's values will take priority over the parents values in the case of a conflict.

#### dummy
Type: Boolean (e.g. `"dummy":true`)

This parameter is used to force this model variant to be considered incomplete/invalid. This is intended for setting up an in-between variant to be used as a base variant to be inherited from by child variants later, but necessary as I may not be able to detect that the model we have defined is incomplete without the child variants texture definitions. This parameter is 100% **OPTIONAL**