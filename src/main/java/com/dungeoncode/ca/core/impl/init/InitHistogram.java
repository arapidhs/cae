package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

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
public class InitHistogram extends InitNextStatesBoolean {

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

                boolean isActive;
                boolean echo;
                // Bottom row: active with echo=true for inhibition baseline
                if (y == height - 1) {
                    isActive = false;
                    echo = true;
                    // Random state (50% chance active), echo=false
                } else {
                    isActive = rnd.nextBoolean();
                    echo = false;
                }
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, echo));
                } else {
                    grid.getCell(x, y).setState(isActive, echo);
                }

            }
        }
    }
}