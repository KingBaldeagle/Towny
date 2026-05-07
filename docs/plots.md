# Plots

## Plot Model (`Plot`)
- Extends `TownyObject` and stores:
  - `WorldCoord` – the location of the plot (the chunk coordinates).
  - `Town` – the owning town of the plot.
  - `Resident` – the owner of the plot (the player who claimed it).
  - `forSale` – boolean flag indicating whether the plot is on the market.
  - `priceInCopper` – the sale price in copper units.

## Claiming a Plot
- Command: `/plot claim`
- Preconditions:
  - Player must be a resident of a town and standing inside a `TownBlock` owned by that town.
  - No existing plot at the location, **or** an existing plot that is for sale.
- Process:
  1. If a plot already exists and is marked `forSale`, the command attempts to purchase it via `TownyEconomyService.purchasePlot`. The buyer must not be delinquent.
  2. If no plot exists, a new `Plot` is created with the player's name as the default plot name (`<player>-plot`).
  3. The plot is stored in the `TownyUniverse` registry.

## Buying a Plot
- When a plot is for sale, a player can run `/plot claim` on the same block to purchase it.
- The purchase flow (`TownyEconomyService.purchasePlot`):
  - Transfers the plot price from the buyer’s wallet to the owning town’s team account.
  - Updates the plot’s owner, clears the sale flag, and resets the price.
  - If the resident is delinquent (unable to pay taxes), the purchase is refused.

## Unclaiming a Plot
- Command: `/plot unclaim`
- Only the plot owner **or** the town mayor can remove a plot.
- The plot is removed from the universe registry, freeing the chunk for a new claim.

## Plot Sale (Future Feature)
- The `Plot` class contains `forSale` and `priceInCopper` fields, but there is currently no command exposed to set a plot for sale. Server admins can modify these fields programmatically or with a future command extension.

---
