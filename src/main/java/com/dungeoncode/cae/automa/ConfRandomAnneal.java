package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleRandomAnneal;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the RAND-ANNEAL cellular automaton, a probabilistic modification of the 5MAJ
 * majority voting rule that introduces thermal noise to smooth domain boundaries. The grid is initialized with a
 * random distribution of active and inactive cells (50% probability) in both the state and echo using
 * {@link InitRandomBoolean}. This configuration is inspired by the RAND-ANNEAL rule described in
 * Chapter 8, Section 8.3 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleRandomAnneal
 * @see InitRandomBoolean
 */
public class ConfRandomAnneal extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new RAND-ANNEAL configuration with metadata for name, description, and citation. The configuration
     * is named "Random Anneal Smoothing," described as a cellular automaton with probabilistic majority voting, and
     * cites the book by Toffoli and Margolus.
     */
    public ConfRandomAnneal() {
        super(19, new InitRandomBoolean(), List.of(new RuleRandomAnneal()));
    }

}