package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleSafePass;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitHistogram;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the SAFE-PASS cellular automaton, simulating particle conservation by allowing
 * tokens to fall downward in columns and pile up above a baseline to form a histogram. The grid is initialized with a
 * random distribution of tokens (state value, 50% probability) and echo set to false, except for the bottom row where
 * state is active and echo is true (inhibition baseline), using {@link InitHistogram}. This configuration is
 * inspired by the SAFE-PASS rule described in Chapter 9, Section 9.1 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleSafePass
 * @see InitHistogram
 */
public class ConfHistogram extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new SAFE-PASS configuration with metadata for name, description, and citation. The configuration
     * is named "Histogram Token Fall," described as a cellular automaton simulating particle conservation for histogram
     * creation, and cites the book by Toffoli and Margolus.
     */
    public ConfHistogram() {
        super(20, new InitHistogram(), List.of(new RuleSafePass()));
    }

}