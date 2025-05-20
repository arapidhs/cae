package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleMajority;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the MAJORITY cellular automaton, where a cell adopts the state of the
 * majority in its 3x3 Moore neighborhood (including the center), becoming active if 5 or more cells are active,
 * and inactive otherwise, producing interpenetrating domains. The grid is initialized with a random distribution
 * of active and inactive cells (50% probability). This configuration is inspired by the MAJORITY rule described
 * in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleMajority
 * @see InitRandomBoolean
 */
public class ConfMajority extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new MAJORITY configuration with metadata for name, description, and citation. The
     * configuration is named "Majority," described as a cellular automaton with voting-based consolidation into
     * interpenetrating domains, and cites the book by Toffoli and Margolus.
     */
    public ConfMajority() {
        super(
                "Majority",
                "A cellular automaton where a cell adopts the state of the majority in its 3x3 Moore neighborhood " +
                        "(including the center), becoming active if 5 or more cells are active, and inactive otherwise. " +
                        "Initialized with a random distribution of active and inactive cells (50% probability), it " +
                        "consolidates regions into interpenetrating black and white domains with stable boundaries, as " +
                        "described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.4, p. 41. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the MAJORITY automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
     * with a random distribution of active and inactive cells (50% probability), and applies the
     * {@link RuleMajority} for state updates.
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
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleMajority());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}