package com.dungeoncode.ca.core;

/**
 * An abstract base class for rules in a cellular automaton, implementing the {@link Rule} interface.
 * Provides a unique identifier for the rule and serves as a foundation for specific rule implementations.
 * Subclasses must implement the {@link #apply(Grid, Cell)} method to define the logic for updating cell states.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public abstract class AbstractRule<C extends Cell<S>, S extends CellState<?>> implements Rule<C, S> {

    /**
     * The unique identifier for this rule.
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
     * @return the rule's ID
     */
    @Override
    public int getId() {
        return id;
    }
}