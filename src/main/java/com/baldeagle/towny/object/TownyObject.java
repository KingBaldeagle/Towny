package com.baldeagle.towny.object;

import java.util.UUID;

public abstract class TownyObject {
    private final UUID uuid;
    private final String name;
    private long registered;
    private long lastOnline;

    protected TownyObject(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        this.registered = System.currentTimeMillis();
        this.lastOnline = this.registered;
    }

    public UUID getUUID() { return uuid; }
    public String getName() { return name; }
    public long getRegistered() { return registered; }
    public long getLastOnline() { return lastOnline; }

    public void setLastOnline(long lastOnline) { this.lastOnline = lastOnline; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TownyObject that = (TownyObject) o;
        return uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() { return uuid.hashCode(); }
}