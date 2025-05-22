package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleAnneal;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the ANNEAL cellular automaton, a variation of MAJORITY where a cell becomes
 * active if it has exactly 4 or 6 or more live cells in its 3x3 Moore neighborhood (including the center), and
 * inactive otherwise, producing domains with dynamic, straightening boundaries. The grid is initialized with a
 * random distribution of active and inactive cells (50% probability). This configuration is inspired by the ANNEAL
 * rule, proposed by Gerard Vichniac, described in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleAnneal
 * @see InitRandomBoolean
 */
public class ConfVichniacAnneal extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new ANNEAL configuration with metadata for name, description, and citation. The configuration
     * is named "Vichniac Anneal," described as a cellular automaton with voting-based domain annealing and
     * straightening boundaries, and cites the book by Toffoli and Margolus.
     */
    public ConfVichniacAnneal() {
        super(12, new InitRandomBoolean(), List.of(new RuleAnneal()));
    }

}