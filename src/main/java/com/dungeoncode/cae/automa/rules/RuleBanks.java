package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the BANKS rule for a cellular automaton, where a cell's state is updated based on its von Neumann
 * neighborhood (north, south, east, west, excluding the center) to "fill pockets, erase corners" in patterns of
 * active cells, enabling computing circuitry construction. Proposed by Banks, this rule is described in
 * Chapter 5, Section 5.5 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleBanks extends RuleBooleanNeighborCount {

    /**
     * Constructs a new BANKS rule with a fixed ID.
     */
    public RuleBanks() {
        super(13);
    }

    /**
     * Applies the BANKS rule to compute the new state of a given cell. Uses the von Neumann neighborhood
     * (north, south, east, west, excluding the center). If north and south neighbors differ, the cell becomes
     * inactive; if exactly two neighbors are active and form a corner (e.g., north and west), the cell becomes
     * inactive (erase corners); if they form a straight line (e.g., north and south), the cell becomes active
     * (fill pockets); otherwise, the cell remains unchanged.nmkj Updates the grid's intermediate state with the new
     * state, echo, and neighbor count.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

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