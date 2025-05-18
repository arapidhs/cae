package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.CellState;

/**
 * Represents the state of a cell in the BRIAN'S-BRAIN cellular automaton, with three possible states:
 * Ready (quiescent, can fire), Firing (active), or Refractory (recovering, insensitive to stimuli).
 * Implements {@link CellState} with a {@link BrainStateValue} enum value, and includes an echo field to
 * track the previous state for second-order dynamics.
 *
 * @see CellState
 */
public class BrainState implements CellState<BrainState.BrainStateValue> {

    /**
     * Enum defining the possible states of a BRIAN'S-BRAIN cell.
     */
    public enum BrainStateValue {
        READY,
        FIRING,
        REFRACTORY
    }

    /**
     * The state value of the cell (Ready, Firing, or Refractory).
     */
    private final BrainStateValue value;

    /**
     * The previous state value of the cell, used for second-order dynamics (echo mechanism).
     */
    private final BrainStateValue echo;

    /**
     * Constructs a new BRIAN'S-BRAIN state with the specified value and a default echo of READY.
     *
     * @param value the state value ({@link BrainStateValue})
     */
    public BrainState(BrainStateValue value) {
        this(value, BrainStateValue.READY);
    }

    /**
     * Constructs a new BRIAN'S-BRAIN state with the specified value and echo.
     *
     * @param value the state value ({@link BrainStateValue})
     * @param echo the previous state value ({@link BrainStateValue}) for second-order dynamics
     */
    public BrainState(BrainStateValue value, BrainStateValue echo) {
        this.value = value;
        this.echo = echo;
    }

    /**
     * Returns the state value of this cell.
     *
     * @return the state value, of type {@link BrainStateValue}
     */
    @Override
    public BrainStateValue getValue() {
        return value;
    }

    /**
     * Returns the previous state value (echo) of this cell.
     *
     * @return the echo value, of type {@link BrainStateValue}
     */
    public BrainStateValue getEcho() {
        return echo;
    }
}