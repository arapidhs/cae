package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Initializes a {@link Grid} for a cellular automaton with a random distribution of active and inactive cells
 * (50% probability, echo=false) and a fixed active baseline at the bottom row (value=true, echo=true).
 * Designed for rules like SAFE-PASS, where the bottom row acts as an inhibition baseline, as described in
 * <i>Cellular Automata Machines</i> (MIT Press).
 *
 * @see BooleanCell
 * @see BooleanState
 * @see InitIntermediateStatesBoolean
 */
public class InitHistogram extends InitIntermediateStatesBoolean {

    /**
     * Random number generator for state assignment.
     */
    private final Random random = new Random();

    /**
     * Constructs a new histogram grid initializer with a fixed ID.
     */
    public InitHistogram() {
        super(2);
    }

    /**
     * Initializes the specified grid with a random state distribution (50% active, echo=false) for all cells
     * except the bottom row, which is set to active (value=true, echo=true) as an inhibition baseline.
     *
     * @param grid the {@link Grid} to initialize, must not be null
     * @throws NullPointerException if grid is null
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BooleanCell, BooleanState> grid) {
        java.util.Objects.requireNonNull(grid, "Grid cannot be null");
        super.initializeGrid(grid);
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isActive;
                boolean echo;

                // Bottom row: active with echo=true for inhibition baseline
                if (y == height - 1) {
                    isActive = true;
                    echo = true;
                } else {
                    // Random state (50% chance active), echo=false
                    isActive = random.nextBoolean();
                    echo = false;
                }

                // Initialize or update cell
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, echo, 0));
                } else {
                    grid.getCell(x, y).setState(isActive, echo, 0);
                }
            }
        }
    }
}