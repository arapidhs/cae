package com.dungeoncode.cae.core.impl.init;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.GridInitializer;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import java.util.Random;

/**
 * Initializes a {@link Grid} by setting cells in a centered rectangular region to random boolean states
 * (active or inactive) and all other cells to inactive. The region is defined by horizontal and vertical
 * offsets from the grid's center, creating a localized "blob" of random activity suitable for cellular
 * automata experiments.
 *
 * @see GridInitializer
 * @see BooleanCell
 * @see BooleanState
 */
public class InitCentralBlob extends InitIntermediateStatesBoolean {

    /**
     * The horizontal offset from the grid's center, defining half the width of the random region.
     */
    private final int dx;

    /**
     * The vertical offset from the grid's center, defining half the height of the random region.
     */
    private final int dy;

    /**
     * Constructs a new initializer with the specified offsets for the centralized random region.
     *
     * @param dx the horizontal offset from the grid's center, must be non-negative
     * @param dy the vertical offset from the grid's center, must be non-negative
     */
    public InitCentralBlob(int dx, int dy) {
        super(4);
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Initializes the specified grid by setting cells within a centered rectangular region to random boolean
     * states (active or inactive) and all other cells to inactive. The region spans from
     * {@code centerX - dx} to {@code centerX + dx} horizontally and {@code centerY - dy} to
     * {@code centerY + dy} vertically, where {@code centerX} and {@code centerY} are the grid's center
     * coordinates.
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

        int startX = centerX - dx;
        int endX = centerX + dx;
        int startY = centerY - dy;
        int endY = centerY + dy;

        Random rnd = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean isActive = x >= startX && x < endX && y >= startY && y < endY;
                isActive = isActive && rnd.nextBoolean();
                if (grid.getCell(x, y) == null) {
                    grid.setCell(x, y, new BooleanCell(x, y, isActive, false, 0));
                } else {
                    grid.getCell(x, y).setState(isActive, false, 0);
                }
            }
        }
    }
}