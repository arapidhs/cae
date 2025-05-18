package com.dungeoncode.ca.view;

import com.dungeoncode.ca.automa.*;
import com.dungeoncode.ca.core.*;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.view.render.*;
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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
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
 * resume, resize, screenshot), and terminal management with distinct fonts for simulation and display modes.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class ControlView<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Logger for recording view events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlView.class);

    /**
     * Maps configuration class names to their corresponding state renderers.
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, StateRenderer> CELL_RENDERER;

    /**
     * Maps configuration class names to a boolean indicating if they use boolean states.
     */
    private static final Map<String, Boolean> IS_CONFIGURATION_BOOLEAN;

    static {
        BooleanStateRenderer booleanRenderer = new BooleanStateRenderer();
        BooleanEchoStateRenderer booleanEchoRenderer = new BooleanEchoStateRenderer();
        LiveSumStateRenderer liveSumStateRenderer = new LiveSumStateRenderer(LiveSumStateRenderer.Palette.DEFAULT);
        PulseWeaverStateRenderer pulseWeaverStateRenderer =new PulseWeaverStateRenderer();

        CELL_RENDERER = new HashMap<>();
        CELL_RENDERER.put(InkspotConfiguration.class.getName(), booleanRenderer);
        CELL_RENDERER.put(GameOfLifeConfiguration.class.getName(), booleanRenderer);
        CELL_RENDERER.put(GameOfLifeWithEchoConfiguration.class.getName(), booleanEchoRenderer);
        CELL_RENDERER.put(GameOfLifeWithTracingConfiguration.class.getName(), booleanEchoRenderer);
        CELL_RENDERER.put(HGlassConfiguration.class.getName(), booleanRenderer);
        CELL_RENDERER.put(ParityConfiguration.class.getName(), liveSumStateRenderer);
        CELL_RENDERER.put(PulseWeaverConfiguration.class.getName(), pulseWeaverStateRenderer);

        IS_CONFIGURATION_BOOLEAN = new HashMap<>();
        IS_CONFIGURATION_BOOLEAN.put(InkspotConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeWithEchoConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(GameOfLifeWithTracingConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(HGlassConfiguration.class.getName(), true);
        IS_CONFIGURATION_BOOLEAN.put(ParityConfiguration.class.getName(), true);
    }

    /**
     * The pixel width of the terminal window.
     */
    private final int px;

    /**
     * The pixel height of the terminal window.
     */
    private final int py;

    /**
     * The controls for keyboard and mouse interactions.
     */
    private final Controls controls;

    /**
     * The configuration defining the automaton's setup and behavior.
     */
    private final Configuration<C, S> configuration;
    /**
     * The font size used for rendering display elements like the controls menu.
     */
    private final int displayFontSize;
    /**
     * The automaton being controlled.
     */
    private Automa<C, S> automaton;
    /**
     * The Lanterna terminal screen for rendering the grid or controls menu.
     */
    private TerminalScreen screen;
    /**
     * The width (number of columns) of the grid and terminal.
     */
    private int width;
    /**
     * The height (number of rows) of the grid and terminal.
     */
    private int height;
    /**
     * The interval between automaton steps, in milliseconds.
     */
    private long intervalMillis;
    /**
     * The text graphics context for rendering on the screen.
     */
    private TextGraphics textGraphics;
    /**
     * The font used for rendering simulation cells.
     */
    private Font cellFont;
    /**
     * The font size used for rendering simulation cells.
     */
    private int cellFontSize;
    /**
     * The font used for rendering display elements like the controls menu.
     */
    private Font displayFont;
    /**
     * The renderer for displaying the grid's boolean states.
     */
    private GridRenderer<Cell<BooleanState>, BooleanState> renderer;

    /**
     * Constructs a new control view with the specified terminal dimensions, cell font size, and configuration.
     * Initializes the grid size based on pixel dimensions and cell font size, sets up fonts, and prepares the
     * automaton and terminal.
     *
     * @param px            the pixel width of the terminal window
     * @param py            the pixel height of the terminal window
     * @param cellFontSize  the font size for rendering simulation cells
     * @param configuration the automaton configuration, must not be null
     * @throws NullPointerException if configuration is null
     * @throws RuntimeException     if font loading fails
     */
    public ControlView(int px, int py, int cellFontSize, @NonNull Configuration<C, S> configuration) {
        Objects.requireNonNull(configuration);
        this.px = px;
        this.py = py;
        this.cellFontSize = cellFontSize;
        this.width = px / cellFontSize;
        this.height = py / cellFontSize;
        this.controls = new Controls();
        this.configuration = configuration;
        this.displayFontSize = 18;
        setupFonts();
        initialize(true);
    }

    /**
     * Loads and configures fonts for simulation cells and display elements, using a custom font for cells and
     * a standard font for the controls menu.
     *
     * @throws RuntimeException if an error occurs during font loading
     */
    private void setupFonts() {
        try {
            final InputStream is = ControlView.class.getResourceAsStream(
                    "/fonts/Px437 IBM Conv/Px437_IBM_Conv.ttf"
            );
            assert is != null;
            cellFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, cellFontSize);
            is.close();
            displayFont = new Font("Courier New", Font.PLAIN, displayFontSize);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load fonts: " + e.getMessage(), e);
        }
    }

    /**
     * Initializes the terminal screen, automaton, and renderer. Sets up the screen, configures the automaton
     * with the provided configuration, and assigns an appropriate renderer based on the configuration type.
     *
     * @param resetAutoma whether to reset the automaton instance and reconfigure it
     * @throws RuntimeException if an error occurs during initialization
     */
    @SuppressWarnings("rawtypes")
    public void initialize(boolean resetAutoma) {
        try {
            setupScreen(true);
            if (resetAutoma) {
                automaton = new Automa<>();
                configureAutoma();
            }
            renderer = new GridRenderer<>(screen, CELL_RENDERER.get(configuration.getClass().getName()));
            automaton.setGridConsumer((GridRenderer<C, S>) renderer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ControlView: " + e.getMessage(), e);
        }
    }

    /**
     * Sets up the Lanterna terminal screen by closing any existing terminal, creating a new one, and starting
     * the screen with text graphics. Configures the terminal for either simulation or display mode based on
     * the specified parameter.
     *
     * @param simulation true to set up for simulation (using cell font), false for display (using display font)
     * @throws RuntimeException if an error occurs during screen setup
     */
    private void setupScreen(boolean simulation) {
        try {
            closeTerminal();
            if (simulation) {
                setupSimulationTerminal();
            } else {
                setupDisplayTerminal();
            }
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
     * Sets up the Lanterna terminal for simulation mode with the specified dimensions and custom cell font.
     * Configures the terminal size, title, and font, and adds mouse listeners for boolean configurations.
     *
     * @throws IOException if an error occurs during terminal creation
     */
    private void setupSimulationTerminal() throws IOException {
        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
        terminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
        terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
        terminalFactory.setTerminalEmulatorFontConfiguration(
                SwingTerminalFontConfiguration.newInstance(cellFont));
        Terminal terminal = terminalFactory.createTerminal();
        if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
            swingTerminalFrame.setResizable(false);
            swingTerminalFrame.setLocationRelativeTo(null);
            swingTerminalFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            if ( IS_CONFIGURATION_BOOLEAN.containsKey(configuration.getClass().getName())
                    && IS_CONFIGURATION_BOOLEAN.get(configuration.getClass().getName())) {
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
     * Sets up the Lanterna terminal for display mode (e.g., controls menu) with fixed dimensions and standard
     * display font. Configures the terminal size, title, and font.
     *
     * @throws IOException if an error occurs during terminal creation
     */
    private void setupDisplayTerminal() throws IOException {
        final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
        terminalFactory.setInitialTerminalSize(new TerminalSize(80, 25));
        terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
        terminalFactory.setTerminalEmulatorFontConfiguration(
                SwingTerminalFontConfiguration.newInstance(displayFont));
        Terminal terminal = terminalFactory.createTerminal();
        if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
            swingTerminalFrame.setResizable(false);
            swingTerminalFrame.setLocationRelativeTo(null);
            swingTerminalFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
    }

    /**
     * Runs the main interaction loop, starting the automaton and processing user input for actions such as
     * pausing, resuming, stepping, resizing, saving screenshots, or exiting. Updates the display and handles
     * terminal events.
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
                } else if (key.isCtrlDown() && key.getKeyType() == KeyType.Character) {
                    if (key.getCharacter() == 's') {
                        saveScreenToImage();
                    }
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
                            --cellFontSize;
                            cellFontSize = Math.max(cellFontSize, 2);
                            width = px / cellFontSize;
                            height = py / cellFontSize;
                            scaleChange = true;
                        }
                        case '-' -> {
                            automaton.stop();
                            ++cellFontSize;
                            cellFontSize = Math.min(cellFontSize, 32);
                            width = px / cellFontSize;
                            height = py / cellFontSize;
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
                    setupFonts();
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
            initialize(true);
            run();
        }

        if (showControls) {
            showControls();
        }
    }

    /**
     * Displays a controls menu listing keyboard and mouse interactions, using a fixed font size and aligned
     * columns for readability. Returns to the main simulation view after user input.
     *
     * @throws RuntimeException if an error occurs during menu display
     */
    private void showControls() {
        try {

            boolean simulation = false;
            setupScreen(simulation);

            int row = 0;
            textGraphics.putString(0, row++, "Keyboard Controls", SGR.BOLD);
            row++;

            // Determine max label width for keyboard controls
            int maxKeyLabelWidth = controls.controls.stream()
                    .mapToInt(c -> c.control().length())
                    .max()
                    .orElse(1);

            // Print keyboard controls with aligned columns
            for (Controls.Control control : controls.controls) {
                String label = String.format("%-" + maxKeyLabelWidth + "s", control.control());
                textGraphics.putString(0, row++, "  " + label + "  " + control.desc());
            }

            row++;
            textGraphics.putString(0, row++, "Mouse Controls", SGR.BOLD);
            row++;

            // Determine max label width for mouse controls
            int maxMouseLabelWidth = controls.mouseControls.stream()
                    .mapToInt(c -> c.control().length())
                    .max()
                    .orElse(1);

            // Print mouse controls with aligned columns
            for (Controls.Control control : controls.mouseControls) {
                String label = String.format("%-" + maxMouseLabelWidth + "s", control.control());
                textGraphics.putString(0, row++, "  " + label + "  " + control.desc());
            }

            screen.refresh();
            screen.readInput();

            boolean resetAutoma = false;
            initialize(resetAutoma);
            run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to display controls: " + e.getMessage(), e);
        }
    }

    /**
     * Saves the current terminal screen as a PNG image in the user's home directory under
     * .cell-automata/screenshots/. The filename is the configuration name appended with a millisecond-precision
     * timestamp (e.g., GameOfLife_20250518115712345.png).
     *
     * @throws IOException if an error occurs during image capture or file writing
     */
    public void saveScreenToImage() throws IOException {
        if (screen != null && screen.getTerminal() instanceof SwingTerminalFrame swingTerminalFrame) {
            Component component = swingTerminalFrame.getContentPane().getComponent(0);

            // Create a BufferedImage to hold the component's content
            BufferedImage image = new BufferedImage(
                    component.getWidth(),
                    component.getHeight(),
                    BufferedImage.TYPE_INT_RGB
            );

            // Paint the component to the image
            Graphics2D g2d = image.createGraphics();
            component.paint(g2d);
            g2d.dispose();

            // Create the screenshots directory in the user's home directory
            String userHome = System.getProperty("user.home");
            File screenshotDir = new File(userHome, ".cell-automata/screenshots");
            if (!screenshotDir.exists() && !screenshotDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + screenshotDir.getAbsolutePath());
            }

            // Generate filename with configuration name and millisecond-precision timestamp
            String configName = configuration.getName().replaceAll("[^a-zA-Z0-9]", "_");
            String timestamp = String.format("%tY%tm%td%tH%tM%tS%tL",
                    System.currentTimeMillis(), System.currentTimeMillis(),
                    System.currentTimeMillis(), System.currentTimeMillis(),
                    System.currentTimeMillis(), System.currentTimeMillis(),
                    System.currentTimeMillis());
            String fileName = configName + "_" + timestamp + ".png";
            File outputFile = new File(screenshotDir, fileName);

            // Save the image
            ImageIO.write(image, "png", outputFile);
            LOGGER.info("Screen saved to {}", outputFile.getAbsolutePath());

            // Draw confirmation line on screen
            getTextGraphics().drawLine(0, height / 2, width, height / 2,
                    TextCharacter.fromString("-", TextColor.ANSI.YELLOW, null, SGR.REVERSE)[0]);
            screen.refresh(Screen.RefreshType.DELTA);
        } else {
            throw new IllegalStateException("Screen capture is only supported with SwingTerminalFrame");
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
     * Returns the current font size used for rendering simulation cells.
     *
     * @return the cell font size
     */
    public int getCellFontSize() {
        return cellFontSize;
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

    /**
     * Returns the text graphics context for rendering on the screen.
     *
     * @return the {@link TextGraphics}
     */
    public TextGraphics getTextGraphics() {
        return textGraphics;
    }

    /**
     * Returns the Lanterna terminal screen.
     *
     * @return the {@link TerminalScreen}
     */
    public TerminalScreen getScreen() {
        return screen;
    }
}