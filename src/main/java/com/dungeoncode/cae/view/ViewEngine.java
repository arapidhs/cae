package com.dungeoncode.cae.view;

import com.dungeoncode.cae.automa.*;
import com.dungeoncode.cae.core.*;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;
import com.dungeoncode.cae.view.render.*;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.googlecode.lanterna.input.KeyType.Character;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * Manages the visualization and user interaction for a cellular automaton, controlling the Lanterna terminal
 * and automaton execution. Supports grid initialization, configuration application, input handling (e.g., pause,
 * resume, resize, screenshot), and terminal management with distinct fonts for simulation and display modes.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class ViewEngine<C extends Cell<S>, S extends CellState<?>> {

    /**
     * Logger for recording view events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewEngine.class);

    /**
     * Maps configuration class names to their corresponding state renderers.
     */
    @SuppressWarnings("rawtypes")
    private static final Map<String, StateRenderer> CELL_RENDERER;

    static {
        RendererBoolean rendererBoolean = new RendererBoolean(RendererBoolean.Palette.DEFAULT);
        RendererBooleanId rendererBooleanId = new RendererBooleanId();

        CELL_RENDERER = new HashMap<>();
        CELL_RENDERER.put(ConfInkspot.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfGameOfLife.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfHglass.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfParity.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfSquares.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfDiamonds.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfTriangles.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfOneOutOfEight.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfLichens.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfLichensWithDeath.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfMajority.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfVichniacAnneal.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfBanks.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfParityFlip.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfTimeTunnel.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfCandleRain.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfRandomAnneal.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfHistogram.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfNaiveDiffusion.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfHandshakeDiffusion.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfGeneticDrift.class.getName(), rendererBooleanId);
        CELL_RENDERER.put(ConfSoilErosion.class.getName(), rendererBoolean);
        CELL_RENDERER.put(ConfSoilErosionRandom.class.getName(), rendererBoolean);

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
     * The font size used for rendering display elements like the controls menu.
     */
    private final int displayFontSize;
    private final List<Configuration> configurations;
    /**
     * The configuration defining the automaton's setup and behavior.
     */
    private Configuration<C, S> configuration;
    /**
     * The automaton being controlled.
     */
    private Automaton<C, S> automaton;
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
    private GridRenderer<C, S> renderer;

    /**
     * Tracks whether the automaton is restarting due to user control input.
     */
    private boolean automaRestarting;

    /**
     * Constructs a new control view with the specified terminal dimensions, cell font size, and configuration.
     * Initializes the grid size based on pixel dimensions and cell font size, sets up fonts, and prepares the
     * automaton and terminal.
     *
     * @param px             the pixel width of the terminal window
     * @param py             the pixel height of the terminal window
     * @param cellFontSize   the font size for rendering simulation cells
     * @param configurations
     * @param configuration  the automaton configuration, must not be null
     * @throws NullPointerException if configuration is null
     * @throws RuntimeException     if font loading fails
     */
    public ViewEngine(int px, int py, int cellFontSize, List<Configuration> configurations, @NonNull Configuration<C, S> configuration) {
        Objects.requireNonNull(configuration);
        this.px = px;
        this.py = py;
        this.cellFontSize = cellFontSize;
        this.width = px / cellFontSize;
        this.height = py / cellFontSize;
        this.controls = new Controls();
        this.configuration = configuration;
        this.displayFontSize = 18;
        this.configurations = configurations;
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
            final InputStream is = ViewEngine.class.getResourceAsStream(
                    "/fonts/ibm/Px437_IBM_Conv.ttf"
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
    public void initialize(boolean resetAutoma) {
        try {
            setupScreen(true);
            if (resetAutoma) {
                if (automaton != null && automaton.isRunning()) {
                    automaton.stop();
                }
                automaton = new Automaton<>();
                configureAutoma();
            } else {
                renderer = new GridRenderer<>(screen, CELL_RENDERER.get(configuration.getClass().getName()));
                automaton.setGridConsumer(renderer);
            }

            Terminal terminal = screen.getTerminal();
            if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
                if (automaton.getGrid().getCell(0, 0) instanceof BooleanCell) {
                    EngineListener<C, S> engineListener = new EngineListener<>(this);
                    swingTerminalFrame.getContentPane().getComponent(0).addMouseListener(engineListener);
                    swingTerminalFrame.getContentPane().getComponent(0).addMouseMotionListener(engineListener);
                    swingTerminalFrame.getContentPane().getComponent(0).addMouseWheelListener(engineListener);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ViewEngine: " + e.getMessage(), e);
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
        renderer = new GridRenderer<>(screen, CELL_RENDERER.get(configuration.getClass().getName()));
        automaton.setGridConsumer(renderer);
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
        boolean showConfigurationDetails = false;
        boolean quit = false;
        automaton.start();
        try {
            while (true) {
                KeyStroke key = screen.readInput();
                KeyType keyType = key.getKeyType();
                if (keyType != Character) {
                    switch (key.getKeyType()) {
                        case Escape -> {
                            quit = true;
                        }
                        case F1 -> {
                            if (renderer.getStateRenderer() instanceof RendererBoolean) {
                                ((RendererBoolean) renderer.getStateRenderer()).toggleInversion();
                                renderer.accept(automaton.getGrid());
                            }
                        }
                        case ArrowLeft -> {
                            if (renderer.getStateRenderer() instanceof RendererBoolean) {
                                ((RendererBoolean) renderer.getStateRenderer()).previousPalette();
                                renderer.accept(automaton.getGrid());
                            }
                        }
                        case ArrowRight -> {
                            if (automaton.getGrid().getCell(0, 0) instanceof BooleanCell) {
                                if (renderer.getStateRenderer() instanceof RendererBoolean) {
                                    ((RendererBoolean) renderer.getStateRenderer()).nextPalette();
                                    renderer.accept(automaton.getGrid());
                                }
                            }
                        }
                        case PageDown -> startNextAutoma();
                        case PageUp -> startPreviousAutoma();
                    }
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
                            if (!automaRestarting) {
                                automaRestarting = true;

                                boolean wasRunning = automaton.isRunning();
                                if (automaton.isRunning()) {
                                    automaton.stop();
                                }
                                automaton.getGrid().initialize();
                                if (wasRunning) {
                                    automaton.resume();
                                } else {
                                    renderer.accept(automaton.getGrid());
                                }
                                automaRestarting = false;
                            }
                        }
                        case 'i', 'I' -> {
                            showConfigurationDetails = true;
                        }
                        case '+' -> {
                            if (cellFontSize > 2) {
                                automaton.stop();
                                --cellFontSize;
                                cellFontSize = Math.max(cellFontSize, 2);
                                width = px / cellFontSize;
                                height = py / cellFontSize;
                                scaleChange = true;
                            }
                        }
                        case '-' -> {
                            if (cellFontSize < 18) {
                                automaton.stop();
                                ++cellFontSize;
                                cellFontSize = Math.min(cellFontSize, 18);
                                width = px / cellFontSize;
                                height = py / cellFontSize;
                                scaleChange = true;
                            }
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
                            renderer.accept(automaton.getGrid());
                            TerminalPosition topLeft = new TerminalPosition(width - 3, 0);
                            TerminalSize size = new TerminalSize(3, 3);
                            TextCharacter textCharacter = TextCharacter.fromCharacter(' ', TextColor.ANSI.BLUE, null, SGR.REVERSE, SGR.BLINK)[0];
                            textGraphics.fillRectangle(topLeft, size, textCharacter);
                            screen.refresh(Screen.RefreshType.DELTA);
                        }
                        case 'w' -> {
                            if (automaton.getGrid().getCell(0, 0) instanceof BooleanCell) {
                                boolean wasRunning = automaton.isRunning();
                                if (automaton.isRunning()) {
                                    automaton.stop();
                                }
                                Grid<BooleanCell, BooleanState> grid = (Grid<BooleanCell, BooleanState>) automaton.getGrid();
                                int width = grid.getWidth();
                                int height = grid.getHeight();
                                for (int y = 0; y < height; y++) {
                                    for (int x = 0; x < width; x++) {
                                        BooleanCell cell = grid.getCell(x, y);
                                        cell.getState().swapEcho();
                                    }
                                }
                                renderer.accept((Grid<C, S>) grid);
                                if (wasRunning) {
                                    automaton.start();
                                }
                            }
                        }
                        case '?' -> showControls = true;
                    }
                }

                if (scaleChange || showControls || showConfigurationDetails || quit) {
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
        } else if (showControls) {
            showControls();
        } else if (showConfigurationDetails) {
            showConfigurationDetails();
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

            // Split keyboard controls into two halves
            List<Controls.Control> keyboardControls = controls.controls;
            int totalControls = keyboardControls.size();
            int midPoint = (totalControls + 1) / 2;

            List<Controls.Control> leftControls = keyboardControls.subList(0, midPoint);
            List<Controls.Control> rightControls = keyboardControls.subList(midPoint, totalControls);

            // Determine max label width for left and right columns
            int maxLeftKeyWidth = leftControls.stream()
                    .mapToInt(c -> c.control().length())
                    .max()
                    .orElse(1);

            int maxRightKeyWidth = rightControls.stream()
                    .mapToInt(c -> c.control().length())
                    .max()
                    .orElse(1);

            // Calculate column positioning - adjust these values as needed for your screen width
            int leftKeyColumn = 2;
            int leftDescColumn = leftKeyColumn + maxLeftKeyWidth + 2;
            int rightKeyColumn = leftDescColumn + 30; // Allow 30 characters for left descriptions with extra spacing

            // Print keyboard controls in two columns
            int maxRows = Math.max(leftControls.size(), rightControls.size());
            for (int i = 0; i < maxRows; i++) {
                // Left column
                if (i < leftControls.size()) {
                    Controls.Control left = leftControls.get(i);
                    textGraphics.putString(leftKeyColumn, row, left.control());
                    textGraphics.putString(leftDescColumn, row, left.desc());
                }

                // Right column
                if (i < rightControls.size()) {
                    Controls.Control right = rightControls.get(i);
                    textGraphics.putString(rightKeyColumn, row, right.control());
                    textGraphics.putString(rightKeyColumn + maxRightKeyWidth + 2, row, right.desc());
                }

                row++;
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
     * Displays the selected configuration details on the left side of the screen and
     * a list of all available configurations on the right.
     * Handles text wrapping for long descriptions and citations.
     */
    private void showConfigurationDetails() {
        try {
            boolean simulation = false;
            setupScreen(simulation);

            // Clear screen
            textGraphics.fill(' ');

            // ===== LEFT SIDE: Configuration Details =====
            int leftColumnWidth = screen.getTerminalSize().getColumns() / 2 - 5; // Half screen minus margin
            int leftStartCol = 2;
            int row = 1;

            // Show configuration details if one is selected
            if (configuration != null) {
                // Display Name with emphasis
                textGraphics.putString(leftStartCol, row++, "Automaton: " + configuration.getClass().getName(), SGR.BOLD);
                row++;

//                // Display Description with word wrapping
//                //textGraphics.putString(leftStartCol, row++, "Description:", SGR.BOLD);
//                String description = configuration.getDescription();
//                if (description != null && !description.isEmpty()) {
//                    row = wrapAndPrintText(description, leftStartCol + 2, row, leftColumnWidth - 2);
//                } else {
//                    textGraphics.putString(leftStartCol + 2, row++, "No description available.");
//                }

            } else {
                textGraphics.putString(leftStartCol, row++, "No configuration selected.", SGR.BOLD);
            }

            // ===== RIGHT SIDE: Configuration List =====
            int rightStartCol = leftColumnWidth + 7; // Position right after left column with padding
            row = 1;

            textGraphics.putString(rightStartCol, row++, "Available Automas:", SGR.BOLD);
            row++;

            // Print numbered list of configurations
            for (int i = 0; i < configurations.size(); i++) {
                Configuration<C, S> config = configurations.get(i);
                String listItem = String.format("%d. %s", i + 1, config.getClass().getName());

                // Highlight selected configuration
                if (config == configuration) {
                    textGraphics.putString(rightStartCol, row++, listItem, SGR.BOLD, SGR.REVERSE);
                } else {
                    textGraphics.putString(rightStartCol, row++, listItem);
                }
            }

            // Instructions at bottom
            int bottomRow = screen.getTerminalSize().getRows() - 2;
            textGraphics.putString(leftStartCol, bottomRow, "Press any key to return", SGR.ITALIC);

            screen.refresh();
            screen.readInput(); // Wait for user input before returning

            boolean resetAutoma = false;
            initialize(resetAutoma);
            run();
        } catch (Exception e) {
            throw new RuntimeException("Failed to display configuration details: " + e.getMessage(), e);
        }
    }

    /**
     * Helper method to wrap text at word boundaries and print it line by line.
     *
     * @param text     The text to wrap and print
     * @param startCol The starting column (x position)
     * @param startRow The starting row (y position)
     * @param maxWidth The maximum width for wrapping
     * @return The next row position after all text is printed
     */
    private int wrapAndPrintText(String text, int startCol, int startRow, int maxWidth) {
        int currentRow = startRow;

        // Split by existing line breaks first
        String[] paragraphs = text.split("\\n");

        for (String paragraph : paragraphs) {
            if (paragraph.isEmpty()) {
                currentRow++;
                continue;
            }

            // Now handle word wrapping within each paragraph
            String[] words = paragraph.split("\\s+");
            StringBuilder currentLine = new StringBuilder();

            for (String word : words) {
                // Check if adding this word would exceed the max width
                if (currentLine.length() + word.length() + 1 > maxWidth) {
                    // Print current line and reset
                    textGraphics.putString(startCol, currentRow++, currentLine.toString());
                    currentLine = new StringBuilder(word);
                } else {
                    // Add word to current line
                    if (!currentLine.isEmpty()) {
                        currentLine.append(" ");
                    }
                    currentLine.append(word);
                }
            }

            // Print any remaining text
            if (!currentLine.isEmpty()) {
                textGraphics.putString(startCol, currentRow++, currentLine.toString());
            }

            // Add a blank line between paragraphs
            currentRow++;
        }

        return currentRow;
    }

    /**
     * Saves the current terminal screen as a PNG image in the user's home directory under
     * .cell-automata/screenshots/. The filename is the configuration name appended with a millisecond-precision
     * timestamp (e.g., GameOfLife_20250518115712345.png).
     *
     * @throws IOException if an error occurs during image capture or file writing
     */
    public void saveScreenToImage() throws IOException {
        boolean wasRunning = automaton.isRunning();
        if (automaton.isRunning()) {
            automaton.stop();
        }
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
            String configName = configuration.getClass().getName().replaceAll("[^a-zA-Z0-9]", "_");
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
                    TextCharacter.fromString(" ", TextColor.ANSI.GREEN_BRIGHT, null, SGR.REVERSE)[0]);
            screen.refresh(Screen.RefreshType.DELTA);
            if (wasRunning) {
                automaton.start();
            } else {
                renderer.accept(automaton.getGrid());
            }
        } else {
            throw new IllegalStateException("Screen capture is only supported with SwingTerminalFrame");
        }
    }

    /**
     * Advances to the next configuration in the list, wrapping around to the first if currently at the end.
     * Stops the current automaton, updates the configuration, reconfigures the automaton, and restarts it.
     */
    public void startNextAutoma() {
        int i = configurations.indexOf(configuration);
        this.configuration = (Configuration<C, S>) configurations.get((i + 1) % configurations.size());
        automaton.stop();
        configureAutoma();
        automaton.start();
    }

    /**
     * Reverts to the previous configuration in the list, wrapping around to the last if currently at the start.
     * Stops the current automaton, updates the configuration, reconfigures the automaton, and restarts it.
     */
    public void startPreviousAutoma() {
        int i = configurations.indexOf(configuration);
        this.configuration = (Configuration<C, S>) configurations.get((i - 1 + configurations.size()) % configurations.size());
        automaton.stop();
        configureAutoma();
        automaton.start();
    }

    /**
     * Returns the automaton being controlled.
     *
     * @return the {@link Automaton}
     */
    public Automaton<C, S> getAutoma() {
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
    public GridRenderer<C, S> getRenderer() {
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