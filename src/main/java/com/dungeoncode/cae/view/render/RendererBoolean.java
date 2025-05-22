package com.dungeoncode.cae.view.render;

import com.dungeoncode.cae.core.impl.BooleanState;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Renders a {@link BooleanState} for a cellular automaton as a Lanterna {@link TextCharacter}, using colors from a
 * selected {@link Palette} based on the cell's state, echo flag, and live sum (number of live neighbors).
 * Visualizes neighborhood activity and state transitions with gradients for active cells with live neighbors,
 * distinct colors for active cells without neighbors, and echo-based feedback for inactive cells.
 *
 * @see StateRenderer
 * @see BooleanState
 */
public class RendererBoolean implements StateRenderer<BooleanState> {

    /**
     * The selected color palette for rendering.
     */
    private Palette palette;

    /**
     * Whether to invert colors during rendering.
     */
    private boolean inverted = false;

    /**
     * Constructs a renderer with the default ANSI color palette.
     */
    public RendererBoolean() {
        this(Palette.DEFAULT);
    }

    /**
     * Constructs a renderer with the specified color palette.
     *
     * @param palette the {@link Palette} to use for rendering, must not be null
     * @throws NullPointerException if palette is null
     */
    public RendererBoolean(@Nonnull Palette palette) {
        this.palette = Objects.requireNonNull(palette, "Palette cannot be null");
    }

    /**
     * Toggles the color inversion state.
     */
    public void toggleInversion() {
        inverted = !inverted;
    }

