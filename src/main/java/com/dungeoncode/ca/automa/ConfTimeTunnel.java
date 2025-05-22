package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleTimeTunnel;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the TIME-TUNNEL cellular automaton, a second-order reversible rule where the
 * cell's state is updated by summing the von Neumann neighborhood, applying a decision table, and XORing with the
 * previous state, producing turbulent wave patterns. The grid is initialized with a centered 6x6 square of active
 * cells, with all other cells inactive, using {@link InitCentralSquare}. This configuration is inspired by the
 * TIME-TUNNEL rule described in Chapter 6, Section 6.3 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i>.
 *
 * @see RuleTimeTunnel
 * @see InitCentralSquare
 */
public class ConfTimeTunnel extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new TIME-TUNNEL configuration with metadata for name, description, and citation. The configuration
     * is named "Time Tunnel Waves," described as a cellular automaton with second-order dynamics producing turbulent
     * patterns, and cites the book by Toffoli and Margolus.
     */
    public ConfTimeTunnel() {
        super(17, new InitCentralSquare(3, true), List.of(new RuleTimeTunnel()));
    }

}