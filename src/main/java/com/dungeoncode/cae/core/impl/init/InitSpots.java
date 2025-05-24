package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Initializes a {@link Grid} for a cellular automaton by placing a specified number of active spots
 * at random positions across the grid. Each spot is a single active cell surrounded by inactive cells.
 * This initializer is useful for creating sparse initial patterns with isolated active regions.
 *
 * @see com.dungeoncode.cae.core.GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class InitSpots extends InitNextStatesBoolean {

    /**
     * The number of active spots to place randomly on the grid.
     */
    private final int spotCount;

    /**
     * Constructs a new initializer with the specified number of spots to place randomly.
     *
     * @param spotCount the number of active spots to place, must be non-negative
     */
    public InitSpots(int spotCount) {
        super(9);
        this.spotCount = spotCount;
    }

    /**
     * Initializes the specified grid by placing the specified number of active spots at random positions.
     * All cells are initially set to inactive, then random positions are selected to be active.
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BooleanCell, BooleanState> grid) {
        super.initializeGrid(grid);
        int width = grid.getWidth();
        int height = grid.getHeight();

        // First, set all cells to inactive
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, false, false, 0));
                } else {
                    grid.getCell(x, y).setState(false, false, 0);
                }
            }
        }

        // Then place the specified number of active spots randomly
        Random random = new Random();
        for (int i = 0; i < spotCount; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            grid.getCell(x, y).setState(true, false, 0);
        }
    }
}
