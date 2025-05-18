package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the rule for Conway's Game of Life cellular automaton. Determines the next state of a cell
 * based on the number of live neighbors in its 3x3 neighborhood. A live cell survives with 2 or 3 live
 * neighbors, dies otherwise; a dead cell becomes alive with exactly 3 live neighbors.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanState
 */
public class GameOfLifeRule extends BooleanNeighborCountRule {

    /**
     * Applies the Game of Life rule to a cell in the specified grid. Evaluates the number of live neighbors
     * and returns the cell's next state: live cells survive with 2 or 3 live neighbors, die otherwise;
     * dead cells become alive with exactly 3 live neighbors.
     *
     * @param grid the grid containing the cell
     * @param cell the cell to evaluate
     * @return the next state of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int liveCount = countLiveMooreNeighbors(grid, x, y);
        boolean echo=cell.getState().getValue();
        if (cell.getState().getValue()) {
            return new BooleanState(liveCount == 2 || liveCount == 3,echo,liveCount);
        } else {
            return new BooleanState(liveCount == 3,echo,liveCount);
        }
    }
}