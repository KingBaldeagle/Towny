package com.baldeagle.towny.object.world;

import com.baldeagle.towny.object.TownyObject;

import java.util.UUID;

public class TownyWorld extends TownyObject {
    private boolean usingTowny = true;
    private boolean claimable = true;
    private boolean pvp = false;
    private boolean forcePvp = false;
    private boolean explosion = false;
    private boolean forceExplosion = false;
    private boolean fire = false;
    private boolean forceFire = false;

    public TownyWorld(UUID uuid, String worldName) {
        super(uuid, worldName);
    }

    public boolean isUsingTowny() { return usingTowny; }
    public void setUsingTowny(boolean usingTowny) { this.usingTowny = usingTowny; }

    public boolean isClaimable() { return claimable; }
    public void setClaimable(boolean claimable) { this.claimable = claimable; }

    public boolean isPvp() { return pvp; }
    public void setPvp(boolean pvp) { this.pvp = pvp; }

    public boolean isForcePvp() { return forcePvp; }
    public void setForcePvp(boolean forcePvp) { this.forcePvp = forcePvp; }

    public boolean isExplosion() { return explosion; }
    public void setExplosion(boolean explosion) { this.explosion = explosion; }

    public boolean isForceExplosion() { return forceExplosion; }
    public void setForceExplosion(boolean forceExplosion) { this.forceExplosion = forceExplosion; }

    public boolean isFire() { return fire; }
    public void setFire(boolean fire) { this.fire = fire; }

    public boolean isForceFire() { return forceFire; }
    public void setForceFire(boolean forceFire) { this.forceFire = forceFire; }
}
