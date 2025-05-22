package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.GridInitializer;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import java.util.Random;

/**
 * Initializes a {@link Grid} for a cellular automaton with a random distribution of live and dead cells,
 * simulating a "primeval soup". Each cell is assigned a random
 * * boolean state (live or dead) with equal probability, creating a chaotic initial configuration.
 *
 * @see GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class InitRandomBoolean extends InitIntermediateStatesBoolean {

    public InitRandomBoolean() {
        super(3);
    }

    /**
     * Initializes the specified grid by assigning each cell a random boolean state (live or dead).
     * Iterates through the grid and sets each cell to a new {@link BooleanCell} with a 50% chance of
     * being live, using a random number generator.
     *
     * @param grid the grid to initialize
     */
    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        super.initializeGrid(grid);
        Random rnd = new Random();
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                boolean isActive = rnd.nextBoolean();
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, false, 0));
                } else {
                    grid.getCell(x, y).setState(isActive, false, 0);
                }
            }
        }
    }
}