package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleParity;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;
import com.dungeoncode.cae.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the Parity cellular automaton, where a cell's next state is determined
 * by the parity (odd or even count) of live cells in its von Neumann neighborhood (center, north, south, west,
 * east). The grid is initialized with a random distribution of live and dead cells, simulating a "primeval soup."
 * This configuration is inspired by the Parity rule described in Chapter 4, Section 4.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleParity
 * @see InitRandomBoolean
 */
public class ConfParity extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Parity configuration with metadata for name, description, and citation. The
     * configuration is named "Parity," described as a cellular automaton with self-replicating patterns
     * driven by neighborhood parity, and cites the book by Toffoli and Margolus.
     */
    public ConfParity() {
        super(4, new InitCentralSquare(8), List.of(new RuleParity()));
    }

}