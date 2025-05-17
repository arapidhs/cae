package com.dungeoncode.ca.core;

/**
 * Defines a strategy for initializing a {@link Grid} with cells of type {@code C} and states of type {@code S}.
 * Implementations specify how to populate the grid with cells and their initial states.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public interface GridInitializer<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Initializes the specified grid with cells and their states.
     *
     * @param grid the {@link Grid} to initialize
     */
    void initializeGrid(Grid<C, S> grid);
}