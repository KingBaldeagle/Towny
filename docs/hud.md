# HUD

The HUD is updated every `Config.HUD_INTERVAL_TICKS` ticks (default **40**). The `TownyHudTickListener` iterates over all online players and displays an action‑bar message showing:

```
[Towny] <TownName> @ <x>,<z>
```

- If the player is in wilderness the area name is "Wilderness".
- The listener retrieves the player’s world coordinate via `TownyProtectionService.toWorldCoord` and then queries the universe for a `TownBlock`. If a block is found, its owning town’s name is shown.
- The HUD can be toggled on or off in the config (`Config.HUD_ENABLED`).
- The interval can be changed in the config (`Config.HUD_INTERVAL_TICKS`).

**Customization**: Server admins can modify the format by editing `TownyHudTickListener` or creating a custom client‑side resource pack that changes the action‑bar style.
