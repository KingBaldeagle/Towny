After completing v2.2.5.0 I've now started keeping a log of additions & removals from the language/translation files added by my mod and noting them here.
Changes noted in the **Pending Build** section are coming in a future update and are subject to change, but should allow translators to preemptively translate any newly added blocks, items, tooltips and/or menus.

### Multi-Line Translations

I have several instances "multi-line" text that support a flexible line counts. These are noted by a bold **Mult-line** text after the initial translation key (e.g. `"some.translation.key.*"`) followed by several smaller bullet points of `*.#` indicating the line number. Under most conditions, you can simply translate this text directly (i.e. `"some.translation.key.1":"example of the first line"`), but there are also ways to add/remove lines from your translation file.

#### ADDING LINES
Adding lines is a super-easy process. Simply just add an extra translation for the next number in the sequence (i.e. assuming 3 existing lines, you can simply add a new translation for `"some.translation.key.4":"Example Text"`) and it'll be detected automatically and displayed in-game with no extra fuss. Theoretically you can add nearly infinite lines to a multi-line text instance, but I should note that going too crazy with these can result in text and/or tooltips being drawn in weird placed and/or not fitting on the screen. I should also note that the lines must be in numerical order, so if you have lines 1,2,3, and 5 the system will check for line 4 and when it's not found it won't bother to check if line 5 exists.

#### REMOVING LINES
Removing lines is unfortunately a slightly more difficult process as the english translations are always loaded in the background, meaning that if you can fit all 3 lines of the english text in only 1 or 2 lines in your language there is a slightly more difficult process to follow. Fortunately there is a workaround, and to force a line to be ignored you can simply replace the translation with nothing but it's translation key (i.e. `"some.translation.key.3":"some.translation.key.3"`) and it'll ignore that line 

# Changelog

## 2.2.6.4
- "button.lightmanscurrency.trader.set_all_trade_directions": "Set %s Types"
- "gui.lightmanscurrency.terminal.sort_type.id": "Oldest"
- "gui.lightmanscurrency.terminal.sort_type.id.inverted": "Newest"
- "gui.lightmanscurrency.terminal.sort_type.name": "Name (A->Z)"
- "gui.lightmanscurrency.terminal.sort_type.name.inverted": "Name (Z->A)"
- "gui.lightmanscurrency.terminal.sort_type.offers": "Most Offers"
- "gui.lightmanscurrency.terminal.sort_type.offers.inverted": "Least Offers"
- "gui.lightmanscurrency.terminal.sort_type.popularity": "Popular"
- "gui.lightmanscurrency.terminal.sort_type.popularity.inverted": "Unpopular"
- "gui.lightmanscurrency.terminal.sort_type.recent": "Recent Activity"
- "gui.lightmanscurrency.terminal.sort_type.recent.inverted": "Older Activity"
- "guide.lightmanscurrency.trader_guide.landing_text": "A guide to Buying and Selling your way to unfathomable riches"
- "guide.lightmanscurrency.trader_guide.name": "Trading Guide (WIP)"
  - **SPECIAL NOTE**: "(WIP)" will 100% be removed in a future update, this guide is simply not yet complete and thus has been flagged as incomplete in its name
- "item.lightmanscurrency.item_trade_filter": "Custom Item Trade Filter"
- "message.lightmanscurrency.seasonal_event.halloween": "Tick or Treat!"
- "stat.lightmanscurrency.auction_bids": "Auction Bids Placed"
- "stat.lightmanscurrency.auction_wins": "Auctions Won"
- "stat.lightmanscurrency.trade_interactions": "Traded with Trading Machines"
- "tooltip.lightmanscurrency.item_trade_filter.label.items": "Items:"
- "tooltip.lightmanscurrency.item_trade_filter.label.tags": "Item Tags:"
- "traderule.lightmanscurrency.player_trade_limit.denial.time_remaining": "You can interact again in %s"

**Changed**
- "tooltip.lightmanscurrency.money_bag.1": "Money Bag: When placed, up to 576 coins can be inserted by interacting with the bag while holding a coin" **ORIGINAL**
  - "tooltip.lightmanscurrency.money_bag.1": "When placed, up to 576 coins can be inserted by interacting with the bag while holding a coin" **NEW**
- "tooltip.lightmanscurrency.trader.open_multi_edit.selected": "Edit Price for All %s Selected Trade(s)" **ORIGINAL**
  - "tooltip.lightmanscurrency.trader.open_multi_edit.selected": "Edit Basic Settings for All %s Selected Trade(s)" **NEW**

