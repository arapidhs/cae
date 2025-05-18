package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.PulseWeaverRule;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.PulseWeaverCell;
import com.dungeoncode.ca.core.impl.PulseWeaverRandomInitializer;
import com.dungeoncode.ca.core.impl.PulseWeaverState;

import java.util.HashMap;
import java.util.Map;

/**
 * Configures an {@link Automa} to run the Pulse Weaver cellular automaton, where cells transition between
 * Dormant, Charged, and Fading states based on their Moore neighborhood, creating pulsating, wave-like
 * patterns. The grid is initialized with a random distribution of states to encourage emergent, self-organizing
 * behavior, with stochastic persistence for Fading cells to introduce dynamic variability.
 *
 * @see PulseWeaverRule
 * @see PulseWeaverRandomInitializer
 */
public class PulseWeaverConfiguration extends AbstractConfiguration<PulseWeaverCell, PulseWeaverState> {

    /**
     * Constructs a new Pulse Weaver configuration with metadata for name and description. The configuration
     * is named "Pulse Weaver" and described as a cellular automaton with pulsating patterns driven by
     * threshold-based and stochastic state transitions.
     */
    public PulseWeaverConfiguration() {
        super(
                "Pulse Weaver",
                "A cellular automaton where cells transition between Dormant (inactive), Charged (actively pulsing), " +
                        "and Fading (decaying energy) states based on their Moore neighborhood. Initialized with a random " +
                        "distribution of states (70% Dormant, 15% Charged, 15% Fading), it produces pulsating, wave-like " +
                        "patterns with stochastic persistence in Fading cells, creating emergent structures resembling " +
                        "cosmic threads or bioluminescent waves. The rule balances activation and decay for sustained, " +
                        "dynamic evolution.",
                null
        );
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the Pulse Weaver automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link PulseWeaverRandomInitializer}
     * with a random distribution of Dormant, Charged, and Fading states, and applies the {@link PulseWeaverRule}
     * for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<PulseWeaverCell, PulseWeaverState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<PulseWeaverCell, PulseWeaverState> grid = new Grid<>(width, height, new PulseWeaverRandomInitializer());
        config.put("grid", grid);
        config.put("rule", new PulseWeaverRule());
        config.put("intervalMillis", intervalMillis);
        automa.configure(config);
    }
}