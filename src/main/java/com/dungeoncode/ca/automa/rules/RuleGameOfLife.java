package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the rule for Conway's Game of Life cellular automaton with an optional tracing mechanism.
 * Determines the next state of a cell based on the standard Game of Life rules and manages an echo flag
 * to track state transitions. When tracing is enabled, the echo flag persists for cells that were ever alive,
 * creating a permanent record of activity; otherwise, the echo flag reflects the cell's previous state for
 * short-term tracking. This rule is inspired by the tracing technique described in Chapter 3, Section 3.3
 * of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanState
 */
public class RuleGameOfLife extends RuleBooleanNeighborCount {

    /**
     * Indicates whether tracing mode is enabled for persistent echo tracking.
     */
    private final boolean tracingEnabled;

    /**
     * Constructs a new Game of Life rule with the specified tracing mode.
     *
     * @param tracingEnabled {@code true} to enable persistent tracing, {@code false} for short-term echo
     */
    public RuleGameOfLife(boolean tracingEnabled) {
        super((2));
//        super(
//                // Rule Type
//                Tag.DETERMINISTIC,  // Rule is deterministic, no random elements
//                // Operation Types
//                Tag.COUNTING,     // Counts live neighbors
//
//                // Behavior Types
//                Tag.STABLE,       // Can produce stable patterns
//                Tag.OSCILLATION,  // Can produce oscillating patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC,
//                Tag.FOUNDATIONAL
//        );
        this.tracingEnabled = tracingEnabled;
    }

    /**
     * Applies the Game of Life rule to a cell in the specified grid, updating its state and echo flag.
     * Uses standard Game of Life rules: live cells survive with 2 or 3 live neighbors, die otherwise;
     * dead cells become alive with exactly 3 live neighbors. The echo flag is set based on tracing mode:
     * in tracing mode, it persists for cells that were ever alive; otherwise, it tracks the cell’s previous
     * state for short-term history.
     *
     * @param grid the grid containing the cell
     * @param cell the cell to evaluate
     * @return the next state of the cell, including its boolean value and echo flag
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();

        // Apply standard Game of Life rules
        boolean newAlive;
        int liveCount = countLiveMooreNeighbors(grid, x, y);
        if (cell.getState().getValue()) {
            newAlive = liveCount == 2 || liveCount == 3;
        } else {
            newAlive = liveCount == 3;
        }

        // Determine echo flag based on tracing mode
        boolean newEcho;
        if (tracingEnabled) {
            newEcho = cell.getState().isEcho() || cell.getState().getValue();
        } else {
            newEcho = cell.getState().getValue();
        }
        grid.getIntermediateStates()[y][x].set(newAlive, newEcho, liveCount);
        return grid.getIntermediateStates()[y][x];
    }

}