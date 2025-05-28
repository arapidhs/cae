package com.dungeoncode.cae.automa.eca.conf;

import com.dungeoncode.cae.automa.eca.rule.ECAXORRule;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.automa.eca.init.InitOneDRandom;

import java.util.List;

/**
 * Runs the ONED-RAND rule, where row t at step t computes each cell's state as the XOR of west, center, and east
 * neighbors from row t-1, forming a random-number generator. Initialized with random boolean states in row 0 using
 * region-specific density probabilities, the grid stores spacetime history, scrolling up when full, as described in
 * Chapter 9, Section 9.7 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 */
public class ConfECAXOR extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new ONED-RAND configuration with a random initializer for row 0 (default 50% density for all regions).
     */
    public ConfECAXOR() {
        super(28, new InitOneDRandom(0.5, 0.5, 0.5, false),
                List.of(new ECAXORRule()), Dimension.ONE);
    }
}