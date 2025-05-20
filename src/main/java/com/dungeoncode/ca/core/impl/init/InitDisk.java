package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Initializes a {@link Grid} for a cellular automaton by forming a disk-shaped region of active cells centered in the
 * grid. Cells within the disk radius have their state (value) set to active ({@code true}), while all other cells are
 * inactive ({@code false}), with echo set to false. This initializer is suitable for diffusion experiments like
 * NAIVE-DIFFUSION.
 *
 * @see GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class InitDisk extends InitNextStatesBoolean {

    /**
     * The radius of the disk-shaped region, measured from the grid's center.
     */
    private final int radius;

    /**
     * Constructs a new initializer with the specified radius for the disk-shaped active region.
     *
     * @param radius the radius of the disk, must be non-negative
     */
    public InitDisk(int radius) {
        this.radius = radius;
    }

    /**
     * Initializes the specified grid by setting cells within a disk-shaped region (radius from the grid's center) to
     * active ({@code true}) states with echo set to false, and all other cells to inactive ({@code false}) with echo
     * set to false.
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        super.initializeGrid(grid);
        int width = grid.getWidth();
        int height = grid.getHeight();
        int centerX = width / 2;
        int centerY = height / 2;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Calculate distance from center using Euclidean distance
                double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                boolean isActive = distance <= radius;
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, false));
                } else {
                    grid.getCell(x, y).setState(isActive, false);
                }
            }
        }
    }
}