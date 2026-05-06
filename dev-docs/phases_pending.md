# Phases 1–4 Pending Work (Completion Checklist)

This file is the **finish list** for all not-yet-done items in phases 1–4.
Use it as a working checklist until every item is complete.

Status legend:
- [x] done
- [ ] not done

---

## Phase 1 – Core Data Model Migration

### Already done
- [x] `Resident` model exists with UUID/name/town-nation links/titles-ranks/online flag.
- [x] `Town` model exists with mayor/residents/optional nation/town blocks/home block.
- [x] `Nation` minimal model exists (capital + towns).
- [x] `Plot` core model exists (coord/owner/for sale/price).
- [x] `WorldCoord`, `Coord`, and `TownBlock` value types exist.
- [x] `TownyUniverse` in-memory registry and claim/unclaim APIs exist.

### Not done (must finish)
- [x] **Town creation requires claimed block**
  - [x] Reject `/town new` if player is not standing on a claimed `TownBlock`.
  - [x] Return explicit failure message.
- [x] **Town creation price is enforced**
  - [x] Read `priceNewTownInCopper` from config.
  - [x] Validate player balance before creating town.
  - [x] Deduct balance atomically with creation.
  - [x] Roll back creation on payment failure.

---

## Phase 2 – Configuration & Persistence

### Already done
- [x] Basic `towny.yml` values mapped to Forge `ModConfigSpec`.
- [x] JSON snapshot persistence implemented.
- [x] Directory JSON persistence implemented.
- [x] Persistence backend selection wired (`json_snapshot` / `directory_json`).

### Not done (must finish)
- [ ] **Legacy Bukkit flat-file migration tooling**
  - [ ] Add migration command/script to import legacy data.
  - [ ] Log unsupported/failed records with reasons.
  - [ ] Write migration summary report (counts per entity type).

---

## Phase 3 – Command Framework Migration

### Already done (baseline)
- [x] Town: `new`, `list`, `here`, `add`, `kick`, `leave`, `claim`, `unclaim`.
- [x] Resident: `self`, `show`.
- [x] Nation: `new`, `list`, `add`, `kick`, `leave`.
- [x] Plot: `claim`, `unclaim`.
- [x] Core command framework (`CommandRegistry`, `SubCommand`, `TownyCommandContext`).

### Not done (must finish)
- [ ] **Town command parity**
  - [ ] `/town invite <player>`
  - [ ] `/town accept`
  - [ ] `/town deny` (optional but expected)
  - [ ] `/town rank ...`
  - [ ] `/town set ...`
  - [ ] `/town delete`
  - [ ] Nation join/leave flows via town commands
- [ ] **Resident admin and jail flows**
  - [ ] Permission-override resident commands
  - [ ] Jail/unjail commands
- [ ] **Nation feature parity**
  - [ ] Nation rank management
  - [ ] Alliances
  - [ ] War mechanics hooks
  - [ ] Tax contribution paths
- [ ] **Plot parity**
  - [ ] For-sale lifecycle
  - [ ] Plot permission flags
  - [ ] Plot tax integration
- [ ] **Framework quality gaps**
  - [ ] Dynamic command registration/discovery
  - [ ] Generated help/usage text
- [ ] **Permission hierarchy**
  - [ ] Mayor > assistant > resident checks throughout commands
  - [ ] Replace placeholder permission checks with nodes
- [ ] **Economy integration**
  - [ ] Provider selection from config
  - [ ] Apply charges/credits in all monetary command paths

---

## Phase 4 – Event Handling (Baseline)

### Already done
- [x] Login/logout resident lifecycle updates.

### Not done (must finish)
- [ ] **Combat protection completion**
  - [ ] Extend PvP controls to policy toggles (friendly fire/faction-wide settings).
- [ ] **Build/break protection listeners**
  - [ ] Hook `BlockEvent.BreakEvent`.
  - [ ] Hook `BlockEvent.EntityPlaceEvent` (or equivalent place event in this codebase).
  - [ ] Enforce `TownyProtectionService.canBuildAt` decisions.
- [ ] **General protection listener migration**
  - [ ] Entity damage protection listeners
  - [ ] Interaction/usage protection listeners
  - [ ] Mob/spawn-related protection listeners
- [ ] **Teleport + world lifecycle listeners**
  - [ ] Teleport boundary/rule checks
  - [ ] World load/unload handling
- [ ] **Daily tasks**
  - [ ] Scheduled tax collection
  - [ ] Upkeep charges
  - [ ] Delinquency consequences + notifications

---

## Definition of Done for this file

This document is considered complete only when:
- [ ] Every "Not done" checkbox above is checked.
- [ ] No phase section contains unresolved command/event/economy/permission TODOs.
- [ ] The implementation plan and specs are updated to match final behavior.

