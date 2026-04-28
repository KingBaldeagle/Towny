package com.baldeagle.towny.object.world;

/**
 * World + coord value object for claim addressing.
 */
public record WorldCoord(String worldName, Coord coord) {
    public WorldCoord {
        if (worldName == null || worldName.isBlank()) {
            throw new IllegalArgumentException("worldName must not be blank");
        }
        if (coord == null) {
            throw new IllegalArgumentException("coord must not be null");
        }
    }

    public static WorldCoord of(String worldName, int x, int z) {
        return new WorldCoord(worldName, Coord.of(x, z));
    }

    public static WorldCoord parseWorldCoord(String worldName, String input) {
        return new WorldCoord(worldName, Coord.parseCoord(input));
    }
}
