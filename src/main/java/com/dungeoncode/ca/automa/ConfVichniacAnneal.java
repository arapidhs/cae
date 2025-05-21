package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleAnneal;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automa} to run the ANNEAL cellular automaton, a variation of MAJORITY where a cell becomes
 * active if it has exactly 4 or 6 or more live cells in its 3x3 Moore neighborhood (including the center), and
 * inactive otherwise, producing domains with dynamic, straightening boundaries. The grid is initialized with a
 * random distribution of active and inactive cells (50% probability). This configuration is inspired by the ANNEAL
 * rule, proposed by Gerard Vichniac, described in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleAnneal
 * @see InitRandomBoolean
 */
public class ConfVichniacAnneal extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new ANNEAL configuration with metadata for name, description, and citation. The configuration
     * is named "Vichniac Anneal," described as a cellular automaton with voting-based domain annealing and
     * straightening boundaries, and cites the book by Toffoli and Margolus.
     */
    public ConfVichniacAnneal() {
        super(12, new InitRandomBoolean(), List.of(new RuleAnneal()));
//        super(
//                "Vichniac Anneal",
//                "A cellular automaton, a variation of MAJORITY, where a cell becomes active if it has exactly 4 or " +
//                        "6 or more live cells in its 3x3 Moore neighborhood (including the center), and inactive otherwise. " +
//                        "Initialized with a random distribution of active and inactive cells (50% probability), it forms " +
//                        "domains with dynamic boundaries that straighten over time, modeling surface tension where bays " +
//                        "fill and capes erode, as proposed by Gerard Vichniac and described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 5, Section 5.4, p. 41. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                41,
//                Tag.DETERMINISTIC,  // Uses deterministic voting rules
//
//                // Neighborhood Type
//                Tag.MOORE,         // Uses 3x3 neighborhood including center
//
//                // Operation Types
//                Tag.VOTING,        // Uses majority voting with specific thresholds
//
//                // Behavior Types
//                Tag.PATTERN_SHAPING, // Smooths and straightens boundaries
//                Tag.DOMAIN,         // Forms distinct domains
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automa} with a grid, rule, and interval for the ANNEAL automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
//     * with a random distribution of active and inactive cells (50% probability), and applies the
//     * {@link RuleAnneal} for state updates.
//     *
//     * @param automa         the {@link Automa} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitRandomBoolean());
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleAnneal());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}