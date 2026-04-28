package com.baldeagle.towny.object.resident;

import com.baldeagle.towny.object.TownyObject;
import com.baldeagle.towny.object.town.Town;

import java.util.*;

public class Resident extends TownyObject {
    private Town town;
    private final List<Resident> friends = new ArrayList<>();
    private final List<String> townRanks = new ArrayList<>();
    private final List<String> nationRanks = new ArrayList<>();
    private String title = "";
    private String surname = "";
    private boolean online = false;
    private boolean npc = false;

    public Resident(UUID uuid, String name) {
        super(uuid, name);
    }

    public boolean hasTown() { return town != null; }
    public void setTown(Town town) { this.town = town; }
    public Town getTown() { return town; }

    public boolean isMayor() {
        return town != null && this.equals(town.getMayor());
    }

    public boolean isKing() {
        return false; // TODO: Nation integration
    }

    public boolean isOnline() { return online; }
    public void setOnline(boolean online) {
        this.online = online;
        setLastOnline(System.currentTimeMillis());
    }

    public boolean isNPC() { return npc; }
    public void setNPC(boolean npc) { this.npc = npc; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title != null ? title : ""; }

    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname != null ? surname : ""; }

    public String getFormattedName() {
        return title + getName() + (surname.isEmpty() ? "" : " " + surname);
    }

    public List<Resident> getFriends() { return Collections.unmodifiableList(friends); }
    public boolean addFriend(Resident resident) { return friends.add(resident); }
    public boolean removeFriend(Resident resident) { return friends.remove(resident); }

    public List<String> getTownRanks() { return Collections.unmodifiableList(townRanks); }
    public void addTownRank(String rank) { if (!townRanks.contains(rank)) townRanks.add(rank); }
    public void removeTownRank(String rank) { townRanks.remove(rank); }

    public List<String> getNationRanks() { return Collections.unmodifiableList(nationRanks); }
    public void addNationRank(String rank) { if (!nationRanks.contains(rank)) nationRanks.add(rank); }
    public void removeNationRank(String rank) { nationRanks.remove(rank); }

    public boolean hasNation() { return hasTown() && getTown().hasNation(); }
}
