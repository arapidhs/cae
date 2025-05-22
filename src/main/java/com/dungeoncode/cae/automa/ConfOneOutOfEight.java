package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleOneOutOfEight;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the 1-OUT-OF-8 cellular automaton, where a cell becomes active if it has
 * exactly one live neighbor in its Moore neighborhood, and remains unchanged otherwise, producing a sparse,
 * fractal-like pattern. The grid is initialized with a small 3x3 central region of active cells, simulating a
 * seed. This configuration is inspired by the 1-OUT-OF-8 rule described in Chapter 5, Section 5.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleOneOutOfEight
 * @see InitCentralSquare
 */
public class ConfOneOutOfEight extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new 1-OUT-OF-8 configuration with metadata for name, description, and citation. The
     * configuration is named "1-Out-Of-8," described as a cellular automaton with constrained, fractal-like
     * growth of active cells, and cites the book by Toffoli and Margolus.
     */
    public ConfOneOutOfEight() {
        super(8, new InitCentralSquare(1), List.of(new RuleOneOutOfEight()));
    }

}