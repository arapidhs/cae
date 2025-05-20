package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.AbstractCell;

/**
 * A cell in a cellular automaton with a boolean state, represented by {@link BooleanState}.
 * Extends {@link AbstractCell} to provide position handling and implements state management.
 */
public class BooleanCell extends AbstractCell<BooleanState> {

    /**
     * The boolean state of the cell.
     */
    private BooleanState state;

    public BooleanCell(int x, int y, boolean state, boolean echo) {
        super(x, y);
        this.state = new BooleanState(state, echo);
    }

    /**
     * Returns the current state of this cell.
     *
     * @return the {@link BooleanState} of the cell
     */
    @Override
    public BooleanState getState() {
        return state;
    }

    /**
     * Sets the state of this cell.
     *
     * @param state the new {@link BooleanState} to set
     */
    @Override
    public void setState(BooleanState state) {
        this.state = state;
    }

    @Override
    public void copyState(BooleanState source) {
        this.state.set(source.getValue(),source.isEcho(), source.getLiveSum());
    }

}