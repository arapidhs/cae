package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the SQUARES rule for a cellular automaton, where a cell's next state is the logical OR of its
 * Moore neighborhood (center and eight surrounding cells). A single active cell (seed) grows into a uniformly
 * expanding square of active cells, demonstrating monotonic, unconstrained growth. This rule is described in
 * Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see Rule
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleSquares extends RuleBooleanNeighborCount {

    public RuleSquares() {
        super(5);
//        super(
//                // Rule Type
//                Tag.DETERMINISTIC,  // Rule is deterministic, no random elements
//
//                // Neighborhood Type
//                Tag.MOORE,         // Uses 8 surrounding cells plus center
//
//                // Operation Types
//                Tag.LOGICAL,       // Uses logical OR operation
//                Tag.LOGICAL_OR,    // Specifically uses OR operation
//
//                // Behavior Types
//                Tag.GROWTH,        // Implements growth patterns
//                Tag.MONOTONIC,     // Shows unidirectional, non-reversible growth
//                Tag.STRUCTURED,    // Creates organized square patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    /**
     * Applies the SQUARES rule to compute the new state of a given cell in the grid. Uses the Moore
     * neighborhood (eight surrounding cells plus the center) to perform a logical OR operation across all
     * states. If any cell in the neighborhood is active (true), the center cell becomes active; otherwise,
     * it remains inactive (false). This results in a growing square of active cells from a single seed.
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

        // Perform logical OR across the Moore neighborhood (center + 8 surrounding cells)
        boolean isActive = false;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                BooleanState neighborState = grid.getCell(nx, ny).getState();
                if (neighborState.getValue()) {
                    isActive = true;
                    break; // Exit loop once any active cell is found, as OR result is determined
                }
            }
            if (isActive) break;
        }

        boolean echo = cell.getState().getValue();
        int liveCount = countLiveMooreNeighbors(grid, x, y);
        grid.getNextStates()[y][x].set(isActive, echo, liveCount);
        return grid.getNextStates()[y][x];
    }

}