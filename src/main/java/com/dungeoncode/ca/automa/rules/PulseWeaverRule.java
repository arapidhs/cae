package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.PulseWeaverCell;
import com.dungeoncode.ca.core.impl.PulseWeaverState;

import java.util.Random;

/**
 * Implements the rule for the Pulse Weaver cellular automaton, updating a cell's state based on its Moore
 * neighborhood. Transitions cells between Dormant, Charged, and Fading states using threshold-based and
 * stochastic rules to create organic, pulsating patterns resembling waves or interconnected structures.
 *
 * @see Rule
 * @see PulseWeaverCell
 * @see PulseWeaverState
 */
public class PulseWeaverRule implements Rule<PulseWeaverCell, PulseWeaverState> {

    /**
     * Random number generator for stochastic transitions.
     */
    private final Random random = new Random();

    /**
     * Applies the Pulse Weaver rule to compute the new state of a given cell in the grid. Uses the Moore
     * neighborhood (eight surrounding cells) to count Charged neighbors and applies the following transitions:
     * <ul>
     *   <li>Dormant to Charged: If 2 to 4 Charged neighbors, or 10% chance with 1 Charged neighbor.</li>
     *   <li>Charged to Fading: Always transitions to Fading (non-persistent), unless 5+ Charged neighbors (5% chance to remain Charged).</li>
     *   <li>Fading to Dormant: Default, unless:</li>
     *   <li>Fading to Charged: If 2+ Charged neighbors (4+ if persistent).</li>
     *   <li>Fading persists: If 1 to 3 Charged neighbors and persistent, or 10% chance regardless of persistence.</li>
     * </ul>
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link PulseWeaverCell} whose state is to be updated
     * @return the new {@link PulseWeaverState} of the cell
     */
    @Override
    public PulseWeaverState apply(Grid<PulseWeaverCell, PulseWeaverState> grid, PulseWeaverCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        PulseWeaverState currentState = cell.getState();

        // Count Charged neighbors in Moore neighborhood
        int chargedCount = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dx == 0 && dy == 0) continue; // Skip center cell
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                PulseWeaverState neighborState = grid.getCell(nx, ny).getState();
                if (neighborState.getValue() == PulseWeaverState.PulseState.CHARGED) {
                    chargedCount++;
                }
            }
        }

        // Apply transition rules
        return switch (currentState.getValue()) {
            case DORMANT -> {
                if (chargedCount >= 2 && chargedCount <= 4 || (chargedCount == 1 && random.nextDouble() < 0.1)) {
                    yield new PulseWeaverState(PulseWeaverState.PulseState.CHARGED);
                }
                yield currentState;
            }
            case CHARGED -> {
                if (chargedCount >= 5 && random.nextDouble() < 0.05) {
                    yield currentState;
                }
                yield new PulseWeaverState(PulseWeaverState.PulseState.FADING);
            }
            case FADING -> {
                if (chargedCount >= (currentState.isPersistent() ? 4 : 2)) {
                    yield new PulseWeaverState(PulseWeaverState.PulseState.CHARGED);
                } else if (((chargedCount >= 1 && chargedCount <= 3) && currentState.isPersistent()) || random.nextDouble() < 0.1) {
                    yield new PulseWeaverState(PulseWeaverState.PulseState.FADING, currentState.isPersistent());
                }
                yield new PulseWeaverState(PulseWeaverState.PulseState.DORMANT);
            }
        };
    }
}