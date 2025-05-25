package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleCyclicRank;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.Dimension;
import com.dungeoncode.cae.core.impl.init.InitRandomSpecies;

import java.util.List;

/**
 * Runs the CYCLIC-RANK rule, where cells with one of four cyclic states (id=0,1,2,3) adopt the state of a randomly
 * selected von Neumann neighbor if it is the next in the cycle (0→1→2→3→0), producing self-organizing spiral patterns.
 * Initialized with a random distribution of 25% active cells across four states using InitRandomSpecies, as described
 * in Chapter 9, Section 9.6 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 */
public class ConfCyclicRank extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new CYCLIC-RANK configuration with a random initializer (4 states, 25% population).
     */
    public ConfCyclicRank() {
        super(27, new InitRandomSpecies(4, 100),
                List.of(new RuleCyclicRank()), Dimension.TWO);
    }
}