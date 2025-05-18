package com.dungeoncode.ca.view.render;

import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.view.StateRenderer;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

/**
 * Renders a {@link BooleanState} for a cellular automaton using Lanterna text characters, applying colors
 * from a selected palette based on the cell's state, echo flag, and live sum (number of live cells in the
 * neighborhood). Visualizes neighborhood activity and state transitions with a color gradient for cells
 * with live neighbors, distinct colors for active cells without live neighbors, and echo-based feedback
 * for inactive cells.
 *
 * @see StateRenderer
 * @see BooleanState
 */
public class LiveSumStateRenderer implements StateRenderer<BooleanState> {

    /**
     * Enum defining color palettes for rendering, mapping live sum values (1–9), active cells with no live
     * neighbors, inactive cells with echo, inactive cells without echo, and default cases to specific colors.
     */
    public enum Palette {
        DEFAULT(
                "Default ANSI",
                "A vibrant ANSI color palette with distinct hues (blue, cyan, green, yellow, red) for varying live neighbor counts, green for active cells with no live neighbors, blue for recently died cells, and bright white for stayed-dead cells or defaults. Ideal for standard, high-contrast visualization.",
                TextColor.ANSI.BLUE,           // Live sum 1
                TextColor.ANSI.CYAN,          // Live sum 2
                TextColor.ANSI.GREEN,         // Live sum 3
                TextColor.ANSI.YELLOW_BRIGHT, // Live sum 4
                TextColor.ANSI.RED,           // Live sum 5
                TextColor.ANSI.RED,           // Live sum 6
                TextColor.ANSI.RED,           // Live sum 7
                TextColor.ANSI.RED,           // Live sum 8
                TextColor.ANSI.RED,           // Live sum 9
                TextColor.ANSI.GREEN,         // Active, live sum 0
                TextColor.ANSI.BLUE,          // Inactive with echo, live sum 0
                TextColor.ANSI.WHITE_BRIGHT,  // Inactive without echo, live sum 0
                TextColor.ANSI.WHITE_BRIGHT   // Default
        ),
        BLUE_GRADIENT(
                "Blue Gradient",
                "A smooth gradient of blue shades, from darkest to lightest blue, for increasing live neighbor counts, with cyan for active cells with no live neighbors, darker blue for recently died cells, and dark blue for stayed-dead cells. Perfect for a cohesive, blue-toned visualization emphasizing neighborhood activity.",
                new TextColor.RGB(0, 0, 51),      // Live sum 1: Darkest blue
                new TextColor.RGB(0, 25, 76),     // Live sum 2: Very dark blue
                new TextColor.RGB(0, 51, 102),    // Live sum 3: Dark blue
                new TextColor.RGB(0, 76, 127),    // Live sum 4: Medium-dark blue
                new TextColor.RGB(0, 102, 153),   // Live sum 5: Medium blue
                new TextColor.RGB(0, 127, 178),   // Live sum 6: Medium-bright blue
                new TextColor.RGB(0, 153, 204),   // Live sum 7: Bright blue
                new TextColor.RGB(25, 178, 229),  // Live sum 8: Light blue
                new TextColor.RGB(51, 204, 255),  // Live sum 9: Lightest blue
                new TextColor.RGB(0, 153, 204),   // Active, live sum 0: Cyan
                new TextColor.RGB(0, 51, 153),    // Inactive with echo, live sum 0: Darker blue
                new TextColor.RGB(0, 25, 76),     // Inactive without echo, live sum 0: Very dark blue
                new TextColor.RGB(255, 255, 255)  // Default: White
        ),
        PURPLE_GRADIENT(
                "Purple Gradient",
                "A gradient of purple shades, from dark to light purple, for increasing live neighbor counts, with magenta for active cells with no live neighbors, deep purple for recently died cells, and darkest purple for stayed-dead cells. Ideal for a rich, vibrant visualization of dynamic patterns.",
                new TextColor.RGB(51, 0, 102),    // Live sum 1: Darkest purple
                new TextColor.RGB(76, 25, 127),   // Live sum 2: Dark purple
                new TextColor.RGB(102, 51, 153),  // Live sum 3: Medium-dark purple
                new TextColor.RGB(127, 76, 178),  // Live sum 4: Medium purple
                new TextColor.RGB(153, 102, 204), // Live sum 5: Medium-bright purple
                new TextColor.RGB(178, 127, 229), // Live sum 6: Bright purple
                new TextColor.RGB(204, 153, 255), // Live sum 7: Light purple
                new TextColor.RGB(229, 178, 255), // Live sum 8: Lighter purple
                new TextColor.RGB(255, 204, 255), // Live sum 9: Lightest purple
                new TextColor.RGB(204, 0, 204),   // Active, live sum 0: Magenta
                new TextColor.RGB(102, 0, 153),   // Inactive with echo, live sum 0: Deep purple
                new TextColor.RGB(51, 0, 102),    // Inactive without echo, live sum 0: Darkest purple
                new TextColor.RGB(255, 255, 255)  // Default: White
        ),
        FIRE_GRADIENT(
        "Fire Gradient",
                "A fiery palette ranging from deep ember red to bright yellow for increasing live neighbor counts. Bright orange for active cells with no live neighbors, dark red for echoing inactive cells, and charcoal gray for inactive, stable dead cells. Useful for intense, heat-map-like visualization.",
                new TextColor.RGB(51, 0, 0),       // Live sum 1: Dark ember
                new TextColor.RGB(102, 0, 0),      // Live sum 2: Deep red
                new TextColor.RGB(153, 0, 0),      // Live sum 3: Red
                new TextColor.RGB(204, 51, 0),     // Live sum 4: Reddish orange
                new TextColor.RGB(255, 102, 0),    // Live sum 5: Orange
                new TextColor.RGB(255, 153, 0),    // Live sum 6: Bright orange
                new TextColor.RGB(255, 178, 51),   // Live sum 7: Soft yellow-orange
                new TextColor.RGB(255, 204, 102),  // Live sum 8: Light yellow-orange
                new TextColor.RGB(255, 255, 153),  // Live sum 9: Bright yellow
                new TextColor.RGB(255, 128, 0),    // Active, live sum 0: Bright orange
                new TextColor.RGB(128, 0, 0),      // Inactive with echo, live sum 0: Dark red
                new TextColor.RGB(64, 64, 64),     // Inactive without echo, live sum 0: Charcoal gray
                new TextColor.RGB(255, 255, 255)   // Default: White
                ),
        EARTH_TONES(
                "Earth Tones",
                "A natural palette using warm earth tones, from deep brown through amber to pale sand, for increasing live neighbor counts, with forest green for active cells with no live neighbors, terracotta for recently died cells, and dark soil color for stayed-dead cells. Perfect for a warm, organic visualization with a natural aesthetic.",
                new TextColor.RGB(59, 31, 24),     // Live sum 1: Deep brown
                new TextColor.RGB(87, 46, 28),     // Live sum 2: Rich brown
                new TextColor.RGB(120, 60, 30),    // Live sum 3: Medium brown
                new TextColor.RGB(150, 82, 45),    // Live sum 4: Terracotta
                new TextColor.RGB(179, 106, 51),   // Live sum 5: Bronze
                new TextColor.RGB(204, 133, 63),   // Live sum 6: Amber
                new TextColor.RGB(224, 163, 97),   // Live sum 7: Light amber
                new TextColor.RGB(237, 195, 137),  // Live sum 8: Sand
                new TextColor.RGB(245, 222, 179),  // Live sum 9: Pale sand
                new TextColor.RGB(34, 102, 51),    // Active, live sum 0: Forest green
                new TextColor.RGB(150, 82, 45),    // Inactive with echo, live sum 0: Terracotta
                new TextColor.RGB(35, 18, 11),     // Inactive without echo, live sum 0: Dark soil
                new TextColor.RGB(255, 255, 255)   // Default: White
        );


