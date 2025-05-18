package com.dungeoncode.ca.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static com.dungeoncode.ca.core.Constants.*;

/**
 * Represents a cellular automaton that manages a grid of cells and applies rules to update their states periodically.
 * The automaton supports configuration, execution control (start, pause, resume, stop), and rendering through a consumer.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class Automa<C extends Cell<S>, S extends CellState<?>> {
    /**
     * Logger for recording automaton events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Automa.class);

    /**
     * The primary grid holding the current state of cells.
     */
    private Grid<C, S> grid;

    private S[][] newStates;

    /**
     * The rule applied to update cell states.
     */
    private Rule<C, S> rule;

    /**
     * The consumer responsible for rendering or processing the grid.
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
    public Automa() {
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * Configures the automaton with the specified settings.
     * Retrieves the grid, rule, and interval from the configuration map.
     *
     * @param config a map containing configuration settings:
     *               <ul>
     *                 <li>"grid": the {@link Grid} to use</li>
     *                 <li>"rule": the {@link Rule} for state updates</li>
     *                 <li>"intervalMillis": the interval in milliseconds (as a String)</li>
     *               </ul>
     * @throws NumberFormatException if "intervalMillis" is not a valid long
     */
    public void configure(Map<String, Object> config) {
        this.grid = (Grid<C, S>) config.get(CONF_GRID);
        this.newStates = (S[][]) new CellState[grid.getWidth()][grid.getHeight()];
        this.rule = (Rule<C, S>) config.get(CONF_RULE);
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
     * Executes a single step of the automaton, updating cell states based on the configured rule.
     * The current grid is copied to a secondary grid, rules are applied to update states, and the
     * updated states are copied back to the primary grid.
     */
    public void step() {

        // Apply rules to update newGrid
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                C cell = grid.getCell(x, y);
                S newState = rule.apply(grid, cell);
                newStates[x][y] = newState;
            }
        }

        // Copy newGrid back to grid
        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                grid.setCellState(x, y, newStates[x][y]);
            }
        }
    }

    /**
     * Sets the consumer responsible for rendering or processing the grid after each step.
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

    public Grid<C, S> getGrid() {
        return grid;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    /**
     * Sets the interval between automaton steps.
     * If the automaton is running, it is paused, updated, and resumed with the new interval.
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
     * Stops the automaton, shutting down the executor and halting all updates.
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