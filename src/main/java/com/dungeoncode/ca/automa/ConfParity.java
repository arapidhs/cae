package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleParity;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the Parity cellular automaton, where a cell's next state is determined
 * by the parity (odd or even count) of live cells in its von Neumann neighborhood (center, north, south, west,
 * east). The grid is initialized with a random distribution of live and dead cells, simulating a "primeval soup."
 * This configuration is inspired by the Parity rule described in Chapter 4, Section 4.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleParity
 * @see InitRandomBoolean
 */
public class ConfParity extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Parity configuration with metadata for name, description, and citation. The
     * configuration is named "Parity," described as a cellular automaton with self-replicating patterns
     * driven by neighborhood parity, and cites the book by Toffoli and Margolus.
     */
    public ConfParity() {
        super(
                "Parity",
                "A cellular automaton where a cell's next state is determined by the parity (odd or even count) of " +
                        "live cells in its von Neumann neighborhood (center, north, south, west, east). Initialized with " +
                        "a random distribution of live and dead cells, it produces self-replicating patterns and linear " +
                        "wave interactions, as described in Toffoli and Margolus (1987). The rule's linearity leads to " +
                        "emergent behaviors, such as initial patterns reproducing in multiple copies over time.",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 4, Section 4.2, p. 31. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the Parity automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
     * with a random distribution of live and dead cells, and applies the {@link RuleParity} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralSquare(8));
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleParity());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}