package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the BANKS rule for a cellular automaton, where a cell's state is updated based on its von Neumann
 * neighborhood (north, south, east, west, excluding the center) to "fill pockets, erase corners" in patterns of
 * active cells, enabling the construction of computing circuitry. This rule, proposed by Banks, is described in
 * Chapter 5, Section 5.5 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanCell
 * @see BooleanState
 */
public class BanksRule extends BooleanNeighborCountRule {

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
    @SuppressWarnings("ConstantValue")
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
                if (northState && southState || eastState && westState) {
                    return new BooleanState(true, echo, liveNeighbors); // Straight line (e.g., north-south or west-east): fill pocket (cell becomes active)
                } else {
                    return new BooleanState(false, echo, liveNeighbors); // Straight line (e.g., north-south or west-east): fill pocket (cell becomes active)
                }
            }
        } else if (liveNeighbors > 2) {
            return new BooleanState(true, echo, liveNeighbors); // Corner configuration: erase corner (cell becomes inactive)
        }

        grid.getNextStates()[y][x].set(currentState,echo,liveNeighbors);
        return grid.getNextStates()[y][x];
    }
}