## 2.2.6.3
- "gui.lightmanscurrency.ticket_station.label.durability": "Uses: %s"
- "gui.lightmanscurrency.ticket_station.label.durability.inf": "∞"
- "jei.lightmanscurrency.info.ticket.durability": "Result can have a custom durability between %1$s and %2$s"
- "jei.lightmanscurrency.info.ticket.durability.allows_infinite": "Result can also have infinite durability"
- "notification.lightmanscurrency.auction_house_seller.fee": "%1$s was actually granted, and %2$s was kept as an Auction Fee"
= "tooltip.lightmanscurrency.ticket.uses": "Uses Remaining: %s"
- "tooltip.lightmanscurrency.trader.auction.fee_warning": "%s%% of the final bid will be collected as a fee for using the auction house"
- "tooltip.lightmanscurrency.trader.auction.limit_exceeded.1": "You cannot submit more than %1$s auctions at once"
- "tooltip.lightmanscurrency.trader.auction.limit_exceeded.2": "You currently have %2$s active auctions"
- "tooltip.lightmanscurrency.trader.auction.price": "Costs %s to submit an auction"
- "tooltip.lightmanscurrency.trader.discount_codes": "Apply Discount Codes"

**Changed**
- "tooltip.lightmanscurrency.pass": "Will not be consumed by Paygates" **ORIGINAL**
  - "tooltip.lightmanscurrency.pass": "Will not be instantly consumed by Paygates" **NEW**

## 2.2.6.2
**Added**
- "create.item_attributes.lightmanscurrency.coin.any": "Is any coin"
- "create.item_attributes.lightmanscurrency.coin.any.inverted": "Is not a coin"
- "create.item_attributes.lightmanscurrency.coin.chain": "Is a '%s' coin"
- "create.item_attributes.lightmanscurrency.coin.chain.inverted": "Is not a '%s' coin"
- "data.lightmanscurrency.name.trader.trades.barter_items": "Bartering for %s Items"
- "data.lightmanscurrency.name.trader.trades.count": "Trades"
- "data.lightmanscurrency.name.trader.trades.item.purchase_items": "Purchasing %s Items"
- "data.lightmanscurrency.name.trader.trades.item.sell_items": "Selling %s Items"
- "data.lightmanscurrency.name.trader.trades.price": "Price"
- "data.lightmanscurrency.name.trader.trades.type": "Trade Type"

## 2.2.6.1
**Added**
- "button.lightmanscurrency.trade_rule.discount_code.change": "Change"
- "button.lightmanscurrency.trade_rule.discount_code.create": "Create"
- "button.lightmanscurrency.trade_rule.discount_code.entry": "%1$s: %2$s%% Discount"
- "data.lightmanscurrency.category.trade.rules": "Trade Rules for Trade #%s"
- "data.lightmanscurrency.name.rules.count": "%s Active Rules"
- "data.lightmanscurrency.name.trader.taxes.ignore_all": "Tax Immunity"
- "data.lightmanscurrency.name.trader.taxes.ignored_count": "Ignored Tax Collectors"
- "data.lightmanscurrency.name.trader.taxes.rate": "Acceptable Tax Rate"
- "data.lightmanscurrency.name.trader.trades": "Trade #%s"
- "gui.lightmanscurrency.ticket_station.label.code": "Code:"
- "gui.lightmanscurrency.trade_rule.discount_code.discount": "Discount"
- "gui.lightmanscurrency.trade_rule.discount_code.limit": "Limit"
- "item.lightmanscurrency.coupon": "Coupon"
- "tooltip.lightmanscurrency.ticket_kiosk.craft_ticket.null": "Sell %s Directly"
- "tooltip.lightmanscurrency.trade_rule.discount_code.back": "Back to Code Selection/Creation"
- "tooltip.lightmanscurrency.trade_rule.discount_code.delete": "Delete Discount Code"
- "tooltip.lightmanscurrency.trade_rule.discount_code.entry": "Click to Edit"
- "tooltip.lightmanscurrency.trader.auction.info.owner": "Submitted By: %s"
- "traderule.lightmanscurrency.discount_code": "Discount Codes"
- "traderule.lightmanscurrency.discount_code.info.limit": "You have used %1$s of your %2$s coupon uses"
- "traderule.lightmanscurrency.discount_code.info.purchase": "You will be paid %s%% more because of a coupon"
- "traderule.lightmanscurrency.discount_code.info.sale": "You have been given a %s%% discount from a coupon"
- "traderule.lightmanscurrency.discount_code.info.timed": "Coupon resets after %s"
- "data.lightmanscurrency.category.trader.taxes": "Tax Settings"

