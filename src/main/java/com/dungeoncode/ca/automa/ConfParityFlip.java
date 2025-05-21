package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleParityFlip;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the PARITY-FLIP cellular automaton, a second-order dynamics version of the
 * PARITY rule where the result is XORed with the cell's previous state, producing complex, history-influenced patterns.
 * The grid is initialized with a centered 17x17 square of active cells, with all other cells inactive, using
 * {@link InitCentralSquare}. This configuration is inspired by the PARITY-FLIP rule described in Chapter 6,
 * Section 6.2 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleParityFlip
 * @see InitCentralSquare
 */
public class ConfParityFlip extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new PARITY-FLIP configuration with metadata for name, description, and citation. The configuration
     * is named "Parity Flip Dynamics," described as a cellular automaton with second-order dynamics modifying the
     * PARITY rule, and cites the book by Toffoli and Margolus.
     */
    public ConfParityFlip() {
        super(16, new InitCentralSquare(8), List.of(new RuleParityFlip()));
//        super(
//                "Parity Flip Dynamics",
//                "A cellular automaton modifying the PARITY rule with second-order dynamics, where the result of the " +
//                        "PARITY rule (XOR of the von Neumann neighborhood: center, north, south, east, west) is XORed " +
//                        "with the cell's previous state (stored in the echo field). Initialized with a centered 17x17 " +
//                        "square of active cells, with all other cells inactive, it produces complex, dynamic patterns " +
//                        "influenced by state history, as described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 6, Section 6.2, p. 50-51. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                50,
//
//                Tag.DETERMINISTIC,
//                Tag.REVERSIBLE,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors plus center
//
//                // Operation Types
//                Tag.LOGICAL,       // Uses XOR operations for parity
//
//                // Behavior Types
//                Tag.DYNAMIC,       // Creates complex, history-influenced patterns
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

//    /**
//     * Configures the specified {@link Automaton} with a grid, rule, and interval for the PARITY-FLIP automaton.
//     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
//     * with a centered 17x17 square of active cells, and applies the {@link RuleParityFlip} for state updates.
//     *
//     * @param automa         the {@link Automaton} to configure
//     * @param width          the width (number of columns) of the grid
//     * @param height         the height (number of rows) of the grid
//     * @param intervalMillis the interval in milliseconds between automaton steps
//     */
//    @Override
//    public void configure(Automaton<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
//        Map<String, Object> config = new HashMap<>();
//        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralSquare(8));
//        config.put(CONF_GRID, grid);
//        config.put(CONF_RULES, new RuleParityFlip());
//        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
//        automa.configure(config);
//    }
}