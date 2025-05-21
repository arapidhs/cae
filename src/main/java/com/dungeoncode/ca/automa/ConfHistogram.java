package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleSafePass;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitHistogram;

import java.util.List;

/**
 * Configures an {@link Automa} to run the SAFE-PASS cellular automaton, simulating particle conservation by allowing
 * tokens to fall downward in columns and pile up above a baseline to form a histogram. The grid is initialized with a
 * random distribution of tokens (state value, 50% probability) and echo set to false, except for the bottom row where
 * state is active and echo is true (inhibition baseline), using {@link InitHistogram}. This configuration is
 * inspired by the SAFE-PASS rule described in Chapter 9, Section 9.1 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleSafePass
 * @see InitHistogram
 */
public class ConfHistogram extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SAFE-PASS configuration with metadata for name, description, and citation. The configuration
     * is named "Histogram Token Fall," described as a cellular automaton simulating particle conservation for histogram
     * creation, and cites the book by Toffoli and Margolus.
     */
    public ConfHistogram() {
        super(20, new InitHistogram(), List.of(new RuleSafePass()));
//        super(
//                "Histogram Token Fall",
//                "A cellular automaton simulating particle conservation, where tokens (state value=true) fall downward " +
//                        "in columns and pile up above a baseline to form a histogram. The baseline (echo=true) is at the " +
//                        "bottom row, inhibiting movement, while echo remains static. Initialized with a random distribution " +
//                        "of tokens (50% probability) and echo=false except for the bottom row (active, echo=true), it " +
//                        "preserves token count while forming a histogram, as described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 9, Section 9.1, p. 78-80. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                78,
//                Tag.DETERMINISTIC,
//
//                // Operation Types
//                Tag.CONSERVATION,  // Preserves token count
//                Tag.GRAVITY,       // New tag for rules that implement gravity-like behavior
//
//                // Behavior Types
//                Tag.STRUCTURED,    // Creates organized histogram patterns
//                Tag.STABLE,        // Forms stable patterns
//                Tag.ACCUMULATION,  // New tag for rules that accumulate states
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automa} with a grid, rule, and interval for the SAFE-PASS automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitHistogram}
//     * with a random distribution of tokens (50% probability) in the state and a bottom-row baseline in the echo, and
//     * applies the {@link RuleSafePass} for state updates.
//     *
//     * @param automa         the {@link Automa} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitHistogram());
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleSafePass());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}