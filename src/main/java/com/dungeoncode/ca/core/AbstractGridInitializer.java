package com.dungeoncode.ca.core;

/**
 * An abstract base class for grid initializers in a cellular automaton, implementing the {@link GridInitializer} interface.
 * Provides a unique identifier for the initializer and serves as a foundation for specific grid initialization strategies.
 * Subclasses must implement the {@link #initializeGrid(Grid)} method to define how the grid is populated with cells and states.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public abstract class AbstractGridInitializer<C extends Cell<S>, S extends CellState<?>> implements GridInitializer<C, S> {

    /**
     * The unique identifier for this grid initializer.
     */
    private final int id;

    /**
     * Constructs a new grid initializer with the specified identifier.
     *
     * @param id the unique identifier for this initializer
     */
    public AbstractGridInitializer(int id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier for this grid initializer.
     *
     * @return the initializer's ID
     */
    @Override
    public int getId() {
        return id;
    }
}