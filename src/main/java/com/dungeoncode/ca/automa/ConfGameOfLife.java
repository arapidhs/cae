package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleGameOfLife;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automa} to run Conway's Game of Life cellular automaton with an echoing mechanism,
 * tracking state transitions to enhance visualization of dynamic patterns. Cells evolve based on standard
 * Game of Life rules, with an echo effect that records recent state changes to distinguish active and static
 * regions. The grid is initialized with a random distribution of live and dead cells, simulating a "primeval
 * soup." This configuration is inspired by the echoing technique described in Chapter 3, Section 3.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleGameOfLife
 * @see InitRandomBoolean
 */
public class ConfGameOfLife extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Game of Life with Echo configuration with metadata for name, description, and citation.
     * The configuration is named "Game of Life with Echo," described as an enhanced Game of Life with state
     * transition tracking, and cites the relevant source.
     */
    public ConfGameOfLife() {
        super(2, new InitRandomBoolean(), List.of(new RuleGameOfLife(false)));
//        super(
//                "Game of Life",
//                "A cellular automaton extending Conway's Game of Life with an echoing mechanism to track state " +
//                        "transitions. Cells follow standard Game of Life rules: live cells survive with 2 or 3 live " +
//                        "neighbors, die otherwise; dead cells become alive with exactly 3 live neighbors. The echo " +
//                        "effect records recent state changes, enabling visualization of dynamic patterns, such as " +
//                        "moving structures versus static backgrounds, even when the simulation is paused. Initialized " +
//                        "with a random distribution of live and dead cells, it creates complex, evolving patterns with " +
//                        "enhanced temporal feedback.",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 3, Section 3.1, p. 20. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                20,
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.MOORE,
//
//                // Operation Types
//                Tag.COUNTING,     // Counts live neighbors
//
//                // Behavior Types
//                Tag.STABLE,       // Can produce stable patterns
//                Tag.OSCILLATION,  // Can produce oscillating patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC,
//                Tag.FOUNDATIONAL
//        );
    }

//    /**
//     * Configures the specified {@link Automa} with a grid, rule, and interval for the Game of Life with Echo
//     * automaton. Creates a {@link Grid} with the given dimensions, initialized by
//     * {@link InitRandomBoolean} with a random distribution of live and dead cells, and applies
//     * the {@link RuleGameOfLife} to manage state updates and echo effects.
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
//        config.put(CONF_RULES, new RuleGameOfLife(false));
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}