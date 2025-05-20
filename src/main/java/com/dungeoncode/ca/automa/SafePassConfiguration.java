package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.SafePassRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.HistogramInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the SAFE-PASS cellular automaton, simulating particle conservation by allowing
 * tokens to fall downward in columns and pile up above a baseline to form a histogram. The grid is initialized with a
 * random distribution of tokens (state value, 50% probability) and echo set to false, except for the bottom row where
 * state is active and echo is true (inhibition baseline), using {@link HistogramInitializer}. This configuration is
 * inspired by the SAFE-PASS rule described in Chapter 9, Section 9.1 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see SafePassRule
 * @see HistogramInitializer
 */
public class SafePassConfiguration extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SAFE-PASS configuration with metadata for name, description, and citation. The configuration
     * is named "Histogram Token Fall," described as a cellular automaton simulating particle conservation for histogram
     * creation, and cites the book by Toffoli and Margolus.
     */
    public SafePassConfiguration() {
        super(
                "Histogram Token Fall",
                "A cellular automaton simulating particle conservation, where tokens (state value=true) fall downward " +
                        "in columns and pile up above a baseline to form a histogram. The baseline (echo=true) is at the " +
                        "bottom row, inhibiting movement, while echo remains static. Initialized with a random distribution " +
                        "of tokens (50% probability) and echo=false except for the bottom row (active, echo=true), it " +
                        "preserves token count while forming a histogram, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 9, Section 9.1, p. 78-80. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the SAFE-PASS automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link HistogramInitializer}
     * with a random distribution of tokens (50% probability) in the state and a bottom-row baseline in the echo, and
     * applies the {@link SafePassRule} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new HistogramInitializer());
        config.put("grid", grid);
        config.put("rule", new SafePassRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}