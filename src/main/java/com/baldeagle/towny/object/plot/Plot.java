package com.baldeagle.towny.object.plot;

import com.baldeagle.towny.object.TownyObject;
import com.baldeagle.towny.object.resident.Resident;
import com.baldeagle.towny.object.town.Town;
import com.baldeagle.towny.object.world.WorldCoord;
import java.util.UUID;

/**
 * Phase-1 plot model used for ownership and sale metadata.
 */
public class Plot extends TownyObject {
    private final WorldCoord worldCoord;
    private Town town;
    private Resident owner;
    private boolean forSale = false;
    private long priceInCopper = 0L;

    public Plot(UUID uuid, String name, WorldCoord worldCoord, Town town, Resident owner) {
        super(uuid, name);
        this.worldCoord = worldCoord;
        this.town = town;
        this.owner = owner;
    }

    public WorldCoord getWorldCoord() { return worldCoord; }
    public Town getTown() { return town; }
    public void setTown(Town town) { this.town = town; }

    public Resident getOwner() { return owner; }
    public void setOwner(Resident owner) { this.owner = owner; }

    public boolean isForSale() { return forSale; }
    public void setForSale(boolean forSale) { this.forSale = forSale; }

    public long getPriceInCopper() { return priceInCopper; }
    public void setPriceInCopper(long priceInCopper) {
        if (priceInCopper < 0) {
            throw new IllegalArgumentException("priceInCopper must be >= 0");
        }
        this.priceInCopper = priceInCopper;
    }
}
