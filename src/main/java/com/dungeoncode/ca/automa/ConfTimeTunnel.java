package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleTimeTunnel;
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
 * Configures an {@link Automa} to run the TIME-TUNNEL cellular automaton, a second-order reversible rule where the
 * cell's state is updated by summing the von Neumann neighborhood, applying a decision table, and XORing with the
 * previous state, producing turbulent wave patterns. The grid is initialized with a centered 6x6 square of active
 * cells, with all other cells inactive, using {@link InitCentralSquare}. This configuration is inspired by the
 * TIME-TUNNEL rule described in Chapter 6, Section 6.2 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see RuleTimeTunnel
 * @see InitCentralSquare
 */
public class ConfTimeTunnel extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new TIME-TUNNEL configuration with metadata for name, description, and citation. The configuration
     * is named "Time Tunnel Waves," described as a cellular automaton with second-order dynamics producing turbulent
     * patterns, and cites the book by Toffoli and Margolus.
     */
    public ConfTimeTunnel() {
        super(
                "Time Tunnel Waves",
                "A cellular automaton with a second-order reversible rule where the cell's state is updated by summing " +
                        "the von Neumann neighborhood (center, north, south, east, west), applying a decision table " +
                        "(returns 1 if not all cells are the same), and XORing with the previous state (stored in the echo " +
                        "field). Initialized with a centered 6x6 square of active cells, with all other cells inactive, it " +
                        "produces turbulent wave patterns that interfere due to the grid's toroidal topology, preserving " +
                        "four-fold symmetry, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 6, Section 6.3, p. 52. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the TIME-TUNNEL automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralSquare}
     * with a centered 6x6 square of active cells, and applies the {@link RuleTimeTunnel} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        boolean withEcho = true;
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitCentralSquare(3, withEcho));
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleTimeTunnel());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}