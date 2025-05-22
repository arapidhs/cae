package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the SQUARES rule for a cellular automaton, where a cell's next state is the logical OR of its
 * Moore neighborhood (center and eight surrounding cells). A single active cell grows into a uniformly
 * expanding square of active cells, demonstrating monotonic, unconstrained growth. Described in Chapter 5,
 * Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleSquares extends RuleBooleanNeighborCount {

    /**
     * Constructs a new SQUARES rule with a fixed ID.
     */
    public RuleSquares() {
        super(5);
    }

    /**
     * Applies the SQUARES rule to compute the new state of a cell. Performs a logical OR across the Moore
     * neighborhood (center and eight surrounding cells). The cell becomes active (true) if any cell in the
     * neighborhood is active, otherwise remains inactive (false). Updates the grid's intermediate state with
     * the new state, echo, and neighbor count, producing a growing square pattern.
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

        // Logical OR across Moore neighborhood (center + 8 surrounding cells)
        boolean isActive = false;
        for (int dy = -1; dy <= 1 && !isActive; dy++) {
            for (int dx = -1; dx <= 1 && !isActive; dx++) {
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                if (grid.getCell(nx, ny).getState().getValue()) {
                    isActive = true;
                }
            }
        }

        boolean echo = cell.getState().getValue();
        int liveCount = countLiveMooreNeighbors(grid, x, y);

        BooleanState[][] intermediateStates = grid.getIntermediateStates();
        intermediateStates[y][x].set(isActive, echo, liveCount);
        return intermediateStates[y][x];
    }
}