package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements the LICHENS rule for a cellular automaton, where a cell becomes active if it has exactly 3, 7, or 8
 * live neighbors in its Moore neighborhood (eight surrounding cells, excluding the center), and remains unchanged
 * otherwise. This constrained growth requires a seed of at least three cells to initiate and produces an irregular,
 * lichen-like pattern. This rule is described in Chapter 5, Section 5.2 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleLichens extends RuleBooleanNeighborCount {

    public RuleLichens() {
        super(9);
    }

    /**
     * Applies the LICHENS rule to compute the new state of a given cell in the grid. Counts the number of live
     * neighbors in the Moore neighborhood (eight surrounding cells, excluding the center). If the count is exactly
     * 3, 7, or 8, the cell becomes active (true); otherwise, it retains its current state. This results in
     * constrained, lichen-like growth from a seed of at least three cells.
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

        // Apply the decision table: activate if exactly 3, 7, or 8 live neighbors, otherwise retain state
        boolean isActive = liveCount == 3 || liveCount == 7 || liveCount == 8;


        boolean echo = cell.getState().getValue();
        grid.getIntermediateStates()[y][x].set(isActive || currentState, echo, liveCount);
        return grid.getIntermediateStates()[y][x];
    }

}