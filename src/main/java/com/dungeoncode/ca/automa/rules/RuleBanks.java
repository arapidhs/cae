package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the BANKS rule for a cellular automaton, where a cell's state is updated based on its von Neumann
 * neighborhood (north, south, east, west, excluding the center) to "fill pockets, erase corners" in patterns of
 * active cells, enabling the construction of computing circuitry. This rule, proposed by Banks, is described in
 * Chapter 5, Section 5.5 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleBanks extends RuleBooleanNeighborCount {

    public RuleBanks() {
        super(13);
//        super(
//                // Rule Type
//                Tag.DETERMINISTIC,  // Rule is deterministic, no random elements
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors plus center
//
//                // Operation Types
//                Tag.COUNTING,      // Counts live neighbors
//                Tag.LOGICAL,       // Uses logical operations for pattern shaping
//                Tag.PATTERN_SHAPING, // Specifically designed for pattern modification
//
//                // Source Type
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    /**
     * Applies the BANKS rule to compute the new state of a given cell in the grid. Uses the von Neumann
     * neighborhood (north, south, east, west, excluding the center) with the following transitions: if north and
     * south neighbors differ, the cell becomes inactive; if exactly two neighbors are active and form a corner
     * (e.g., north and west), the cell becomes inactive (erase corners); if they form a straight line (e.g., north
     * and south), the cell becomes active (fill pockets); otherwise, the cell remains unchanged. This shapes patterns
     * to support computing circuitry.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        boolean currentState = cell.getState().getValue();
        boolean echo = currentState;

        // Count live neighbors in the von Neumann neighborhood (excluding the center)
        int liveNeighbors = countLiveVonNeumannNeighbors(grid, x, y);

        // Check north and south neighbors
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        boolean northState = grid.getCell(nxNorth, nyNorth).getState().getValue();
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        boolean southState = grid.getCell(nxSouth, nySouth).getState().getValue();

        // Handle the case of exactly 2 live neighbors: check for corner or straight line
        boolean nextState = currentState;
        if (liveNeighbors == 2) {
            // Check west and east neighbors
            int nxWest = (x - 1 + width) % width;
            int nyWest = y;
            boolean westState = grid.getCell(nxWest, nyWest).getState().getValue();
            int nxEast = (x + 1) % width;
            int nyEast = y;
            boolean eastState = grid.getCell(nxEast, nyEast).getState().getValue();

            // Check if the two live neighbors form a corner (at 90°) or a straight line
            if (currentState) {
                // Straight line (e.g., north-south or west-east): fill pocket (cell becomes active)
                nextState = northState && southState || eastState && westState; // Straight line (e.g., north-south or west-east): fill pocket (cell becomes active)
            }
        } else if (liveNeighbors > 2) {
            nextState = true; // Corner configuration: erase corner (cell becomes inactive)
        }

        grid.getIntermediateStates()[y][x].set(nextState, echo, liveNeighbors);
        return grid.getIntermediateStates()[y][x];
    }

}