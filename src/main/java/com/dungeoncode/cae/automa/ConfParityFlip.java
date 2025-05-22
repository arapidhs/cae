package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleParityFlip;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the PARITY-FLIP cellular automaton, a second-order dynamics version of the
 * PARITY rule where the result is XORed with the cell's previous state, producing complex, history-influenced patterns.
 * The grid is initialized with a centered 17x17 square of active cells, with all other cells inactive, using
 * {@link InitCentralSquare}. This configuration is inspired by the PARITY-FLIP rule described in Chapter 6,
 * Section 6.2 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleParityFlip
 * @see InitCentralSquare
 */
public class ConfParityFlip extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new PARITY-FLIP configuration with metadata for name, description, and citation. The configuration
     * is named "Parity Flip Dynamics," described as a cellular automaton with second-order dynamics modifying the
     * PARITY rule, and cites the book by Toffoli and Margolus.
     */
    public ConfParityFlip() {
        super(16, new InitCentralSquare(8), List.of(new RuleParityFlip()));
    }

}