package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleInkspot;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitCentralBlob;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the Inkspot cellular automaton, where cells become black
 * if they have exactly three black neighbors in a 3x3 neighborhood or are already black.
 * The grid is initialized with random boolean states in a 5x5 central region.
 * This configuration is inspired by the "INKSPOT" recipe described in Chapter 1, Section 1.2
 * of "Cellular Automata Machines: A New Environment for Modeling" by Toffoli and Margolus.
 *
 * @see RuleInkspot
 * @see InitCentralBlob
 */
public class ConfInkspot extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new Inkspot configuration with metadata for name, description, and citation.
     * The configuration is named "Inkspot," described as a cellular automaton with a spreading
     * ink-like pattern, and cites the book by Toffoli and Margolus.
     */
    public ConfInkspot() {
        super(1, new InitCentralBlob(3, 3), List.of(new RuleInkspot()));
    }

}