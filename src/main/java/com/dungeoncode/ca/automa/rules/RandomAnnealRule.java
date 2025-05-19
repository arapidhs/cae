package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.RuleCategory;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the RAND-ANNEAL rule for a cellular automaton, a probabilistic modification of the 5MAJ majority voting
 * rule. Uses a von Neumann neighborhood (center, north, south, east, west) to compute the sum of active cells (0 to 5).
 * For sums 0-1, the cell becomes inactive; for sums 4-5, active; for sum 2, active with probability 1/32; for sum 3,
 * inactive with probability 1/32. This introduces thermal noise to smooth domain boundaries, as described in Chapter 8,
 * Section 8.3 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanCell
 * @see BooleanState
 */
public class RandomAnnealRule extends BooleanNeighborCountRule {

    private final Random random = new Random();

    /**
     * Applies the RAND-ANNEAL rule to compute the new state of a given cell. Sums the active cells in the von Neumann
     * neighborhood (center, north, south, east, west). If sum ≤ 1, the cell becomes inactive; if sum ≥ 4, active; if
     * sum = 2, active with probability 1/32; if sum = 3, inactive with probability 1/32. Echo tracks the previous state.
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

        // Sum active cells in von Neumann neighborhood (center + 4 orthogonal cells)
        int liveSum = currentState.getValue() ? 1 : 0;
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        if (grid.getCell(nxNorth, nyNorth).getState().getValue()) liveSum++;
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        if (grid.getCell(nxSouth, nySouth).getState().getValue()) liveSum++;
        int nxWest = (x - 1 + width) % width;
        int nyWest = y;
        if (grid.getCell(nxWest, nyWest).getState().getValue()) liveSum++;
        int nxEast = (x + 1) % width;
        int nyEast = y;
        if (grid.getCell(nxEast, nyEast).getState().getValue()) liveSum++;

        // Apply decision table with probabilistic outcomes for marginal cases
        boolean newValue;
        switch (liveSum) {
            case 0:
            case 1:
                newValue = false;
                break;
            case 2:
                newValue = random.nextDouble() < (1.0 / 32.0); // RAND: active with p=1/32
                break;
            case 3:
                newValue = random.nextDouble() >= (1.0 / 32.0); // ~RAND: active with p=31/32
                break;
            default: // 4, 5
                newValue = true;
                break;
        }

        // Echo tracks the previous state
        return new BooleanState(newValue, currentState.getValue(),liveSum);
    }

    @Override
    public RuleCategory getRuleCategory() {
        return RuleCategory.PROBABILISTIC;
    }
}