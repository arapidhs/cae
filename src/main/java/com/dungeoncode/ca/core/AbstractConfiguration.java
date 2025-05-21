package com.dungeoncode.ca.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * An abstract base class for cellular automaton configurations, implementing the {@link Configuration} interface.
 * Provides common functionality for configuring an {@link Automaton} with a grid, rules, and update interval.
 * Subclasses must specify the grid initializer and rules for specific automaton behavior.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public abstract class AbstractConfiguration<C extends Cell<S>, S extends CellState<?>> implements Configuration<C, S> {

    /**
     * The unique identifier for this configuration.
     */
    private final int id;

    /**
     * The list of rules governing state transitions in the automaton.
     */
    private final List<Rule<C, S>> rules;

    /**
     * The initializer used to populate the grid with cells and states.
     */
    private GridInitializer<C, S> gridInitializer;

    /**
     * Constructs a new configuration with the specified identifier, grid initializer, and rules.
     *
     * @param id              the unique identifier for this configuration
     * @param gridInitializer the {@link GridInitializer} to initialize the grid
     * @param rules           the list of {@link Rule} objects for state updates
     * @throws NullPointerException if rules is null
     */
    protected AbstractConfiguration(int id, GridInitializer<C, S> gridInitializer, List<Rule<C, S>> rules) {
        Objects.requireNonNull(rules, "Rules cannot be null");
        this.id = id;
        this.gridInitializer = gridInitializer;
        this.rules = rules;
    }

    /**
     * Returns the unique identifier for this configuration.
     *
     * @return the configuration ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the grid initializer used to populate the grid.
     *
     * @return the {@link GridInitializer} for this configuration
     */
    @Override
    public GridInitializer<C, S> getGridInitializer() {
        return gridInitializer;
    }

    /**
     * Returns the list of rules governing state transitions.
     *
     * @return the list of {@link Rule} objects
     */
    @Override
    public List<Rule<C, S>> getRules() {
        return rules;
    }

    /**
     * Configures the specified {@link Automaton} with a grid, rules, and update interval.
     * Creates a {@link Grid} with the given dimensions, initialized by the configured
     * {@link GridInitializer}, and applies the configured list of {@link Rule} objects
     * for state updates.
     *
     * @param automaton      the {@link Automaton} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automaton<C, S> automaton, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<C, S> grid = new Grid<>(width, height, getGridInitializer());
        config.put(CONF_GRID, grid);
        config.put(CONF_RULES, getRules());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automaton.configure(config);
    }

    /**
     * Sets the grid initializer for this configuration.
     *
     * @param gridInitializer the {@link GridInitializer} to set
     */
    public void setGridInitializer(GridInitializer<C, S> gridInitializer) {
        this.gridInitializer = gridInitializer;
    }
}