    /**
     * Sets the color inversion state.
     *
     * @param inverted true to invert colors, false for normal colors
     */
    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }

    /**
     * Renders the specified {@link BooleanState} as a Lanterna {@link TextCharacter} with a color from the selected
     * palette based on:
     * <ul>
     *   <li>Live sum 1–9: Gradient colors for active cells with neighbors (e.g., blue to red).</li>
     *   <li>Live sum 0, active: Distinct color for active cells with no neighbors (e.g., green).</li>
     *   <li>Live sum 0, inactive with echo: Color for recently inactive cells (e.g., blue).</li>
     *   <li>Live sum 0, inactive without echo: Color for long-inactive cells (e.g., white).</li>
     *   <li>Other cases: Default color (e.g., white).</li>
     * </ul>
     * Returns a reverse-style text character with a space symbol. Colors may be inverted if enabled.
     *
     * @param state the {@link BooleanState} to render, must not be null
     * @return the rendered {@link TextCharacter}
     * @throws NullPointerException if state is null
     */
    @Override
    public TextCharacter render(@Nonnull BooleanState state) {
        Objects.requireNonNull(state, "State cannot be null");
        TextColor color;
        if (state.getValue() && state.getLiveSum() > 0) {
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
        } else if (state.getValue()) {
            color = palette.activeNoLiveSum;
        } else if (state.isEcho()) {
            color = palette.inactiveEcho;
        } else {
            color = palette.inactiveNoEcho;
        }

        // Apply color inversion if enabled
        if (inverted) {
            color = invertColor(color);
        }

        return CellCharacter.fromColor(color);
    }

    /**
     * Inverts the specified {@link TextColor}, swapping light and dark ANSI colors or inverting RGB components
     * (255 - value) for custom colors.
     *
     * @param color the {@link TextColor} to invert, must not be null
     * @return the inverted {@link TextColor}
     * @throws NullPointerException if color is null
     */
    private TextColor invertColor(@Nonnull TextColor color) {
        Objects.requireNonNull(color, "Color cannot be null");
        if (color instanceof TextColor.ANSI ansiColor) {
            return switch (ansiColor) {
                case BLACK -> TextColor.ANSI.WHITE_BRIGHT;
                case WHITE -> TextColor.ANSI.BLACK_BRIGHT;
                case BLACK_BRIGHT -> TextColor.ANSI.WHITE;
                case WHITE_BRIGHT -> TextColor.ANSI.BLACK;
                case RED -> TextColor.ANSI.CYAN_BRIGHT;
                case CYAN -> TextColor.ANSI.RED_BRIGHT;
                case GREEN -> TextColor.ANSI.MAGENTA_BRIGHT;
                case MAGENTA -> TextColor.ANSI.GREEN_BRIGHT;
                case BLUE -> TextColor.ANSI.YELLOW_BRIGHT;
                case YELLOW -> TextColor.ANSI.BLUE_BRIGHT;
                case RED_BRIGHT -> TextColor.ANSI.CYAN;
                case CYAN_BRIGHT -> TextColor.ANSI.RED;
                case GREEN_BRIGHT -> TextColor.ANSI.MAGENTA;
                case MAGENTA_BRIGHT -> TextColor.ANSI.GREEN;
                case BLUE_BRIGHT -> TextColor.ANSI.YELLOW;
                case YELLOW_BRIGHT -> TextColor.ANSI.BLUE;
                case DEFAULT -> TextColor.ANSI.DEFAULT;
            };
        }
        if (color instanceof TextColor.RGB rgbColor) {
            return new TextColor.RGB(
                    255 - rgbColor.getRed(),
                    255 - rgbColor.getGreen(),
                    255 - rgbColor.getBlue()
            );
        }
        return color; // Fallback for unsupported color types
    }

    /**
     * Selects the previous color palette in the {@link Palette} enum cycle.
     */
    public void previousPalette() {
        Palette[] palettes = Palette.values();
        int currentIndex = palette.ordinal();
        int prevIndex = (currentIndex - 1 + palettes.length) % palettes.length;
        palette = palettes[prevIndex];
    }

    /**
     * Selects the next color palette in the {@link Palette} enum cycle.
     */
    public void nextPalette() {
        Palette[] palettes = Palette.values();
        int currentIndex = palette.ordinal();
        int nextIndex = (currentIndex + 1) % palettes.length;
        palette = palettes[nextIndex];
    }

    /**
     * Defines color palettes for rendering, mapping live sums (1–9), active cells with no neighbors,
     * inactive cells with/without echo, and default cases to specific {@link TextColor} values.
     */
    public enum Palette {
        DEFAULT(
                "Default Black & White",
                "Black and white ANSI palette for minimalistic visualization.",
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 1
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 2
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 3
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 4
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 5
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 6
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 7
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 8
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 9
                TextColor.ANSI.WHITE_BRIGHT,  // Active, live sum 0
                TextColor.ANSI.BLACK,         // Inactive with echo, live sum 0
                TextColor.ANSI.BLACK,         // Inactive without echo, live sum 0
                TextColor.ANSI.BLACK          // Default
        ),
        GREYSCALE(
                "Greyscale",
                "Greyscale palette for subtle, monochromatic visualization.",
                TextColor.ANSI.WHITE,         // Live sum 1
                TextColor.ANSI.WHITE,         // Live sum 2
                new TextColor.RGB(212, 212, 212),  // Live sum 3
                new TextColor.RGB(212, 212, 212),  // Live sum 4
                new TextColor.RGB(212, 212, 212),  // Live sum 5
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 6
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 7
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 8
                TextColor.ANSI.WHITE_BRIGHT,  // Live sum 9
                TextColor.ANSI.WHITE,         // Active, live sum 0
                TextColor.ANSI.BLACK_BRIGHT,  // Inactive with echo, live sum 0
                new TextColor.RGB(16, 16, 16),     // Inactive without echo, live sum 0
                TextColor.ANSI.BLACK          // Default
        ),
        ANSI(
                "ANSI",
                "Vibrant ANSI palette with blue, cyan, green, yellow, and red for live sums, green for active cells with no neighbors, blue for recently inactive cells, and white for long-inactive or default cells.",
                TextColor.ANSI.BLUE,          // Live sum 1
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
                "Gradient of blue shades from dark to light for live sums, cyan for active cells with no neighbors, darker blue for recently inactive cells, and dark blue for long-inactive cells.",
                new TextColor.RGB(0, 0, 51),      // Live sum 1
                new TextColor.RGB(0, 25, 76),     // Live sum 2
                new TextColor.RGB(0, 51, 102),    // Live sum 3
                new TextColor.RGB(0, 76, 127),    // Live sum 4
                new TextColor.RGB(0, 102, 153),   // Live sum 5
                new TextColor.RGB(0, 127, 178),   // Live sum 6
                new TextColor.RGB(0, 153, 204),   // Live sum 7
                new TextColor.RGB(25, 178, 229),  // Live sum 8
                new TextColor.RGB(51, 204, 255),  // Live sum 9
                new TextColor.RGB(0, 153, 204),   // Active, live sum 0
                new TextColor.RGB(0, 51, 153),    // Inactive with echo, live sum 0
                new TextColor.RGB(0, 25, 76),     // Inactive without echo, live sum 0
                TextColor.ANSI.WHITE          // Default
        ),
        PURPLE_GRADIENT(
                "Purple Gradient",
                "Gradient of purple shades from dark to light for live sums, magenta for active cells with no neighbors, deep purple for recently inactive cells, and dark purple for long-inactive cells.",
                new TextColor.RGB(51, 0, 102),    // Live sum 1
                new TextColor.RGB(76, 25, 127),   // Live sum 2
                new TextColor.RGB(102, 51, 153),  // Live sum 3
                new TextColor.RGB(127, 76, 178),  // Live sum 4
                new TextColor.RGB(153, 102, 204), // Live sum 5
                new TextColor.RGB(178, 127, 229), // Live sum 6
                new TextColor.RGB(204, 153, 255), // Live sum 7
                new TextColor.RGB(229, 178, 255), // Live sum 8
                new TextColor.RGB(255, 204, 255), // Live sum 9
                new TextColor.RGB(204, 0, 204),   // Active, live sum 0
                new TextColor.RGB(102, 0, 153),   // Inactive with echo, live sum 0
                new TextColor.RGB(51, 0, 102),    // Inactive without echo, live sum 0
                TextColor.ANSI.WHITE          // Default
        ),
        FIRE_GRADIENT(
                "Fire Gradient",
                "Fiery palette from deep red to yellow for live sums, bright orange for active cells with no neighbors, dark red for recently inactive cells, and charcoal gray for long-inactive cells.",
                new TextColor.RGB(51, 0, 0),       // Live sum 1
                new TextColor.RGB(102, 0, 0),      // Live sum 2
                new TextColor.RGB(153, 0, 0),      // Live sum 3
                new TextColor.RGB(204, 51, 0),     // Live sum 4
                new TextColor.RGB(255, 102, 0),    // Live sum 5
                new TextColor.RGB(255, 153, 0),    // Live sum 6
                new TextColor.RGB(255, 178, 51),   // Live sum 7
                new TextColor.RGB(255, 204, 102),  // Live sum 8
                new TextColor.RGB(255, 255, 153),  // Live sum 9
                new TextColor.RGB(255, 128, 0),    // Active, live sum 0
                new TextColor.RGB(128, 0, 0),      // Inactive with echo, live sum 0
                new TextColor.RGB(64, 64, 64),     // Inactive without echo, live sum 0
                TextColor.ANSI.WHITE           // Default
        ),
        EARTH_TONES(
                "Earth Tones",
                "Earthy palette from deep brown to pale sand for live sums, forest green for active cells with no neighbors, terracotta for recently inactive cells, and dark soil for long-inactive cells.",
                new TextColor.RGB(59, 31, 24),     // Live sum 1
                new TextColor.RGB(87, 46, 28),     // Live sum 2
                new TextColor.RGB(120, 60, 30),    // Live sum 3
                new TextColor.RGB(150, 82, 45),    // Live sum 4
                new TextColor.RGB(179, 106, 51),   // Live sum 5
                new TextColor.RGB(204, 133, 63),   // Live sum 6
                new TextColor.RGB(224, 163, 97),   // Live sum 7
                new TextColor.RGB(237, 195, 137),  // Live sum 8
                new TextColor.RGB(245, 222, 179),  // Live sum 9
                new TextColor.RGB(34, 102, 51),    // Active, live sum 0
                new TextColor.RGB(150, 82, 45),    // Inactive with echo, live sum 0
                new TextColor.RGB(35, 18, 11),     // Inactive without echo, live sum 0
                TextColor.ANSI.WHITE           // Default
        );

        /**
         * Color for live sum 1.
         */
        final TextColor liveSum1;
        /**
         * Color for live sum 2.
         */
        final TextColor liveSum2;
        /**
         * Color for live sum 3.
         */
        final TextColor liveSum3;
        /**
         * Color for live sum 4.
         */
        final TextColor liveSum4;
        /**
         * Color for live sum 5.
         */
        final TextColor liveSum5;
        /**
         * Color for live sum 6.
         */
        final TextColor liveSum6;
        /**
         * Color for live sum 7.
         */
        final TextColor liveSum7;
        /**
         * Color for live sum 8.
         */
        final TextColor liveSum8;
        /**
         * Color for live sum 9.
         */
        final TextColor liveSum9;
        /**
         * Color for active cells with live sum 0.
         */
        final TextColor activeNoLiveSum;
        /**
         * Color for inactive cells with echo and live sum 0.
         */
        final TextColor inactiveEcho;
        /**
         * Color for inactive cells without echo and live sum 0.
         */
        final TextColor inactiveNoEcho;
        /**
         * Default color for unexpected cases.
         */
        final TextColor defaultColor;
        /**
         * The user-friendly name of the palette.
         */
        private final String name;
        /**
         * A brief description of the palette's visual style.
         */
        private final String description;

        /**
         * Constructs a palette with the specified name, description, and colors for live sums (1–9), active/inactive states, and default cases.
         *
         * @param name            the user-friendly name
         * @param description     the description of the palette's style
         * @param liveSum1        color for live sum 1
         * @param liveSum2        color for live sum 2
         * @param liveSum3        color for live sum 3
         * @param liveSum4        color for live sum 4
         * @param liveSum5        color for live sum 5
         * @param liveSum6        color for live sum 6
         * @param liveSum7        color for live sum 7
         * @param liveSum8        color for live sum 8
         * @param liveSum9        color for live sum 9
         * @param activeNoLiveSum color for active cells with live sum 0
         * @param inactiveEcho    color for inactive cells with echo, live sum 0
         * @param inactiveNoEcho  color for inactive cells without echo, live sum 0
         * @param defaultColor    default color for unexpected cases
         */
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
         * Returns a brief description of the palette's visual style.
         *
         * @return the palette description
         */
        public String getDescription() {
            return description;
        }
    }
}