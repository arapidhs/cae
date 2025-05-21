package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleHGlass;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automa} to run the HGLASS cellular automaton, where cell states evolve based on a
 * 32-bit lookup table using the states of the cell and its four orthogonal neighbors (east, west, south,
 * north). The grid is initialized with a random distribution of live and dead cells, simulating a "primeval
 * soup," and produces diverse behaviors, from chaotic growth to structured patterns, depending on initial
 * conditions. This configuration is inspired by the HGLASS rule described in Chapter 4, Section 4.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleHGlass
 * @see InitRandomBoolean
 */
public class ConfHglass extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new HGLASS configuration with metadata for name, description, and citation. The
     * configuration is named "HGlass," described as a cellular automaton with varied behaviors driven by a
     * lookup table, and cites the relevant source.
     */
    public ConfHglass() {
        super(3, new InitRandomBoolean(), List.of(new RuleHGlass()));
//        super(
//                "HGlass",
//                "A cellular automaton where cell states are determined by a 32-bit lookup table based on the cell " +
//                        "and its four orthogonal neighbors (east, west, south, north). Initialized with a random " +
//                        "distribution of live and dead cells, it exhibits a range of behaviors, including chaotic " +
//                        "expansion, structured growth, or stabilization, depending on initial conditions such as random " +
//                        "patterns or localized blobs. The HGLASS rule is noted for its ability to produce complex, " +
//                        "emergent patterns from simple rules.",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 4, Section 4.1, p. 29. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                29,
//                // Rule Type
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors
//
//                // Operation Types
//                Tag.LOOKUP_TABLE,
//
//                // Behavior Types
//                Tag.CHAOTIC,       // Can exhibit chaotic behavior
//                Tag.STRUCTURED,    // Can produce structured patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automa} with a grid, rule, and interval for the HGLASS automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitRandomBoolean}
//     * with a random distribution of live and dead cells, and applies the {@link RuleHGlass} for state updates.
//     *
//     * @param automa         the {@link Automa} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitRandomBoolean());
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleHGlass());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}