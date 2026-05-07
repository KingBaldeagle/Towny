package com.baldeagle.towny.object.town;

import com.baldeagle.towny.object.Government;
import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.world.WorldCoord;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Town domain object.
 * <p>
 * This follows the dev-docs phase 1 model enough for resident/town/nation links:
 * mayor, residents, optional nation membership, and basic resident management.
 */
public class Town extends Government {
    private Resident mayor;
    private Nation nation;
    private final Set<Resident> residents = new HashSet<>();
    private final Map<WorldCoord, TownBlock> townBlocks = new ConcurrentHashMap<>();
    private WorldCoord homeBlock;
    private WorldCoord jailBlock;

    public Town(UUID uuid, String name, Resident mayor) {
        super(uuid, name);
        setMayor(mayor);
    }

    public Resident getMayor() { return mayor; }
    public void setMayor(Resident mayor) {
        this.mayor = mayor;
        if (mayor != null) {
            residents.add(mayor);
            if (mayor.getTown() != this) {
                mayor.setTown(this);
            }
        }
    }
    public Set<Resident> getResidents() { return Collections.unmodifiableSet(residents); }

    public boolean addResident(Resident resident) {
        if (resident == null) {
            return false;
        }
        resident.setTown(this);
        return residents.add(resident);
    }

    public boolean removeResident(Resident resident) {
        if (resident == null) {
            return false;
        }
        if (resident.equals(mayor)) {
            return false;
        }
        boolean removed = residents.remove(resident);
        if (removed && resident.getTown() == this) {
            resident.setTown(null);
        }
        return removed;
    }

    public boolean hasNation() { return nation != null; }
    public Nation getNation() { return nation; }
    public void setNation(Nation nation) { this.nation = nation; }

    public Map<WorldCoord, TownBlock> getTownBlocks() {
        return Collections.unmodifiableMap(townBlocks);
    }

    public boolean hasHomeBlock() { return homeBlock != null; }
    public WorldCoord getHomeBlock() { return homeBlock; }
    public void setHomeBlock(WorldCoord homeBlock) { this.homeBlock = homeBlock; }
    public WorldCoord getJailBlock() { return jailBlock; }
    public void setJailBlock(WorldCoord jailBlock) { this.jailBlock = jailBlock; }

    public boolean addTownBlock(TownBlock townBlock) {
        if (townBlock == null) {
            return false;
        }
        townBlock.setTown(this);
        return townBlocks.putIfAbsent(townBlock.getWorldCoord(), townBlock) == null;
    }

    public boolean removeTownBlock(WorldCoord worldCoord) {
        return townBlocks.remove(worldCoord) != null;
    }

    // Backward-compat convenience aliases for older code paths.
    public Resident getOwner() { return getMayor(); }
    public void setOwner(Resident owner) { setMayor(owner); }
}
