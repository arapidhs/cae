package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.PulseWeaverState;
import com.dungeoncode.ca.view.StateRenderer;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link PulseWeaverState} for the Pulse Weaver cellular automaton using Lanterna text characters,
 * applying colors based on the cell's state (Dormant, Charged, or Fading) and persistence flag. Visualizes
 * pulsating patterns with distinct colors for active, fading, and inactive states, using a unique color for
 * persistent Fading cells to highlight resonance.
 *
 * @see StateRenderer
 * @see PulseWeaverState
 */
public class PulseWeaverStateRenderer implements StateRenderer<PulseWeaverState> {

    /**
     * Renders the specified {@link PulseWeaverState} as a Lanterna {@link TextCharacter}. Assigns colors based
     * on the state and persistence flag:
     * <ul>
     *   <li>Dormant: Inactive state (black).</li>
     *   <li>Charged: Active, pulsing state (cyan).</li>
     *   <li>Fading, non-persistent: Decaying energy (blue).</li>
     *   <li>Fading, persistent: Decaying energy with resonance (magenta).</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol for all states.
     *
     * @param state the {@link PulseWeaverState} to render
     * @return the rendered {@link TextCharacter}
     */
    @Override
    public TextCharacter render(PulseWeaverState state) {
        TextColor color = switch (state.getValue()) {
            case DORMANT -> TextColor.ANSI.BLACK;
            case CHARGED -> TextColor.ANSI.CYAN;
            case FADING -> state.isPersistent() ? TextColor.ANSI.MAGENTA : TextColor.ANSI.BLUE;
        };
        return TextCharacter.fromString(" ", color, null, SGR.REVERSE)[0];
    }
}