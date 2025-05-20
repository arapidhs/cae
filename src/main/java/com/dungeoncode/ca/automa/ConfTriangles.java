package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleTriangles;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the TRIANGLES cellular automaton, where a cell's next state is the logical
 * OR of a subset of its von Neumann neighborhood (north, west, center, east), causing a single active cell
 * (seed) to grow into a uniformly expanding triangle pointing south. The grid is initialized with a small 3x3
 * central region of active cells, simulating a seed. This configuration is inspired by the TRIANGLES rule
 * described in Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleTriangles
 * @see InitCentralSquare
 */
public class ConfTriangles extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new TRIANGLES configuration with metadata for name, description, and citation. The
     * configuration is named "Triangles," described as a cellular automaton with monotonic, unconstrained growth
     * of active triangles pointing south, and cites the book by Toffoli and Margolus.
     */
    public ConfTriangles() {
        super(
                "Triangles",
                "A cellular automaton where a cell's next state is the logical OR of a subset of its von Neumann " +
                        "neighborhood (north, west, center, east), causing a single active cell (seed) to grow into a " +
                        "uniformly expanding triangle pointing south, breaking four-fold symmetry. Initialized with a " +
                        "3x3 central region of active cells, it demonstrates monotonic, unconstrained growth, as " +
                        "described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 5, Section 5.1, p. 38. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the TRIANGLES automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
     * with a 3x3 central region of active cells, and applies the {@link RuleTriangles} for state updates.
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
        config.put("grid", grid);
        config.put("rule", new RuleTriangles());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}