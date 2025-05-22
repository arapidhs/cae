package com.dungeoncode.cae.core.impl;

import com.dungeoncode.cae.core.CellState;

/**
 * Represents a boolean state for a cell in a cellular automaton, indicating an active ({@code true}) or inactive
 * ({@code false}) state, with fields for tracking state history (echo), neighborhood activity (live sum), and a timer
 * for countdown mechanisms. Implements {@link CellState} with a boolean value and supports configurations requiring
 * temporal state tracking, neighbor-based rendering, or timed dynamics.
 */
public class BooleanState implements CellState<Boolean> {

    /**
     * The boolean value of the state ({@code true} for active, {@code false} for inactive).
     */
    private boolean value;

    /**
     * Tracks the previous state value for second-order dynamics.
     */
    private boolean echo;

    /**
     * The number of live (active) cells in the cell's neighborhood, used for rendering based on local activity.
     */
    private int liveSum;

    /**
     * A timer for countdown mechanisms, such as retraction cycles in rules like TUBE-WORMS (0 = idle, >0 = counting down).
     */
    private int timer;

    /**
     * An identifier for the cell state, used to distinguish different categories or types (0 = none, >0 = specific category).
     */
    private int id;

    /**
     * Constructs a new boolean state with the specified value, no echo effect, and zero live sum, timer, and id.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     */
    public BooleanState(boolean value) {
        this(value, false, 0, 0, 0);
    }

    /**
     * Constructs a new boolean state with the specified value, echo flag, live sum, timer, and id.
     *
     * @param value   the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo    {@code true} to enable echo effect, {@code false} otherwise
     * @param liveSum the number of live cells in the neighborhood
     * @param timer   the timer value for countdown mechanisms (0 = idle, >0 = counting down)
     * @param id      the identifier for the state (0 = none, >0 = specific category)
     */
    public BooleanState(boolean value, boolean echo, final int liveSum, final int timer, final int id) {
        this.value = value;
        this.echo = echo;
        this.liveSum = liveSum;
        this.timer = timer;
        this.id = id;
    }

    /**
     * Constructs a new boolean state with the specified value, echo flag, and zero live sum, timer, and id.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo  {@code true} to enable echo effect, {@code false} otherwise
     */
    public BooleanState(boolean value, boolean echo) {
        this(value, echo, 0, 0, 0);
    }

    /**
     * Constructs a new boolean state with default values: inactive, no echo, zero live sum, timer, and id.
     */
    public BooleanState() {
        this(false, false, 0, 0, 0);
    }

    /**
     * Returns the boolean value of this state.
     *
     * @return {@code true} if the state is active, {@code false} if inactive
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * Returns whether the echo effect is enabled for this state.
     *
     * @return {@code true} if the echo effect is enabled, {@code false} otherwise
     */
    public boolean isEcho() {
        return echo;
    }

    /**
     * Returns the number of live cells in the cell's neighborhood.
     *
     * @return the live sum
     */
    public int getLiveSum() {
        return liveSum;
    }

    /**
     * Returns the timer value for countdown mechanisms.
     *
     * @return the timer value (0 = idle, >0 = counting down)
     */
    public int getTimer() {
        return timer;
    }

    /**
     * Returns the identifier for this state.
     *
     * @return the id (0 = none, >0 = specific category)
     */
    public int getId() {
        return id;
    }

    /**
     * Swaps the values of the state (value) and echo fields.
     */
    public void swapEcho() {
        boolean v = value;
        this.value = this.echo;
        this.echo = v;
    }

    /**
     * Sets the state, echo, live sum, timer, and id to the specified values.
     *
     * @param value   the new boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo    the new echo value ({@code true} to enable echo, {@code false} otherwise)
     * @param liveSum the new number of live cells in the neighborhood
     * @param timer   the new timer value (0 = idle, >0 = counting down)
     * @param id      the new identifier (0 = none, >0 = specific category)
     */
    public void set(boolean value, boolean echo, int liveSum, int timer, int id) {
        this.value = value;
        this.echo = echo;
        this.liveSum = liveSum;
        this.timer = timer;
        this.id = id;
    }

    /**
     * Sets the state, echo, live sum, and timer to the specified values, keeping the current id.
     *
     * @param value   the new boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo    the new echo value ({@code true} to enable echo, {@code false} otherwise)
     * @param liveSum the new number of live cells in the neighborhood
     * @param timer   the new timer value (0 = idle, >0 = counting down)
     */
    public void set(boolean value, boolean echo, int liveSum, int timer) {
        this.value = value;
        this.echo = echo;
        this.liveSum = liveSum;
        this.timer = timer;
    }

    /**
     * Sets the state, echo, live sum, and timer to the specified values, keeping the current id.
     *
     * @param value   the new boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo    the new echo value ({@code true} to enable echo, {@code false} otherwise)
     * @param liveSum the new number of live cells in the neighborhood
     */
    public void set(boolean value, boolean echo, int liveSum) {
        this.value = value;
        this.echo = echo;
        this.liveSum = liveSum;
        this.timer = 0;
    }
}