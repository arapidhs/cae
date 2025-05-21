package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleDiamonds;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;
import com.dungeoncode.ca.core.impl.init.InitSpots;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the DIAMONDS cellular automaton, where a cell's next state is the logical
 * OR of its von Neumann neighborhood, causing a single active cell (seed) to grow into a uniformly expanding
 * diamond of active cells. The grid is initialized with a small 3x3 central region of active cells, simulating
 * a seed. This configuration is inspired by the DIAMONDS rule described in Chapter 5, Section 5.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleDiamonds
 * @see InitCentralSquare
 */
public class ConfDiamonds extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new DIAMONDS configuration with metadata for name, description, and citation. The
     * configuration is named "Diamonds," described as a cellular automaton with monotonic, unconstrained growth
     * of active diamonds, and cites the book by Toffoli and Margolus.
     */
    public ConfDiamonds() {
        super(6, new InitSpots(10), List.of(new RuleDiamonds()));
//        super(
//                "Diamonds",
//                "A cellular automaton where a cell's next state is the logical OR of its von Neumann neighborhood " +
//                        "(center and four orthogonal cells: north, south, east, west), causing a single active cell " +
//                        "(seed) to grow into a uniformly expanding diamond of active cells. Initialized with a 3x3 " +
//                        "central region of active cells, it demonstrates monotonic, unconstrained growth, filling the " +
//                        "grid with overlapping diamonds if multiple seeds are present, as described in Toffoli and " +
//                        "Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 5, Section 5.1, p. 37. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                37,
//                // Rule Type
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,
//
//                // Operation Types
//                Tag.LOGICAL,      // Uses logical OR operation
//                Tag.GROWTH,       // New tag for rules that grow patterns
//
//                // Behavior Types
//                Tag.MONOTONIC,    // New tag for rules with unidirectional growth
//                Tag.STRUCTURED,   // Creates organized diamond patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the DIAMONDS automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
//     * with a 3x3 central region of active cells, and applies the {@link RuleDiamonds} for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitSpots(10));
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleDiamonds());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}