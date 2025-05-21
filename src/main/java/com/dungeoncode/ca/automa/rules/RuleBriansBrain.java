package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.AbstractRule;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;

/**
 * Implements the BRIAN'S-BRAIN rule for a cellular automaton, where cells transition between Ready, Firing, and
 * Refractory states based on their Moore neighborhood (eight surrounding cells, excluding the center). A Ready cell
 * fires if exactly two neighbors are Firing; a Firing cell becomes Refractory; a Refractory cell returns to Ready.
 * This second-order dynamics rule produces fast-paced, dynamic patterns resembling neural firing waves. This rule,
 * suggested by Brian Silverman, is described in Chapter 6, Section 6.1 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see Rule
 * @see BrainCell
 * @see BrainState
 */
public class RuleBriansBrain extends AbstractRule<BrainCell, BrainState> {

    public RuleBriansBrain() {
        super(14);
//        super(
//                // Rule Type
//                Tag.SECOND_ORDER,  // Uses multiple states (Ready, Firing, Refractory) with state transitions
//
//                // Neighborhood Type
//                Tag.MOORE,        // Uses 8 surrounding cells (excluding center)
//
//                // Operation Types
//                Tag.COUNTING,     // Counts firing neighbors
//                Tag.STATE_MACHINE, // Implements a state machine with three distinct states and transitions
//
//                // Behavior Types
//                Tag.WAVE,          // Creates wave-like patterns
//                Tag.DYNAMIC,       // Fast-paced, changing patterns
//                Tag.NEURAL,
//
//                // Source Type
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    /**
     * Applies the BRIAN'S-BRAIN rule to compute the new state of a given cell in the grid. Uses the Moore
     * neighborhood (eight surrounding cells, excluding the center) to count Firing neighbors. Transitions follow:
     * Ready to Firing if exactly two neighbors are Firing; Firing to Refractory; Refractory to Ready. This results
     * in dynamic, fast-paced patterns with no static structures.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BrainCell} whose state is to be updated
     * @return the new {@link BrainState} of the cell
     */
    @Override
    public BrainState apply(Grid<BrainCell, BrainState> grid, BrainCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        BrainState currentState = cell.getState();
        BrainState.BrainStateValue echo = currentState.getValue();

        // Count Firing neighbors in the Moore neighborhood (excluding the center)
        int firingCount = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip center cell
                // Wrap-around coordinates
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                BrainState neighborState = grid.getCell(nx, ny).getState();
                if (neighborState.getValue() == BrainState.BrainStateValue.FIRING) {
                    firingCount++;
                }
            }
        }

        // Apply transition rules based on current state
        return switch (currentState.getValue()) {
            case READY -> {
                // Ready cell fires if exactly two neighbors are Firing
                if (firingCount == 2) {
                    grid.getIntermediateStates()[y][x].set(BrainState.BrainStateValue.FIRING, echo);
                    yield grid.getIntermediateStates()[y][x];
                }
                grid.getIntermediateStates()[y][x].set(currentState.getValue(), currentState.getEcho());
                yield grid.getIntermediateStates()[y][x];
            }
            case FIRING -> {
                // Firing cell always becomes Refractory
                grid.getIntermediateStates()[y][x].set(BrainState.BrainStateValue.REFRACTORY, currentState.getEcho());
                yield grid.getIntermediateStates()[y][x];
            }
            case REFRACTORY -> {
                // Refractory cell always returns to Ready
                grid.getIntermediateStates()[y][x].set(BrainState.BrainStateValue.READY, currentState.getEcho());
                yield grid.getIntermediateStates()[y][x];
            }
        };
    }

}