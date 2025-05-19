package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.RuleCategory;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the CANDLE-RAIN rule for a cellular automaton, simulating candles being extinguished by random raindrops.
 * Uses second-order dynamics where the state (value) represents candles (true = lit, false = blown out), and the echo
 * tracks the previous state. A candle is blown out if a randomly generated raindrop (probability 1/32) is present.
 * This rule is described in Chapter 8, Sections 8.1-8.2 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see BooleanCell
 * @see BooleanState
 */
public class CandleRainRule implements Rule<BooleanCell, BooleanState> {

    private final Random random = new Random();

    /**
     * Applies the CANDLE-RAIN rule to compute the new state of a given cell. State (value): a candle is blown out if a
     * raindrop (generated with probability 1/32 using Java's Random) is present; otherwise, it retains its state.
     * Echo: tracks the previous state as part of second-order dynamics.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        BooleanState currentState = cell.getState();

        // Generate raindrop with probability 1/32 (1/2^5)
        boolean raindrop = random.nextDouble() < (1.0 / 32.0);
        boolean echo = cell.getState().getValue();

        // CANDLE-RAIN for state (value): blow out candle if raindrop is present, otherwise retain state
        boolean newValue = currentState.getValue() && !raindrop;

        return new BooleanState(newValue, echo);
    }

    @Override
    public RuleCategory getRuleCategory() {
        return RuleCategory.PROBABILISTIC;
    }
}