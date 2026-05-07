# Outposts

## What is an Outpost?
An **outpost** is a flag on a `TownBlock` that marks the block as a special strategic point rather than regular town land. The flag is stored in the `outpost` boolean field of `TownBlock`:
```java
private boolean outpost = false;
public boolean isOutpost() { return outpost; }
public void setOutpost(boolean outpost) { this.outpost = outpost; }
```

## Current Use
At present the mod does not expose a dedicated command to toggle this flag. The outpost status is persisted via the JSON data sources (`JsonTownyDataSource` and `DirectoryJsonTownyDataSource`). Server administrators can change the flag programmatically (e.g., via a custom command, a script, or by editing the persistence files) to enable future features such as:
- Defensive structures that are only allowed on outposts.
- Resource‑gathering zones that generate extra income.
- Visual markers on the map indicating strategic locations.

## Persistence
When the world data is saved, the outpost flag is written to each `TownBlock` entry:
```json
"outpost": true
```
and re‑loaded on server start.

## Future Development
Future updates may add:
- A `/town outpost set <true|false>` command for mayors.
- Permissions to allow only certain ranks to create outposts.
- Integration with the HUD to show outpost status.

---
