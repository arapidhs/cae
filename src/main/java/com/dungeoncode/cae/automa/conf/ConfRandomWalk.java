package com.dungeoncode.cae.automa.conf;

import com.dungeoncode.cae.automa.rule.RuleRandomWalk;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.init.InitOneDRandom;

import java.util.List;

/**
 * Runs the RANDOM-WALK rule, a one-dimensional rule where particles in row t at step t move left or right randomly with
 * no collision detection. Initialized with random boolean states in row 0 at low density to simulate multiple particles,
 * the 2D grid stores spacetime history, scrolling up when full of toroidal left/right wrapping, as described in
 * Chapter 10, Section 10.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 */
public class ConfRandomWalk extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new RANDOM-WALK configuration with a random initializer for row 0 (5% density for all regions).
     */
    public ConfRandomWalk() {
        super(30, new InitOneDRandom(0.10, 0.25, 0.05),
                List.of(new RuleRandomWalk()), Dimension.ONE);
    }
}