package com.dungeoncode.ca.core;

import java.util.List;

public interface Configuration<C extends Cell<S>, S extends CellState<?>> {

    int getId();

    /**
     * Returns the grid initializer for this configuration.
     *
     * @return the grid initializer
     */
    GridInitializer<C, S> getGridInitializer();

    /**
     * Returns the list of rules for this configuration.
     *
     * @return the list of rules
     */
    List<Rule<C, S>> getRules();

    /**
     * Configures the specified {@link Automa} with the given dimensions and interval.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    void configure(Automa<C, S> automa, int width, int height, long intervalMillis);
}