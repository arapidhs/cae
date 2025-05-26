package com.dungeoncode.cae.automa.rule;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the ONE-OR-FOUR rule for a two-dimensional cellular automaton, where a cell becomes active (black) if
 * exactly one or all four of its von Neumann neighbors (north, south, east, west) are active, otherwise retaining its
 * current state. This produces sparse, structured patterns from a small active seed. Inspired by two-dimensional
 * cellular automata concepts in <i>A New Kind of Science</i> by Stephen Wolfram.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleOneOrFour extends RuleBooleanNeighborCount {

    /**
     * Constructs a new ONE-OR-FOUR rule with a fixed ID.
     */
    public RuleOneOrFour() {
        super(31); // Rule ID
    }

    /**
     * Applies the ONE-OR-FOUR rule to compute the new state of a given cell. Counts the number of active neighbors
     * in the von Neumann neighborhood (north, south, east, west). If exactly one or all four neighbors are active,
     * the cell becomes active (true); otherwise, it retains its current state. Updates the grid's intermediate state
     * with the new state, echo, and neighbor count.
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
        boolean currentState = cell.getState().getValue();
        boolean echo = cell.getState().isEcho();

        // Count live neighbors in the von Neumann neighborhood (north, south, east, west)
        int liveNeighbors = countLiveVonNeumannNeighbors(grid, x, y);

        // Apply the rule: activate if exactly one or all four neighbors are active
        boolean isActive = liveNeighbors == 1 || liveNeighbors == 4;

        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(isActive || currentState, echo, liveNeighbors);
        return intermediateStates[y][x];
    }
}