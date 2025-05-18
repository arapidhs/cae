package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.CellState;

/**
 * Represents a boolean state for a cell in a cellular automaton, indicating an active ({@code true}) or inactive
 * ({@code false}) state, with an optional echo flag for tracking state history and a live sum for neighborhood
 * activity. Implements {@link CellState} with a boolean value and supports configurations requiring temporal
 * state visualization or neighbor-based rendering.
 */
public class BooleanState implements CellState<Boolean> {

    /**
     * The boolean value of the state ({@code true} for active, {@code false} for inactive).
     */
    private boolean value;

    /**
     * Indicates whether the state includes an echo effect for visualization.
     */
    private boolean echo;

    /**
     * The number of live (active) cells in the cell's neighborhood, used for rendering based on local activity.
     */
    private int liveSum;

    /**
     * Constructs a new boolean state with the specified value, no echo effect, and zero live sum.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     */
    public BooleanState(boolean value) {
        this(value, false, 0);
    }

    /**
     * Constructs a new boolean state with the specified value, echo flag, and zero live sum.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo  {@code true} to enable echo effect, {@code false} otherwise
     */
    public BooleanState(boolean value, boolean echo) {
        this(value, echo, 0);
    }

    /**
     * Constructs a new boolean state with the specified value, echo flag, and live sum.
     *
     * @param value   the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo    {@code true} to enable echo effect, {@code false} otherwise
     * @param liveSum the number of live cells in the neighborhood
     */
    public BooleanState(boolean value, boolean echo, final int liveSum) {
        this.value = value;
        this.echo = echo;
        this.liveSum = liveSum;
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
     * Swap the values of value and echo.
     */
    public void swapEcho() {
        boolean v=value;
        this.value=this.echo;
        this.echo=v;
    }

}