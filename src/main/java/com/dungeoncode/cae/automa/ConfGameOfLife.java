package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleGameOfLife;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run Conway's Game of Life cellular automaton with an echoing mechanism,
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
    }

}