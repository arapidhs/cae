package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Initializes a {@link Grid} by setting cells in a centered square region to active ({@code true}) states
 * and all other cells to inactive ({@code false}). The square region is defined by a half-side length offset
 * from the grid's center, creating a localized active area suitable for cellular automata experiments.
 *
 * @see GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class InitCentralSquare extends InitNextStatesBoolean {

    /**
     * The half-side length offset from the grid's center, defining the size of the active square region.
     */
    private final int halfSideLength;
    private final boolean withEcho;

    public InitCentralSquare(int halfSideLength) {
        this(halfSideLength, false);
    }

    /**
     * Constructs a new initializer with the specified half-side length for the centered active square.
     *
     * @param halfSideLength the half-side length offset from the grid's center, must be non-negative
     */
    public InitCentralSquare(int halfSideLength, boolean withEcho) {
        this.halfSideLength = halfSideLength;
        this.withEcho = withEcho;
    }

    /**
     * Initializes the specified grid by setting cells within a centered square region to active
     * ({@code true}) states and all other cells to inactive ({@code false}). The square spans from
     * {@code centerX - halfSideLength} to {@code centerX + halfSideLength} horizontally and
     * {@code centerY - halfSideLength} to {@code centerY + halfSideLength} vertically, where
     * {@code centerX} and {@code centerY} are the grid's center coordinates.
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

        int startX = centerX - halfSideLength;
        int endX = centerX + halfSideLength;
        int startY = centerY - halfSideLength;
        int endY = centerY + halfSideLength;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isActive = x >= startX && x < endX && y >= startY && y < endY;
                boolean echo = withEcho && isActive;
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, echo));
                } else {
                    grid.getCell(x, y).setState(isActive, echo);
                }
            }
        }
    }
}