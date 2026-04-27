# Event Hooks & Extensibility Specification (Towny)

## Overview
Towny emits a rich set of **Bukkit events** for virtually every significant action (town creation, resident joins, plot claims, tax collection, etc.). These events allow third‑party plugins to modify, augment, or cancel core behavior, providing a powerful extensibility point.

## Event Classification
| Category | Example Events | Primary Use |
|---|---|---|
| **Creation / Deletion** | `TownCreateEvent`, `TownDeleteEvent`, `NationCreateEvent`, `NationDeleteEvent` | Hook into lifecycle of towns/nations.
| **Membership** | `TownAddResidentEvent`, `TownRemoveResidentEvent`, `NationAddTownEvent`, `NationRemoveTownEvent` | Adjust permissions, send notifications, sync with external systems.
| **Plot / Claim** | `TownBlockClaimEvent`, `TownBlockUnclaimEvent`, `TownBlockOwnerChangeEvent`, `TownBlockPermissionChangeEvent` | Validate claim locations, enforce custom plot limits, apply extra costs.
| **Economy** | `TownTransactionEvent`, `NationTransactionEvent`, `BankTransactionEvent`, `ResidentJailEvent` | Modify tax amounts, apply discounts, integrate alternate currencies.
| **Diplomacy** | `NationInviteAllyEvent`, `NationAllyAcceptEvent`, `NationWarEvent`, `NationPeaceEvent` | Implement custom diplomatic rules, trigger alerts.
| **Teleportation** | `TownyTeleportEvent`, `TownyTeleportSuccessEvent` | Cancel or redirect teleports, add effects.
| **World / Chunk** | `TownyChunkLoadEvent`, `TownyChunkUnloadEvent` | Manage resources when chunks load/unload.
| **Custom Hooks** | Plugins may define their own events extending `TownyEvent`.

## Event Hierarchy
- All Towny events extend `TownyEvent`, which implements `Cancellable` when appropriate.
- **Cancelable Events**: Most actions that change state (e.g., claim, join, delete) are cancelable. Cancelling aborts the action and prevents further processing.
- **Post‑Action Events**: Some events fire *after* the change (e.g., `TownAddResidentEvent` fires after the resident is added). These are not cancelable but can be observed for side‑effects.

## Extensibility Workflow
1. **Listen** – Register a listener in your plugin’s `onEnable`:
   ```java
   @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
   public void onTownClaim(TownBlockClaimEvent e) {
       // custom logic
   }
   ```
2. **Inspect** – Access event data (town, resident, location) via getters.
3. **Modify / Cancel** – Call `e.setCancelled(true)` or adjust mutable fields (e.g., `e.setCost(newCost)`).
4. **Post‑Processing** – Use non‑cancelable events to trigger rewards, logs, or external API calls.

## Common Extension Scenarios
- **Custom Claim Costs** – Listen to `TownBlockClaimEvent`, compute a dynamic cost based on region rarity, and set `e.setCost(customAmount)`.
- **Alliance Restrictions** – In `NationInviteAllyEvent`, verify the target nation meets custom criteria (e.g., population threshold) and cancel if not.
- **Tax Modifiers** – Hook `TownTransactionEvent` to apply discounts for residents with special ranks.
- **Teleport Cooldowns** – Use `TownyTeleportEvent` to enforce additional cooldowns or redirect to custom locations.
- **WorldGuard Sync** – On `TownBlockClaimEvent`, create a matching WorldGuard region.

## Event Registration Guidelines
- **Priority** – Choose appropriate priority (`LOWEST` for early intervention, `HIGHEST` for final overrides).
- **Performance** – Avoid heavy computations inside listeners; defer to async tasks if needed.
- **Thread Safety** – All Bukkit events run on the main thread; interacting with async APIs must be scheduled back to the main thread.
- **Compatibility** – Respect `ignoreCancelled` to avoid processing events that other plugins have already cancelled.

## Documentation & Discovery
- Towny provides a **developer wiki** page listing all events with method signatures.
- Use the `TownyCommandAddonAPI` to register additional sub‑commands that fire custom events.

## Testing Hooks
- **Unit Tests** – Mock Bukkit’s `EventPriority` and verify listeners receive events with expected data.
- **Integration Tests** – Deploy a test server with a mock plugin that cancels a `TownBlockClaimEvent` and assert the claim does not persist.
- **Event Order** – Verify that cancelable events fire before non‑cancelable post‑action events.

## Future Enhancements
- **Event Batching** – Group related events (e.g., multiple plot claims) into a single batch event for performance.
- **Async Event API** – Provide an asynchronous variant for long‑running extensions.
- **Event Data Serialization** – Allow plugins to persist custom event data across server restarts.
