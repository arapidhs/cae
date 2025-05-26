package com.dungeoncode.cae.automa.conf;

import com.dungeoncode.cae.automa.rule.RuleFredkinModulo2;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Runs the Fredkin Modulo 2 Neighbor Rule, a two-dimensional cellular automaton, invented by Edward Fredkin, where a
 * cell becomes active if the sum of its active von Neumann neighbors is odd (1 or 3), or inactive if even (0, 2, or 4).
 * Initialized with an 8x8 central square of active cells, it produces dynamic patterns, as described in
 * <i>Information Processing and Transmission in Cellular Automata</i>.
 */
public class ConfFredkinModulo2 extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Fredkin Modulo 2 Neighbor Rule configuration with an 8x8 central square of active cells.
     */
    public ConfFredkinModulo2() {
        super(34, new InitCentralSquare(8), List.of(new RuleFredkinModulo2()), Dimension.TWO);
    }
}