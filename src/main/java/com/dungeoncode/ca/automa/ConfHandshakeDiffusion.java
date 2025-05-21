package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleNaiveDiffusion;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitDisk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an {@link Automa} to run the NAIVE-DIFFUSION cellular automaton with a handshake protocol, modeling
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
//        super(
//                "Naive Diffusion with Handshake",
//                "A cellular automaton modeling diffusion of particles with a handshake protocol, where the state (value) " +
//                        "represents particles (true = particle, false = empty). Active cells give their state (becoming inactive) " +
//                        "to an empty neighbor, and inactive cells take a state (becoming active) from a neighbor with a particle, " +
//                        "moving in one of four directions (north, south, east, west). Initialized with a disk-shaped region of " +
//                        "active cells (radius based on grid dimensions), it produces a diffusion pattern that breaks up into " +
//                        "tongues of fire, as described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
//                        "Chapter 9, Section 9.4, p. 84-86. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                85,
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors
//
//                // Operation Types
//                Tag.DIFFUSION,     // Particle diffusion behavior
//                Tag.HANDSHAKE,     // Tag for rules that use handshake protocols
//                Tag.CONSERVATION,  // Tag for rules that preserve particle count
//
//                // Behavior Types
//                Tag.DYNAMIC,       // Changes over time
//                Tag.PATTERN_SHAPING,
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the NAIVE-DIFFUSION automaton with
     * handshaking. Creates a {@link Grid} with the given dimensions, initialized by {@link InitDisk} with a disk-shaped
     * region of active cells (radius based on grid dimensions), and applies the {@link RuleNaiveDiffusion} with
     * handshaking enabled for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        InitDisk initDisk = new InitDisk((width + height) / 8);
        setGridInitializer(initDisk);
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, initDisk);
        config.put(CONF_GRID, grid);
        config.put(CONF_RULES, getRules());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}