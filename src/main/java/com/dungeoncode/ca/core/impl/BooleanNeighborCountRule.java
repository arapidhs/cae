package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;

/**
 * Abstract base class for rules that rely on counting live neighbors in a cellular automaton grid.
 * Provides utility methods for counting live neighbors in different neighborhoods, with implementations
 * using wrap-around boundaries (toroidal topology).
 *
 * @see Rule
 * @see BooleanCell
 * @see BooleanState
 */
public abstract class BooleanNeighborCountRule implements Rule<BooleanCell, BooleanState> {

    /**
     * Counts the number of 'live' ({@code true}) neighbors in the Moore neighborhood (3x3 grid, excluding the
     * center cell) of the specified cell. Uses wrap-around boundaries (toroidal topology) to connect opposite
     * grid edges, ensuring all cells have a full neighborhood, as described in Toffoli and Margolus (1987,
     * Chapter 1, Section 1.2, p. 6).
     *
     * @param grid the {@link Grid} containing the cells
     * @param x    the x-coordinate (column) of the center cell
     * @param y    the y-coordinate (row) of the center cell
     * @return the number of 'live' neighbors in the Moore neighborhood
     */
    protected int countLiveMooreNeighbors(Grid<BooleanCell, BooleanState> grid, int x, int y) {
        int count = 0;
        int width = grid.getWidth();
        int height = grid.getHeight();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip center cell
                // Wrap-around coordinates
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                BooleanCell neighbor = grid.getCell(nx, ny);
                if (neighbor.getState().getValue()) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Counts the number of 'live' ({@code true}) neighbors in the von Neumann neighborhood (four orthogonal
     * cells: north, south, east, west, excluding the center cell) of the specified cell. Uses wrap-around
     * boundaries (toroidal topology) to connect opposite grid edges, ensuring all cells have a full neighborhood,
     * as described in Toffoli and Margolus (1987, Chapter 1, Section 1.2, p. 6).
     *
     * @param grid the {@link Grid} containing the cells
     * @param x    the x-coordinate (column) of the center cell
     * @param y    the y-coordinate (row) of the center cell
     * @return the number of 'live' neighbors in the von Neumann neighborhood
     */
    protected int countLiveVonNeumannNeighbors(Grid<BooleanCell, BooleanState> grid, int x, int y) {
        int count = 0;
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Check the four orthogonal neighbors (north, south, east, west)
        // North neighbor
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        if (grid.getCell(nxNorth, nyNorth).getState().getValue()) {
            count++;
        }
        // South neighbor
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        if (grid.getCell(nxSouth, nySouth).getState().getValue()) {
            count++;
        }
        // West neighbor
        int nxWest = (x - 1 + width) % width;
        int nyWest = y;
        if (grid.getCell(nxWest, nyWest).getState().getValue()) {
            count++;
        }
        // East neighbor
        int nxEast = (x + 1) % width;
        int nyEast = y;
        if (grid.getCell(nxEast, nyEast).getState().getValue()) {
            count++;
        }

        return count;
    }
}