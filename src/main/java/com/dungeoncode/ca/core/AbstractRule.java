package com.dungeoncode.ca.core;

/**
 * An abstract base class for rules in a cellular automaton, implementing the {@link Rule} interface.
 * Provides default implementation for storing and retrieving rule descriptors.
 * Subclasses must implement the {@link #apply(Grid, Cell)} method to define specific rule logic.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public abstract class AbstractRule<C extends Cell<S>, S extends CellState<?>> implements Rule<C, S> {

    /**
     * Unique identifier of the rule.
     */
    private final int id;

    /**
     * Constructs a new rule with the specified identifier.
     *
     * @param id the unique identifier for this rule
     */
    public AbstractRule(int id) {
        this.id = id;
    }

    /**
     * Returns the unique identifier for this rule.
     *
     * @return the rule's identifier
     */
    @Override
    public int getId() {
        return id;
    }

}