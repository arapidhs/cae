package com.dungeoncode.ca.view;

import com.dungeoncode.ca.automa.*;
import com.dungeoncode.ca.core.*;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.view.render.BooleanEchoStateRenderer;
import com.dungeoncode.ca.view.render.BooleanStateRenderer;
import com.dungeoncode.ca.view.render.GridRenderer;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Manages the visualization and user interaction for a cellular automaton, controlling the Lanterna terminal
 * and automaton execution. Supports grid initialization, configuration application, input handling (e.g., pause,
 * resume, resize), and terminal management.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class ControlView<C extends Cell<S>, S extends CellState<?>> {

    /** Logger for recording view events and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlView.class);

    /** Maps configuration class names to their corresponding state renderers. */
    @SuppressWarnings("rawtypes")
    private static final Map<String, StateRenderer> CELL_RENDERER;

    /** Maps configuration class names to a boolean indicating if they use boolean states. */
    private static final Map<String, Boolean> IS_CONFIGURATION_BOOLEAN;

    static {
        BooleanStateRenderer booleanRenderer = new BooleanStateRenderer();
        BooleanEchoStateRenderer booleanEchoRenderer = new BooleanEchoStateRenderer();

        CELL_RENDERER = new HashMap<>();
        CELL_RENDERER.put(InkspotConfiguration.class.getName(), booleanRenderer);
        CELL_RENDERER.put(GameOfLifeConfiguration.class.getName(), booleanRenderer);
        CELL_RENDERER.put(GameOfLifeWithEchoConfiguration.class.getName(), booleanEchoRenderer);
        CELL_RENDERER.put(GameOfLifeWithTracingConfiguration.class.getName(), booleanEchoRenderer);
        CELL_RENDERER.put(HGlassConfiguration.class.getName(), booleanRenderer);

        IS_CONFIGURATION_BOOLEAN = new HashMap<>();
        IS_CONFIGURATION_BOOLEAN.put(InkspotConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeWithEchoConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeWithTracingConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(HGlassConfiguration.class.getName(), true);
    }

    /** The pixel width of the terminal window. */
    private final int px;

    /** The pixel height of the terminal window. */
    private final int py;

    /** The controls for keyboard and mouse interactions. */
    private final Controls controls;

    /** The configuration defining the automaton's setup and behavior. */
    private final Configuration<C, S> configuration;

    /** The automaton being controlled. */
    private Automa<C, S> automaton;

    /** The Lanterna terminal screen for rendering the grid. */
    private TerminalScreen screen;

    /** The width (number of columns) of the grid and terminal. */
    private int width;

    /** The height (number of rows) of the grid and terminal. */
    private int height;

    /** The interval between automaton steps, in milliseconds. */
    private long intervalMillis;

    /** The text graphics context for rendering on the screen. */
    private TextGraphics textGraphics;

    /** The font size used for terminal rendering. */
    private int fontSize;

    /** The renderer for displaying the grid's boolean states. */
    private GridRenderer<Cell<BooleanState>, BooleanState> renderer;

    /**
     * Constructs a new view controller with the specified terminal dimensions, font size, and configuration.
     * Initializes the grid size based on pixel dimensions and font size, and sets up the automaton and terminal.
     *
     * @param px            the pixel width of the terminal window
     * @param py            the pixel height of the terminal window
     * @param fontSize      the font size for terminal rendering
     * @param configuration the automaton configuration, must not be null
     * @throws NullPointerException if configuration is null
     */
    public ControlView(int px, int py, int fontSize, @NonNull Configuration<C, S> configuration) {
        Objects.requireNonNull(configuration);
        this.px = px;
        this.py = py;
        this.fontSize = fontSize;
        this.width = px / fontSize;
        this.height = py / fontSize;
        this.controls = new Controls();
        this.configuration = configuration;
        initialize();
    }

    /**
     * Initializes the terminal screen, automaton, and renderer. Sets up the screen, configures the automaton
     * with the provided configuration, and assigns an appropriate renderer based on the configuration type.
     *
     * @throws RuntimeException if an error occurs during initialization
     */
    @SuppressWarnings("rawtypes")
    public void initialize() {
        try {
            setupScreen();
            automaton = new Automa<>();
            configureAutoma();
            renderer = new GridRenderer<>(screen, CELL_RENDERER.get(configuration.getClass().getName()));
            automaton.setGridConsumer((GridRenderer<C, S>) renderer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ControlView: " + e.getMessage(), e);
        }
    }

    /**
     * Sets up the Lanterna terminal screen by closing any existing terminal, creating a new one, and starting
     * the screen with text graphics.
     *
     * @throws RuntimeException if an error occurs during screen setup
     */
    private void setupScreen() {
        try {
            closeTerminal();
            setupTerminal();
            assert screen != null;
            screen.startScreen();
            textGraphics = screen.newTextGraphics();
        } catch (Exception e) {
            throw new RuntimeException("Failed to set up screen: " + e.getMessage(), e);
        }
    }

    /**
     * Configures the automaton using the current configuration, applying the grid size and update interval.
     * Uses a default interval of 100 milliseconds if none is specified.
     */
    private void configureAutoma() {
        if (intervalMillis <= 0) {
            intervalMillis = 100;
        }
        configuration.configure(automaton, width, height, intervalMillis);
    }

    /**
     * Closes the terminal, stopping the screen and releasing resources. Logs any errors during closure.
     */
    private void closeTerminal() {
        try {
            if (screen != null) {
                LOGGER.debug("Stopping screen");
                screen.stopScreen(true);
                screen.close();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to close terminal: {}", e.getMessage(), e);
        }
    }

    /**
     * Sets up the Lanterna terminal with the specified dimensions and a custom font. Configures the terminal
     * size, title, and font, and adds mouse listeners for boolean configurations.
     *
     * @throws IOException         if an error occurs during terminal creation
     * @throws FontFormatException if the custom font cannot be loaded
     */
    private void setupTerminal() throws IOException, FontFormatException {
        final InputStream is = ControlView.class.getResourceAsStream(
                "/fonts/Px437 IBM Conv/Px437_IBM_Conv.ttf"
        );
        assert is != null;
        final Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, fontSize);
        is.close();

        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
        terminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
        terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
        terminalFactory.setTerminalEmulatorFontConfiguration(
                SwingTerminalFontConfiguration.newInstance(font));
        Terminal terminal = terminalFactory.createTerminal();
        if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
            swingTerminalFrame.setResizable(false);
            swingTerminalFrame.setLocationRelativeTo(null);
            swingTerminalFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            if (IS_CONFIGURATION_BOOLEAN.get(configuration.getClass().getName())) {
                ViewMouseListener<C, S> viewMouseListener = new ViewMouseListener<>(this);
                swingTerminalFrame.getContentPane().getComponent(0).addMouseListener(viewMouseListener);
                swingTerminalFrame.getContentPane().getComponent(0).addMouseMotionListener(viewMouseListener);
                swingTerminalFrame.getContentPane().getComponent(0).addMouseWheelListener(viewMouseListener);
            }
        }
        screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
    }

    /**
     * Runs the main interaction loop, starting the automaton and processing user input for actions such as
     * pausing, resuming, stepping, resizing, or exiting. Updates the display and handles terminal events.
     *
     * @throws RuntimeException if an I/O error occurs during input processing
     */
    public void run() {
        boolean scaleChange = false;
        boolean showControls = false;
        boolean quit = false;
        automaton.start();
        try {
            while (true) {
                KeyStroke key = screen.readInput();
                if (key.getKeyType() == KeyType.Escape) {
                    break; // Exit on ESC
                } else if (key.getKeyType() == KeyType.Character) {
                    Character character = key.getCharacter();
                    switch (character) {
                        case 'q', 'Q' -> quit = true;
                        case 'p', 'P', ' ' -> {
                            if (automaton.isRunning()) {
                                automaton.stop();
                                TerminalPosition topLeft = new TerminalPosition(width - 3, 0);
                                TerminalSize size = new TerminalSize(3, 3);
                                TextCharacter textCharacter = TextCharacter.fromCharacter(' ', TextColor.ANSI.RED, null, SGR.REVERSE, SGR.BLINK)[0];
                                textGraphics.fillRectangle(topLeft, size, textCharacter);
                                screen.refresh(Screen.RefreshType.DELTA);
                                LOGGER.debug("Automaton stopped");
                            } else {
                                automaton.resume();
                                LOGGER.debug("Automaton resumed");
                            }
                        }
                        case 'r', 'R' -> {
                            automaton.stop();
                            automaton.getGrid().initialize();
                            automaton.resume();
                        }
                        case '+' -> {
                            automaton.stop();
                            --fontSize;
                            fontSize = Math.max(fontSize, 2);
                            width = px / fontSize;
                            height = py / fontSize;
                            scaleChange = true;
                        }
                        case '-' -> {
                            automaton.stop();
                            ++fontSize;
                            fontSize = Math.min(fontSize, 32);
                            width = px / fontSize;
                            height = py / fontSize;
                            scaleChange = true;
                        }
                        case '<' -> {
                            if (automaton.isRunning()) {
                                long intervalMillis = automaton.getIntervalMillis();
                                intervalMillis = (long) Math.min(2000, intervalMillis + intervalMillis * 0.25);
                                automaton.setIntervalMillis(intervalMillis);
                                this.intervalMillis = intervalMillis;
                            }
                        }
                        case '>' -> {
                            if (automaton.isRunning()) {
                                long intervalMillis = automaton.getIntervalMillis();
                                intervalMillis = (long) Math.max(20, intervalMillis - intervalMillis * 0.25);
                                automaton.setIntervalMillis(intervalMillis);
                                this.intervalMillis = intervalMillis;
                            }
                        }
                        case 's' -> {
                            if (automaton.isRunning()) {
                                automaton.stop();
                            }
                            automaton.step();
                            renderer.accept((Grid<Cell<BooleanState>, BooleanState>) automaton.getGrid());
                            TerminalPosition topLeft = new TerminalPosition(width - 3, 0);
                            TerminalSize size = new TerminalSize(3, 3);
                            TextCharacter textCharacter = TextCharacter.fromCharacter(' ', TextColor.ANSI.BLUE, null, SGR.REVERSE, SGR.BLINK)[0];
                            textGraphics.fillRectangle(topLeft, size, textCharacter);
                            screen.refresh(Screen.RefreshType.DELTA);
                        }
                        case '?' -> showControls = true;
                    }
                }

                if (scaleChange || showControls || quit) {
                    break;
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error reading input: {}", e.getMessage(), e);
        } finally {
            automaton.stop();
            closeTerminal();
        }

        if (scaleChange) {
            initialize();
            run();
        }

        if (showControls) {
            showControls();
        }
    }

    /**
     * Displays a controls menu listing keyboard and mouse interactions, temporarily resizing the terminal
     * to a fixed font size for readability. Returns to the main view after user input.
     *
     * @throws RuntimeException if an error occurs during menu display
     */
    private void showControls() {
        try {
            int previousFontSize = fontSize;
            fontSize = 14;
            width = px / fontSize;
            height = py / fontSize;
            setupScreen();

            int row = 2;
            textGraphics.putString(0, row++, "\tKeyboard Controls", SGR.BOLD);
            row++;
            for (Controls.Control control : controls.controls) {
                textGraphics.putString(0, row++, "\t\t"+control.control() + ":\t" + control.desc());
                row++;
            }

            row++;
            textGraphics.putString(0, row++, "\tMouse Controls", SGR.BOLD);
            row++;
            for (Controls.Control control : controls.mouseControls) {
                textGraphics.putString(0, row++, "\t\t"+control.control() + ":\t" + control.desc());
                row++;
            }

            screen.refresh();
            screen.readInput();

            fontSize = previousFontSize;
            width = px / fontSize;
            height = py / fontSize;
            initialize();
            run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to display controls: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the automaton being controlled.
     *
     * @return the {@link Automa}
     */
    public Automa<C, S> getAutomaton() {
        return automaton;
    }

    /**
     * Returns the current font size used for terminal rendering.
     *
     * @return the font size
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Returns the renderer used for displaying the grid's boolean states.
     *
     * @return the {@link GridRenderer} for boolean states
     */
    public GridRenderer<Cell<BooleanState>, BooleanState> getRenderer() {
        return renderer;
    }

    /**
     * Returns the width (number of columns) of the grid and terminal.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height (number of rows) of the grid and terminal.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the configuration defining the automaton's setup and behavior.
     *
     * @return the {@link Configuration}
     */
    public Configuration<C, S> getConfiguration() {
        return configuration;
    }
}