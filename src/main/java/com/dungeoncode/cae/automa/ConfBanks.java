package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleBanks;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the BANKS cellular automaton, where a cell's state is updated to "fill pockets,
 * erase corners" in patterns of active cells, enabling the construction of computing circuitry. The grid is
 * initialized with a random distribution of active and inactive cells (50% probability). This configuration is
 * inspired by the BANKS rule, proposed by Banks, described in Chapter 5, Section 5.5 of <i>Cellular Automata
 * Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBanks
 * @see InitRandomBoolean
 */
public class ConfBanks extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new BANKS configuration with metadata for name, description, and citation. The configuration
     * is named "Banks Computer," described as a cellular automaton for building computing circuitry with dynamic
     * pattern shaping, and cites the book by Toffoli and Margolus.
     */
    public ConfBanks() {
        super(13, new InitRandomBoolean(), List.of(new RuleBanks()));
    }

}