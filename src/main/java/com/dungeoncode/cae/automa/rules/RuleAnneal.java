package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the ANNEAL rule for a cellular automaton, a voting rule variation of MAJORITY where a cell becomes
 * active if it has exactly 4 or 6 or more live cells in its 3x3 Moore neighborhood (including the center), and
 * inactive otherwise. Proposed by Gerard Vichniac, this rule encourages boundary reshuffling, leading to gradual
 * domain annealing with straighter boundaries, modeling surface tension. Described in Chapter 5, Section 5.4 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleAnneal extends RuleBooleanNeighborCount {

    /**
     * Constructs a new ANNEAL rule with a fixed ID.
     */
    public RuleAnneal() {
        super(12);
    }

    /**
     * Applies the ANNEAL rule to compute the new state of a given cell. Counts live cells in the 3x3 Moore
     * neighborhood (including the center). The cell becomes active (true) if the count is exactly 4 or 6 or more,
     * otherwise inactive (false). Updates the grid's intermediate state with the new state, echo, and neighbor count.
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

        // Count live neighbors in Moore neighborhood (excluding center)
        int liveNeighbors = countLiveMooreNeighbors(grid, x, y);
        // Include center cell (9SUM)
        if (cell.getState().getValue()) {
            liveNeighbors++;
        }

        // Decision table: active for 4 or 6+ live cells, inactive otherwise
        boolean isActive = liveNeighbors == 4 || liveNeighbors >= 6;
        boolean echo = cell.getState().getValue();

        BooleanState[][] intermediateStates = grid.getIntermediateStates();
        intermediateStates[y][x].set(isActive, echo, liveNeighbors);
        return intermediateStates[y][x];
    }
}