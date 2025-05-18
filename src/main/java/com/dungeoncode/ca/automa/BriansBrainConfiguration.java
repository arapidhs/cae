package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.BriansBrainRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BrainCell;
import com.dungeoncode.ca.core.impl.BrainRandomInitializer;
import com.dungeoncode.ca.core.impl.BrainState;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the BRIAN'S-BRAIN cellular automaton, where cells transition between Ready,
 * Firing, and Refractory states, producing dynamic, neural firing-like patterns. The grid is initialized with a
 * random distribution of Ready (70%), Firing (15%), and Refractory (15%) states. This configuration is inspired by
 * the BRIAN'S-BRAIN rule, suggested by Brian Silverman, described in Chapter 6, Section 6.1 of <i>Cellular Automata
 * Machines: A New Environment for Modeling</i>.
 *
 * @see BriansBrainRule
 * @see BrainRandomInitializer
 */
public class BriansBrainConfiguration extends AbstractConfiguration<BrainCell, BrainState> {

    /**
     * Constructs a new BRIAN'S-BRAIN configuration with metadata for name, description, and citation. The
     * configuration is named "Silverman's Brain," described as a cellular automaton with second-order dynamics
     * modeling neural firing patterns, and cites the book by Toffoli and Margolus.
     */
    public BriansBrainConfiguration() {
        super(
                "Silverman's Brain",
                "A cellular automaton where cells transition between Ready (quiescent), Firing (active), and " +
                        "Refractory (recovering) states based on their Moore neighborhood (eight surrounding cells, " +
                        "excluding the center). A Ready cell fires if exactly two neighbors are Firing; a Firing cell " +
                        "becomes Refractory; a Refractory cell returns to Ready. Initialized with a random distribution " +
                        "of Ready (70%), Firing (15%), and Refractory (15%) states, it produces fast-paced, dynamic " +
                        "patterns resembling neural firing waves, as suggested by Brian Silverman and described in " +
                        "Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 6, Section 6.1, p. 48-49. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the BRIAN'S-BRAIN automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link BrainRandomInitializer}
     * with a random distribution of Ready, Firing, and Refractory states, and applies the {@link BriansBrainRule}
     * for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BrainCell, BrainState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BrainCell, BrainState> grid = new Grid<>(width, height, new BrainRandomInitializer());
        config.put("grid", grid);
        config.put("rule", new BriansBrainRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}