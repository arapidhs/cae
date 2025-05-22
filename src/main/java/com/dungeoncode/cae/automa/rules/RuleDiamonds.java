package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the DIAMONDS rule for a cellular automaton, where a cell's next state is the logical OR of its
 * von Neumann neighborhood (center and four orthogonal cells: north, south, east, west). A single active cell
 * grows into a uniformly expanding diamond of active cells, demonstrating monotonic, unconstrained growth.
 * Described in Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>
 * (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleDiamonds extends RuleBooleanNeighborCount {

    /**
     * Constructs a new DIAMONDS rule with a fixed ID.
     */
    public RuleDiamonds() {
        super(6);
    }

    /**
     * Applies the DIAMONDS rule to compute the new state of a cell. Performs a logical OR across the von Neumann
     * neighborhood (center and four orthogonal cells: north, south, east, west). The cell becomes active (true)
     * if any cell in the neighborhood is active, otherwise remains inactive (false). Updates the grid's
     * intermediate state with the new state, echo, and neighbor count, producing a growing diamond pattern.
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

        // Logical OR across von Neumann neighborhood (center + 4 orthogonal cells)
        boolean isActive = cell.getState().getValue() ||
                grid.getCell(x, (y - 1 + height) % height).getState().getValue() || // North
                grid.getCell(x, (y + 1) % height).getState().getValue() ||         // South
                grid.getCell((x - 1 + width) % width, y).getState().getValue() ||  // West
                grid.getCell((x + 1) % width, y).getState().getValue();            // East

        boolean echo = cell.getState().getValue();
        int liveCount = countLiveVonNeumannNeighbors(grid, x, y);

        BooleanState[][] intermediateStates = grid.getIntermediateStates();
        intermediateStates[y][x].set(isActive, echo, liveCount);
        return intermediateStates[y][x];
    }
}