package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleLichensWithDeath;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the LICHENS-WITH-DEATH cellular automaton, where a cell becomes active if
 * it has exactly 3, 7, or 8 live neighbors, becomes inactive if it has exactly 4 live neighbors, and remains
 * unchanged otherwise, in its Moore neighborhood, producing a complex, unpredictable pattern. The grid is
 * initialized with a small 5x5 central region of random state cells, simulating a seed. This configuration is inspired
 * by the LICHENS-WITH-DEATH rule described in Chapter 5, Section 5.3 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleLichensWithDeath
 * @see InitCentralSquare
 */
public class ConfLichensWithDeath extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new LICHENS-WITH-DEATH configuration with metadata for name, description, and citation. The
     * configuration is named "LichensWithDeath," described as a cellular automaton with competitive growth and
     * unpredictable patterns, and cites the book by Toffoli and Margolus.
     */
    public ConfLichensWithDeath() {
        super(10, new InitCentralBlob(2, 2), List.of(new RuleLichensWithDeath()));
    }

}