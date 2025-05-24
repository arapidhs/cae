package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleSoilErosionRandom;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomBooleanProbability;

import java.util.List;

/**
 * Runs the SOIL-EROSION-RANDOM rule, where cells (soil, active=true) remain active if supported by at least one active
 * neighbor in each of the north, south, west, and east directions in their 3x3 Moore neighborhood, otherwise becoming
 * inactive (eroded). Updates occur with probability 1/512 to emulate asynchronous updates. Initialized with a random
 * distribution of 83% active cells to simulate solid soil with sparse holes, as described in Chapter 9, Section 9.5 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 */
public class ConfSoilErosionRandom extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SOIL-EROSION-RANDOM configuration with a random initializer (83% active cells).
     */
    public ConfSoilErosionRandom() {
        super(26, new InitRandomBooleanProbability(0.83), List.of(new RuleSoilErosionRandom()));
    }
}