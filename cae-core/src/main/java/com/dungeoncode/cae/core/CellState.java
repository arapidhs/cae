package com.dungeoncode.cae.core;

/**
 * Represents the state of a cell in a cellular automaton.
 * The state is associated with a value of type {@code T}, which can be retrieved.
 *
 * @param <T> the type of the state value
 */
public interface CellState<T> {

    /**
     * Returns the value of this cell state.
     *
     * @return the state value, of type {@code T}
     */
    T getValue();
}