# Alliances & Warfare Specification (Towny)

## Overview
Alliances and wars are diplomatic relationships between **Nations**. They affect permissions, tax calculations, and can trigger special event handling such as combat restrictions.

## Alliance Mechanics
1. **Invitation Flow**
   - A nation leader (king) or minister with `towny.nation.alliance` permission can invite another nation using `/nation ally <nation>`.
   - The target nation receives an invitation event (`NationInviteAllyEvent`). A minister or king of the target nation can accept with `/nation ally accept` or reject with `/nation ally deny`.
2. **State Tracking**
   - Alliances are stored as a set of nation UUIDs inside each `Nation` object.
   - Mutual acceptance is required; the relationship is symmetric.
3. **Permissions Granted to Allies**
   - Residents of allied nations receive the `towny.nation.ally.interact` permission, allowing them to interact with each other’s towns (e.g., open doors, trade) without being considered outsiders.
   - Allied towns can see each other on the nation map (`TownyMap`).
4. **Tax Implications**
   - Alliance status does **not** affect tax rates directly, but allied nations may agree to tax exemptions via custom plugins listening to `NationTaxEvent`.

## Warfare Mechanics
1. **Declaration of War**
   - A nation king or minister with `towny.nation.war` permission can declare war using `/nation war <nation>`.
   - This creates a `NationWarEvent` which is broadcast to both nations.
2. **War State**
   - While at war, the following restrictions apply:
   - **PvP Enabled** between residents of the two nations regardless of town settings.
   - **Plot Protection** – allied flag protections (`fire`, `explosions`) are ignored for enemy residents.
   - **Travel Restrictions** – gates and teleport commands that require `towny.town.spawn` may be blocked for enemy residents.
3. **War Duration & Ceasefire**
   - Wars persist until one nation issues `/nation peace <nation>` (requires `towny.nation.peace` permission).
   - A `NationPeaceEvent` is fired; the relationship returns to neutral.
4. **War Penalties**
   - Optional configuration (`nation.war.tax-penalty`) can increase tax percentages for the losing side after the war ends (handled by `NationWarEndEvent`).

## Configuration (`towny.yml`)
```yaml
nation:
  alliance:
    allow-mob-fight: false   # whether mobs can attack across allied nations
    allow-pvp: true          # PvP between allied nations (default false)
  war:
    enable-pvp: true         # PvP between warring nations
    tax-penalty-percent: 10 # extra tax for loser after war
```

## Events
- `NationInviteAllyEvent` – fired when an alliance invitation is sent.
- `NationAllyAcceptEvent` – fired when an invitation is accepted.
- `NationWarEvent` – fired on war declaration.
- `NationPeaceEvent` – fired on cease‑fire.
- `NationWarEndEvent` – fired after a war ends, provides winner/loser information.

## Edge Cases
- **Circular Alliances** – The system prevents a nation from allying with a nation that is already allied to an enemy (would create indirect conflict).
- **Self‑Alliances** – Disallowed; a nation cannot ally with itself.
- **War While Allied** – Declaring war automatically revokes any existing alliance between the two nations.
- **Offline Nations** – Alliances and wars are stored persistently; actions can be performed by offline leaders when they log back in.
- **Admin Override** – Players with `towny.admin` can force an alliance or war via `/towny admin alliance <nation1> <nation2>` and `/towny admin war <nation1> <nation2>`.

## Integration Points
- `Nation` class (`com.baldeagle.towny.object.Nation`).
- `NationToggleEvent` subclasses for alliance/war state changes.
- Permission nodes: `towny.nation.ally.interact`, `towny.nation.war.pvp`.
- Command handlers: `NationCommand` processes `ally`, `war`, and `peace` sub‑commands.

## Tests
- Invite, accept, and deny alliance flow with proper event firing.
- War declaration disables allied protections and enables PvP.
- Cease‑fire restores neutral permissions.
- Tax penalty applied correctly after war end.
- Ensure circular alliance attempts are rejected.
