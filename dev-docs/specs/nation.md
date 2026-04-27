# Nation Feature Specification

## Overview
A **Nation** groups multiple towns under a shared government. Nations have ranks, taxes, alliances, and war mechanics.

## Core Requirements
1. **Creation**
   - Command: `/nation new <name>`
   - Creator becomes the king.
   - Name validation similar to towns.
2. **Town Management**
   - Towns can join (`/nation invite <town>`) and be accepted by the town mayor.
   - King can kick towns.
3. **Ranks & Permissions**
   - Ranks: King, Minister, Resident.
   - Permissions control tax settings, alliances, and war declarations.
4. **Economy**
   - Nation bank account receives tax contributions from member towns.
   - Nation can set tax percentage (`/nation tax <percent>`).
5. **Alliances & Wars**
   - Nations can ally or declare war (`/nation ally <nation>`, `/nation war <nation>`).
   - Ally status affects diplomatic permissions.
6. **Settings**
   - Configurable spawn location, name color, neutral/open status.
7. **Deletion**
   - Only the king can delete a nation. All member towns become nation‑less.

## Edge Cases
- Prevent circular alliances.
- Ensure tax percentage is within 0‑100.
- Disallow nation creation if player already leads a nation.
- Handle world unload – persist nation data.

## Integration Points
- `Nation` class in `com.baldeagle.towny.object.Nation`.
- Event hooks: `NationAddTownEvent`, `NationRemoveTownEvent`, `NationDeleteEvent`.
- Economy via `TownyEconomyHandler`.

## Tests
- Nation creation and king assignment.
- Town join/leave flow.
- Alliance declaration and revocation.
- Tax collection correctness.
