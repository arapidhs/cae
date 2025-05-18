package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.BrainState;
import com.dungeoncode.ca.view.StateRenderer;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link BrainState} for the BRIAN'S-BRAIN cellular automaton using Lanterna text characters, applying
 * colors based on the cell's state (Ready, Firing, or Refractory). Visualizes neural firing patterns with distinct
 * colors for quiescent, active, and recovering states.
 *
 * @see StateRenderer
 * @see BrainState
 */
public class BrainStateRenderer implements StateRenderer<BrainState> {

    /**
     * Renders the specified {@link BrainState} as a Lanterna {@link TextCharacter}. Assigns colors based on the
     * state:
     * <ul>
     *   <li>Ready: Quiescent state (black).</li>
     *   <li>Firing: Active, firing state (cyan).</li>
     *   <li>Refractory: Recovering state (blue).</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol for all states.
     *
     * @param state the {@link BrainState} to render
     * @return the rendered {@link TextCharacter}
     */
    @Override
    public TextCharacter render(BrainState state) {
        TextColor color = switch (state.getValue()) {
            case READY -> TextColor.ANSI.BLACK; // Dark color for quiescent neurons
            case FIRING -> TextColor.ANSI.CYAN; // Bright color for active, firing neurons
            case REFRACTORY -> TextColor.ANSI.BLUE; // Cool color for recovering neurons
        };
        // Use reverse style with a space for consistent grid appearance
        return TextCharacter.fromString(" ", color, null, SGR.REVERSE)[0];
    }
}