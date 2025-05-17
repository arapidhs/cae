package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.GameOfLifeRule;
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
 * Configures an {@link Automa} to run Conway's Game of Life cellular automaton, where cells evolve
 * based on simple rules of birth, survival, and death determined by their eight nearest neighbors.
 * The grid is initialized with random boolean states across the entire grid, simulating a "primeval soup."
 * This configuration is based on the "LIFE" automaton described in Chapter 3, Section 3.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i> by Toffoli and Margolus.
 *
 * @see com.dungeoncode.ca.automa.rules.GameOfLifeRule
 * @see PrimevalSoupBooleanInitializer
 */
public class GameOfLifeConfiguration extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Game of Life configuration with metadata for name, description, and citation.
     * The configuration is named "Game of Life," described as a cellular automaton modeling population
     * dynamics, and cites the book by Toffoli and Margolus.
     */
    public GameOfLifeConfiguration() {
        super(
                "Game of Life",
                "A cellular automaton where cells represent stylized organisms, evolving under rules of birth, " +
                        "survival, and death based on their eight nearest neighbors. Live cells survive with 2 or 3 " +
                        "live neighbors, die otherwise, and dead cells become alive with exactly 3 live neighbors. " +
                        "Initialized with a random distribution of live and dead cells, it exhibits chaotic yet structured " +
                        "patterns, including blinkers, gliders, and cyclic formations. Described by John Conway in 1970 " +
                        "and detailed in Toffoli and Margolus (1987), it models complex emergent behavior from simple rules.",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 3, Section 3.1, p. 20. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the Game of Life automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link PrimevalSoupBooleanInitializer} with
     * a random distribution of live and dead cells, and applies the Game of Life rules for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height,
                          long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new PrimevalSoupBooleanInitializer());
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new GameOfLifeRule());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}