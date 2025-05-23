package com.dungeoncode.cae.core;

/**
 * Manages a 2D grid of cells in a cellular automaton, storing cells of type {@code C} with states of type {@code S}.
 * The grid has a fixed width (columns) and height (rows), supports initialization, and allows manipulation of cell states.
 * Cells are stored in row-major order and accessed or modified using coordinates.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class Grid<C extends Cell<S>, S extends CellState<?>> {

    /**
     * The 2D array storing cells, indexed as [row][column] in row-major order.
     */
    private final C[][] grid;

    /**
     * The number of columns (x-axis) in the grid.
     */
    private final int width;

    /**
     * The number of rows (y-axis) in the grid.
     */
    private final int height;

    /**
     * The initializer used to populate the grid with cells, or null if no initialization is performed.
     */
    private final GridInitializer<C, S> initializer;

    /**
     * The 2D array storing next states for the next grid update.
     */
    private S[][] nextStates;

    /**
     * Constructs a new grid with the specified dimensions and initializer.
     * The grid is initialized using the provided {@link GridInitializer}, or all cells are set to null if no initializer is provided.
     *
     * @param width       the number of columns (x-axis), must be positive
     * @param height      the number of rows (y-axis), must be positive
     * @param initializer the {@link GridInitializer} to populate the grid, or null to set cells to null
     */
    public Grid(final int width, final int height, GridInitializer<C, S> initializer) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be positive: width=" + width + ", height=" + height);
        }
        this.width = width;
        this.height = height;
        this.grid = (C[][]) new Cell[height][width];
        this.initializer = initializer;
        initialize();
    }

    /**
     * Initializes the grid using the stored {@link GridInitializer}. If no initializer is provided, all cells remain null.
     */
    public void initialize() {
        if (initializer != null) {
            initializer.initializeGrid(this);
        }
    }

    /**
     * Copies the next state to the cell at the specified coordinates.
     *
     * @param x the x-coordinate (column), zero-based
     * @param y the y-coordinate (row), zero-based
     * @throws IllegalArgumentException if the coordinates are out of bounds
     * @throws IllegalStateException    if the cell or next state at (x, y) is null
     */
    public void copyCellState(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        C cell = grid[y][x];
        S state = nextStates[y][x];
        if (cell == null) {
            throw new IllegalStateException("Cannot set state: cell at (" + x + ", " + y + ") is null");
        }
        if (state == null) {
            throw new IllegalStateException("Cannot set state: next state at (" + x + ", " + y + ") is null");
        }
        cell.copyState(state);
    }

    /**
     * Retrieves the cell at the specified coordinates.
     *
     * @param x the x-coordinate (column), zero-based
     * @param y the y-coordinate (row), zero-based
     * @return the cell at (x, y), of type {@code C}, or null if uninitialized
     * @throws IllegalArgumentException if the coordinates are out of bounds
     */
    public C getCell(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        return grid[y][x];
    }

    /**
     * Sets the cell at the specified coordinates.
     *
     * @param x    the x-coordinate (column), zero-based
     * @param y    the y-coordinate (row), zero-based
     * @param cell the cell to set, of type {@code C}
     * @throws IllegalArgumentException if the coordinates are out of bounds
     */
    public void setCell(int x, int y, C cell) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates out of bounds: (" + x + ", " + y + ")");
        }
        grid[y][x] = cell;
    }

    /**
     * Returns the width (number of columns) of the grid.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height (number of rows) of the grid.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the 2D array of next states for the next grid update.
     *
     * @return the next states array, or null if not set
     */
    public S[][] getNextStates() {
        return nextStates;
    }

    /**
     * Sets the 2D array of next states for the next grid update.
     *
     * @param nextStates the next states array to set
     */
    public void setNextStates(S[][] nextStates) {
        this.nextStates = nextStates;
    }
}