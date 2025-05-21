package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.BooleanState;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link BooleanState} for a cellular automaton using Lanterna text characters, applying simple ANSI
 * colors based on the cell's boolean value and identifier (id). Visualizes active cells with distinct colors for
 * different id values, inactive cells with a default color, and uses echo to differentiate recent state transitions
 * for inactive cells.
 *
 * @see StateRenderer
 * @see BooleanState
 */
public class RendererBooleanId implements StateRenderer<BooleanState> {

    /**
     * Constructs a renderer with simple ANSI color mappings.
     */
    public RendererBooleanId() {
    }

    /**
     * Renders the specified {@link BooleanState} as a Lanterna {@link TextCharacter}. Assigns a color based on
     * the boolean value (active or inactive), id (0 for none, >0 for specific categories), and echo flag (indicating
     * recent state transitions):
     * <ul>
     *   <li>Active, id=1: Red</li>
     *   <li>Active, id=2: Green</li>
     *   <li>Active, id=3: Blue</li>
     *   <li>Active, id=4: Yellow</li>
     *   <li>Active, other id: Cyan</li>
     *   <li>Inactive with echo: Magenta</li>
     *   <li>Inactive without echo: Black</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol.
     *
     * @param state the {@link BooleanState} to render
     * @return the rendered {@link TextCharacter}
     */
    @Override
    public TextCharacter render(BooleanState state) {
        TextColor color;
        if (state.getValue()) {
            color = switch (state.getId()) {
                case 1 -> TextColor.ANSI.RED;
                case 2 -> TextColor.ANSI.GREEN;
                case 3 -> TextColor.ANSI.BLUE;
                case 4 -> TextColor.ANSI.YELLOW;
                default -> TextColor.ANSI.BLACK_BRIGHT; // For id=0 or other ids
            };
        } else if (state.isEcho()) {
            color = TextColor.ANSI.MAGENTA;
        } else {
            color = TextColor.ANSI.BLACK;
        }

        return CellCharacter.fromColor(color);
    }
}