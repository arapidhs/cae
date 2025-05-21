package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleGreenberg;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;
import com.dungeoncode.ca.core.impl.init.InitRandomFiring;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the GREENBERG cellular automaton, combining the DIAMONDS rule with a READY
 * inhibitor in a second-order dynamics system, producing diamond-shaped wave fronts with hollow interiors. The grid
 * is initialized with a sparse distribution of Firing cells (10 cells) among mostly Ready cells. This configuration
 * is inspired by the GREENBERG rule, studied by Greenberg and Hastings, described in Chapter 6, Section 6.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleGreenberg
 * @see InitRandomFiring
 */
public class ConfGreenberg extends AbstractConfiguration<BrainCell, BrainState> {

    /**
     * Constructs a new GREENBERG configuration with metadata for name, description, and citation. The configuration
     * is named "Greenberg-Hastings Waves," described as a cellular automaton with second-order dynamics producing
     * hollow wave fronts, and cites the book by Toffoli and Margolus.
     */
    public ConfGreenberg() {
        super(15, new InitRandomFiring(10), List.of(new RuleGreenberg()));
//        super(
//                "Greenberg-Hastings Waves",
//                "A cellular automaton combining the DIAMONDS rule with a READY inhibitor in a second-order dynamics " +
//                        "system, where cells transition between Ready, Firing, and Refractory states. A Ready cell applies " +
//                        "the DIAMONDS rule (logical OR on von Neumann neighborhood, including the center) to become Firing; " +
//                        "Firing becomes Refractory; Refractory returns to Ready. Initialized with a sparse distribution of " +
//                        "10 Firing cells among mostly Ready cells, it produces diamond-shaped wave fronts with hollow " +
//                        "interiors that merge into larger structures, studied by Greenberg and Hastings, as described in " +
//                        "Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 6, Section 6.1, p. 49-50. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                49,
//                // Rule Type
//                Tag.SECOND_ORDER,  // Has multiple states (Ready, Firing, Refractory)
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors
//
//                // Operation Types
//                Tag.LOGICAL,       // Uses logical OR from DIAMONDS rule
//                Tag.STATE_MACHINE,
//
//                // Behavior Types
//                Tag.WAVE,          // Creates wave-like patterns
//                Tag.HOLLOW,        // Creates hollow patterns
//                Tag.STRUCTURED,    // Creates organized patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the GREENBERG automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomFiring}
//     * with a sparse distribution of 10 Firing cells among mostly Ready cells, and applies the {@link RuleGreenberg}
//     * for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BrainCell, BrainState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BrainCell, BrainState> grid = new Grid<>(width, height, new InitRandomFiring(10));
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleGreenberg());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}