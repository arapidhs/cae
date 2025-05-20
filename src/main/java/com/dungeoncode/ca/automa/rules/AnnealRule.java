package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the ANNEAL rule for a cellular automaton, where a cell becomes active if it has exactly 4 or 6 or
 * more live cells in its 3x3 Moore neighborhood (including the center), and inactive otherwise. This voting rule,
 * a variation of MAJORITY, encourages reshuffling at boundaries, leading to gradual annealing of domains with
 * straighter boundaries over time. This rule, proposed by Gerard Vichniac, is described in Chapter 5, Section 5.4
 * of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanCell
 * @see BooleanState
 */
public class AnnealRule extends BooleanNeighborCountRule {

    /**
     * Applies the ANNEAL rule to compute the new state of a given cell in the grid. Counts the number of live
     * cells in the 3x3 Moore neighborhood (including the center). If the count is exactly 4 or 6 or more, the
     * cell becomes active (true); otherwise, it becomes inactive (false). This results in interpenetrating domains
     * with dynamic boundaries that straighten over time, modeling surface tension.
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

        // Apply the decision table: active if exactly 4 or 6+ live cells, inactive otherwise
        boolean isActive = liveNeighbors == 4 || liveNeighbors >= 6;

        boolean echo=cell.getState().getValue();
        grid.getNextStates()[y][x].set(isActive,echo,liveNeighbors);
        return grid.getNextStates()[y][x];
    }
}