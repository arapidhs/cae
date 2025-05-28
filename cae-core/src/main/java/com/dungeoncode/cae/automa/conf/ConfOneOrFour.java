package com.dungeoncode.cae.automa.conf;

import com.dungeoncode.cae.automa.rule.RuleOneOrFour;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Runs the ONE-OR-FOUR rule, a two-dimensional rule where a cell becomes active (black) if exactly one or all four of
 * its von Neumann neighbors (north, south, east, west) are active, otherwise retaining its current state. Initialized
 * with a 3x3 square of active cells in the center, it produces sparse, structured patterns, as inspired by concepts in
 * <i>A New Kind of Science</i> by Stephen Wolfram.
 */
public class ConfOneOrFour extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new ONE-OR-FOUR configuration with a central 3x3 square initializer.
     */
    public ConfOneOrFour() {
        super(32, new InitCentralSquare(0),
                List.of(new RuleOneOrFour()), Dimension.TWO);
    }
}