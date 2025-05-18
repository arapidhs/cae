package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the 1-OUT-OF-8 rule for a cellular automaton, where a cell becomes active if it has exactly one
 * live neighbor in its Moore neighborhood (eight surrounding cells, excluding the center), and remains unchanged
 * otherwise. This constrained growth produces a sparse, fractal-like pattern from a single active cell (seed).
 * This rule is described in Chapter 5, Section 5.2 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanCell
 * @see BooleanState
 */
public class OneOutOfEightRule extends BooleanNeighborCountRule {

    /**
     * Applies the 1-OUT-OF-8 rule to compute the new state of a given cell in the grid. Counts the number of
     * live neighbors in the Moore neighborhood (eight surrounding cells, excluding the center). If exactly one
     * neighbor is live, the cell becomes active (true); otherwise, it retains its current state. This results
     * in sparse, fractal-like growth from a single seed.
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
        int liveNeighbors = countLiveMooreNeighbors(grid, x, y);

        // Apply the rule: activate if exactly one live neighbor, otherwise retain current state
        boolean isActive = liveNeighbors == 1;

        boolean echo=cell.getState().getValue();
        return new BooleanState(isActive || currentState,echo,liveNeighbors);
    }
}