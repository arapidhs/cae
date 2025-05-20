package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;

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
public class PrimevalSoupBooleanInitializer extends NextStatesBooleanInitializer {

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
                grid.setCell(x, y, new BooleanCell(x, y, rnd.nextBoolean(), false));
            }
        }
    }
}