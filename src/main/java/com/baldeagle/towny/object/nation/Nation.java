package com.baldeagle.towny.object.nation;

import com.baldeagle.towny.object.Government;
import com.baldeagle.towny.object.town.Town;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Minimal Nation model for phase-1 Resident -> Town -> Nation linkage.
 */
public class Nation extends Government {
    private Town capital;
    private final Set<Town> towns = new HashSet<>();

    public Nation(UUID uuid, String name, Town capital) {
        super(uuid, name);
        this.capital = capital;
        if (capital != null) {
            towns.add(capital);
            capital.setNation(this);
        }
    }

    public Town getCapital() { return capital; }

    public void setCapital(Town capital) {
        this.capital = capital;
        if (capital != null) {
            addTown(capital);
        }
    }

    public Set<Town> getTowns() { return Collections.unmodifiableSet(towns); }

    public boolean addTown(Town town) {
        if (town == null) {
            return false;
        }
        town.setNation(this);
        return towns.add(town);
    }

    public boolean removeTown(Town town) {
        if (town == null || town.equals(capital)) {
            return false;
        }
        boolean removed = towns.remove(town);
        if (removed && town.getNation() == this) {
            town.setNation(null);
        }
        return removed;
    }
}
