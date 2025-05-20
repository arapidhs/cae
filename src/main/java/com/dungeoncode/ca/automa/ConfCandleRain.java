package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleCandleRain;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the CANDLE-RAIN cellular automaton, simulating candles being extinguished by
 * random raindrops with an exponential decay pattern. The grid is initialized with a random distribution of active
 * (lit) and inactive (blown out) candles in the state (50% probability) and a random pattern in the echo (50%
 * probability) using {@link InitRandomBoolean}. This configuration is inspired by the CANDLE-RAIN rule
 * described in Chapter 8, Sections 8.1-8.2 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleCandleRain
 * @see InitRandomBoolean
 */
public class ConfCandleRain extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new CANDLE-RAIN configuration with metadata for name, description, and citation. The configuration
     * is named "Exponential Candle Decay," described as a cellular automaton simulating exponential decay of candles,
     * and cites the book by Toffoli and Margolus.
     */
    public ConfCandleRain() {
        super(
                "Exponential Candle Decay",
                "A cellular automaton simulating candles being extinguished by random raindrops, where the state (value) " +
                        "represents candles (true = lit, false = blown out) and the echo tracks the previous state as " +
                        "part of second-order dynamics. Raindrops are generated with probability 1/32. " +
                        "Initialized with a random distribution of active (lit) and inactive (blown out) candles in the " +
                        "state (50% probability) and a random pattern in the echo (50% probability), it produces an " +
                        "exponential decay pattern, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 8, Sections 8.1-8.2, p. 68-70. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the CANDLE-RAIN automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
     * with a random distribution of active and inactive cells (50% probability) in both the state and echo, and
     * applies the {@link RuleCandleRain} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitRandomBoolean());
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleCandleRain());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}