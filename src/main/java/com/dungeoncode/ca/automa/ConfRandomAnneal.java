package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleRandomAnneal;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the RAND-ANNEAL cellular automaton, a probabilistic modification of the 5MAJ
 * majority voting rule that introduces thermal noise to smooth domain boundaries. The grid is initialized with a
 * random distribution of active and inactive cells (50% probability) in both the state and echo using
 * {@link InitRandomBoolean}. This configuration is inspired by the RAND-ANNEAL rule described in
 * Chapter 8, Section 8.3 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleRandomAnneal
 * @see InitRandomBoolean
 */
public class ConfRandomAnneal extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new RAND-ANNEAL configuration with metadata for name, description, and citation. The configuration
     * is named "Random Anneal Smoothing," described as a cellular automaton with probabilistic majority voting, and
     * cites the book by Toffoli and Margolus.
     */
    public ConfRandomAnneal() {
        super(
                "Random Anneal Smoothing",
                "A cellular automaton modifying the 5MAJ majority voting rule with probabilistic transitions, using a " +
                        "von Neumann neighborhood (center, north, south, east, west). For marginal sums (2 or 3 out of 5), " +
                        "the state changes probabilistically (p=1/32), introducing thermal noise to smooth domain boundaries. " +
                        "Initialized with a random distribution of active and inactive cells (50% probability) in both the " +
                        "state and echo, it produces dynamic patterns with smoother domains, as described in Toffoli and " +
                        "Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 8, Section 8.3, p. 70. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the RAND-ANNEAL automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
     * with a random distribution of active and inactive cells (50% probability) in both the state and echo, and
     * applies the {@link RuleRandomAnneal} for state updates.
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
        config.put("grid", grid);
        config.put("rule", new RuleRandomAnneal());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}