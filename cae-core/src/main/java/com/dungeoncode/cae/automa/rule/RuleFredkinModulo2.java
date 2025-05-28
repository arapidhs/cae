package com.dungeoncode.cae.automa.rule;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements a two-dimensional cellular automaton rule, invented by Edward Fredkin, where a cell’s state is determined
 * by the sum of its active von Neumann neighbors (north, south, east, west) modulo 2. If the sum is odd (1 or 3), the
 * cell becomes active (true); if even (0, 2, or 4), it becomes inactive (false). The grid stores patterns, typically
 * initialized with random active cells. Described in <i>Information Processing and Transmission in Cellular Automata</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleFredkinModulo2 extends RuleBooleanNeighborCount {

    /**
     * Constructs a new Fredkin Modulo 2 rule with a fixed ID.
     */
    public RuleFredkinModulo2() {
        super(33); // Rule ID
    }

    /**
     * Applies the rule to compute the new state of a cell. Counts active neighbors in the von Neumann neighborhood
     * (north, south, east, west). Sets the cell to active (true) if the sum of active neighbors is odd (1 or 3),
     * or inactive (false) if even (0, 2, or 4). Updates the grid’s intermediate state with the new state, echo,
     * and Moore neighborhood live sum for rendering.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @param step the current step
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell, int step) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        boolean echo = cell.getState().isEcho();

        // Count active neighbors in the von Neumann neighborhood
        int activeNeighbors = countLiveVonNeumannNeighbors(grid, x, y);

        // Compute new state: active (true) if sum is odd, inactive (false) if even
        boolean isActive = (activeNeighbors % 2) == 1;

        // Calculate live sum for Moore neighborhood (used in rendering)
        int liveSum = countLiveMooreNeighbors(grid, x, y);

        // Update the cell’s state in the intermediate state array
        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(isActive, echo, liveSum, 0);
        return intermediateStates[y][x];
    }
}