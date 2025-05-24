package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Implements the SOIL-EROSION-RANDOM rule for a cellular automaton, a probabilistic variant of SOIL-EROSION. A cell
 * (soil, active=true) remains active if it has at least one active neighbor in each of the north, south, west, and east
 * directions in its 3x3 Moore neighborhood, and becomes inactive (eroded) otherwise, but updates only with probability
 * 1/512 to emulate asynchronous updates. This reduces synchronous update symmetries, as described in Chapter 9, Section 9.5
 * of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see RuleSoilErosion
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleSoilErosionRandom extends RuleSoilErosion {

    /** Random number generator for update probability. */
    private final Random random = new Random();

    /** Probability of updating a cell (1/512 as per book). */
    private static final double UPDATE_PROBABILITY = 1.0 / 512.0;

    /**
     * Constructs a new SOIL-EROSION-RANDOM rule with a fixed ID.
     */
    public RuleSoilErosionRandom() {
        super(25); // New ID, adjust as needed
    }

    /**
     * Applies the SOIL-EROSION-RANDOM rule to compute the new state of a given cell. With probability 1/512, applies
     * the SOIL-EROSION logic: the cell remains active if it has at least one active neighbor in each of the north, south,
     * west, and east directions in its 3x3 Moore neighborhood, otherwise becomes inactive. If not updated, retains the
     * current state. Updates the grid's intermediate state with the new state, echo, and neighbor count.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        boolean currentState = cell.getState().getValue();
        boolean echo = currentState;
        int liveNeighbors = countLiveMooreNeighbors(grid, x, y);

        // Determine if cell updates (random update with p=1/512)
        boolean shouldUpdate = random.nextDouble() < UPDATE_PROBABILITY;

        boolean nextState = currentState;
        if (shouldUpdate) {
            // Apply SOIL-EROSION logic from parent class
            BooleanState parentState = super.apply(grid, cell);
            nextState = parentState.getValue();
        }

        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(nextState, echo, liveNeighbors);
        return intermediateStates[y][x];
    }
}