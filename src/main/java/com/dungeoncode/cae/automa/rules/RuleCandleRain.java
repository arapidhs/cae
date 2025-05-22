package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.AbstractRule;
import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Implements the CANDLE-RAIN rule for a cellular automaton, simulating candles extinguished by random raindrops.
 * A candle (true = lit, false = blown out) is blown out with a 1/32 probability of a raindrop hit; otherwise, it
 * retains its state. This probabilistic rule produces an exponential decay of lit candles. Described in Chapter 8,
 * Section 8.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see AbstractRule
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleCandleRain extends AbstractRule<BooleanCell, BooleanState> {

    /** Random number generator for raindrop probability. */
    private final Random random = new Random();

    /**
     * Constructs a new CANDLE-RAIN rule with a fixed ID.
     */
    public RuleCandleRain() {
        super(18);
    }

    /**
     * Applies the CANDLE-RAIN rule to compute the new state of a cell. A candle is blown out (false) if a raindrop
     * is generated (1/32 probability); otherwise, it retains its state. The echo tracks the previous state. Updates
     * the grid's intermediate state with the new state, echo, and zero neighbor count.
     *
     * @param grid the {@link Grid} containing the cell, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        BooleanState currentState = cell.getState();

        // Generate raindrop with 1/32 probability
        boolean raindrop = random.nextDouble() < (1.0 / 32.0);
        boolean echo = currentState.getValue();

        // Rule: blow out candle if raindrop present, otherwise retain state
        boolean newValue = currentState.getValue() && !raindrop;

        BooleanState[][] intermediateStates = grid.getIntermediateStates();
        intermediateStates[y][x].set(newValue, echo, 0);
        return intermediateStates[y][x];
    }
}