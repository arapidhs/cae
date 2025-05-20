package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleLichensWithDeath;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the LICHENS-WITH-DEATH cellular automaton, where a cell becomes active if
 * it has exactly 3, 7, or 8 live neighbors, becomes inactive if it has exactly 4 live neighbors, and remains
 * unchanged otherwise, in its Moore neighborhood, producing a complex, unpredictable pattern. The grid is
 * initialized with a small 5x5 central region of random state cells, simulating a seed. This configuration is inspired
 * by the LICHENS-WITH-DEATH rule described in Chapter 5, Section 5.3 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleLichensWithDeath
 * @see InitCentralSquare
 */
public class ConfLichensWithDeath extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new LICHENS-WITH-DEATH configuration with metadata for name, description, and citation. The
     * configuration is named "LichensWithDeath," described as a cellular automaton with competitive growth and
     * unpredictable patterns, and cites the book by Toffoli and Margolus.
     */
    public ConfLichensWithDeath() {
        super(
                "Lichens With Death",
                "A cellular automaton where a cell becomes active if it has exactly 3, 7, or 8 live neighbors, " +
                        "becomes inactive if it has exactly 4 live neighbors, and remains unchanged otherwise, in its " +
                        "Moore neighborhood (eight surrounding cells, excluding the center). Initialized with a 5x5 " +
                        "central region of random state cells, it requires a seed of at least three cells to initiate growth, " +
                        "producing a complex, unpredictable pattern with competitive growth, as described in Toffoli and " +
                        "Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.3, p. 40. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the LICHENS-WITH-DEATH automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
     * with a 5x5 central region of random state cells, and applies the {@link RuleLichensWithDeath} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralBlob(2, 2));
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleLichensWithDeath());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}