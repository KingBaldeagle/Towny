# Pending Work for Phases 1‑4

This document tracks the **remaining** tasks for the first four phases of the NeoTowny implementation.  All items marked **✅** are complete, **⚠️** are partially‑implemented (work‑in‑progress or stubbed), and **❌** are still missing.

---

## Phase 1 – Core Data Model Migration
| Item | Status | Notes |
|------|--------|-------|
| `Resident` class (UUID, name, town/nation linkage, titles, ranks, online flag) | ✅ | Fully implemented. |
| `Town` class (mayor, residents, optional nation, town‑blocks, home block) | ✅ | Backward‑compatible `getOwner`/`setOwner` aliases present. |
| `Nation` minimal model (capital, town set) | ✅ | Minimal phase‑1 implementation; ranks/alliances pending. |
| `Plot` model (world‑coord, owner, for‑sale flag, price) | ✅ | Core fields present; permission flags & tax handling pending. |
| `WorldCoord`, `Coord`, `TownBlock` types | ✅ | Value‑object model matches spec. |
| `TownyUniverse` – in‑memory registry, creation APIs, claim/unclaim logic | ✅ | Supports residents, towns, nations, town‑blocks and plots. |
| **Missing / In‑complete** | ⚠️ | • No validation that a new town is created on a **claimed** plot (spec requirement).  • No enforcement of town creation price (`priceNewTownInCopper`). |

---

## Phase 2 – Configuration & Persistence
| Item | Status | Notes |
|------|--------|-------|
| `towny.yml` → Forge `ModConfigSpec` conversion | ✅ | Basic config values (block size, ratios, creation prices, persistence backend). |
| JSON snapshot persistence (`JsonTownyDataSource`) | ✅ | Writes/loads residents, towns, nations, worlds, town‑blocks and plots. |
| Directory‑based JSON persistence (`DirectoryJsonTownyDataSource`) | ✅ | Mirrors legacy flat‑file layout for easier migration. |
| Persistence backend selection (`json_snapshot` vs `directory_json`) | ✅ | Configurable via `Config.PERSISTENCE_BACKEND`. |
| **Missing / In‑complete** | ⚠️ | • No automatic migration from the legacy Bukkit flat‑files (TODO migration script). |

---

## Phase 3 – Command Framework Migration
| Command Group | Implemented Commands | Status | Gaps |
|---------------|----------------------|--------|------|
| **Town** | `new`, `list`, `here`, `add`, `kick`, `leave`, `claim`, `unclaim` | ✅ core set functional via `CommandRegistry` and Brigadier wrappers. | ❌ `invite`/`accept` flow, `rank`, `set`, `delete`, nation‑join/leave, tax/price checks, plot size limits, creation‑price deduction. |
| **Resident** | `self` (default), `show` | ✅ | ❌ permission‑override commands, jail/unjail commands. |
| **Nation** | `new`, `list`, `add`, `kick`, `leave` | ✅ | ❌ nation ranks, alliances, war mechanics, tax contribution logic. |
| **Plot** | `claim`, `unclaim` | ✅ | ❌ plot for‑sale handling, permission flags, tax integration. |
| **Framework** | `CommandRegistry`, `SubCommand`, `TownyCommandContext` | ✅ | No dynamic command auto‑registration beyond the static block; missing help/usage generation. |
| **Permissions** | Simple mayor checks (`isMayor()`) | ⚠️ | Full permission hierarchy (mayor > assistant > resident) not wired; no LuckPerms integration. |
| **Economy Hooks** | `LightmansCurrencyProvider` stub | ⚠️ | Provider selection from config not wired; town creation cost and tax deductions not applied. |

---

## Phase 4 – Event Handling (Baseline)
| Event Area | Implemented | Status | Outstanding Work |
|------------|------------|--------|-------------------|
| Player login/logout | `TownyPlayerListener` creates/marks `Resident` online status | ✅ | None – basic lifecycle works. |
| PVP protection | `TownyCombatListener` blocks PvP when attacker is not in the same town block | ✅ | Extend to faction‑wide PvP settings, friendly fire options. |
| Block‑place/break protection | `TownyProtectionService.canBuildAt` (used by future block listeners) | ⚠️ | No actual `BlockEvent` listeners yet; should hook `BlockEvent.BreakEvent` / `PlaceEvent` and cancel when `canBuildAt` returns false. |
| General protection events (entity damage, mob spawning, teleport, tick updates) | – | ❌ No listeners registered; spec calls for migration of all Bukkit listeners to NeoForge equivalents. |
| Scheduled daily tasks (tax collection, upkeep) | – | ❌ Not implemented – planned for Phase 6. |

---

## Summary of Open Work (Phases 1‑4)
1. **Town creation validation** – require the player to stand on a claimed `TownBlock` and deduct the creation price from the resident’s account.
2. **Invitation workflow** – implement `/town invite <player>` and `/town accept` (and optional `/town deny`).
3. **Rank & permission system** – expose rank commands, enforce hierarchical permission checks, and integrate with LuckPerms/NeoForge permission API.
4. **Plot size/claim limits** – enforce `TOWN_BLOCK_RATIO` and `MAX_TOWN_BLOCKS` when claiming blocks/plots.
5. **Economy provider selection** – read `economy.provider` from config, instantiate the correct `EconomyProvider`, and wire it into `TownyEconomyHandler` for all monetary actions.
6. **Tax & upkeep logic** – schedule daily tasks, calculate taxes per town/nation, and handle delinquency.
7. **Full event migration** – add listeners for block interactions, entity damage, teleport, world load/unload, and other essential Bukkit events.
8. **Permission migration** – replace remaining `source.hasPermission(0)` checks with meaningful permission nodes.
9. **Documentation** – keep this and other spec files up‑to‑date as features land.

Once the above items are completed, Phase 3 will reach full parity with the original Bukkit commands and Phase 4 will provide a robust protection layer, paving the way for the later phases (taxes, GUI, testing, packaging).
