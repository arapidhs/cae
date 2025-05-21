package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleBanks;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the BANKS cellular automaton, where a cell's state is updated to "fill pockets,
 * erase corners" in patterns of active cells, enabling the construction of computing circuitry. The grid is
 * initialized with a random distribution of active and inactive cells (50% probability). This configuration is
 * inspired by the BANKS rule, proposed by Banks, described in Chapter 5, Section 5.5 of <i>Cellular Automata
 * Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBanks
 * @see InitRandomBoolean
 */
public class ConfBanks extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new BANKS configuration with metadata for name, description, and citation. The configuration
     * is named "Banks Computer," described as a cellular automaton for building computing circuitry with dynamic
     * pattern shaping, and cites the book by Toffoli and Margolus.
     */
    public ConfBanks() {
        super(13, new InitRandomBoolean(), List.of(new RuleBanks()));
//        super(
//                "Banks Computer",
//                "A cellular automaton where a cell's state is updated based on its von Neumann neighborhood " +
//                        "(north, south, east, west, excluding the center) to fill pockets and erase corners in patterns " +
//                        "of active cells, enabling the construction of computing circuitry. Initialized with a random " +
//                        "distribution of active and inactive cells (50% probability), it shapes patterns through specific " +
//                        "transitions, as proposed by Banks and described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 5, Section 5.5, p. 42. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                42,
//                // Rule Type
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,
//
//                // Operation Types
//                Tag.PATTERN_SHAPING,
//                Tag.COUNTING,
//
//                // Behavior Types
//                Tag.STRUCTURED,
//                Tag.STABLE,
//
//                // Source Type
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the BANKS automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
//     * with a random distribution of active and inactive cells (50% probability), and applies the
//     * {@link RuleBanks} for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitRandomBoolean());
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleBanks());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}