package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;

import java.util.Random;

/**
 * Initializes a {@link Grid} for the BRIAN'S-BRAIN cellular automaton by randomly assigning Ready, Firing, or
 * Refractory states to cells, with a configurable probability distribution. Suitable for creating dynamic initial
 * patterns in second-order dynamics automata.
 *
 * @see GridInitializer
 * @see BrainCell
 * @see BrainState
 */
public class InitBrainRandom implements GridInitializer<BrainCell, BrainState> {

    /**
     * Random number generator for state assignment.
     */
    private final Random random = new Random();

    /**
     * Initializes the specified grid by assigning each cell a random state: Ready (70% probability),
     * Firing (15% probability), or Refractory (15% probability).
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(Grid<BrainCell, BrainState> grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        grid.setNextStates(new BrainState[height][width]);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                grid.getNextStates()[y][x] = new BrainState();

                // Randomly assign Ready, Firing, or Refractory state
                double rand = random.nextDouble();
                BrainState.BrainStateValue value;
                BrainState.BrainStateValue echo = BrainState.BrainStateValue.READY;
                if (rand < 0.70) {
                    value = BrainState.BrainStateValue.READY;
                } else if (rand < 0.85) {
                    value = BrainState.BrainStateValue.FIRING;
                } else {
                    value = BrainState.BrainStateValue.REFRACTORY;
                }

                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BrainCell(x, y, new BrainState(value, echo)));
                } else {
                    grid.getCell(x, y).setState(value, echo);
                }
            }
        }
    }
}