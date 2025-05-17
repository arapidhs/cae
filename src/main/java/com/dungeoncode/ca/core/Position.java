package com.dungeoncode.ca.core;

/**
 * Represents a 2D position with x and y coordinates in a grid.
 * This class is immutable, providing read-only access to its coordinates.
 */
public class Position {

    /**
     * The x-coordinate (column) of the position.
     */
    private final int x;

    /**
     * The y-coordinate (row) of the position.
     */
    private final int y;

    /**
     * Constructs a new Position with the specified x and y coordinates.
     *
     * @param x the x-coordinate (column)
     * @param y the y-coordinate (row)
     */
    public Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate (column) of this position.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate (row) of this position.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
}