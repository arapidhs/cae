package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.RuleCategory;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the LICHENS-WITH-DEATH rule for a cellular automaton, where a cell becomes active if it has exactly
 * 3, 7, or 8 live neighbors, becomes inactive if it has exactly 4 live neighbors, and remains unchanged otherwise,
 * in its Moore neighborhood (eight surrounding cells, excluding the center). This competitive growth introduces
 * a death mechanism, producing a complex, unpredictable pattern. This rule is described in Chapter 5, Section 5.3
 * of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleLichensWithDeath extends RuleBooleanNeighborCount {

    /**
     * Applies the LICHENS-WITH-DEATH rule to compute the new state of a given cell in the grid. Counts the number
     * of live neighbors in the Moore neighborhood (eight surrounding cells, excluding the center). If the count is
     * exactly 3, 7, or 8, the cell becomes active (true); if the count is 4, the cell becomes inactive (false);
     * otherwise, it retains its current state. This results in competitive growth with complex, unpredictable
     * long-term behavior.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        boolean currentState = cell.getState().getValue();

        // Count live neighbors in the Moore neighborhood (excluding the center)
        int liveCount = countLiveMooreNeighbors(grid, x, y);

        // Apply the decision table: activate for 3, 7, or 8 live neighbors, deactivate for 4, otherwise retain state
        boolean newState;
        if (liveCount == 3 || liveCount == 7 || liveCount == 8) {
            newState = true; // Turn on
        } else if (liveCount == 4) {
            newState = false; // Turn off (death mechanism)
        } else {
            newState = currentState; // Retain current state
        }

        boolean echo = cell.getState().getValue();
        grid.getNextStates()[y][x].set(newState, echo, liveCount);
        return grid.getNextStates()[y][x];
    }

    @Override
    public RuleCategory getRuleCategory() {
        return RuleCategory.DETERMINISTIC;
    }
}