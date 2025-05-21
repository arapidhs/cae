package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleGeneticDrift;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomSpecies;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Configures an Automaton to run the GENETIC-DRIFT cellular automaton, modeling diffusion of genes with species IDs.
 * Each cell copies or exchanges states with a random neighbor, restricted by species ID compatibility, producing
 * a mottled distribution of species. Initialized with a random distribution of active cells with species IDs.
 */
public class ConfGeneticDrift extends AbstractConfiguration<BooleanCell, BooleanState> {

    public ConfGeneticDrift() {
        super(24, null, List.of(new RuleGeneticDrift()));
//        super(
//                "Genetic Drift",
//                "A cellular automaton modeling genetic drift, where particles (genes) with species IDs diffuse by copying or exchanging states with a random neighbor (north, south, east, west). Movement is restricted to neighbors with no species (ID 0) or matching species IDs. Optionally splits the grid into 3x3 subgrids to simulate isolated populations. Initialized with a random distribution of active cells with species IDs, it produces a mottled distribution of species, as described in Toffoli and Margolus (1987).",
//                "Toffoli, T., & Margolus, N. (1987). Cellular Automata Machines: A New Environment for Modeling, Chapter 9, Section 9.4, p. 86. MIT Press. https://doi.org/10.7551/mitpress/1763.001.0001",
//                86,
//                // Rule Type
//                Tag.PROBABILISTIC,  // Due to random neighbor selection
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors
//
//                // Operation Types
//                Tag.DIFFUSION,     // Particle diffusion behavior
//                Tag.SPECIES,
//
//                // Behavior Types
//                Tag.DYNAMIC,       // Changes over time
//                Tag.STRUCTURED,    // Creates organized patterns of species
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
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