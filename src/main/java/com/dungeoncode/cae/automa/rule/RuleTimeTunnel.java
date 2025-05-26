package com.dungeoncode.cae.automa.rule;

import com.dungeoncode.cae.core.AbstractRule;
import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

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
public class RuleTimeTunnel extends RuleBooleanNeighborCount {

    public RuleTimeTunnel() {
        super(17);
    }

    /**
     * Applies the TIME-TUNNEL rule to compute the new state of a given cell in the grid. Uses second-order dynamics
     * with a von Neumann neighborhood (center, north, south, east, west). Sums the states (0 to 5), applies a
     * decision table (returns 1 if sum is 1 to 4, 0 otherwise), and XORs with the previous state (echo). The echo
     * field is updated to the current state for the next step, creating waves that interfere and form turbulent patterns
     * with four-fold symmetry.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @param step the current step
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell, int step) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        BooleanState currentState = cell.getState();

        // Compute sum of live cells in von Neumann neighborhood (center + 4 orthogonal cells)
        int activeSum = currentState.getValue() ? 1 : 0;
        // North neighbor
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        if (grid.getCell(nxNorth, nyNorth).getState().getValue()) {
            activeSum++;
        }
        // South neighbor
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        if (grid.getCell(nxSouth, nySouth).getState().getValue()) {
            activeSum++;
        }
        // West neighbor
        int nxWest = (x - 1 + width) % width;
        int nyWest = y;
        if (grid.getCell(nxWest, nyWest).getState().getValue()) {
            activeSum++;
        }
        // East neighbor
        int nxEast = (x + 1) % width;
        int nyEast = y;
        if (grid.getCell(nxEast, nyEast).getState().getValue()) {
            activeSum++;
        }

        // Apply decision table: return 1 if not all cells are the same (sum 1 to 4), 0 otherwise
        boolean decision = activeSum >= 1 && activeSum <= 4;

        // XOR the decision table result with the previous state (echo)
        boolean newState = decision ^ currentState.isEcho();

        int liveSum = countLiveMooreNeighbors(grid,x,y);
        grid.getNextStates()[y][x].set(newState, currentState.getValue(), liveSum);
        return grid.getNextStates()[y][x];
    }

}