        /** The user-friendly name of the palette. */
        private final String name;

        /** A short description of the palette's visual style and use case. */
        private final String description;

        /** Color for live sum 1. */
        final TextColor liveSum1;
        /** Color for live sum 2. */
        final TextColor liveSum2;
        /** Color for live sum 3. */
        final TextColor liveSum3;
        /** Color for live sum 4. */
        final TextColor liveSum4;
        /** Color for live sum 5. */
        final TextColor liveSum5;
        /** Color for live sum 6. */
        final TextColor liveSum6;
        /** Color for live sum 7. */
        final TextColor liveSum7;
        /** Color for live sum 8. */
        final TextColor liveSum8;
        /** Color for live sum 9. */
        final TextColor liveSum9;
        /** Color for active cells with live sum 0. */
        final TextColor activeNoLiveSum;
        /** Color for inactive cells with echo and live sum 0. */
        final TextColor inactiveEcho;
        /** Color for inactive cells without echo and live sum 0. */
        final TextColor inactiveNoEcho;
        /** Default color for unexpected cases. */
        final TextColor defaultColor;

        Palette(String name, String description, TextColor liveSum1, TextColor liveSum2, TextColor liveSum3,
                TextColor liveSum4, TextColor liveSum5, TextColor liveSum6, TextColor liveSum7, TextColor liveSum8,
                TextColor liveSum9, TextColor activeNoLiveSum, TextColor inactiveEcho,
                TextColor inactiveNoEcho, TextColor defaultColor) {
            this.name = name;
            this.description = description;
            this.liveSum1 = liveSum1;
            this.liveSum2 = liveSum2;
            this.liveSum3 = liveSum3;
            this.liveSum4 = liveSum4;
            this.liveSum5 = liveSum5;
            this.liveSum6 = liveSum6;
            this.liveSum7 = liveSum7;
            this.liveSum8 = liveSum8;
            this.liveSum9 = liveSum9;
            this.activeNoLiveSum = activeNoLiveSum;
            this.inactiveEcho = inactiveEcho;
            this.inactiveNoEcho = inactiveNoEcho;
            this.defaultColor = defaultColor;
        }

