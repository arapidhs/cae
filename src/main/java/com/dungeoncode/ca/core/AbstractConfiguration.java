package com.dungeoncode.ca.core;

/**
 * An abstract base class for configurations in a cellular automaton, implementing the {@link Configuration} interface.
 * Provides default implementations for storing and retrieving configuration metadata (name, description, citation).
 * Subclasses must implement the {@link #configure(Automa, int, int, long)} method to define specific configuration logic.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public abstract class AbstractConfiguration<C extends Cell<S>, S extends CellState<?>> implements Configuration<C, S> {
    /**
     * The name of the configuration.
     */
    private final String name;

    /**
     * The description of the configuration.
     */
    private final String description;

    /**
     * The citation or reference for the configuration.
     */
    private final String citation;

    /**
     * The citation page;
     */
    private final int page;

    /**
     * Constructs a new configuration with the specified metadata.
     *
     * @param name        the name of the configuration
     * @param description a description of the configuration
     * @param citation    a citation or reference for the configuration, or an empty string if none
     */
    public AbstractConfiguration(String name, String description, String citation, int page) {
        this.name = name;
        this.description = description;
        this.citation = citation;
        this.page=page;
    }

    /**
     * Returns the name of this configuration.
     *
     * @return the configuration name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Returns a description of this configuration.
     *
     * @return the configuration description
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * Returns a citation or reference for this configuration.
     *
     * @return the citation or an empty string if none
     */
    @Override
    public String getCitation() {
        return citation;
    }

    /**
     * Returns the citation page.
     *
     * @return the citation page or 0 if N/A.
     */
    public int getPage() {
        return page;
    }
}