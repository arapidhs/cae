package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.AbstractGridInitializer;
import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;

/**
 * Initializes the next states array for a {@link Grid} in a cellular automaton with {@link BooleanState} objects.
 * Ensures each cell has a default {@link BooleanState} in the grid's next states, used for rules requiring state buffering.
 *
 * @see BooleanCell
 * @see BooleanState
 * @see AbstractGridInitializer
 */
public class InitNextStatesBoolean extends AbstractGridInitializer<BooleanCell, BooleanState> {

    /**
     * Constructs a new next states initializer with the specified ID.
     *
     * @param id the unique identifier for this initializer
     */
    public InitNextStatesBoolean(int id) {
        super(id);
    }

    /**
     * Initializes the specified grid's next states array with default {@link BooleanState} objects.
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

        if (grid.getNextStates() == null) {
            BooleanState[][] nextStates = new BooleanState[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    nextStates[y][x] = new BooleanState();
                }
            }
            grid.setNextStates(nextStates);
        }
    }
}