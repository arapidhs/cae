package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.GreenbergRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainState;
import com.dungeoncode.ca.core.impl.RandomFiringInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the GREENBERG cellular automaton, combining the DIAMONDS rule with a READY
 * inhibitor in a second-order dynamics system, producing diamond-shaped wave fronts with hollow interiors. The grid
 * is initialized with a sparse distribution of Firing cells (10 cells) among mostly Ready cells. This configuration
 * is inspired by the GREENBERG rule, studied by Greenberg and Hastings, described in Chapter 6, Section 6.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see GreenbergRule
 * @see RandomFiringInitializer
 */
public class GreenbergConfiguration extends AbstractConfiguration<BrainCell, BrainState> {

    /**
     * Constructs a new GREENBERG configuration with metadata for name, description, and citation. The configuration
     * is named "Greenberg-Hastings Waves," described as a cellular automaton with second-order dynamics producing
     * hollow wave fronts, and cites the book by Toffoli and Margolus.
     */
    public GreenbergConfiguration() {
        super(
                "Greenberg-Hastings Waves",
                "A cellular automaton combining the DIAMONDS rule with a READY inhibitor in a second-order dynamics " +
                        "system, where cells transition between Ready, Firing, and Refractory states. A Ready cell applies " +
                        "the DIAMONDS rule (logical OR on von Neumann neighborhood, including the center) to become Firing; " +
                        "Firing becomes Refractory; Refractory returns to Ready. Initialized with a sparse distribution of " +
                        "10 Firing cells among mostly Ready cells, it produces diamond-shaped wave fronts with hollow " +
                        "interiors that merge into larger structures, studied by Greenberg and Hastings, as described in " +
                        "Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 6, Section 6.1, p. 49-50. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the GREENBERG automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link RandomFiringInitializer}
     * with a sparse distribution of 10 Firing cells among mostly Ready cells, and applies the {@link GreenbergRule}
     * for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BrainCell, BrainState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BrainCell, BrainState> grid = new Grid<>(width, height, new RandomFiringInitializer(10));
        config.put("grid", grid);
        config.put("rule", new GreenbergRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}