package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.AbstractCell;

import javax.annotation.Nonnull;

/**
 * Represents a cell in the BRIAN'S-BRAIN cellular automaton, managing its position and {@link BrainState} (Ready, Firing, or Refractory).
 * Extends {@link AbstractCell} to support the automaton's neural firing patterns, as described in <i>Cellular Automata Machines</i> (MIT Press).
 *
 * @see BrainState
 * @see AbstractCell
 */
public class BrainCell extends AbstractCell<BrainState> {

    /**
     * The current state of the cell, representing its neural firing status.
     */
    private BrainState state;

    /**
     * Constructs a new BRIAN'S-BRAIN cell with the specified coordinates and initial state.
     *
     * @param x     the x-coordinate (column) of the cell
     * @param y     the y-coordinate (row) of the cell
     * @param state the initial {@link BrainState}, must not be null
     * @throws NullPointerException if state is null
     */
    public BrainCell(int x, int y, @Nonnull BrainState state) {
        super(x, y);
        this.state = java.util.Objects.requireNonNull(state, "State cannot be null");
    }

    /**
     * Returns the current state of the cell.
     *
     * @return the {@link BrainState} of the cell
     */
    @Override
    public BrainState getState() {
        return state;
    }

    /**
     * Sets the state of the cell to the specified value.
     *
     * @param state the new {@link BrainState}, must not be null
     * @throws NullPointerException if state is null
     */
    @Override
    public void setState(@Nonnull BrainState state) {
        this.state = java.util.Objects.requireNonNull(state, "State cannot be null");
    }

    /**
     * Copies the specified state into this cell, updating its state and echo values.
     *
     * @param state the {@link BrainState} to copy, must not be null
     * @throws NullPointerException if state is null
     */
    @Override
    public void copyState(@Nonnull BrainState state) {
        this.state = java.util.Objects.requireNonNull(state, "State cannot be null");
    }

    /**
     * Sets the cell's state and echo values directly.
     *
     * @param value the new {@link BrainState.BrainStateValue}, must not be null
     * @param echo  the echo {@link BrainState.BrainStateValue}, must not be null
     * @throws NullPointerException if value or echo is null
     */
    public void setState(@Nonnull BrainState.BrainStateValue value, @Nonnull BrainState.BrainStateValue echo) {
        java.util.Objects.requireNonNull(value, "Value cannot be null");
        java.util.Objects.requireNonNull(echo, "Echo cannot be null");
        this.state.set(value, echo);
    }
}