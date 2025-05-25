package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements the ONED-RAND rule for a one-dimensional cellular automaton, where the current row computes its state as
 * the XOR of west, center, and east neighbors from the row above, forming a random-number generator. Extends
 * RuleOneDScroll to use its scrolling logic, computing row t at step t, with no shifting until step >= height, then
 * scrolls the history. Described in Chapter 9, Section 9.7 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i> (MIT Press).
 *
 * @see RuleOneDScroll
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleOneDRand extends RuleOneDScroll {

    /**
     * Constructs a new ONED-RAND rule with a fixed ID.
     */
    public RuleOneDRand() {
        super(27);
    }

    /**
     * Computes the new state for a cell in the target row as the XOR of west, center, and east neighbors from the
     * row above (y-1), using toroidal wrapping for left/right edges.
     *
     * @param grid  the {@link Grid} containing the cell and its neighbors
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell (target row)
     * @param width the grid width for toroidal wrapping
     * @return the new boolean state (XOR of west, center, east neighbors)
     */
    @Override
    protected boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int width) {
        boolean west = grid.getCell((x - 1 + width) % width, y).getState().getValue();
        boolean center = grid.getCell(x, y).getState().getValue();
        boolean east = grid.getCell((x + 1) % width, y).getState().getValue();
        return west ^ center ^ east;
    }
}

//package com.dungeoncode.cae.automa.rules;
//
//import com.dungeoncode.cae.core.Grid;
//import com.dungeoncode.cae.core.impl.BooleanCell;
//import com.dungeoncode.cae.core.impl.BooleanState;
//
///**
// * Implements the SCARVES rule for a one-dimensional cellular automaton, where row t at step t sets a cell's state to
// * true if exactly two of its four neighbors (west, east, west-of-west, east-of-east) from row t-1 are active, else false.
// * The 2D grid stores spacetime history, scrolling up when full with toroidal left/right wrapping. Described in Chapter 9,
// * Section 9.7 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
// *
// * @see RuleOneDScroll
// * @see BooleanCell
// * @see BooleanState
// */
//public class RuleScarves extends RuleOneDScroll {
//
//    /**
//     * Constructs a new SCARVES rule with a fixed ID.
//     */
//    public RuleScarves() {
//        super(28); // New ID, adjust as needed
//    }
//
//    /**
//     * Computes the new state for a cell in the target row. The state is true if exactly two of the four neighbors
//     * (west, east, west-of-west, east-of-east) from the row above (y-1) are active, else false, using toroidal wrapping
//     * for left/right edges.
//     *
//     * @param grid  the {@link Grid} containing the cell and its neighbors
//     * @param x     the x-coordinate of the cell
//     * @param y     the y-coordinate of the cell (target row)
//     * @param width the grid width for toroidal wrapping
//     * @return the new boolean state
//     */
//    @Override
//    protected boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int width) {
//        // Get neighbor states from row above (y-1)
//        boolean west = grid.getCell((x - 1 + width) % width, y - 1).getState().getValue();
//        boolean east = grid.getCell((x + 1) % width, y - 1).getState().getValue();
//        boolean westOfWest = grid.getCell((x - 2 + width) % width, y - 1).getState().getValue();
//        boolean eastOfEast = grid.getCell((x + 2) % width, y - 1).getState().getValue();
//
//        // Count active neighbors
//        int activeCount = (west ? 1 : 0) + (east ? 1 : 0) + (westOfWest ? 1 : 0) + (eastOfEast ? 1 : 0);
//
//        // State is true if exactly two neighbors are active
//        return activeCount == 2;
//    }
//}