package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleGeneticDrift;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomSpecies;

import java.util.HashMap;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an Automa to run the GENETIC-DRIFT cellular automaton, modeling diffusion of genes with species IDs.
 * Each cell copies or exchanges states with a random neighbor, restricted by species ID compatibility, producing
 * a mottled distribution of species. Initialized with a random distribution of active cells with species IDs.
 */
public class ConfGeneticDrift extends AbstractConfiguration<BooleanCell, BooleanState> {

    public ConfGeneticDrift() {
        super(
                "Genetic Drift",
                "A cellular automaton modeling genetic drift, where particles (genes) with species IDs diffuse by copying or exchanging states with a random neighbor (north, south, east, west). Movement is restricted to neighbors with no species (ID 0) or matching species IDs. Optionally splits the grid into 3x3 subgrids to simulate isolated populations. Initialized with a random distribution of active cells with species IDs, it produces a mottled distribution of species, as described in Toffoli and Margolus (1987).",
                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, Chapter 9, Section 9.4, p. 86. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
                86
        );
    }

    @Override
    public void configure(Automa<BooleanCell, BooleanState> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        // Initialize with 4 species and 10% of grid as active cells
        Grid<BooleanCell, BooleanState> grid = new Grid<>(width, height, new InitRandomSpecies(4, (width * height) / 40));
        config.put(CONF_GRID, grid);
        config.put(CONF_RULE, new RuleGeneticDrift());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }
}