# Nations

## Core Model (`Nation`)
- Extends `Government`.
- Holds a **capital** (`Town`) and a mutable `Set<Town>` of member towns.
- The capital is the first town that creates the nation and is automatically added to the member set.
- When a town is added via `Nation#addTown`, the town’s `nation` reference is set to this nation.
- Removing a town clears its `nation` reference, unless it is the capital (the capital cannot be removed).

## Creating a Nation
- Command: `/nation new <name>`
- Preconditions:
  - The executing player must be a mayor of a town that does **not** already belong to a nation.
  - The player must have enough copper (`Config.PRICE_NEW_NATION_IN_COPPER`).
- Process:
  1. Withdraw the creation cost from the mayor’s wallet.
  2. Create a new `Nation` instance with the player’s town as the capital.
  3. Deposit the cost into the nation’s LightmansCurrency team account (handled by `TownyEconomyHandler`).

## Managing Nation Membership
- **Add Town** – `/nation add <town>`
  - Only the **king** (mayor of the capital town) can run this command.
  - Adds the specified town to the nation’s member set and sets the town’s `nation` reference.
- **Kick Town** – `/nation kick <town>`
  - King‑only command.
  - Removes the town from the nation and clears its `nation` reference.
- **Leave Nation** – `/town nation leave`
  - Executed by the mayor of a town that belongs to a nation.
  - The town is removed from the nation’s member set.

## Nation Tax
- Configurable percentage (`Config.NATION_TAX_PERCENT`) of each member town’s balance is transferred to the nation’s account each tax cycle.
- The collection is performed by `TownyEconomyService.collectNationTaxes`.

## War Interaction
- Nations can declare war on each other through the `/townywar declare <nation>` command (see the *War System* documentation).

---
