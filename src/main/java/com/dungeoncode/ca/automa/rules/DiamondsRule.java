package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the DIAMONDS rule for a cellular automaton, where a cell's next state is the logical OR of its
 * von Neumann neighborhood (center and four orthogonal cells: north, south, east, west). A single active cell
 * (seed) grows into a uniformly expanding diamond of active cells, demonstrating monotonic, unconstrained growth.
 * This rule is described in Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see Rule
 * @see BooleanCell
 * @see BooleanState
 */
public class DiamondsRule extends BooleanNeighborCountRule {

    /**
     * Applies the DIAMONDS rule to compute the new state of a given cell in the grid. Uses the von Neumann
     * neighborhood (center and four orthogonal cells: north, south, east, west) to perform a logical OR operation
     * across all states. If any cell in the neighborhood is active (true), the center cell becomes active;
     * otherwise, it remains inactive (false). This results in a growing diamond of active cells from a single seed.
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

        // Perform logical OR across the von Neumann neighborhood (center + 4 orthogonal cells)
        boolean isActive = false;
        // Center cell
        if (grid.getCell(x, y).getState().getValue()) {
            isActive = true;
        }
        // North neighbor
        if (!isActive) {
            int nx = x;
            int ny = (y - 1 + height) % height;
            if (grid.getCell(nx, ny).getState().getValue()) {
                isActive = true;
            }
        }
        // South neighbor
        if (!isActive) {
            int nx = x;
            int ny = (y + 1) % height;
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

        boolean echo=cell.getState().getValue();
        int liveCount= countLiveVonNeumannNeighbors(grid,x,y);

        grid.getNextStates()[y][x].set(isActive,echo,liveCount);
        return grid.getNextStates()[y][x];
    }
}