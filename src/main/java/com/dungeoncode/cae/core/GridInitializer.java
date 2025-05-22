package com.dungeoncode.cae.core;

/**
 * Defines a strategy for initializing a {@link Grid} with cells of type {@code C} and states of type {@code S}.
 * Implementations populate the grid with cells and set their initial states according to specific rules or patterns.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public interface GridInitializer<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Returns the unique identifier for this grid initializer.
     *
     * @return the initializer ID
     */
    int getId();

    /**
     * Initializes the specified grid by populating it with cells and setting their initial states.
     *
     * @param grid the {@link Grid} to initialize
     */
    void initializeGrid(Grid<C, S> grid);
}