package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleHGlass;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the HGLASS cellular automaton, where cell states evolve based on a
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
    }

}