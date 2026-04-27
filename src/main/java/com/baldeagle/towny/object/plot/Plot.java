package com.baldeagle.towny.object.plot;

import com.baldeagle.towny.object.resident.Resident;
import java.util.UUID;

/** Minimal Plot representation */
public class Plot {
    private final UUID uuid;
    private final String worldName;
    private final int x;
    private final int z;
    private Resident owner;

    public Plot(UUID uuid, String worldName, int x, int z, Resident owner) {
        this.uuid = uuid;
        this.worldName = worldName;
        this.x = x;
        this.z = z;
        this.owner = owner;
    }

    public UUID getUUID() { return uuid; }
    public String getWorldName() { return worldName; }
    public int getX() { return x; }
    public int getZ() { return z; }
    public Resident getOwner() { return owner; }
    public void setOwner(Resident owner) { this.owner = owner; }
}
