# Changelog

## 2026-04-28

### Added
- Added `Nation` domain model at `com.baldeagle.towny.object.nation.Nation` with capital + member town management.
- Added initial `TownyUniverse` central registry for resident/town/nation creation and lookups.
- Added phase-1 world/claim object types: `Coord`, `WorldCoord`, `TownyWorld`, `TownBlock`, and `TownBlockType`.
- Added phase-1 `Plot` model and plot registry operations in `TownyUniverse`.
- Added phase-2 persistence bootstrap interfaces/classes: `TownyDataSource` and `JsonTownyDataSource`.
- Added `DirectoryJsonTownyDataSource` and `TownyDataSourceFactory` for configurable persistence backend selection.

### Changed
- Refactored `Town` to extend `Government` and support mayor/nation links.
- Updated resident-town synchronization when setting mayor or adding/removing residents.
- Updated `Resident#isMayor()` to a null-safe comparison path.
- Updated specs for Resident, Town, and Nation with implementation status and corrected package references.
- Expanded economy provider contracts to use copper-unit operations and denomination breakdown/formatting.
- Documented Lightman's Currency implementation status in economy spec docs.
- Updated implementation plan status to reflect initial TownyUniverse progress.
- Extended `Town` and `TownyUniverse` with basic town-block claim/home-block flows.
- Marked Phase 1 core data model migration complete in `dev-docs/implementation-plan.md`.
- Started Phase 2 with JSON snapshot persistence for residents/towns/nations under `config/towny/universe.json`.
- Expanded phase-2 JSON snapshot persistence to include worlds, town blocks, and plots.
- Replaced placeholder config values with Towny-focused common config options for economy/pricing/persistence settings.
- Marked Phase 2 complete for the current JSON persistence implementation track.
- 2026-05-05: Re-pushed Phase 4 CI signature fix commit for branch visibility (no functional behavior change).
