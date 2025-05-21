package com.dungeoncode.ca.core;

import com.dungeoncode.ca.automa.rules.RuleInkspot;
import com.dungeoncode.ca.core.impl.init.InitCentralBlob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.dungeoncode.ca.core.Constants.*;

public abstract class AbstractConfiguration<C extends Cell<S>, S extends CellState<?>> implements Configuration<C, S> {

    private final int id;
    private final List<Rule<C, S>> rules;
    private GridInitializer<C, S> gridInitializer;

    protected AbstractConfiguration(int id, GridInitializer<C, S> gridInitializer, List<Rule<C, S>> rules) {
        Objects.requireNonNull(rules);
        this.id = id;
        this.gridInitializer = gridInitializer;
        this.rules = rules;
    }

    public int getId() {
        return id;
    }

    @Override
    public GridInitializer<C, S> getGridInitializer() {
        return gridInitializer;
    }

    @Override
    public List<Rule<C, S>> getRules() {
        return rules;
    }

    /**
     * Configures the specified {@link Automa} with a grid, rule, and interval for the Inkspot automaton.
     * Creates a {@link Grid} with the given dimensions, initialized by {@link InitCentralBlob}
     * with a 7x7 random central region, and applies the {@link RuleInkspot} for state updates.
     *
     * @param automa         the {@link Automa} to configure
     * @param width          the width (number of columns) of the grid
     * @param height         the height (number of rows) of the grid
     * @param intervalMillis the interval in milliseconds between automaton steps
     */
    @Override
    public void configure(Automa<C, S> automa, int width, int height, long intervalMillis) {
        Map<String, Object> config = new HashMap<>();
        Grid<C, S> grid = new Grid<>(width, height, getGridInitializer());
        config.put(CONF_GRID, grid);
        config.put(CONF_RULES, getRules());
        config.put(CONF_INTERVAL_MILLIS, intervalMillis);
        automa.configure(config);
    }

    public void setGridInitializer(GridInitializer<C, S> gridInitializer) {
        this.gridInitializer = gridInitializer;
    }
}