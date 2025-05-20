package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the MAJORITY rule for a cellular automaton, where a cell adopts the state of the majority in its
 * 3x3 Moore neighborhood (including the center cell). If 5 or more cells are active, the cell becomes active;
 * otherwise, it becomes inactive. This voting rule consolidates regions into interpenetrating domains. This rule
 * is described in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleMajority extends RuleBooleanNeighborCount {

    /**
     * Applies the MAJORITY rule to compute the new state of a given cell in the grid. Counts the number of live
     * cells in the 3x3 Moore neighborhood (including the center). If the count is 5 or more, the cell becomes
     * active (true); otherwise, it becomes inactive (false). This results in interpenetrating black and white
     * domains with stable boundaries.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();

        // Count live neighbors in the Moore neighborhood (excluding the center)
        int liveNeighbors = countLiveMooreNeighbors(grid, x, y);
        // Include the center cell in the count (9SUM includes CENTER)
        if (cell.getState().getValue()) {
            liveNeighbors++;
        }

        // Apply the decision table: active if 5 or more live cells, inactive otherwise
        boolean isActive = liveNeighbors >= 5;

        boolean echo = cell.getState().getValue();
        grid.getNextStates()[y][x].set(isActive, echo, liveNeighbors);
        return grid.getNextStates()[y][x];
    }
}