package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleOneOutOfEight;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the 1-OUT-OF-8 cellular automaton, where a cell becomes active if it has
 * exactly one live neighbor in its Moore neighborhood, and remains unchanged otherwise, producing a sparse,
 * fractal-like pattern. The grid is initialized with a small 3x3 central region of active cells, simulating a
 * seed. This configuration is inspired by the 1-OUT-OF-8 rule described in Chapter 5, Section 5.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleOneOutOfEight
 * @see InitCentralSquare
 */
public class ConfOneOutOfEight extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new 1-OUT-OF-8 configuration with metadata for name, description, and citation. The
     * configuration is named "1-Out-Of-8," described as a cellular automaton with constrained, fractal-like
     * growth of active cells, and cites the book by Toffoli and Margolus.
     */
    public ConfOneOutOfEight() {
        super(
                "1-Out-Of-8",
                "A cellular automaton where a cell becomes active if it has exactly one live neighbor in its Moore " +
                        "neighborhood (eight surrounding cells, excluding the center), and remains unchanged otherwise. " +
                        "Initialized with a 3x3 central region of active cells, it produces a sparse, fractal-like pattern " +
                        "with constrained growth compared to the SQUARES rule, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.2, p. 38. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the 1-OUT-OF-8 automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
     * with a 3x3 central region of active cells, and applies the {@link RuleOneOutOfEight} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralSquare(1));
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleOneOutOfEight());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}