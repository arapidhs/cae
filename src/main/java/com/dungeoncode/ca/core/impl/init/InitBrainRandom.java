package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.AbstractGridInitializer;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Initializes a {@link Grid} for the BRIAN'S-BRAIN cellular automaton by randomly assigning Ready, Firing, or Refractory
 * states to cells with a probability distribution (70% Ready, 15% Firing, 15% Refractory). Supports dynamic initial
 * patterns for second-order neural firing automata, as described in <i>Cellular Automata Machines</i> (MIT Press).
 *
 * @see BrainCell
 * @see BrainState
 * @see AbstractGridInitializer
 */
public class InitBrainRandom extends AbstractGridInitializer<BrainCell, BrainState> {

    /**
     * Random number generator for state assignment.
     */
    private final Random random = new Random();

    /**
     * Constructs a new BRIAN'S-BRAIN grid initializer with a fixed ID.
     */
    public InitBrainRandom() {
        super(1);
    }

    /**
     * Initializes the specified grid by assigning each cell a random {@link BrainState}: Ready (70% probability),
     * Firing (15% probability), or Refractory (15% probability). Sets the grid's intermediate states for consistency.
     *
     * @param grid the {@link Grid} to initialize, must not be null
     * @throws NullPointerException if grid is null
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BrainCell, BrainState> grid) {
        java.util.Objects.requireNonNull(grid, "Grid cannot be null");
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Initialize intermediate states array
        BrainState[][] intermediateStates = new BrainState[height][width];
        grid.setIntermediateStates(intermediateStates);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
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

                // Set intermediate state
                intermediateStates[y][x] = new BrainState(value, echo);

                // Initialize or update cell
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BrainCell(x, y, new BrainState(value, echo)));
                } else {
                    grid.getCell(x, y).setState(value, echo);
                }
            }
        }
    }
}