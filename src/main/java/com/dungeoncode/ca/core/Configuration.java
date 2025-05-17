package com.dungeoncode.ca.core;

/**
 * Defines a configuration strategy for initializing and setting up an {@link Automa} in a cellular automaton.
 * Implementations provide metadata (name, description, citation) and configure the automaton with specific parameters.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public interface Configuration<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Returns the name of this configuration.
     *
     * @return the configuration name
     */
    String getName();

    /**
     * Returns a description of this configuration.
     *
     * @return the configuration description
     */
    String getDescription();

    /**
     * Configures the specified {@link Automa} with the given dimensions and interval.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    void configure(Automa<C, S> automa, int width, int height, long intervalMillis);

    /**
     * Returns a citation or reference for this configuration, if applicable.
     *
     * @return the citation or an empty string if none
     */
    String getCitation();
}