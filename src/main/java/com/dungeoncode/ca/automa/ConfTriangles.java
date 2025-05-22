package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleTriangles;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitCentralSquare;
import com.dungeoncode.ca.core.impl.init.InitSpots;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the TRIANGLES cellular automaton, where a cell's next state is the logical
 * OR of a subset of its von Neumann neighborhood (north, west, center, east), causing a single active cell
 * (seed) to grow into a uniformly expanding triangle pointing south. The grid is initialized with a small 3x3
 * central region of active cells, simulating a seed. This configuration is inspired by the TRIANGLES rule
 * described in Chapter 5, Section 5.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleTriangles
 * @see InitCentralSquare
 */
public class ConfTriangles extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new TRIANGLES configuration with metadata for name, description, and citation. The
     * configuration is named "Triangles," described as a cellular automaton with monotonic, unconstrained growth
     * of active triangles pointing south, and cites the book by Toffoli and Margolus.
     */
    public ConfTriangles() {
        super(7, new InitSpots(10), List.of(new RuleTriangles()));
    }

}