        /**
         * Returns the user-friendly name of the palette.
         *
         * @return the palette name
         */
        public String getName() {
            return name;
        }

        /**
         * Returns a short description of the palette's visual style and use case.
         *
         * @return the palette description
         */
        public String getDescription() {
            return description;
        }
    }

    /** The selected color palette for rendering. */
    private final Palette palette;

    /**
     * Constructs a renderer with the default ANSI color palette.
     */
    public LiveSumStateRenderer() {
        this(Palette.DEFAULT);
    }

    /**
     * Constructs a renderer with the specified color palette.
     *
     * @param palette the {@link Palette} to use for rendering
     */
    public LiveSumStateRenderer(Palette palette) {
        this.palette = palette;
    }

    /**
     * Renders the specified {@link BooleanState} as a Lanterna {@link TextCharacter}. Assigns a color from the
     * selected palette based on the live sum (number of live cells in the neighborhood), boolean value (active
     * or inactive), and echo flag (indicating recent state transitions):
     * <ul>
     *   <li>Live sum 1: Sparse activity (e.g., blue).</li>
     *   <li>Live sum 2: Low activity (e.g., cyan).</li>
     *   <li>Live sum 3: Moderate activity (e.g., green).</li>
     *   <li>Live sum 4: High activity (e.g., bright yellow).</li>
     *   <li>Live sum 5–9: Dense activity (e.g., red).</li>
     *   <li>Live sum outside 1–9: Default (e.g., bright white).</li>
     *   <li>Live sum 0, active: Active with no live neighbors (e.g., green).</li>
     *   <li>Live sum 0, inactive with echo: Recently died (e.g., blue).</li>
     *   <li>Live sum 0, inactive without echo: Stayed dead (e.g., bright white).</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol.
     *
     * @param state the {@link BooleanState} to render
     * @return the rendered {@link TextCharacter}
     */
    @Override
    public TextCharacter render(BooleanState state) {
        TextColor color;
        if(state.getLiveSum()>0) {
            color = switch (state.getLiveSum()) {
                case 1 -> palette.liveSum1;
                case 2 -> palette.liveSum2;
                case 3 -> palette.liveSum3;
                case 4 -> palette.liveSum4;
                case 5 -> palette.liveSum5;
                case 6 -> palette.liveSum6;
                case 7 -> palette.liveSum7;
                case 8 -> palette.liveSum8;
                case 9 -> palette.liveSum9;
                default -> palette.defaultColor;
            };
        } else if (state.getValue()){
            color=palette.activeNoLiveSum;
        } else if (state.isEcho()){
            color=palette.inactiveEcho;
        } else {
            color=palette.inactiveNoEcho;
        }
        return TextCharacter.fromString(" ", color, null, SGR.REVERSE)[0];
    }
}