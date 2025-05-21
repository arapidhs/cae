package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleInkspot;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the Inkspot cellular automaton, where cells become black
 * if they have exactly three black neighbors in a 3x3 neighborhood or are already black.
 * The grid is initialized with random boolean states in a 5x5 central region.
 * This configuration is inspired by the "INKSPOT" recipe described in Chapter 1, Section 1.2
 * of "Cellular Automata Machines: A New Environment for Modeling" by Toffoli and Margolus.
 *
 * @see RuleInkspot
 * @see InitCentralBlob
 */
public class ConfInkspot extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Inkspot configuration with metadata for name, description, and citation.
     * The configuration is named "Inkspot," described as a cellular automaton with a spreading
     * ink-like pattern, and cites the book by Toffoli and Margolus.
     */
    public ConfInkspot() {
        super(1, new InitCentralBlob(3, 3), List.of(new RuleInkspot()));
//        super(
//                "Inkspot",
//                "A cellular automaton where cells become black if they have exactly three black neighbors " +
//                        "in a 3x3 neighborhood or are already black, with initial random states in a central 7x7 region. " +
//                        "Inspired by the INKSPOT recipe in Toffoli and Margolus (1987), it produces an irregular, " +
//                        "organic spread resembling ink on fabric, with capes, bays, and occasional straight filaments.",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, Chapter 1, Section 1.2, p. 6. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                6,
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.MOORE,         // Uses 3x3 neighborhood
//
//                // Operation Types
//                Tag.COUNTING,      // Counts exact number of neighbors
//                Tag.SPREAD,        // New tag for rules that spread patterns
//
//                // Behavior Types
//                Tag.ORGANIC,       // New tag for rules that create organic patterns
//                Tag.DYNAMIC,       // Changes over time
//                Tag.PATTERN_SHAPING,       // Creates distinct patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the Inkspot automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralBlob}
//     * with a 7x7 random central region, and applies the {@link RuleInkspot} for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BooleanCell, BooleanState> automa, int width, int height,
//                          long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralBlob(3, 3));
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleInkspot());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}