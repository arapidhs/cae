package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.CellState;

/**
 * Represents the state of a cell in the Pulse Weaver cellular automaton, with three possible states:
 * Dormant (inactive), Charged (actively pulsing), or Fading (decaying energy). Implements {@link CellState}
 * with a {@link PulseState} enum value and supports tracking persistent Fading states for stochastic behavior.
 *
 * @see CellState
 */
public class PulseWeaverState implements CellState<PulseWeaverState.PulseState> {

    /**
     * Enum defining the possible states of a Pulse Weaver cell.
     */
    public enum PulseState {
        DORMANT,
        CHARGED,
        FADING
    }

    /**
     * The state value of the cell (Dormant, Charged, or Fading).
     */
    private PulseState value;

    /**
     * Indicates whether the cell is in a persistent Fading state, used for stochastic behavior.
     */
    private boolean persistent;

    /**
     * Constructs a new Pulse Weaver state with the specified value and no persistence.
     *
     * @param value the state value ({@link PulseState})
     */
    public PulseWeaverState(PulseState value) {
        this(value, false);
    }

    /**
     * Constructs a new Pulse Weaver state with the specified value and persistence flag.
     *
     * @param value        the state value ({@link PulseState})
     * @param isPersistent {@code true} if the Fading state is persistent, {@code false} otherwise
     */
    public PulseWeaverState(PulseState value, boolean isPersistent) {
        this.value = value;
        this.persistent = isPersistent;
    }

    public PulseWeaverState() {
        this(null, false);
    }

    /**
     * Returns the state value of this cell.
     *
     * @return the state value, of type {@link PulseState}
     */
    @Override
    public PulseState getValue() {
        return value;
    }

    public void set(PulseState value, boolean persistent) {
        this.value=value;
        this.persistent =persistent;
    }


    /**
     * Returns whether the Fading state is persistent.
     *
     * @return {@code true} if the Fading state is persistent, {@code false} otherwise
     */
    public boolean isPersistent() {
        return persistent;
    }
}