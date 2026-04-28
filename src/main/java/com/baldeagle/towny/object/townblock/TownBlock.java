package com.baldeagle.towny.object.townblock;

import com.baldeagle.towny.object.TownyObject;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.world.WorldCoord;

import java.util.UUID;

public class TownBlock extends TownyObject {
    private final WorldCoord worldCoord;
    private Town town;
    private Resident resident;
    private TownBlockType type = TownBlockType.NORMAL;
    private boolean outpost = false;
    private boolean forSale = false;
    private long plotPriceInCopper = 0L;

    public TownBlock(UUID uuid, String name, WorldCoord worldCoord, Town town) {
        super(uuid, name);
        this.worldCoord = worldCoord;
        this.town = town;
    }

    public WorldCoord getWorldCoord() { return worldCoord; }
    public Town getTown() { return town; }
    public void setTown(Town town) { this.town = town; }

    public boolean hasResident() { return resident != null; }
    public Resident getResident() { return resident; }
    public void setResident(Resident resident) { this.resident = resident; }

    public TownBlockType getType() { return type; }
    public void setType(TownBlockType type) { this.type = type != null ? type : TownBlockType.NORMAL; }

    public boolean isOutpost() { return outpost; }
    public void setOutpost(boolean outpost) { this.outpost = outpost; }

    public boolean isForSale() { return forSale; }
    public void setForSale(boolean forSale) { this.forSale = forSale; }

    public long getPlotPriceInCopper() { return plotPriceInCopper; }
    public void setPlotPriceInCopper(long plotPriceInCopper) {
        if (plotPriceInCopper < 0) {
            throw new IllegalArgumentException("plotPriceInCopper must be >= 0");
        }
        this.plotPriceInCopper = plotPriceInCopper;
    }
}
