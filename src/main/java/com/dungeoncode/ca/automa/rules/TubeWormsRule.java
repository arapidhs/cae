package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanNeighborCountRule;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the TUBE-WORMS rule for a cellular automaton, modeling a reef of tube-worms with coupled dynamics.
 * The state (value) represents the worm's status (true = active/out, false = hiding), and the timer (stored in the
 * state) counts down (3 to 0) when triggered. Worms retract if the number of active neighbors exceeds a threshold
 * \( n \), triggering the timer before re-emerging. This rule produces dynamic patterns like ripples or spirals, as
 * described in Chapter 9, Section 9.3 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see BooleanNeighborCountRule
 * @see BooleanCell
 * @see BooleanState
 */
public class TubeWormsRule extends BooleanNeighborCountRule {

    private final int threshold;

    /**
     * Constructs a new TUBE-WORMS rule with the specified threshold for active neighbors.
     *
     * @param threshold the number of active neighbors required to trigger retraction, default is 3
     */
    public TubeWormsRule(int threshold) {
        this.threshold = threshold;
    }

    /**
     * Constructs a new TUBE-WORMS rule with the default threshold of 3.
     */
    public TubeWormsRule() {
        this(3);
    }

    /**
     * Applies the TUBE-WORMS rule to compute the new state of a given cell. Counts active neighbors in the Moore
     * neighborhood; if the count exceeds the threshold and the worm is active, it retracts and starts the timer.
     * The timer counts down (3 to 0), and the worm re-emerges when the timer reaches 0. Produces dynamic wave patterns
     * influenced by the threshold.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        BooleanState currentState = cell.getState();

        // State: worm status (true = active/out, false = hiding)
        boolean nextState = currentState.getValue();
        // Timer: countdown (0 = idle, 1-3 = counting down)
        int timer = currentState.getTimer();

        // Count active neighbors in Moore neighborhood (excluding center)
        int activeNeighbors = countLiveMooreNeighbors(grid,x,y);

        if (currentState.getValue() && timer == 0) { // Timer idle
            if ( activeNeighbors > threshold) {
                timer=3;
                nextState = false;
            }
        } else {
            --timer;
            //nextState = false;
            if(timer==0) {
                nextState = true;
            }
        }

        // Echo tracks previous state for second-order dynamics
        boolean echo = currentState.getValue();

        grid.getNextStates()[y][x].set(nextState, echo, activeNeighbors, timer);
        return grid.getNextStates()[y][x];
    }
}