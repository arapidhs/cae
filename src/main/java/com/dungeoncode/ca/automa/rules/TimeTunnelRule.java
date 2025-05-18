package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the TIME-TUNNEL rule for a cellular automaton, a second-order reversible rule where the cell's state
 * is determined by summing the von Neumann neighborhood (center, north, south, east, west), applying a decision
 * table (returns 1 if not all cells are the same), and XORing with the previous state. The grid's toroidal topology
 * causes waves to circle back, creating complex, turbulent patterns. This rule is described in Chapter 6, Section 6.3
 * of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see BooleanCell
 * @see BooleanState
 */
public class TimeTunnelRule implements Rule<BooleanCell, BooleanState> {

    /**
     * Applies the TIME-TUNNEL rule to compute the new state of a given cell in the grid. Uses second-order dynamics
     * with a von Neumann neighborhood (center, north, south, east, west). Sums the states (0 to 5), applies a
     * decision table (returns 1 if sum is 1 to 4, 0 otherwise), and XORs with the previous state (echo). The echo
     * field is updated to the current state for the next step, creating waves that interfere and form turbulent patterns
     * with four-fold symmetry.
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
        BooleanState currentState = cell.getState();

        // Compute sum of live cells in von Neumann neighborhood (center + 4 orthogonal cells)
        int liveSum = currentState.getValue() ? 1 : 0;
        // North neighbor
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        if (grid.getCell(nxNorth, nyNorth).getState().getValue()) {
            liveSum++;
        }
        // South neighbor
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        if (grid.getCell(nxSouth, nySouth).getState().getValue()) {
            liveSum++;
        }
        // West neighbor
        int nxWest = (x - 1 + width) % width;
        int nyWest = y;
        if (grid.getCell(nxWest, nyWest).getState().getValue()) {
            liveSum++;
        }
        // East neighbor
        int nxEast = (x + 1) % width;
        int nyEast = y;
        if (grid.getCell(nxEast, nyEast).getState().getValue()) {
            liveSum++;
        }

        // Apply decision table: return 1 if not all cells are the same (sum 1 to 4), 0 otherwise
        boolean decision = liveSum >= 1 && liveSum <= 4;

        // XOR the decision table result with the previous state (echo)
        boolean newState = decision ^ currentState.isEcho();

        // Modeling, Chapter 6, Section 6.2, p. 51. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001
        return new BooleanState(newState, currentState.getValue(),liveSum);
    }
}