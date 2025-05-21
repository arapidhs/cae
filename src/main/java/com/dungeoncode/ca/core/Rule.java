package com.dungeoncode.ca.core;

/**
 * Defines a rule for updating the state of a cell in a cellular automaton.
 * Implementations specify how a cell's state is computed based on the current grid and the cell's properties.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public interface Rule<C extends Cell<S>, S extends CellState<?>> {


    /**
     * Returns the unique identifier for this rule.
     *
     * @return the rule's identifier
     */
    int getId();

    /**
     * Applies the rule to compute the new state of a given cell in the grid.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link Cell} whose state is to be updated
     * @return the new state of the cell, of type {@code S}
     */
    S apply(Grid<C, S> grid, C cell);

}