## 2.2.6.0
**Added**
- "config.jade.plugin_lightmanscurrency.model_variant.locked": "Model Variant Locked Status"
- "lightmanscurrency.block_variant.modifier.inverted": "#Inverted"
- "message.lightmanscurrency.tax_collector.placement.disable": "Click to disable further notifications of this type"
- "data.lightmanscurrency.category.creative": "Creative Settings"
- "data.lightmanscurrency.category.ownership": "Ownership Settings"
- "data.lightmanscurrency.category.trader.allies": "Allies"
- "data.lightmanscurrency.category.trader.ally_perms": "Ally Permissions"
- "data.lightmanscurrency.category.trader.rules": "Trader Rules"
- "data.lightmanscurrency.category.trader.trades": "Trades"
- "data.lightmanscurrency.label": "%1$s: %2$s"
- "data.lightmanscurrency.name.allies": "Allies"
- "data.lightmanscurrency.name.allies.count": "%s Players"
- "data.lightmanscurrency.name.input_output_sides.count": "%1$s Input and & %2$s Output Side(s)"
- "data.lightmanscurrency.name.owner": "Owner"
- "data.lightmanscurrency.name.permissions": "Permissions"
- "data.lightmanscurrency.name.permissions.count": "%s Perms"
- "data.lightmanscurrency.name_backup": "Copied Settings"
- "data.lightmanscurrency.name_format": "%s Settings"
- "tooltip.lightmanscurrency.trader.info": "Trader Info"
- "tooltip.lightmanscurrency.trader.log.settings": "Settings Logs"
- "tooltip.lightmanscurrency.trader.settings_clipboard": "Settings Clipboard"
- "tooltip.lightmanscurrency.trader.settings_clipboard.copy": "Copy"
- "tooltip.lightmanscurrency.trader.settings_clipboard.paste": "Paste"

**Changed**
- "data.lightmanscurrency.category.trader_bank": "Trader Bank Settings" **ORIGINAL**
  - "data.lightmanscurrency.category.trader.bank": "Bank Settings" **NEW**
- "data.lightmanscurrency.category.trader_display": "Trader Display Settings"
  - "data.lightmanscurrency.category.trader.display": "Display Settings"

**Added in Patch A**
- "create.item_attributes.lightmanscurrency.model_variant": "Has the Model Variant '%s'"
- "create.item_attributes.lightmanscurrency.model_variant.inverted":"Does not have the Model Variant '%s'"
- "create.item_attributes.lightmanscurrency.model_variant.null": "Does not have any Model Variant"
- "create.item_attributes.lightmanscurrency.model_variant.null.inverted": "Has any Model Variant"

## 2.2.5.4
**Added**
- "command.lightmanscurrency.lcadmin.emptyWallet.success": "Emptied %s wallets"
- "data.lightmanscurrency.category.misc_settings": "Misc Settings"
- "data.lightmanscurrency.name.trader.paygate.conflict_handling": "Output Conflict Handling"
- "gui.lightmanscurrency.trader.paygate.conflict_label": "Output Conflict Handling:"
- "gui.trader.paygate.conflict.add_time": "Allow: Add to Timer"
- "gui.trader.paygate.conflict.deny_any": "Deny: Any Outputs"
- "gui.trader.paygate.conflict.deny_side_conflict": "Deny: Conflicting Outputs"
- "gui.trader.paygate.conflict.override_time": "Allow: Override Timer"
- "tooltip.lightmanscurrency.trader.paygate.time_remaining.sided": "%1$s has %2$s remaining"
- "lightmanscurrency.block_variant.armor_display.skin.herobrine"
- "lightmanscurrency.block_variant.modifier.label": "⏵ Modifier: %s"
- "tooltip.lightmanscurrency.model_variant.locked": "Variant is Locked"


**Changed**
- "lightmanscurrency.block_variant.glassless": "Glassless" **ORIGINAL**
  - "lightmanscurrency.block_variant.modifier.glassless": "#Glassless" **NEW**
