package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleMajority;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the MAJORITY cellular automaton, where a cell adopts the state of the
 * majority in its 3x3 Moore neighborhood (including the center), becoming active if 5 or more cells are active,
 * and inactive otherwise, producing interpenetrating domains. The grid is initialized with a random distribution
 * of active and inactive cells (50% probability). This configuration is inspired by the MAJORITY rule described
 * in Chapter 5, Section 5.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleMajority
 * @see InitRandomBoolean
 */
public class ConfMajority extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new MAJORITY configuration with metadata for name, description, and citation. The
     * configuration is named "Majority," described as a cellular automaton with voting-based consolidation into
     * interpenetrating domains, and cites the book by Toffoli and Margolus.
     */
    public ConfMajority() {
        super(11, new InitRandomBoolean(), List.of(new RuleMajority()));
    }

}