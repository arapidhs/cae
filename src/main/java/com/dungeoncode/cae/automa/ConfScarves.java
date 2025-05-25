package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleScarves;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitOneDRandom;

import java.util.List;

/**
 * Runs the SCARVES rule, a one-dimensional rule where row t at step t sets a cell's state to true if exactly two of its
 * four neighbors (west, east, west-of-west, east-of-east) from row t-1 are active, else false. Initialized with random
 * boolean states in row 0 using region-specific density probabilities, the grid stores spacetime history, scrolling up
 * when full, as described in Chapter 9, Section 9.7 of <i>Cellular Automata Machines: A New Environment for Modeling</i>
 * (MIT Press).
 */
public class ConfScarves extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SCARVES configuration with a random initializer for row 0 (default 50% density for all regions).
     */
    public ConfScarves() {
        super(29, new InitOneDRandom(0.5, 0.5, 0.5), List.of(new RuleScarves()));
    }
}