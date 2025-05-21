package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.Random;

/**
 * Initializes a grid with a random distribution of active cells with species IDs.
 * Places a specified number of active cells (population) with random non-zero species IDs (1 to numSpecies).
 * Remaining cells are inactive with ID 0.
 */
public class InitRandomSpecies extends InitNextStatesBoolean {
    private final int numSpecies;
    private final int population;

    public InitRandomSpecies(int numSpecies, int population) {
        this.numSpecies = numSpecies;
        this.population = population;
    }

    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        super.initializeGrid(grid);

        if ( numSpecies > 4 ) {
            throw new RuntimeException("Number of species cannot be higher than 4.");
        }

        Random rnd = new Random();
        int placed = 0;
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Set cells to inactive with ID 0
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BooleanCell cell = grid.getCell(x, y);
                if (cell == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, false, false, 0));
                } else {
                    cell.setState(false, false, 0);
                }
            }
        }

        // Ensure population doesn't exceed grid size
        int maxPopulation = Math.min(population, width * height);

        while (placed < maxPopulation) {
            int x = rnd.nextInt(width);
            int y = rnd.nextInt(height);
            BooleanCell cell = grid.getCell(x, y);
            if (cell == null || !cell.getState().getValue()) {
                int speciesId = rnd.nextInt(numSpecies) + 1; // Random ID from 1 to numSpecies
                if (cell == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, true, false, speciesId));
                } else {
                    cell.setState(true, false, speciesId);
                }
                placed++;
            }
        }

    }
}