package com.dungeoncode.cae.core;

import java.util.List;

/**
 * Defines a configuration strategy for setting up a cellular automaton, specifying its grid, rules, and update interval.
 * Implementations configure an {@link Automaton} with specific initialization and state transition logic.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public interface Configuration<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Returns the unique identifier for this configuration.
     *
     * @return the configuration ID
     */
    int getId();

    /**
     * Returns the grid initializer for this configuration.
     *
     * @return the {@link GridInitializer} for populating the grid
     */
    GridInitializer<C, S> getGridInitializer();

    /**
     * Returns the list of rules for this configuration.
     *
     * @return an unmodifiable list of {@link Rule} objects for state transitions
     */
    List<Rule<C, S>> getRules();

    /**
     * Configures the specified {@link Automaton} with the given grid dimensions and update interval.
     *
     * @param automaton      the {@link Automaton} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    void configure(Automaton<C, S> automaton, int width, int height, long intervalMillis);
}