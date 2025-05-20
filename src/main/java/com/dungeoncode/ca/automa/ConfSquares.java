package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleSquares;
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
 * Configures an {@link Automa} to run the SQUARES cellular automaton, where a cell's next state is the logical
 * OR of its Moore neighborhood, causing a single active cell (seed) to grow into a uniformly expanding square
 * of active cells. The grid is initialized with a small 3x3 central region of active cells, simulating a seed.
 * This configuration is inspired by the SQUARES rule described in Chapter 5, Section 5.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleSquares
 * @see InitCentralSquare
 */
public class ConfSquares extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SQUARES configuration with metadata for name, description, and citation. The
     * configuration is named "Squares," described as a cellular automaton with monotonic, unconstrained growth
     * of active squares, and cites the book by Toffoli and Margolus.
     */
    public ConfSquares() {
        super(
                "Squares",
                "A cellular automaton where a cell's next state is the logical OR of its Moore neighborhood " +
                        "(center and eight surrounding cells), causing a single active cell (seed) to grow into a " +
                        "uniformly expanding square of active cells. Initialized with a 3x3 central region of active " +
                        "cells, it demonstrates monotonic, unconstrained growth, filling the grid with overlapping " +
                        "squares if multiple seeds are present, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.1, p. 38. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the SQUARES automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
     * with a 3x3 central region of active cells, and applies the {@link RuleSquares} for state updates.
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
        config.put(CONF_RULE, new RuleSquares());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}