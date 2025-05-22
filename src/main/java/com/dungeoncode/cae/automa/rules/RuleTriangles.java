package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.Rule;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements the TRIANGLES rule for a cellular automaton, where a cell's next state is the logical OR of a
 * subset of its von Neumann neighborhood (north, west, center, east). A single active cell (seed) grows into
 * a uniformly expanding triangle pointing south, breaking four-fold symmetry. This rule is described in
 * Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see Rule
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleTriangles extends RuleBooleanNeighborCount {

    public RuleTriangles() {
        super(7);
    }

    /**
     * Applies the TRIANGLES rule to compute the new state of a given cell in the grid. Uses a subset of the
     * von Neumann neighborhood (north, west, center, east) to perform a logical OR operation across these states.
     * If any cell in this neighborhood is active (true), the center cell becomes active; otherwise, it remains
     * inactive (false). This results in a growing triangle of active cells pointing south from a single seed.
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

        // Perform logical OR across the von Neumann neighborhood subset (north, west, center, east)
        boolean isActive = grid.getCell(x, y).getState().getValue();
        // Center cell
        // North neighbor
        if (!isActive) {
            int nx = x;
            int ny = (y - 1 + height) % height;
            if (grid.getCell(nx, ny).getState().getValue()) {
                isActive = true;
            }
        }
        // West neighbor
        if (!isActive) {
            int nx = (x - 1 + width) % width;
            int ny = y;
            if (grid.getCell(nx, ny).getState().getValue()) {
                isActive = true;
            }
        }
        // East neighbor
        if (!isActive) {
            int nx = (x + 1) % width;
            int ny = y;
            if (grid.getCell(nx, ny).getState().getValue()) {
                isActive = true;
            }
        }

        boolean echo = cell.getState().getValue();
        int liveCount = countLiveMooreNeighbors(grid, x, y);
        grid.getIntermediateStates()[y][x].set(isActive, echo, liveCount);
        return grid.getIntermediateStates()[y][x];
    }

}