- "lightmanscurrency.block_variant.vending_machine.footless": "#Footless" **ORIGINAL**
  - "lightmanscurrency.block_variant.modifier.footless": "#Footless" **NEW**


**Removed**
- "lightmanscurrency.block_variant.armor_display.glassless_skin": "Skin (%s) #Glassless"

## 2.2.5.2
**Added**
- "button.lightmanscurrency.block_variants.select": "Select Variant"
- "gui.lightmanscurrency.block_variants.title": "Variant Selection"
- "item.lightmanscurrency.variant_wand": "Variant Wand"
- "lightmanscurrency.block_variant.default": "Default Model"
- "lightmanscurrency.block_variant.unnamed": "Unnammed"
- "lightmanscurrency.block_variant.glassless": "#Glassless"
- "lightmanscurrency.block_variant.vending_machine.footless": "#Footless"
- "lightmanscurrency.block_variant.armor_display.skin": "Skin (%s)"
- "lightmanscurrency.block_variant.armor_display.glassless_skin": "Skin (%s) #Glassless"
- "lightmanscurrency.block_variant.armor_display.skin.1": "Steve"
- "lightmanscurrency.block_variant.armor_display.skin.2": "Zuri"
- "lightmanscurrency.block_variant.armor_display.skin.3": "Ari"
- "lightmanscurrency.block_variant.armor_display.skin.4": "Kai"
- "lightmanscurrency.block_variant.armor_display.skin.5": "Garrett"
- "tooltip.lightmanscurrency.model_variant.id": "Variant ID: %s"
- "tooltip.lightmanscurrency.model_variant.name": "Variant: %s"
- "tooltip.lightmanscurrency.variant_wand.X" **Multi-Line**
  - "*.1": "Right click on most Lightman's Currency blocks to select a model variant"
  - "*.2": "Not all blocks have variants by default, but more can be added via resource pack"
- "tooltip.lightmanscurrency.trader.paygate.time_remaining": "Time Remaining: %s"
- "config.jade.plugin_lightmanscurrency.model_variant": "Model Variant"
- "config.jade.plugin_lightmanscurrency.paygate": "Paygate Info"

## 2.2.5.1a
**Added**
- "data.lightmanscurrency.name.trader.bank_link": "Bank Account Link"
- "data.lightmanscurrency.name.trader_icon": "Trader Terminal Icon"
- "notification.lightmanscurrency.change_settings_dumb": "%1$s changed %2$s"

## 2.2.5.1

**Added**
- "command.lightmanscurrency.lcadmin.viewWallet.empty": "%s does not have a wallet equipped"
- "command.lightmanscurrency.lcadmin.viewWallet.success": "%1$s has a %2$s equipped"
- "rei.lightmanscurrency.group.display_case": "Display Cases"
- "data.lightmanscurrency.category.trader_bank": "Trader Bank Settings"
- "data.lightmanscurrency.name.store_money_in_creative": "Store Money in Creative"
- "gui.lightmanscurrency.trader.paygate.tooltip": "Tooltip:"
- "block.lightmanscurrency.money_bag": "Money Bag"
- "data.lightmanscurrency.category.input_settings": "Input/Output Settings"
- "data.lightmanscurrency.category.trader_display": "Trader Display Settings"
- "data.lightmanscurrency.name.creative": "Creative"
- "data.lightmanscurrency.name.input_output_sides": "Input/Output Side: "
- "data.lightmanscurrency.name.trader_name": "Trader Name"
- "notifications.source.event": "Events"
- "tooltip.lightmanscurrency.container_item.loot_table": "Has Loot Table '%s'"
- "tooltip.lightmanscurrency.money_bag.*X*" **Multi-Line**
  - "*.1":"Money Bag: When placed, up to 576 coins can be inserted by interacting with the bag while holding a coin"
  - "*.2": "Interacting with the bag with an empty hand will extract a random coin from the bag"
  - "*.3": "When mined the bag will drop with the coins still inside"
  - "*.4": "Bag increases in size the more coins are contained within"
  - "*.5": "In a pinch a sufficiently full bag can be used as a blunt weapon"
- "tooltip.lightmanscurrency.money_bag.size": "Bag Size: %s"

**Changed**
- "tooltip.lightmanscurrency.coinjar.holdshift": "Hold SHIFT to view contents" **ORIGINAL**
  - "tooltip.lightmanscurrency.coinjar.holdctrl": "Hold CTRL to view contents" **NEW**


## 2.2.5.0
**Start of Logs**