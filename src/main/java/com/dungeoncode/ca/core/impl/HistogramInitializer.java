package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;

import java.util.Random;

/**
 * Initializes a {@link Grid} for a cellular automaton by combining a random distribution of active and inactive cells
 * with a fixed baseline at the bottom row. The grid (except the bottom row) is filled with a 50% chance of active
 * cells (state value) and echo set to false, while the bottom row is set to active (value=true) with echo=true to act
 * as an inhibition baseline for rules like SAFE-PASS.
 *
 * @see GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class HistogramInitializer extends NextStatesBooleanInitializer {

    /**
     * Initializes the specified grid by assigning each cell a random state (value) with a 50% chance of being active
     * and echo set to false, except for the bottom row, where cells are active (value=true) with echo=true to form an
     * inhibition baseline.
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        super.initializeGrid(grid);
        Random rnd = new Random();
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (y == height - 1) {
                    // Bottom row: active with echo=true for inhibition baseline
                    grid.setCell(x, y, new BooleanCell(x, y, false, true));
                } else {
                    // Random state (50% chance active), echo=false
                    boolean value = rnd.nextBoolean();
                    grid.setCell(x, y, new BooleanCell(x, y, value, false));
                }
            }
        }
    }
}