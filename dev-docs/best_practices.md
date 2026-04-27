# Neo‑Towny Development Best Practices

## General Coding Guidelines
- **Follow NeoForge conventions** – use capabilities, `ModConfig`, and Forge event buses instead of Bukkit APIs.
- **Package Structure** – all classes should live under `com.baldeagle.towny` (e.g., `com.baldeagle.towny.object`, `com.baldeagle.towny.command`).
- **Use Registries** – register towns, nations, and plots via Forge registries where possible to enable data‑sync across dimensions.
- **Prefer immutable data** – immutable value objects for IDs, coordinates, and money amounts reduce race conditions.
- **Thread Safety** – only the main server thread may modify world data. If background tasks are needed, schedule work back onto the main thread with `MinecraftServer#execute`.

## Event Handling
- **Cancelable Early** – fire cancelable events **before** any state changes (e.g., `TownBlockClaimEvent`).
- **Post‑Action Events** – fire non‑cancelable events after a change (e.g., `TownBlockClaimedEvent`).
- **Priority** – use `EventPriority.LOWEST` for validation, `HIGHEST` for overrides, and respect `ignoreCancelled`.

## Configuration Management
- Store all configurable values in `ModConfig.Type.COMMON` and expose them via `/towny reload`.
- Validate config values on load; fall back to sensible defaults if parsing fails.
- Document each config option with comments or a separate wiki page.

## Economy Integration
- **Always work in copper units** – convert to higher denominations only when presenting to the player.
- Use the `EconomyProvider` abstraction; never call Lightmans Currency directly outside the provider implementation.
- Ensure all monetary operations are atomic – withdraw + deposit should be a single transaction.

## Permission System
- Leverage NeoForge’s permission API or a custom capability to store per‑player permission data.
- Keep permission checks in a central utility (`PermissionHandler`) to avoid duplication.
- Provide a GUI (using Minecraft’s screen system) for admins to edit permissions, mirroring the Bukkit `PermissionGUI`.

## Testing Strategy
- **Unit Tests** – cover pure logic (level calculations, tax formulas, claim validation) with JUnit.
- **Integration Tests** – run a headless Minecraft server using the Forge test framework; validate chunk loading, claim persistence, and command execution.
- **Mock Provider** – implement a mock `EconomyProvider` for tests that need monetary operations without Lightmans Currency.
- **Continuous Integration** – run tests on every push; ensure compatibility with Minecraft 1.21.1.

## Documentation
- Keep the `dev-docs/` specs up‑to‑date; each feature should have a dedicated markdown file.
- Add a table of contents to `dev-docs/README.md` linking to all spec files.
- Use inline code blocks for commands and class names; keep explanations concise.

## Performance Tips
- Cache frequently accessed data (e.g., a town’s plot list) in memory; persist only when necessary.
- Avoid heavy I/O on the main thread – save claim data asynchronously during chunk unload.
- Limit the use of reflection; prefer direct class references for better compile‑time safety.

## Release Checklist
1. Bump version in `mods.toml`.
2. Run full test suite.
3. Verify compatibility with Lightmans Currency (currency conversion, denominations).
4. Update the changelog and documentation.
5. Build the jar with `./gradlew build` and test in a clean world.

---

## Documentation After Implementing a Feature
1. **Update the Spec** – Create or modify the relevant markdown file in `dev-docs/specs/` to reflect the new behavior, API changes, and any configuration options.
2. **Add a Changelog Entry** – Record the change in `dev-docs/changelog.md` (or the project's `CHANGELOG.md`) with a concise description and the version tag.
3. **Synchronize Commands** – If new commands were added, update `commands.md` and `commands_detailed.md` with syntax, permissions, and side‑effects.
4. **Review Permission Changes** – Document any new permission nodes in `permissions.md` and explain their intended usage.
5. **Cross‑Reference** – Add links between related specs (e.g., a new economy feature should link to `economy.md` and `lightmans_currency.md`).
6. **Generate API Javadocs** – Run the Gradle `javadoc` task to keep the generated API docs up‑to‑date.
7. **Pull Request Checklist** – Ensure the PR description includes a summary of documentation updates and that all new/updated spec files are reviewed.

*Maintaining up‑to‑date documentation ensures that contributors, users, and future maintainers can quickly understand new features and how they interact with existing systems.*

*These best‑practice guidelines are meant to help maintain a clean, performant, and extensible Neo‑Towny codebase.*
