package com.dungeoncode.cae.view.render;

import com.dungeoncode.cae.core.CellState;
import com.googlecode.lanterna.TextCharacter;

/**
 * Defines a strategy for rendering a cell state as a {@link TextCharacter} for display in a Lanterna terminal.
 * Implementations convert a state of type {@code S} into a visual representation suitable for terminal output.
 *
 * @param <S> the type of cell state, extending {@link CellState}
 */
public interface StateRenderer<S extends CellState<?>> {

    /**
     * Renders the specified cell state as a {@link TextCharacter}.
     *
     * @param state the cell state to render, of type {@code S}
     * @return the {@link TextCharacter} representing the state
     */
    TextCharacter render(S state);
}