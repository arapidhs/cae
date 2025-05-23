package com.dungeoncode.cae.automa;

import com.dungeoncode.cae.automa.rules.RuleGeneticDrift;
import com.dungeoncode.cae.core.AbstractConfiguration;
import com.dungeoncode.cae.core.Automaton;
import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.impl.init.InitRandomSpecies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeoncode.cae.core.Constants.*;

/**
 * Configures an Automaton to run the GENETIC-DRIFT cellular automaton, modeling diffusion of genes with species IDs.
 * Each cell copies or exchanges states with a random neighbor, restricted by species ID compatibility, producing
 * a mottled distribution of species. Initialized with a random distribution of active cells with species IDs.
 */
public class ConfGeneticDrift extends AbstractConfiguration<BooleanCell, BooleanState> {

    public ConfGeneticDrift() {
        super(24, null, List.of(new RuleGeneticDrift()));
    }

    @Override
    public void configure(Automaton<BooleanCell, BooleanState> automaton, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        // Initialize with 4 species and 10% of grid as active cells
        InitRandomSpecies initRandomSpecies = new InitRandomSpecies(4, (width * height) / 40);
        setGridInitializer(initRandomSpecies);
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, initRandomSpecies);
        config.put(CONF_GRID, grid);
        config.put(CONF_RULES, getRules());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automaton.configure(config);
    }
}