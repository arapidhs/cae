package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.AbstractCell;
import com.dungeoncode.ca.core.CellState;

/**
 * A cell in the Pulse Weaver cellular automaton, holding a {@link PulseWeaverState} that represents its
 * state (Dormant, Charged, or Fading). Extends {@link AbstractCell} to manage position and state, supporting
 * the automaton's dynamic pulse-based patterns.
 *
 * @see PulseWeaverState
 * @see AbstractCell
 */
public class PulseWeaverCell extends AbstractCell<PulseWeaverState> {

    /**
     * The current state of the cell.
     */
    private PulseWeaverState state;

    /**
     * Constructs a new Pulse Weaver cell with the specified coordinates and an initial Dormant state.
     *
     * @param x the x-coordinate (column) of the cell
     * @param y the y-coordinate (row) of the cell
     */
    public PulseWeaverCell(int x, int y) {
        super(x, y);
        this.state = new PulseWeaverState(PulseWeaverState.PulseState.DORMANT);
    }

    /**
     * Constructs a new Pulse Weaver cell with the specified coordinates and initial state.
     *
     * @param x     the x-coordinate (column) of the cell
     * @param y     the y-coordinate (row) of the cell
     * @param state the initial {@link PulseWeaverState} of the cell
     */
    public PulseWeaverCell(int x, int y, PulseWeaverState state) {
        super(x, y);
        this.state = state;
    }

    /**
     * Returns the current state of the cell.
     *
     * @return the {@link PulseWeaverState} of the cell
     */
    @Override
    public PulseWeaverState getState() {
        return state;
    }

    @Override
    public void setState(PulseWeaverState state) {
        this.state=state;
    }

}