package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;

/**
 * Implements the GREENBERG rule for a cellular automaton, combining the DIAMONDS rule with a READY inhibitor in a
 * second-order dynamics system. A Ready cell applies the DIAMONDS rule (logical OR on von Neumann neighborhood,
 * including the center) to become Firing; Firing transitions to Refractory; Refractory returns to Ready. This rule
 * produces diamond-shaped wave fronts with hollow interiors, studied by Greenberg and Hastings, as described in
 * Chapter 6, Section 6.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see Rule
 * @see BrainCell
 * @see BrainState
 */
public class GreenbergRule implements Rule<BrainCell, BrainState> {

    /**
     * Applies the GREENBERG rule to compute the new state of a given cell in the grid. Uses second-order dynamics
     * with a von Neumann neighborhood (north, south, east, west, including the center for DIAMONDS). If the cell is
     * Ready, the DIAMONDS rule is applied: the cell becomes Firing if any neighbor or itself is Firing. Firing cells
     * become Refractory; Refractory cells return to Ready. The echo field tracks the previous state to implement the
     * inhibition wave trailing the firing wave.
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
        BrainState.BrainStateValue previousState = currentState.getEcho();

        // Transition states: Firing → Refractory → Ready
        BrainState.BrainStateValue nextValue;
        BrainState.BrainStateValue nextEcho;

        switch (currentState.getValue()) {
            case READY -> {
                // Apply DIAMONDS rule only if the cell is READY
                // Logical OR across von Neumann neighborhood (including center) on firing states
                boolean shouldFire = currentState.getValue() == BrainState.BrainStateValue.FIRING;
                // North neighbor
                int nxNorth = x;
                int nyNorth = (y - 1 + height) % height;
                if (grid.getCell(nxNorth, nyNorth).getState().getValue() == BrainState.BrainStateValue.FIRING) {
                    shouldFire = true;
                }
                // South neighbor
                if (!shouldFire) {
                    int nxSouth = x;
                    int nySouth = (y + 1) % height;
                    if (grid.getCell(nxSouth, nySouth).getState().getValue() == BrainState.BrainStateValue.FIRING) {
                        shouldFire = true;
                    }
                }
                // West neighbor
                if (!shouldFire) {
                    int nxWest = (x - 1 + width) % width;
                    int nyWest = y;
                    if (grid.getCell(nxWest, nyWest).getState().getValue() == BrainState.BrainStateValue.FIRING) {
                        shouldFire = true;
                    }
                }
                // East neighbor
                if (!shouldFire) {
                    int nxEast = (x + 1) % width;
                    int nyEast = y;
                    if (grid.getCell(nxEast, nyEast).getState().getValue() == BrainState.BrainStateValue.FIRING) {
                        shouldFire = true;
                    }
                }

                // If DIAMONDS rule results in firing, transition to Firing; otherwise, stay Ready
                nextValue = shouldFire ? BrainState.BrainStateValue.FIRING : BrainState.BrainStateValue.READY;
                nextEcho = currentState.getValue(); // ECHO: store the current state as the previous state
            }
            case FIRING -> {
                // Firing always transitions to Refractory
                nextValue = BrainState.BrainStateValue.REFRACTORY;
                nextEcho = currentState.getValue(); // ECHO: store the current state as the previous state
            }
            case REFRACTORY -> {
                // Refractory always transitions to Ready
                nextValue = BrainState.BrainStateValue.READY;
                nextEcho = currentState.getValue(); // ECHO: store the current state as the previous state
            }
            default -> {
                // Fallback for safety, though not expected
                nextValue = currentState.getValue();
                nextEcho = previousState;
            }
        }

        return new BrainState(nextValue, nextEcho);
    }
}