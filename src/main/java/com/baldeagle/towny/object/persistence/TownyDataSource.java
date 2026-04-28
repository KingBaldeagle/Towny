package com.baldeagle.towny.object.persistence;

import com.baldeagle.towny.object.nation.Nation;
import com.baldeagle.towny.object.plot.Plot;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.townblock.TownBlock;
import com.baldeagle.towny.object.universe.TownyUniverse;
import com.baldeagle.towny.object.world.TownyWorld;

public interface TownyDataSource {
    void loadAll(TownyUniverse universe);
    void saveAll(TownyUniverse universe);

    void saveResident(Resident resident);
    void saveTown(Town town);
    void saveNation(Nation nation);
    void saveTownBlock(TownBlock townBlock);
    void savePlot(Plot plot);
    void saveWorld(TownyWorld world);
}
