package com.dungeoncode.ca.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Manages a cellular automaton, handling a grid of cells and applying rules to update their states periodically.
 * Supports configuration, execution control (start, resume, stop), and rendering via a consumer.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class Automaton<C extends Cell<S>, S extends CellState<?>> {
    /**
     * Logger for recording automaton events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Automaton.class);

    /**
     * The grid holding the current state of cells.
     */
    private Grid<C, S> grid;

    /**
     * The list of rules applied to update cell states.
     */
    private List<Rule<C, S>> rules;

    /**
     * The consumer for rendering or processing the grid after each step.
     */
    private Consumer<Grid<C, S>> gridConsumer;

    /**
     * The interval in milliseconds between automaton steps.
     */
    private long intervalMillis;

    /**
     * Indicates whether the automaton is currently running.
     */
    private volatile boolean isRunning;

    /**
     * Executor service for scheduling periodic updates.
     */
    private ScheduledExecutorService executor;

    /**
     * Constructs a new automaton with a single-threaded executor for periodic updates.
     */
    public Automaton() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Configures the automaton with settings from the provided map.
     *
     * @param config a map containing:
     *               <ul>
     *                 <li>{@code CONF_GRID}: the {@link Grid} to use</li>
     *                 <li>{@code CONF_RULES}: the list of {@link Rule} objects for state updates</li>
     *                 <li>{@code CONF_INTERVAL_MILLIS}: the interval in milliseconds (as a String or Long)</li>
     *               </ul>
     * @throws NumberFormatException if {@code CONF_INTERVAL_MILLIS} is not a valid long
     */
    public void configure(Map<String, Object> config) {
        this.grid = (Grid<C, S>) config.get(CONF_GRID);
        this.rules = (List<Rule<C, S>>) config.get(CONF_RULES);
        this.intervalMillis = Long.parseLong(String.valueOf(config.get(CONF_INTERVAL_MILLIS)));
    }

    /**
     * Starts the automaton by invoking {@link #resume()}.
     */
    public void start() {
        resume();
    }

    /**
     * Resumes or starts the automaton, scheduling periodic updates at the configured interval.
     * If already running, this method has no effect.
     */
    public void resume() {
        if (!isRunning) {
            isRunning = true;
            LOGGER.info("Automaton resumed with interval: {}ms", intervalMillis);
            executor.scheduleAtFixedRate(() -> {
                try {
                    if (isRunning) {
                        step();
                        if (gridConsumer != null) {
                            gridConsumer.accept(grid);
                        } else {
                            LOGGER.warn("No gridConsumer set");
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Error in step execution: {}", e.getMessage(), e);
                    isRunning = false;
                }
            }, 0, intervalMillis, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * Executes a single step of the automaton, applying all configured rules to update cell states.
     * Rules are applied sequentially to each cell, and updated states are copied back to the grid.
     */
    public void step() {
        for (Rule<C, S> rule : rules) {
            for (int x = 0; x < grid.getWidth(); x++) {
                for (int y = 0; y < grid.getHeight(); y++) {
                    C cell = grid.getCell(x, y);
                    rule.apply(grid, cell);
                }
            }
        }
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                grid.copyCellState(x, y);
            }
        }
    }

    /**
     * Sets the consumer for rendering or processing the grid after each step.
     *
     * @param gridConsumer the consumer to process the grid, or null to disable rendering
     */
    public void setGridConsumer(Consumer<Grid<C, S>> gridConsumer) {
        this.gridConsumer = gridConsumer;
    }

    /**
     * Checks whether the automaton is currently running.
     *
     * @return true if the automaton is running, false otherwise
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Returns the grid used by the automaton.
     *
     * @return the current {@link Grid}
     */
    public Grid<C, S> getGrid() {
        return grid;
    }

    /**
     * Returns the interval between automaton steps.
     *
     * @return the interval in milliseconds
     */
    public long getIntervalMillis() {
        return intervalMillis;
    }

    /**
     * Sets the interval between automaton steps. If running, the automaton is stopped, updated, and resumed.
     *
     * @param newIntervalMillis the new interval in milliseconds
     * @throws IllegalArgumentException if the interval is not positive
     */
    public void setIntervalMillis(long newIntervalMillis) {
        if (newIntervalMillis <= 0) {
            throw new IllegalArgumentException("Interval must be positive: " + newIntervalMillis);
        }
        boolean wasRunning = isRunning;
        if (isRunning) {
            stop();
        }
        this.intervalMillis = newIntervalMillis;
        if (wasRunning) {
            resume();
        }
        LOGGER.info("Interval updated to {}ms", newIntervalMillis);
    }

    /**
     * Stops the automaton, shutting down the executor and halting updates.
     * A new executor is created for future starts.
     */
    public void stop() {
        if (executor != null) {
            isRunning = false;
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
            LOGGER.info("Automaton stopped");
            this.executor = Executors.newSingleThreadScheduledExecutor();
        }
    }
}