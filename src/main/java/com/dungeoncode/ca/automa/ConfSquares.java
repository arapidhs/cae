package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleSquares;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;
import com.dungeoncode.ca.core.impl.init.InitSpots;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the SQUARES cellular automaton, where a cell's next state is the logical
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
        super(5, new InitSpots(10), List.of(new RuleSquares()));
    }

}