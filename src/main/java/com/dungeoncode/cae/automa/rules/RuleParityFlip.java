package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements the PARITY-FLIP rule for a cellular automaton, modifying the PARITY rule by XORing its result with the
 * cell's previous state in a second-order dynamics system. The PARITY rule computes the parity (odd/even count) of
 * live cells in the von Neumann neighborhood (center, north, south, east, west); the result is then XORed with the
 * previous state (stored in the echo field) to determine the new state. This rule produces complex, dynamic patterns
 * influenced by state history, as described in Chapter 6, Section 6.2 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleParityFlip extends RuleParity {

    public RuleParityFlip() {
        super(16);
    }

    /**
     * Applies the PARITY-FLIP rule to compute the new state of a given cell in the grid. Uses second-order dynamics
     * with a von Neumann neighborhood (center, north, south, east, west). First computes the PARITY result (XOR of
     * the neighborhood states), then XORs this with the previous state (echo). The echo field is updated to the
     * current state for the next step, creating a feedback loop that influences future states.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        BooleanState parityCell = super.apply(grid, cell);
        boolean parity = parityCell.getValue();

        // XOR the PARITY result with the previous state (echo)
        BooleanState currentState = cell.getState();
        boolean newState = parity ^ currentState.isEcho();
        grid.getIntermediateStates()[y][x].set(newState, currentState.getValue(), parityCell.getLiveSum());
        return grid.getIntermediateStates()[y][x];
    }

}