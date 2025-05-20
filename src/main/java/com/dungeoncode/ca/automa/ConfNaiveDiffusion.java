package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleNaiveDiffusion;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitDisk;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the NAIVE-DIFFUSION cellular automaton, modeling diffusion of particles by
 * randomly moving them in one of four directions (north, south, east, west). The grid is initialized with a disk-shaped
 * region of active cells (radius 10) using {@link InitDisk}. This configuration produces a diffusion pattern
 * that breaks up into tongues of fire, as described in Chapter 9, Section 9.4 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see RuleNaiveDiffusion
 * @see InitDisk
 */
public class ConfNaiveDiffusion extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new NAIVE-DIFFUSION configuration with metadata for name, description, and citation. The
     * configuration is named "Naive Particle Diffusion," described as a cellular automaton modeling particle diffusion,
     * and cites the book by Toffoli and Margolus.
     */
    public ConfNaiveDiffusion() {
        super(
                "Naive Particle Diffusion",
                "A cellular automaton modeling diffusion of particles, where the state (value) represents particles " +
                        "(true = particle, false = empty). Particles move randomly in one of four directions (north, south, " +
                        "east, west) without handshaking, leading to non-conserved particle counts. Initialized with a " +
                        "disk-shaped region of active cells (radius 10), it produces a diffusion pattern that breaks up into " +
                        "tongues of fire, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 9, Section 9.4, p. 84-86. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the NAIVE-DIFFUSION automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitDisk} with a disk-shaped
     * region of active cells (radius 10), and applies the {@link RuleNaiveDiffusion} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitDisk(10));
        config.put("grid", grid);
        config.put("rule", new RuleNaiveDiffusion());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}