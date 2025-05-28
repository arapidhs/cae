package com.dungeoncode.cae.terminal.render;

import com.dungeoncode.cae.core.impl.BooleanState;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
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
     * Cache for inverted RGB colors.
     */
    private static final Map<TextColor.RGB, TextColor.RGB> invertedColorCache = new HashMap<>();

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
        if (state.getValue()) {
            color = palette.getLiveSumColor(state.getLiveSum());
        } else if (state.isEcho()) {
            color = palette.getInactiveEchoColor(state.getLiveSum());
        } else {
            color = palette.getInactiveNoEchoColor(state.getLiveSum());
        }

        // Apply color inversion if enabled
        if (inverted) {
            color = invertColor(color);
        }

        return CellCharacter.fromColor(color);
    }

    /**
     * Inverts the specified {@link TextColor}, swapping light and dark ANSI colors or retrieving/caching inverted
     * RGB components (255 - value) for custom colors.
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
            // Check cache for inverted color
            if (invertedColorCache.containsKey(rgbColor)) {
                return invertedColorCache.get(rgbColor);
            } else {
                TextColor.RGB invertedRgb = new TextColor.RGB(
                        255 - rgbColor.getRed(),
                        255 - rgbColor.getGreen(),
                        255 - rgbColor.getBlue());
                invertedColorCache.put(rgbColor, invertedRgb);
                return invertedRgb;
            }
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
        QUANTUM(
                "Quantum",
                "A palette inspired by quantum mechanics and particle physics. Active cells represent high-energy states with vibrant colors, echo cells show quantum tunneling effects, and inactive cells reveal the quantum vacuum state.",
                Map.of(
                    0, new TextColor.RGB(255, 255, 255),    // Active, live sum 0 - Pure Energy (White)
                    1, new TextColor.RGB(255, 200, 0),      // Live sum 1 - Quark Up (Gold)
                    2, new TextColor.RGB(0, 200, 255),      // Live sum 2 - Quark Down (Blue)
                    3, new TextColor.RGB(255, 100, 0),      // Live sum 3 - Gluon (Orange)
                    4, new TextColor.RGB(200, 0, 255),      // Live sum 4 - W Boson (Purple)
                    5, new TextColor.RGB(0, 255, 200),      // Live sum 5 - Z Boson (Teal)
                    6, new TextColor.RGB(255, 0, 200),      // Live sum 6 - Higgs (Pink)
                    7, new TextColor.RGB(200, 255, 0),      // Live sum 7 - Photon (Lime)
                    8, new TextColor.RGB(0, 200, 0),        // Live sum 8 - Electron (Green)
                    9, new TextColor.RGB(255, 0, 0)         // Live sum 9 - Proton (Red)
                ),
                Map.of(
                    0, new TextColor.RGB(15, 15, 15),       // Inactive no echo, live sum 0 - Quantum Vacuum
                    1, new TextColor.RGB(25, 20, 0),        // Live sum 1 - Virtual Up Quark
                    2, new TextColor.RGB(0, 20, 25),        // Live sum 2 - Virtual Down Quark
                    3, new TextColor.RGB(25, 10, 0),        // Live sum 3 - Virtual Gluon
                    4, new TextColor.RGB(20, 0, 25),        // Live sum 4 - Virtual W Boson
                    5, new TextColor.RGB(0, 25, 20),        // Live sum 5 - Virtual Z Boson
                    6, new TextColor.RGB(25, 0, 20),        // Live sum 6 - Virtual Higgs
                    7, new TextColor.RGB(20, 25, 0),        // Live sum 7 - Virtual Photon
                    8, new TextColor.RGB(0, 20, 0),         // Live sum 8 - Virtual Electron
                    9, new TextColor.RGB(25, 0, 0)          // Live sum 9 - Virtual Proton
                ),
                Map.of(
                    0, new TextColor.RGB(128, 128, 128),    // Inactive with echo, live sum 0 - Quantum Fluctuation
                    1, new TextColor.RGB(128, 100, 0),      // Live sum 1 - Tunneling Up Quark
                    2, new TextColor.RGB(0, 100, 128),      // Live sum 2 - Tunneling Down Quark
                    3, new TextColor.RGB(128, 50, 0),       // Live sum 3 - Tunneling Gluon
                    4, new TextColor.RGB(100, 0, 128),      // Live sum 4 - Tunneling W Boson
                    5, new TextColor.RGB(0, 128, 100),      // Live sum 5 - Tunneling Z Boson
                    6, new TextColor.RGB(128, 0, 100),      // Live sum 6 - Tunneling Higgs
                    7, new TextColor.RGB(100, 128, 0),      // Live sum 7 - Tunneling Photon
                    8, new TextColor.RGB(0, 100, 0),        // Live sum 8 - Tunneling Electron
                    9, new TextColor.RGB(128, 0, 0)         // Live sum 9 - Tunneling Proton
                ),
                TextColor.ANSI.BLACK          // Default - Absolute Zero
        ),
        AURORA(
                "Aurora",
                "Ethereal palette inspired by the Northern Lights, featuring flowing transitions between cool, mystical colors. Active cells shimmer with aurora colors, echo cells show fading aurora traces, and inactive cells reveal subtle aurora remnants.",
                Map.of(
                    0, new TextColor.RGB(0, 255, 255),    // Active, live sum 0 - Bright Cyan
                    1, new TextColor.RGB(0, 204, 255),    // Live sum 1 - Sky Blue
                    2, new TextColor.RGB(0, 153, 255),    // Live sum 2 - Deep Sky Blue
                    3, new TextColor.RGB(0, 102, 255),    // Live sum 3 - Royal Blue
                    4, new TextColor.RGB(51, 51, 255),    // Live sum 4 - Indigo
                    5, new TextColor.RGB(102, 0, 255),    // Live sum 5 - Purple
                    6, new TextColor.RGB(153, 0, 255),    // Live sum 6 - Violet
                    7, new TextColor.RGB(204, 0, 255),    // Live sum 7 - Magenta
                    8, new TextColor.RGB(255, 0, 204),    // Live sum 8 - Hot Pink
                    9, new TextColor.RGB(255, 0, 153)     // Live sum 9 - Deep Pink
                ),
                Map.of(
                    0, new TextColor.RGB(0, 25, 25),      // Inactive no echo, live sum 0
                    1, new TextColor.RGB(0, 20, 25),      // Live sum 1
                    2, new TextColor.RGB(0, 15, 25),      // Live sum 2
                    3, new TextColor.RGB(0, 10, 25),      // Live sum 3
                    4, new TextColor.RGB(5, 5, 25),       // Live sum 4
                    5, new TextColor.RGB(10, 0, 25),      // Live sum 5
                    6, new TextColor.RGB(15, 0, 25),      // Live sum 6
                    7, new TextColor.RGB(20, 0, 25),      // Live sum 7
                    8, new TextColor.RGB(25, 0, 20),      // Live sum 8
                    9, new TextColor.RGB(25, 0, 15)       // Live sum 9
                ),
                Map.of(
                    0, new TextColor.RGB(0, 128, 128),    // Inactive with echo, live sum 0
                    1, new TextColor.RGB(0, 102, 128),    // Live sum 1
                    2, new TextColor.RGB(0, 76, 128),     // Live sum 2
                    3, new TextColor.RGB(0, 51, 128),     // Live sum 3
                    4, new TextColor.RGB(25, 25, 128),    // Live sum 4
                    5, new TextColor.RGB(51, 0, 128),     // Live sum 5
                    6, new TextColor.RGB(76, 0, 128),     // Live sum 6
                    7, new TextColor.RGB(102, 0, 128),    // Live sum 7
                    8, new TextColor.RGB(128, 0, 102),    // Live sum 8
                    9, new TextColor.RGB(128, 0, 76)      // Live sum 9
                ),
                TextColor.ANSI.BLACK          // Default
        ),
        NEON(
                "Neon",
                "Vibrant cyberpunk-inspired palette with neon colors. Active cells glow with bright neon colors, echo cells have a dimmed neon effect, and inactive cells show subtle neon traces.",
                Map.of(
                    0, new TextColor.RGB(255, 0, 255),    // Active, live sum 0 - Neon Pink
                    1, new TextColor.RGB(0, 255, 255),    // Live sum 1 - Cyan
                    2, new TextColor.RGB(0, 255, 128),    // Live sum 2 - Neon Green
                    3, new TextColor.RGB(255, 255, 0),    // Live sum 3 - Yellow
                    4, new TextColor.RGB(255, 128, 0),    // Live sum 4 - Orange
                    5, new TextColor.RGB(255, 0, 0),      // Live sum 5 - Red
                    6, new TextColor.RGB(255, 0, 128),    // Live sum 6 - Pink
                    7, new TextColor.RGB(128, 0, 255),    // Live sum 7 - Purple
                    8, new TextColor.RGB(0, 128, 255),    // Live sum 8 - Blue
                    9, new TextColor.RGB(255, 255, 255)   // Live sum 9 - White
                ),
                Map.of(
                    0, new TextColor.RGB(25, 0, 25),      // Inactive no echo, live sum 0
                    1, new TextColor.RGB(0, 25, 25),      // Live sum 1
                    2, new TextColor.RGB(0, 25, 12),      // Live sum 2
                    3, new TextColor.RGB(25, 25, 0),      // Live sum 3
                    4, new TextColor.RGB(25, 12, 0),      // Live sum 4
                    5, new TextColor.RGB(25, 0, 0),       // Live sum 5
                    6, new TextColor.RGB(25, 0, 12),      // Live sum 6
                    7, new TextColor.RGB(12, 0, 25),      // Live sum 7
                    8, new TextColor.RGB(0, 12, 25),      // Live sum 8
                    9, new TextColor.RGB(25, 25, 25)      // Live sum 9
                ),
                Map.of(
                    0, new TextColor.RGB(128, 0, 128),    // Inactive with echo, live sum 0
                    1, new TextColor.RGB(0, 128, 128),    // Live sum 1
                    2, new TextColor.RGB(0, 128, 64),     // Live sum 2
                    3, new TextColor.RGB(128, 128, 0),    // Live sum 3
                    4, new TextColor.RGB(128, 64, 0),     // Live sum 4
                    5, new TextColor.RGB(128, 0, 0),      // Live sum 5
                    6, new TextColor.RGB(128, 0, 64),     // Live sum 6
                    7, new TextColor.RGB(64, 0, 128),     // Live sum 7
                    8, new TextColor.RGB(0, 64, 128),     // Live sum 8
                    9, new TextColor.RGB(128, 128, 128)   // Live sum 9
                ),
                TextColor.ANSI.BLACK          // Default
        ),
        DEFAULT(
                "Default Black & White",
                "Black and white ANSI palette for minimalistic visualization.",
                Map.of(
                    0, TextColor.ANSI.WHITE_BRIGHT,  // Active, live sum 0
                    1, TextColor.ANSI.WHITE_BRIGHT,
                    2, TextColor.ANSI.WHITE_BRIGHT,
                    3, TextColor.ANSI.WHITE_BRIGHT,
                    4, TextColor.ANSI.WHITE_BRIGHT,
                    5, TextColor.ANSI.WHITE_BRIGHT,
                    6, TextColor.ANSI.WHITE_BRIGHT,
                    7, TextColor.ANSI.WHITE_BRIGHT,
                    8, TextColor.ANSI.WHITE_BRIGHT,
                    9, TextColor.ANSI.WHITE_BRIGHT
                ),
                Map.of(
                    0, TextColor.ANSI.BLACK,         // Inactive no echo, live sum 0
                    1, TextColor.ANSI.BLACK,
                    2, TextColor.ANSI.BLACK,
                    3, TextColor.ANSI.BLACK,
                    4, TextColor.ANSI.BLACK,
                    5, TextColor.ANSI.BLACK,
                    6, TextColor.ANSI.BLACK,
                    7, TextColor.ANSI.BLACK,
                    8, TextColor.ANSI.BLACK,
                    9, TextColor.ANSI.BLACK
                ),
                Map.of(
                    0, TextColor.ANSI.BLACK,         // Inactive with echo, live sum 0
                    1, TextColor.ANSI.BLACK,
                    2, TextColor.ANSI.BLACK,
                    3, TextColor.ANSI.BLACK,
                    4, TextColor.ANSI.BLACK,
                    5, TextColor.ANSI.BLACK,
                    6, TextColor.ANSI.BLACK,
                    7, TextColor.ANSI.BLACK,
                    8, TextColor.ANSI.BLACK,
                    9, TextColor.ANSI.BLACK
                ),
                TextColor.ANSI.BLACK          // Default
        ),
        GREYSCALE(
                "Greyscale",
                "Greyscale palette for subtle, monochromatic visualization.",
                Map.of(
                    0, TextColor.ANSI.WHITE,         // Active, live sum 0
                    1, TextColor.ANSI.WHITE,
                    2, TextColor.ANSI.WHITE,
                    3, new TextColor.RGB(212, 212, 212),
                    4, new TextColor.RGB(212, 212, 212),
                    5, new TextColor.RGB(212, 212, 212),
                    6, TextColor.ANSI.WHITE_BRIGHT,
                    7, TextColor.ANSI.WHITE_BRIGHT,
                    8, TextColor.ANSI.WHITE_BRIGHT,
                    9, TextColor.ANSI.WHITE_BRIGHT
                ),
                Map.of(
                    0, new TextColor.RGB(16, 16, 16),     // Inactive no echo, live sum 0
                    1, new TextColor.RGB(16, 16, 16),
                    2, new TextColor.RGB(16, 16, 16),
                    3, new TextColor.RGB(32, 32, 32),
                    4, new TextColor.RGB(32, 32, 32),
                    5, new TextColor.RGB(32, 32, 32),
                    6, new TextColor.RGB(48, 48, 48),
                    7, new TextColor.RGB(48, 48, 48),
                    8, new TextColor.RGB(48, 48, 48),
                    9, new TextColor.RGB(64, 64, 64)
                ),
                Map.of(
                    0, TextColor.ANSI.BLACK_BRIGHT,  // Inactive with echo, live sum 0
                    1, TextColor.ANSI.BLACK_BRIGHT,
                    2, TextColor.ANSI.BLACK_BRIGHT,
                    3, new TextColor.RGB(64, 64, 64),
                    4, new TextColor.RGB(64, 64, 64),
                    5, new TextColor.RGB(64, 64, 64),
                    6, new TextColor.RGB(96, 96, 96),
                    7, new TextColor.RGB(96, 96, 96),
                    8, new TextColor.RGB(96, 96, 96),
                    9, new TextColor.RGB(128, 128, 128)
                ),
                TextColor.ANSI.BLACK          // Default
        ),
        ANSI(
                "ANSI",
                "Vibrant ANSI palette with blue, cyan, green, yellow, and red for live sums, green for active cells with no neighbors, blue for recently inactive cells, and white for long-inactive or default cells.",
                Map.of(
                    0, TextColor.ANSI.MAGENTA,         // Active, live sum 0
                    1, TextColor.ANSI.BLUE,
                    2, TextColor.ANSI.CYAN,
                    3, TextColor.ANSI.GREEN,
                    4, TextColor.ANSI.YELLOW,
                    5, TextColor.ANSI.RED_BRIGHT,
                    6, TextColor.ANSI.RED,
                    7, TextColor.ANSI.RED,
                    8, TextColor.ANSI.RED,
                    9, TextColor.ANSI.RED
                ),
                Map.of(
                    0, TextColor.ANSI.BLACK,         // Inactive no echo, live sum 0
                    1, TextColor.ANSI.BLACK,
                    2, TextColor.ANSI.BLACK,
                    3, TextColor.ANSI.BLACK,
                    4, TextColor.ANSI.BLACK,
                    5, TextColor.ANSI.BLACK,
                    6, TextColor.ANSI.BLACK,
                    7, TextColor.ANSI.BLACK,
                    8, TextColor.ANSI.BLACK,
                    9, TextColor.ANSI.BLACK
                ),
                Map.of(
                    0, TextColor.ANSI.BLACK,          // Inactive with echo, live sum 0
                    1, TextColor.ANSI.BLACK,
                    2, TextColor.ANSI.BLACK,
                    3, TextColor.ANSI.BLACK,
                    4, TextColor.ANSI.BLACK,
                    5, TextColor.ANSI.BLACK_BRIGHT,
                    6, TextColor.ANSI.BLACK_BRIGHT,
                    7, TextColor.ANSI.BLACK_BRIGHT,
                    8, TextColor.ANSI.BLACK_BRIGHT,
                    9, TextColor.ANSI.BLACK_BRIGHT
                ),
                TextColor.ANSI.WHITE_BRIGHT   // Default
        ),
        BLUE_GRADIENT(
                "Blue Gradient",
                "Gradient of blue shades from dark to light for live sums, cyan for active cells with no neighbors, darker blue for recently inactive cells, and dark blue for long-inactive cells.",
                Map.of(
                    0, new TextColor.RGB(0, 153, 204),   // Active, live sum 0
                    1, new TextColor.RGB(0, 0, 51),
                    2, new TextColor.RGB(0, 25, 76),
                    3, new TextColor.RGB(0, 51, 102),
                    4, new TextColor.RGB(0, 76, 127),
                    5, new TextColor.RGB(0, 102, 153),
                    6, new TextColor.RGB(0, 127, 178),
                    7, new TextColor.RGB(0, 153, 204),
                    8, new TextColor.RGB(25, 178, 229),
                    9, new TextColor.RGB(51, 204, 255)
                ),
                Map.of(
                    0, new TextColor.RGB(0, 25, 76),     // Inactive no echo, live sum 0
                    1, new TextColor.RGB(0, 0, 25),
                    2, new TextColor.RGB(0, 12, 38),
                    3, new TextColor.RGB(0, 25, 51),
                    4, new TextColor.RGB(0, 38, 64),
                    5, new TextColor.RGB(0, 51, 76),
                    6, new TextColor.RGB(0, 64, 89),
                    7, new TextColor.RGB(0, 76, 102),
                    8, new TextColor.RGB(12, 89, 115),
                    9, new TextColor.RGB(25, 102, 128)
                ),
                Map.of(
                    0, new TextColor.RGB(0, 51, 153),    // Inactive with echo, live sum 0
                    1, new TextColor.RGB(0, 25, 102),
                    2, new TextColor.RGB(0, 38, 115),
                    3, new TextColor.RGB(0, 51, 128),
                    4, new TextColor.RGB(0, 64, 140),
                    5, new TextColor.RGB(0, 77, 153),
                    6, new TextColor.RGB(0, 89, 166),
                    7, new TextColor.RGB(0, 102, 179),
                    8, new TextColor.RGB(25, 115, 192),
                    9, new TextColor.RGB(51, 128, 204)
                ),
                TextColor.ANSI.WHITE          // Default
        ),
        PURPLE_GRADIENT(
                "Purple Gradient",
                "Gradient of purple shades from dark to light for live sums, magenta for active cells with no neighbors, deep purple for recently inactive cells, and dark purple for long-inactive cells.",
                Map.of(
                    0, new TextColor.RGB(204, 0, 204),   // Active, live sum 0
                    1, new TextColor.RGB(51, 0, 102),
                    2, new TextColor.RGB(76, 25, 127),
                    3, new TextColor.RGB(102, 51, 153),
                    4, new TextColor.RGB(127, 76, 178),
                    5, new TextColor.RGB(153, 102, 204),
                    6, new TextColor.RGB(178, 127, 229),
                    7, new TextColor.RGB(204, 153, 255),
                    8, new TextColor.RGB(229, 178, 255),
                    9, new TextColor.RGB(255, 204, 255)
                ),
                Map.of(
                    0, new TextColor.RGB(51, 0, 102),    // Inactive no echo, live sum 0
                    1, new TextColor.RGB(25, 0, 51),
                    2, new TextColor.RGB(38, 12, 64),
                    3, new TextColor.RGB(51, 25, 77),
                    4, new TextColor.RGB(64, 38, 89),
                    5, new TextColor.RGB(77, 51, 102),
                    6, new TextColor.RGB(89, 64, 115),
                    7, new TextColor.RGB(102, 77, 128),
                    8, new TextColor.RGB(115, 89, 140),
                    9, new TextColor.RGB(128, 102, 153)
                ),
                Map.of(
                    0, new TextColor.RGB(102, 0, 153),   // Inactive with echo, live sum 0
                    1, new TextColor.RGB(51, 0, 102),
                    2, new TextColor.RGB(64, 12, 115),
                    3, new TextColor.RGB(77, 25, 128),
                    4, new TextColor.RGB(89, 38, 140),
                    5, new TextColor.RGB(102, 51, 153),
                    6, new TextColor.RGB(115, 64, 166),
                    7, new TextColor.RGB(128, 77, 179),
                    8, new TextColor.RGB(140, 89, 192),
                    9, new TextColor.RGB(153, 102, 204)
                ),
                TextColor.ANSI.WHITE          // Default
        ),
        FIRE_GRADIENT(
                "Fire Gradient",
                "Fiery palette from deep red to yellow for live sums, bright orange for active cells with no neighbors, dark red for recently inactive cells, and charcoal gray for long-inactive cells.",
                Map.of(
                    0, new TextColor.RGB(255, 128, 0),    // Active, live sum 0
                    1, new TextColor.RGB(51, 0, 0),
                    2, new TextColor.RGB(102, 0, 0),
                    3, new TextColor.RGB(153, 0, 0),
                    4, new TextColor.RGB(204, 51, 0),
                    5, new TextColor.RGB(255, 102, 0),
                    6, new TextColor.RGB(255, 153, 0),
                    7, new TextColor.RGB(255, 178, 51),
                    8, new TextColor.RGB(255, 204, 102),
                    9, new TextColor.RGB(255, 255, 153)
                ),
                Map.of(
                    0, new TextColor.RGB(64, 64, 64),     // Inactive no echo, live sum 0
                    1, new TextColor.RGB(25, 0, 0),
                    2, new TextColor.RGB(51, 0, 0),
                    3, new TextColor.RGB(77, 0, 0),
                    4, new TextColor.RGB(102, 25, 0),
                    5, new TextColor.RGB(128, 51, 0),
                    6, new TextColor.RGB(153, 77, 0),
                    7, new TextColor.RGB(178, 102, 25),
                    8, new TextColor.RGB(204, 128, 51),
                    9, new TextColor.RGB(229, 153, 77)
                ),
                Map.of(
                    0, new TextColor.RGB(128, 0, 0),      // Inactive with echo, live sum 0
                    1, new TextColor.RGB(51, 0, 0),
                    2, new TextColor.RGB(77, 0, 0),
                    3, new TextColor.RGB(102, 0, 0),
                    4, new TextColor.RGB(128, 25, 0),
                    5, new TextColor.RGB(153, 51, 0),
                    6, new TextColor.RGB(179, 77, 0),
                    7, new TextColor.RGB(204, 102, 25),
                    8, new TextColor.RGB(229, 128, 51),
                    9, new TextColor.RGB(255, 153, 77)
                ),
                TextColor.ANSI.WHITE           // Default
        ),
        EARTH_TONES(
                "Earth Tones",
                "Earthy palette from deep brown to pale sand for live sums, forest green for active cells with no neighbors, terracotta for recently inactive cells, and dark soil for long-inactive cells.",
                Map.of(
                    0, new TextColor.RGB(34, 102, 51),    // Active, live sum 0
                    1, new TextColor.RGB(59, 31, 24),
                    2, new TextColor.RGB(87, 46, 28),
                    3, new TextColor.RGB(120, 60, 30),
                    4, new TextColor.RGB(150, 82, 45),
                    5, new TextColor.RGB(179, 106, 51),
                    6, new TextColor.RGB(204, 133, 63),
                    7, new TextColor.RGB(224, 163, 97),
                    8, new TextColor.RGB(237, 195, 137),
                    9, new TextColor.RGB(245, 222, 179)
                ),
                Map.of(
                    0, new TextColor.RGB(35, 18, 11),     // Inactive no echo, live sum 0
                    1, new TextColor.RGB(29, 15, 12),
                    2, new TextColor.RGB(43, 23, 14),
                    3, new TextColor.RGB(60, 30, 15),
                    4, new TextColor.RGB(75, 41, 22),
                    5, new TextColor.RGB(89, 53, 25),
                    6, new TextColor.RGB(102, 66, 31),
                    7, new TextColor.RGB(112, 81, 48),
                    8, new TextColor.RGB(118, 97, 68),
                    9, new TextColor.RGB(122, 111, 89)
                ),
                Map.of(
                    0, new TextColor.RGB(150, 82, 45),    // Inactive with echo, live sum 0
                    1, new TextColor.RGB(89, 46, 36),
                    2, new TextColor.RGB(115, 69, 42),
                    3, new TextColor.RGB(140, 90, 45),
                    4, new TextColor.RGB(165, 112, 67),
                    5, new TextColor.RGB(191, 134, 77),
                    6, new TextColor.RGB(204, 156, 95),
                    7, new TextColor.RGB(214, 179, 125),
                    8, new TextColor.RGB(224, 202, 155),
                    9, new TextColor.RGB(234, 225, 185)
                ),
                TextColor.ANSI.WHITE           // Default
        );

        /**
         * Map of live sum values to their corresponding colors for active cells.
         */
        private final Map<Integer, TextColor> liveSumColors;
        /**
         * Map of live sum values to their corresponding colors for inactive cells with no echo.
         */
        private final Map<Integer, TextColor> inactiveNoEchoColors;
        /**
         * Map of live sum values to their corresponding colors for inactive cells with echo.
         */
        private final Map<Integer, TextColor> inactiveEchoColors;
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
         * Constructs a palette with the specified name, description, and colors for live sums (0–9), active/inactive states, and default cases.
         *
         * @param name                the user-friendly name
         * @param description         the description of the palette's style
         * @param liveSumColors       map of live sum values to their corresponding colors for active cells
         * @param inactiveNoEchoColors map of live sum values to their corresponding colors for inactive cells with no echo
         * @param inactiveEchoColors  map of live sum values to their corresponding colors for inactive cells with echo
         * @param defaultColor        default color for unexpected cases
         */
        Palette(String name, String description, Map<Integer, TextColor> liveSumColors,
                Map<Integer, TextColor> inactiveNoEchoColors, Map<Integer, TextColor> inactiveEchoColors,
                TextColor defaultColor) {
            this.name = name;
            this.description = description;
            this.liveSumColors = liveSumColors;
            this.inactiveNoEchoColors = inactiveNoEchoColors;
            this.inactiveEchoColors = inactiveEchoColors;
            this.defaultColor = defaultColor;
        }

        /**
         * Returns the color for a given live sum value.
         *
         * @param liveSum the live sum value (0-9)
         * @return the corresponding color, or defaultColor if not found
         */
        public TextColor getLiveSumColor(int liveSum) {
            return liveSumColors.getOrDefault(liveSum, defaultColor);
        }

        /**
         * Returns the color for a given live sum value for inactive cells with no echo.
         *
         * @param liveSum the live sum value (0-9)
         * @return the corresponding color, or defaultColor if not found
         */
        public TextColor getInactiveNoEchoColor(int liveSum) {
            return inactiveNoEchoColors.getOrDefault(liveSum, defaultColor);
        }

        /**
         * Returns the color for a given live sum value for inactive cells with echo.
         *
         * @param liveSum the live sum value (0-9)
         * @return the corresponding color, or defaultColor if not found
         */
        public TextColor getInactiveEchoColor(int liveSum) {
            return inactiveEchoColors.getOrDefault(liveSum, defaultColor);
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