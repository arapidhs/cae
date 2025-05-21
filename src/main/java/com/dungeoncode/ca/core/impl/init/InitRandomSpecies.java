package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Initializes a {@link Grid} for a cellular automaton with a random distribution of active cells assigned non-zero species IDs
 * (1 to numSpecies), up to a specified population size. Remaining cells are inactive with species ID 0. Designed for species-based
 * rules, as described in <i>Cellular Automata Machines</i> (MIT Press).
 *
 * @see BooleanCell
 * @see BooleanState
 * @see InitIntermediateStatesBoolean
 */
public class InitRandomSpecies extends InitIntermediateStatesBoolean {

    /**
     * The number of distinct species (1 to numSpecies).
     */
    private final int numSpecies;

    /**
     * The number of active cells to place.
     */
    private final int population;

    /**
     * Random number generator for cell placement and species assignment.
     */
    private final Random random = new Random();

    /**
     * Constructs a new species-based grid initializer with the specified number of species and population.
     *
     * @param numSpecies the number of distinct species (1 to 4), must be positive
     * @param population the number of active cells to place, must be non-negative
     * @throws IllegalArgumentException if numSpecies is not in [1, 4] or population is negative
     */
    public InitRandomSpecies(int numSpecies, int population) {
        super(8);
        if (numSpecies < 1 || numSpecies > 4) {
            throw new IllegalArgumentException("Number of species must be between 1 and 4, got: " + numSpecies);
        }
        if (population < 0) {
            throw new IllegalArgumentException("Population cannot be negative, got: " + population);
        }
        this.numSpecies = numSpecies;
        this.population = population;
    }

    /**
     * Initializes the specified grid by setting all cells to inactive (value=false, species ID=0), then randomly
     * placing up to the specified population of active cells (value=true) with random species IDs (1 to numSpecies).
     * Ensures the population does not exceed the grid size.
     *
     * @param grid the {@link Grid} to initialize, must not be null
     * @throws NullPointerException if grid is null
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BooleanCell, BooleanState> grid) {
        java.util.Objects.requireNonNull(grid, "Grid cannot be null");
        super.initializeGrid(grid);

        int width = grid.getWidth();
        int height = grid.getHeight();
        int maxPopulation = Math.min(population, width * height);
        int placed = 0;

        // Set all cells to inactive with species ID 0
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, false, false, 0));
                } else {
                    grid.getCell(x, y).setState(false, false, 0);
                }
            }
        }

        // Place active cells with random species IDs
        while (placed < maxPopulation) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            BooleanCell cell = grid.getCell(x, y);
            if (cell != null && !cell.getState().getValue()) {
                int speciesId = random.nextInt(numSpecies) + 1; // Random ID from 1 to numSpecies
                cell.setState(true, false, speciesId);
                placed++;
            }
        }
    }
}