package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.AnnealRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.PrimevalSoupBooleanInitializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the ANNEAL cellular automaton, a variation of MAJORITY where a cell becomes
 * active if it has exactly 4 or 6 or more live cells in its 3x3 Moore neighborhood (including the center), and
 * inactive otherwise, producing domains with dynamic, straightening boundaries. The grid is initialized with a
 * random distribution of active and inactive cells (50% probability). This configuration is inspired by the ANNEAL
 * rule, proposed by Gerard Vichniac, described in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see AnnealRule
 * @see PrimevalSoupBooleanInitializer
 */
public class AnnealConfiguration extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new ANNEAL configuration with metadata for name, description, and citation. The configuration
     * is named "Vichniac Anneal," described as a cellular automaton with voting-based domain annealing and
     * straightening boundaries, and cites the book by Toffoli and Margolus.
     */
    public AnnealConfiguration() {
        super(
                "Vichniac Anneal",
                "A cellular automaton, a variation of MAJORITY, where a cell becomes active if it has exactly 4 or " +
                        "6 or more live cells in its 3x3 Moore neighborhood (including the center), and inactive otherwise. " +
                        "Initialized with a random distribution of active and inactive cells (50% probability), it forms " +
                        "domains with dynamic boundaries that straighten over time, modeling surface tension where bays " +
                        "fill and capes erode, as proposed by Gerard Vichniac and described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.4, p. 41. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the ANNEAL automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link PrimevalSoupBooleanInitializer}
     * with a random distribution of active and inactive cells (50% probability), and applies the
     * {@link AnnealRule} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new PrimevalSoupBooleanInitializer());
        config.put("grid", grid);
        config.put("rule", new AnnealRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}