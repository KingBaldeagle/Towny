package com.baldeagle.towny.object.town;

import com.baldeagle.towny.object.resident.Resident;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Minimal Town representation. Holds a set of residents, a name and an owner.
 */
public class Town {
    private final UUID uuid;
    private final String name;
    private Resident owner;
    private final Set<Resident> residents = new HashSet<>();

    public Town(UUID uuid, String name, Resident owner) {
        this.uuid = uuid;
        this.name = name;
        this.owner = owner;
        this.residents.add(owner);
    }

    public UUID getUUID() { return uuid; }
    public String getName() { return name; }
    public Resident getOwner() { return owner; }
    public void setOwner(Resident owner) { this.owner = owner; }
    public Set<Resident> getResidents() { return residents; }

    public boolean addResident(Resident r) { return residents.add(r); }
    public boolean removeResident(Resident r) { return residents.remove(r); }
}
