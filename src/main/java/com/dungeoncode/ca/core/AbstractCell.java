package com.dungeoncode.ca.core;

/**
 * An abstract base class for cells in a cellular automaton, implementing the {@link Cell} interface.
 * Provides a default implementation for storing and retrieving the cell's position.
 * Subclasses must implement state-related methods ({@link #getState()} and {@link #setState(CellState)} (Object)}).
 *
 * @param <T> the type of the cell's state, extending {@link CellState}
 */
public abstract class AbstractCell<T extends CellState<?>> implements Cell<T> {
    /**
     * The position of the cell in the grid.
     */
    private final Position position;

    /**
     * Constructs a new cell with the specified x and y coordinates.
     *
     * @param x the x-coordinate (column) of the cell
     * @param y the y-coordinate (row) of the cell
     */
    public AbstractCell(final int x, final int y) {
        this.position = new Position(x, y);
    }

    /**
     * Returns the position of this cell in the grid.
     *
     * @return the {@link Position} of the cell
     */
    @Override
    public Position getPosition() {
        return position;
    }
}