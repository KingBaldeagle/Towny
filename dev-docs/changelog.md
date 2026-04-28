# Changelog

## 2026-04-28

### Added
- Added `Nation` domain model at `com.baldeagle.towny.object.nation.Nation` with capital + member town management.

### Changed
- Refactored `Town` to extend `Government` and support mayor/nation links.
- Updated resident-town synchronization when setting mayor or adding/removing residents.
- Updated `Resident#isMayor()` to a null-safe comparison path.
- Updated specs for Resident, Town, and Nation with implementation status and corrected package references.
