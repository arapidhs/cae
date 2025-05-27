package com.dungeoncode.cae.automa.conf;

import com.dungeoncode.cae.automa.eca.rule.RuleECA;
import com.dungeoncode.cae.automa.rule.RuleVonNeumannNeighborState;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Configuration;
import com.dungeoncode.cae.core.Dimension;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Runs the Von Neumann Neighbor-State rule, a two-dimensional cellular automaton where a cell’s state is determined by
 * the number of active von Neumann neighbors (0-4) and its own state, using a 10-bit code number (0-1023) to define
 * outputs: bits 0-1 for 4 neighbors (center active/inactive), bits 2-3 for 3 neighbors, bits 4-5 for 2 neighbors,
 * bits 6-7 for 1 neighbor, and bits 8-9 for 0 neighbors. Initialized with a single active cell in the center, it
 * produces structured patterns, as described in <i>A New Kind of Science</i> by Stephen Wolfram.
 */
public class ConfVonNeumannNeighborState extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Von Neumann Neighbor-State configuration with a single active cell initializer and the specified
     * code number.
     *
     * @param codeNumber the code number (0-1023) defining the rule’s output table
     * @throws IllegalArgumentException if codeNumber is not in [0, 1023]
     */
    public ConfVonNeumannNeighborState(int codeNumber) {
        super(33, new InitCentralSquare(1), List.of(new RuleVonNeumannNeighborState(codeNumber)), Dimension.TWO);
    }

    @Override
    public String toString() {
        final RuleVonNeumannNeighborState rule = (RuleVonNeumannNeighborState) getRules().get(0);
        return super.toString() + "-Code-"+rule.getCodeNumber();
    }

    @Override
    public Configuration<BooleanCell, BooleanState> next() {
        final RuleVonNeumannNeighborState rule = (RuleVonNeumannNeighborState) getRules().get(0);
        final int nextCodeNumber = (rule.getCodeNumber()+1+1024)%1024;
        rule.setCodeNumber(nextCodeNumber);
        return this;
    }

    @Override
    public Configuration<BooleanCell, BooleanState> previous() {
        final RuleVonNeumannNeighborState rule = (RuleVonNeumannNeighborState) getRules().get(0);
        int previousCodeNumber = (rule.getCodeNumber()-1+1024)%1024;
        if (previousCodeNumber < 0) {
            previousCodeNumber = 1023;
        }
        rule.setCodeNumber(previousCodeNumber);
        return this;
    }

}