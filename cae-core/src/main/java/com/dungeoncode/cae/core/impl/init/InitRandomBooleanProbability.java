package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Initializes a {@link Grid} with a random distribution of active and inactive cells, with a specified probability
 * for active cells.
 */
public class InitRandomBooleanProbability extends InitNextStatesBoolean {

    private final Random random = new Random();
    private final double activeProbability;

    /**
     * Constructs a new initializer with the specified probability for active cells.
     *
     * @param activeProbability probability of a cell being active (0.0 to 1.0)
     */
    public InitRandomBooleanProbability(double activeProbability) {
        super(10); // New ID, adjust as needed
        if (activeProbability < 0.0 || activeProbability > 1.0) {
            throw new IllegalArgumentException("Active probability must be between 0.0 and 1.0");
        }
        this.activeProbability = activeProbability;
    }

    /**
     * Initializes the grid with random boolean states (active/inactive) based on the specified active probability.
     *
     * @param grid the {@link Grid} to initialize, must not be null
     * @throws NullPointerException if grid is null
     */
    @Override
    public void initializeGrid(@Nonnull Grid<BooleanCell, BooleanState> grid) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        super.initializeGrid(grid);
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isActive = random.nextDouble() < activeProbability;
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, false, 0));
                } else {
                    grid.getCell(x, y).setState(isActive, false, 0);
                }
            }
        }
    }
}