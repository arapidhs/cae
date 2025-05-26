package com.dungeoncode.cae.automa.conf;

import com.dungeoncode.cae.automa.rule.RuleElementaryCA;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitOneDRandom;

import java.util.List;

/**
 * Runs the elementary cellular automaton (ECA) rule, where row t at step t computes a cell's state based on the left,
 * center, and right neighbors from row t-1, using a rule number (0-255) whose binary representation defines outputs
 * for all 8 neighborhood configurations. Initialized with a single active cell in the center of row 0, the 2D grid
 * stores spacetime history, scrolling up when full, as described in <i>A New Kind of Science</i> by Stephen Wolfram.
 */
public class ConfElementaryCA extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new elementary cellular automaton configuration with a single active cell initializer for row 0
     * and the specified rule number.
     *
     * @param ruleNumber the rule number (0-255) defining the ECA's output table
     * @throws IllegalArgumentException if ruleNumber is not in [0, 255]
     */
    public ConfElementaryCA(int ruleNumber) {
        super(31, new InitOneDRandom(0.0, 0.0, 0.0, true),
                List.of(new RuleElementaryCA(ruleNumber)), Dimension.ONE);
    }

    @Override
    public String toString() {
        final RuleElementaryCA rule = (RuleElementaryCA) getRules().get(0);
        return super.toString() + "-Rule-"+rule.getRuleNumber();
    }

}