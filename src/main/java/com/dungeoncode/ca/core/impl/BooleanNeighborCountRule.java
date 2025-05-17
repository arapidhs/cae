package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;

public abstract class BooleanNeighborCountRule implements Rule<BooleanCell, BooleanState> {

    /**
     * Counts the number of 'live' ({@code true}) neighbors in the 3x3 neighborhood of the specified cell.
     * Uses wrap-around boundaries (toroidal topology) to connect opposite grid edges, ensuring all cells have a full
     * neighborhood, as described in Toffoli and Margolus (1987, Chapter 1, Section 1.2, p. 6).
     *
     * @param grid the {@link Grid} containing the cells
     * @param x    the x-coordinate (column) of the center cell
     * @param y    the y-coordinate (row) of the center cell
     * @return the number of 'live' neighbors in the 3x3 neighborhood
     */
    protected int countLiveNeighbors(Grid<BooleanCell, BooleanState> grid, int x, int y) {
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

}
