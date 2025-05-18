package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.CellState;

/**
 * Represents the state of a cell in the BRIAN'S-BRAIN cellular automaton, with three possible states:
 * Ready (quiescent, can fire), Firing (active), or Refractory (recovering, insensitive to stimuli).
 * Implements {@link CellState} with a {@link BrainStateValue} enum value.
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
     * Constructs a new BRIAN'S-BRAIN state with the specified value.
     *
     * @param value the state value ({@link BrainStateValue})
     */
    public BrainState(BrainStateValue value) {
        this.value = value;
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
}