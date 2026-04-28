package com.baldeagle.towny.object.world;

/**
 * 2D chunk/town-block coordinate.
 */
public record Coord(int x, int z) {
    public static Coord of(int x, int z) {
        return new Coord(x, z);
    }

    public static Coord parseCoord(String input) {
        if (input == null || input.isBlank()) {
            throw new IllegalArgumentException("input must not be blank");
        }
        String[] parts = input.split(",");
        if (parts.length != 2) {
            throw new IllegalArgumentException("coord must be in format x,z");
        }
        return new Coord(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
    }
}
