package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.AbstractRule;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the Parity rule for a cellular automaton, where a cell's next state is determined by the
 * parity (odd or even count) of live cells in its von Neumann neighborhood (center, north, south, west,
 * east). The state is live if the number of live cells is odd, dead if even, computed as the XOR of the
 * neighborhood states. This rule, known for its linearity and pattern replication properties, is described
 * in Chapter 4, Section 4.2 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see Rule
 * @see BooleanState
 */
public class RuleParity extends AbstractRule<BooleanCell, BooleanState> {

    public RuleParity() {
        super(4);
//        super(
//                // Rule Type
//                Tag.DETERMINISTIC,  // Rule is deterministic, no random elements
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors plus center
//
//                // Operation Types
//                Tag.LOGICAL,       // Uses XOR operations for parity
//
//                // Behavior Types
//                Tag.STRUCTURED,    // Creates organized, self-replicating patterns
//                Tag.FRACTAL,        // Creates self-similar patterns that repeat at different scales
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    public RuleParity(int id) {
        super(id);
    }

    // .

    /**
     * Applies the Parity rule to a cell in the specified grid. Computes the next state by XORing the boolean
     * states of the cell and its four orthogonal neighbors (north, south, west, east). A cell becomes live
     * if the number of live cells in this neighborhood is odd, dead if even.
     *
     * @param grid the grid containing the cell
     * @param cell the cell to evaluate
     * @return the next state of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Get states of the von Neumann neighborhood (center, north, south, west, east)
        boolean center = cell.getState().getValue();
        boolean north = grid.getCell(x, (y - 1 + height) % height).getState().getValue();
        boolean south = grid.getCell(x, (y + 1) % height).getState().getValue();
        boolean west = grid.getCell((x - 1 + width) % width, y).getState().getValue();
        boolean east = grid.getCell((x + 1) % width, y).getState().getValue();

        // Compute the sum of live (true) states
        int liveSum = (center ? 1 : 0) + (north ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0) + (east ? 1 : 0);

        // Compute parity using XOR (odd number of true values yields true)
        boolean newState = center ^ north ^ south ^ west ^ east;
        grid.getIntermediateStates()[y][x].set(newState, cell.getState().getValue(), liveSum);
        return grid.getIntermediateStates()[y][x];
    }

}