package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.AbstractGridInitializer;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import javax.annotation.Nonnull;

/**
 * Initializes the intermediate states array for a {@link Grid} in a cellular automaton with {@link BooleanState} objects.
 * Ensures each cell has a default {@link BooleanState} in the grid's intermediate states, used for rules requiring state buffering.
 *
 * @see BooleanCell
 * @see BooleanState
 * @see AbstractGridInitializer
 */
public class InitIntermediateStatesBoolean extends AbstractGridInitializer<BooleanCell, BooleanState> {

    /**
     * Constructs a new intermediate states initializer with the specified ID.
     *
     * @param id the unique identifier for this initializer
     */
    public InitIntermediateStatesBoolean(int id) {
        super(id);
    }

    /**
     * Initializes the specified grid's intermediate states array with default {@link BooleanState} objects.
     * Allocates a new array if none exists, populating each position with a new {@link BooleanState}.
     *
     * @param grid the {@link Grid} to initialize, must not be null
     * @throws NullPointerException if grid is null
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BooleanCell, BooleanState> grid) {
        java.util.Objects.requireNonNull(grid, "Grid cannot be null");
        int width = grid.getWidth();
        int height = grid.getHeight();

        if (grid.getIntermediateStates() == null) {
            BooleanState[][] intermediateStates = new BooleanState[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    intermediateStates[y][x] = new BooleanState();
                }
            }
            grid.setIntermediateStates(intermediateStates);
        }
    }
}