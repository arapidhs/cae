package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.core.Grid;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Initializes a {@link Grid} for a one-dimensional cellular automaton. If singleActiveCenter is true, sets only the
 * center cell of row 0 to active (true), with all others inactive (false). Otherwise, assigns random boolean states
 * in row 0 based on region-specific density probabilities for west, center, and east segments. Other rows are inactive.
 * Designed for rules like ONED-RAND and elementary cellular automata, as described in <i>Cellular Automata Machines</i>
 * (MIT Press).
 *
 * @see BooleanCell
 * @see BooleanState
 * @see InitNextStatesBoolean
 */
public class InitOneDRandom extends InitNextStatesBoolean {

    /** Random number generator for state assignment. */
    private final Random random = new Random();

    /** Probability of active cells in the west region (columns 0 to width/3-1). */
    private final double westDensity;

    /** Probability of active cells in the center region (columns width/3 to 2*width/3-1). */
    private final double centerDensity;

    /** Probability of active cells in the east region (columns 2*width/3 to width-1). */
    private final double eastDensity;

    /** Whether to initialize with only one active cell at the center of row 0. */
    private final boolean singleActiveCenter;

    /**
     * Constructs a new one-dimensional random initializer with specified density probabilities for west, center,
     * and east regions of row 0, and an option for single active center cell.
     *
     * @param westDensity       probability of active cells in west region (0.0 to 1.0)
     * @param centerDensity     probability of active cells in center region (0.0 to 1.0)
     * @param eastDensity       probability of active cells in east region (0.0 to 1.0)
     * @param singleActiveCenter if true, only the center cell of row 0 is active; if false, uses random densities
     * @throws IllegalArgumentException if any density is not in [0.0, 1.0]
     */
    public InitOneDRandom(double westDensity, double centerDensity, double eastDensity, boolean singleActiveCenter) {
        super(11);
        if (westDensity < 0.0 || westDensity > 1.0 ||
                centerDensity < 0.0 || centerDensity > 1.0 ||
                eastDensity < 0.0 || eastDensity > 1.0) {
            throw new IllegalArgumentException("Density probabilities must be between 0.0 and 1.0");
        }
        this.westDensity = westDensity;
        this.centerDensity = centerDensity;
        this.eastDensity = eastDensity;
        this.singleActiveCenter = singleActiveCenter;
    }

    /**
     * Constructs a new one-dimensional random initializer with default density of 0.5 for all regions and random mode.
     */
    public InitOneDRandom() {
        this(0.5, 0.5, 0.5, false);
    }

    /**
     * Initializes the grid. If singleActiveCenter is true, sets only the center cell of row 0 (column floor(width/2))
     * to active (value=true), with all others inactive (value=false). Otherwise, assigns random boolean states in row 0
     * based on region-specific density probabilities (west, center, east). Other rows are inactive (value=false,
     * echo=false, liveSum=0, id=0).
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
        int centerX = width / 2; // Center column (floor division)

        // Initialize grid
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isActive = false;
                boolean echo = false;

                // Row 0: single active center or random density
                if (y == 0) {
                    if (singleActiveCenter) {
                        isActive = x == centerX;
                    } else {
                        double density;
                        if (x < width / 3) {
                            density = westDensity; // West region
                        } else if (x < 2 * width / 3) {
                            density = centerDensity; // Center region
                        } else {
                            density = eastDensity; // East region
                        }
                        isActive = random.nextDouble() < density;
                    }
                }

                // Set cell state
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, echo, 0));
                } else {
                    grid.getCell(x, y).setState(isActive, echo, 0);
                }
            }
        }
    }
}