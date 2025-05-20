package com.dungeoncode.ca.core;

/**
 * Represents a cell in a cellular automaton grid, holding a position and a state.
 * The cell's state is of type {@code T}, which extends {@link CellState}.
 *
 * @param <T> the type of the cell's state, extending {@link CellState}
 */
public interface Cell<T extends CellState<?>> {

    /**
     * Returns the position of this cell in the grid.
     *
     * @return the {@link Position} of the cell
     */
    Position getPosition();

    /**
     * Returns the current state of this cell.
     *
     * @return the state of the cell, of type {@code T}
     */
    T getState();

    /**
     * Sets the state of this cell to the specified value.
     *
     * @param state the new state to set, of type {@code T}
     */
    void setState(T state);

    void copyState(T state);
}