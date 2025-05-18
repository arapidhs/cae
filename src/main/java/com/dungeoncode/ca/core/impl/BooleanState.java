package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.CellState;

/**
 * Represents a boolean state for a cell in a cellular automaton, indicating an active ({@code true}) or inactive
 * ({@code false}) state, with an optional echo flag for tracking state history. Implements {@link CellState} with a
 * boolean value and supports configurations requiring temporal state visualization.
 */
public class BooleanState implements CellState<Boolean> {

    /**
     * The boolean value of the state ({@code true} for active, {@code false} for inactive).
     */
    private final boolean value;

    /**
     * Indicates whether the state includes an echo effect for visualization.
     */
    private final boolean echo;

    /**
     * Constructs a new boolean state with the specified value and no echo effect.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     */
    public BooleanState(boolean value) {
        this(value, false);
    }

    /**
     * Constructs a new boolean state with the specified value and echo flag.
     *
     * @param value the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo  {@code true} to enable echo effect, {@code false} otherwise
     */
    public BooleanState(boolean value, boolean echo) {
        this.value = value;
        this.echo = echo;
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
}