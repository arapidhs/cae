package com.dungeoncode.cae.core.impl;

import com.dungeoncode.cae.core.AbstractCell;

/**
 * A cell in a cellular automaton with a boolean state, represented by {@link BooleanState}.
 * Extends {@link AbstractCell} to provide position handling and implements state management.
 */
public class BooleanCell extends AbstractCell<BooleanState> {

    /**
     * The boolean state of the cell.
     */
    private BooleanState state;

    /**
     * Constructs a new cell at the specified position with the given state, echo, and id.
     *
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell
     * @param state the boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo  {@code true} to enable echo effect, {@code false} otherwise
     * @param id    the identifier for the state (0 = none, >0 = specific category)
     */
    public BooleanCell(int x, int y, boolean state, boolean echo, int id) {
        super(x, y);
        this.state = new BooleanState(state, echo, 0,  id);
    }

    /**
     * Constructs a new cell at the specified position with the given state.
     *
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell
     * @param state the {@link BooleanState} to set
     */
    public BooleanCell(int x, int y, BooleanState state) {
        super(x, y);
        this.state = state;
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

    /**
     * Copies the state from the source, including value, echo, live sum, timer, and id.
     *
     * @param source the source {@link BooleanState} to copy from
     */
    @Override
    public void copyState(BooleanState source) {
        this.state.set(source.getValue(), source.isEcho(), source.getLiveSum(), source.getId());
    }

    /**
     * Sets the state, echo, and id, with zero live sum and timer.
     *
     * @param value the new boolean value ({@code true} for active, {@code false} for inactive)
     * @param echo  the new echo value ({@code true} to enable echo, {@code false} otherwise)
     * @param id    the new identifier (0 = none, >0 = specific category)
     */
    public void setState(boolean value, boolean echo, int id) {
        this.state.set(value, echo, 0, id);
    }
}