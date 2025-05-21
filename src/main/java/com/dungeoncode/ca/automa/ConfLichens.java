package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleLichens;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the LICHENS cellular automaton, where a cell becomes active if it has
 * exactly 3, 7, or 8 live neighbors in its Moore neighborhood, and remains unchanged otherwise, producing an
 * irregular, lichen-like pattern. The grid is initialized with a small 5x5 central region of random state cells,
 * simulating a seed. This configuration is inspired by the LICHENS rule described in Chapter 5, Section 5.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleLichens
 * @see InitCentralSquare
 */
public class ConfLichens extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new LICHENS configuration with metadata for name, description, and citation. The
     * configuration is named "Lichens," described as a cellular automaton with constrained, lichen-like growth
     * of active cells, and cites the book by Toffoli and Margolus.
     */
    public ConfLichens() {
        super(9, new InitCentralBlob(2, 2), List.of(new RuleLichens()));
//        super(
//                "Lichens",
//                "A cellular automaton where a cell becomes active if it has exactly 3, 7, or 8 live neighbors in " +
//                        "its Moore neighborhood (eight surrounding cells, excluding the center), and remains unchanged " +
//                        "otherwise. Initialized with a 5x5 central region of random state cells, it requires a seed of at " +
//                        "least three cells to initiate growth, producing an irregular, lichen-like pattern with " +
//                        "constrained growth, as described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 5, Section 5.2, p. 40. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                40,
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.MOORE,         // Uses 8 surrounding cells
//
//                // Operation Types
//                Tag.COUNTING,      // Counts exact number of neighbors
//                Tag.GROWTH,        // Implements growth patterns
//
//                // Behavior Types
//                Tag.ORGANIC,       // Creates natural, irregular patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the LICHENS automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralBlob}
//     * with a 5x5 central region of cells, and applies the {@link RuleLichens} for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralBlob(2, 2));
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleLichens());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}