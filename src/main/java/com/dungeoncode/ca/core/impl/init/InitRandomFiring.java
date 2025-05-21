package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.AbstractGridInitializer;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;

import java.util.Random;

/**
 * Initializes a {@link Grid} for the BRIAN'S-BRAIN cellular automaton by setting all cells to the Ready state and
 * then randomly placing a specified number of cells in the Firing state across the grid. This initializer supports
 * creating sparse initial patterns for second-order dynamics automata.
 *
 * @see GridInitializer
 * @see BrainCell
 * @see BrainState
 */
public class InitRandomFiring extends AbstractGridInitializer<BrainCell, BrainState> {

    /**
     * The number of cells to randomly set to the Firing state.
     */
    private final int firingCellCount;

    /**
     * Constructs a new initializer with the specified number of Firing cells to place randomly.
     *
     * @param firingCellCount the number of cells to set to the Firing state, must be non-negative
     */
    public InitRandomFiring(int firingCellCount) {
        super(7);
        this.firingCellCount = firingCellCount;
    }

    /**
     * Initializes the specified grid by setting all cells to the Ready state and then randomly placing the specified
     * number of cells in the Firing state across the grid.
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(Grid<BrainCell, BrainState> grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        grid.setIntermediateStates(new BrainState[height][width]);

        // Set all cells to Ready state initially
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                grid.getIntermediateStates()[y][x] = new BrainState();
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BrainCell(x, y, new BrainState(BrainState.BrainStateValue.READY, BrainState.BrainStateValue.READY)));
                } else {
                    grid.getCell(x, y).setState(BrainState.BrainStateValue.READY, BrainState.BrainStateValue.READY);
                }
                grid.setCell(x, y, new BrainCell(x, y, new BrainState(BrainState.BrainStateValue.READY)));
            }
        }

        // Randomly place the specified number of Firing cells
        Random random = new Random();
        for (int index = 0; index < firingCellCount; index++) {
            int randomX = random.nextInt(width);
            int randomY = random.nextInt(height);
            grid.setCell(randomX, randomY, new BrainCell(randomX, randomY, new BrainState(BrainState.BrainStateValue.FIRING)));
        }
    }
}