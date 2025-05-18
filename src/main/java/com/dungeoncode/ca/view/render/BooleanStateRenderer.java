package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.view.StateRenderer;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link BooleanState} as a {@link TextCharacter} for display in a Lanterna terminal.
 * A {@code true} state (black) is rendered as a space with reverse styling, and a {@code false} state
 * (white) is rendered as a plain space.
 */
public class BooleanStateRenderer implements StateRenderer<BooleanState> {

    /**
     * Renders the specified boolean state as a {@link TextCharacter}.
     * A {@code true} state is represented as a space with reverse styling (black background),
     * while a {@code false} state is a plain space.
     *
     * @param state the {@link BooleanState} to render
     * @return the {@link TextCharacter} representing the state
     */
    @Override
    public TextCharacter render(BooleanState state) {
        TextCharacter[] textCharacter;
        if (state.getValue()) {
            textCharacter = TextCharacter.fromString(" ", TextColor.ANSI.WHITE_BRIGHT, null, SGR.REVERSE);
        } else {
            textCharacter = TextCharacter.fromString(" ");
        }
        return textCharacter[0];
    }
}