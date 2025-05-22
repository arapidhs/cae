package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleLichens;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the LICHENS cellular automaton, where a cell becomes active if it has
 * exactly 3, 7, or 8 live neighbors in its Moore neighborhood, and remains unchanged otherwise, producing an
 * irregular, lichen-like pattern. The grid is initialized with a small 5x5 central region of random state cells,
 * simulating a seed. This configuration is inspired by the LICHENS rule described in Chapter 5, Section 5.2 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleLichens
 * @see InitCentralSquare
 */
public class ConfLichens extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new LICHENS configuration with metadata for name, description, and citation. The
     * configuration is named "Lichens," described as a cellular automaton with constrained, lichen-like growth
     * of active cells, and cites the book by Toffoli and Margolus.
     */
    public ConfLichens() {
        super(9, new InitCentralBlob(2, 2), List.of(new RuleLichens()));
    }

}