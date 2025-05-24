package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleSoilErosion;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomBooleanProbability;

import java.util.List;

/**
 * Runs the SOIL-EROSION rule, where cells (soil, active=true) remain active if supported by at least one active neighbor
 * in each of the north, south, west, and east directions in their 3x3 Moore neighborhood, otherwise becoming inactive
 * (eroded). Initialized with a random distribution of 83% active cells to simulate solid soil with sparse holes, as
 * described in Chapter 9, Section 9.5 of <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 */
public class ConfSoilErosion extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SOIL-EROSION configuration with a random initializer (83% active cells).
     */
    public ConfSoilErosion() {
        super(25, new InitRandomBooleanProbability(0.83), List.of(new RuleSoilErosion()));
    }
}