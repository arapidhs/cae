package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements the SCARVES rule for a one-dimensional cellular automaton, a second-order reversible rule where a cell
 * flips its past state (echo) if exactly two of its four neighbors (west, east, west-of-west, east-of-east) from the
 * row above are active. The 2D grid stores spacetime history, computing row t at step t, scrolling up when full of
 * toroidal left/right wrapping. Described in Chapter 9, Section 9.7 of <i>Cellular Automata Machines: A New Environment
 * for Modeling</i> (MIT Press).
 *
 * @see RuleOneDScroll
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleScarves extends RuleOneDScroll {

    /**
     * Constructs a new SCARVES rule with a fixed ID.
     */
    public RuleScarves() {
        super(28); // New ID, adjust as needed
    }

    /**
     * Computes the new state for a cell in the target row. The state flips (XOR with past state in echo) if exactly
     * two of the four neighbors (west, east, west-of-west, east-of-east) from the row above (y-1) are active, using
     * toroidal wrapping for left/right edges.
     *
     * @param grid  the {@link Grid} containing the cell and its neighbors
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell (target row)
     * @param width the grid width for toroidal wrapping
     * @return the new boolean state
     */
    @Override
    protected boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int width) {
        // Get neighbor states from row above (y-1)
        boolean currentValue = grid.getCell((x + width) % width, y).getState().getValue();
        boolean west = grid.getCell((x - 1 + width) % width, y).getState().getValue();
        boolean east = grid.getCell((x + 1) % width, y).getState().getValue();
        boolean westOfWest = grid.getCell((x - 2 + width) % width, y).getState().getValue();
        boolean eastOfEast = grid.getCell((x + 2) % width, y).getState().getValue();

        // Count active neighbors
        int activeCount = (west ? 1 : 0) + (east ? 1 : 0) + (westOfWest ? 1 : 0) + (eastOfEast ? 1 : 0);

        // Flip past state if exactly two neighbors are active
        if(activeCount==2)
            return !currentValue;
        return currentValue;
    }

}