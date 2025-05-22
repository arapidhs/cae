package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleNaiveDiffusion;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitDisk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automaton} to run the NAIVE-DIFFUSION cellular automaton, modeling diffusion of particles by
 * randomly copying the state of a neighboring cell in one of four directions (north, south, east, west). The grid is
 * initialized with a disk-shaped region of active cells (radius based on grid dimensions) using {@link InitDisk}.
 * This configuration produces a diffusion pattern that breaks up into tongues of fire, as described in Chapter 9,
 * Section 9.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
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
        super(22, null, List.of(new RuleNaiveDiffusion()));
    }

    /**
     * Configures the specified {@link Automaton} with a grid, rule, and interval for the NAIVE-DIFFUSION automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitDisk} with a disk-shaped region of
     * active cells (radius based on grid dimensions), and applies the {@link RuleNaiveDiffusion} for state updates.
     *
     * @param automaton      the {@link Automaton} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automaton<BooleanCell, BooleanState> automaton, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        InitDisk initDisk = new InitDisk((width + height) / 8);
        setGridInitializer(initDisk);
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, initDisk);
        config.put(CONF_GRID, grid);
        config.put(CONF_RULES, getRules());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automaton.configure(config);
    }

}