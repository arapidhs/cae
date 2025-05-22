package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleNaiveDiffusion;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitDisk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeoncode.cae.core.Constants.*;

/**
 * Configures an {@link Automaton} to run the NAIVE-DIFFUSION cellular automaton with a handshake protocol, modeling
 * diffusion of particles by randomly moving them in one of four directions (north, south, east, west). The grid is
 * initialized with a disk-shaped region of active cells (radius based on grid dimensions) using {@link InitDisk}.
 * This configuration produces a diffusion pattern that breaks up into tongues of fire, as described in Chapter 9,
 * Section 9.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleNaiveDiffusion
 * @see InitDisk
 */
public class ConfHandshakeDiffusion extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new NAIVE-DIFFUSION configuration with a handshake protocol, including metadata for name,
     * description, and citation. The configuration is named "Naive Diffusion with Handshake," described as a cellular
     * automaton modeling particle diffusion with handshaking, and cites the book by Toffoli and Margolus.
     */
    public ConfHandshakeDiffusion() {
        super(23, null, List.of(new RuleNaiveDiffusion(false, true)));
    }

    /**
     * Configures the specified {@link Automaton} with a grid, rule, and interval for the NAIVE-DIFFUSION automaton with
     * handshaking. Creates a {@link Grid} with the given dimensions, initialized by {@link InitDisk} with a disk-shaped
     * region of active cells (radius based on grid dimensions), and applies the {@link RuleNaiveDiffusion} with
     * handshaking enabled for state updates.
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