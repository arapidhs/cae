package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.AbstractCell;
import com.dungeoncode.ca.core.CellState;

/**
 * A cell in the BRIAN'S-BRAIN cellular automaton, holding a {@link BrainState} that represents its state (Ready,
 * Firing, or Refractory). Extends {@link AbstractCell} to manage position and state, supporting the automaton's
 * neural firing patterns.
 *
 * @see BrainState
 * @see AbstractCell
 */
public class BrainCell extends AbstractCell<BrainState> {

    /**
     * The current state of the cell.
     */
    private BrainState state;

    /**
     * Constructs a new BRIAN'S-BRAIN cell with the specified coordinates and initial state.
     *
     * @param x     the x-coordinate (column) of the cell
     * @param y     the y-coordinate (row) of the cell
     * @param state the initial {@link BrainState} of the cell
     */
    public BrainCell(int x, int y, BrainState state) {
        super(x, y);
        this.state = state;
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
     * Sets the state of the cell.
     *
     * @param state the new {@link BrainState} to set
     */
    @Override
    public void setState(BrainState state) {
        this.state = state;
    }
}