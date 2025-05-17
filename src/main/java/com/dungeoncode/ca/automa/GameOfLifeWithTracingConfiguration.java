package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.GameOfLifeEchoRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.PrimevalSoupBooleanInitializer;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run Conway's Game of Life cellular automaton with a tracing mechanism,
 * permanently recording cells that were ever alive to highlight dynamic patterns. Cells evolve based on
 * standard Game of Life rules, with a tracing effect that persists state transitions to distinguish active
 * regions, such as glider paths, from static backgrounds. The grid is initialized with a random distribution
 * of live and dead cells, simulating a "primeval soup." This configuration is inspired by the tracing
 * technique described in Chapter 3, Section 3.3 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see GameOfLifeEchoRule
 * @see PrimevalSoupBooleanInitializer
 */
public class GameOfLifeWithTracingConfiguration extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Game of Life with Tracing configuration with metadata for name, description, and
     * citation. The configuration is named "Game of Life with Tracing," described as an enhanced Game of Life
     * with persistent state transition tracking, and cites the relevant source.
     */
    public GameOfLifeWithTracingConfiguration() {
        super(
                "Game of Life with Tracing",
                "A cellular automaton extending Conway's Game of Life with a tracing mechanism to permanently " +
                        "record state transitions. Cells follow standard Game of Life rules: live cells survive with " +
                        "2 or 3 live neighbors, die otherwise; dead cells become alive with exactly 3 live neighbors. " +
                        "The tracing effect marks cells that were ever alive, enabling visualization of dynamic patterns, " +
                        "such as glider paths or evolving structures, against static backgrounds. Initialized with a " +
                        "random distribution of live and dead cells, it creates complex patterns with persistent " +
                        "historical feedback.",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 3, Section 3.3, p. 23. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the Game of Life with Tracing
     * automaton. Creates a {@link Grid} with the given dimensions, initialized by
     * {@link PrimevalSoupBooleanInitializer} with a random distribution of live and dead cells, and applies
     * the {@link GameOfLifeEchoRule} with tracing enabled to manage state updates and persistent tracing effects.
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
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new GameOfLifeEchoRule(true));
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}