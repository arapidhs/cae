package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.view.StateRenderer;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link BooleanState} for a cellular automaton using Lanterna text characters, applying distinct
 * colors based on the cell's current state and echo flag. Visualizes state transitions to highlight dynamic
 * patterns, such as cells that stayed alive, were just born, just died, or stayed dead, using a reverse-style
 * text character.
 *
 * @see StateRenderer
 * @see BooleanState
 */
public class BooleanEchoStateRenderer implements StateRenderer<BooleanState> {

    /**
     * Renders the specified {@link BooleanState} as a Lanterna {@link TextCharacter}. Assigns a color based on
     * the combination of the cell's boolean value (active or inactive) and echo flag (indicating recent state
     * transitions):
     * <ul>
     *   <li>Active with echo: Cell stayed alive (red).</li>
     *   <li>Active without echo: Cell was just born (green).</li>
     *   <li>Inactive with echo: Cell just died (blue).</li>
     *   <li>Inactive without echo: Cell stayed dead (black).</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol.
     *
     * @param state the {@link BooleanState} to render
     * @return the rendered {@link TextCharacter}
     */
    @Override
    public TextCharacter render(BooleanState state) {
        TextColor color;
        if (state.getValue() && state.isEcho()) {   // Cell stayed alive
            color = TextColor.ANSI.RED;
        } else if (state.getValue() && !state.isEcho()) {   // Cell was just born
            color = TextColor.ANSI.GREEN;
        } else if (!state.getValue() && state.isEcho()) {   // Cell just died
            color = TextColor.ANSI.BLUE;
        } else {    // Cell stayed dead
            color = TextColor.ANSI.BLACK;
        }
        return TextCharacter.fromString(" ", color, null, SGR.REVERSE)[0];
    }
}