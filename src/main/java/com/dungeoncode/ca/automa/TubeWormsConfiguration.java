package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.TubeWormsRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Configures an {@link Automa} to run the TUBE-WORMS cellular automaton, modeling a reef of tube-worms with coupled
 * dynamics. The grid is initialized with a random distribution of active (out) and hiding worms in the state (50%
 * probability) and random timer values (0-3), using a custom initializer. This configuration produces dynamic wave
 * patterns like ripples or spirals, as described in Chapter 9, Section 9.3 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i>.
 *
 * @see TubeWormsRule
 */
public class TubeWormsConfiguration extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new TUBE-WORMS configuration with metadata for name, description, and citation. The configuration
     * is named "Tube Worms Dynamics," described as a cellular automaton modeling tube-worms with coupled dynamics,
     * and cites the book by Toffoli and Margolus.
     */
    public TubeWormsConfiguration() {
        super(
                "Tube Worms Dynamics",
                "A cellular automaton modeling a reef of tube-worms, where the state (value) represents the worm's status " +
                        "(true = active/out, false = hiding) and a timer (stored in the state) counts down (3 to 0). Worms " +
                        "retract if active neighbors exceed a threshold (default 3), triggering the timer before re-emerging. " +
                        "Initialized with a random distribution of active/hiding worms (50% probability) and random timer " +
                        "values (0-3), it produces dynamic wave patterns like ripples or spirals, as described in Toffoli and " +
                        "Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, " +
                        "Chapter 9, Section 9.3, p. 82-84. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001"
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the TUBE-WORMS automaton.
     * Creates a {@link Grid} with the given dimensions, initialized with a random distribution of active/hiding worms
     * (50% probability) in the state and random timer values (0-3), and applies the {@link TubeWormsRule} for state
     * updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, (gridToInit) -> {
            gridToInit.setNextStates(new BooleanState[height][width]);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    BooleanState[][] intermediateStates = gridToInit.getNextStates();
                    intermediateStates[y][x] = new BooleanState();
                }
            }
            int w = gridToInit.getWidth();
            int h = gridToInit.getHeight();
            Random random = new Random();

            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    // Random worm state (50% active/out) and random timer (0-3)
                    boolean value = random.nextBoolean();
                    int timer = random.nextInt(3); // 0 to 3
                    gridToInit.setCell(x, y, new BooleanCell(x, y, new BooleanState(timer==0, false, 0, timer)));
                }
            }
        });
        config.put("grid", grid);
        config.put("rule", new TubeWormsRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}