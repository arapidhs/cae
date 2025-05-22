package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.*;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.FileDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.annotation.Nonnull;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.googlecode.lanterna.gui2.Window.Hint.*;

/**
 * Terminal-based user interface for selecting cellular automaton configurations before starting a simulation.
 * Renders an interactive selection window using Lanterna's {@link MultiWindowTextGUI}, allowing users to browse
 * configurations, view details, and launch simulations. Work-in-progress; some features (e.g., font loading, multi-window)
 * are experimental.
 *
 * @param <C> the type of cells, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class View<C extends Cell<S>, S extends CellState<?>> {

    /**
     * The default terminal width in columns.
     */
    private final int width = 180;

    /**
     * The default terminal height in rows.
     */
    private final int height = 60;

    /**
     * The font size for terminal rendering.
     */
    private final int fontSize = 12;

    /**
     * The repository of available configurations.
     */
    private final Repository<C, S> repository;

    /**
     * The index of the currently selected configuration, or -1 if none selected.
     */
    private int selectedConfId;

    /**
     * The Lanterna terminal screen for rendering the UI.
     */
    private TerminalScreen screen;

    /**
     * Constructs a new view with the specified configuration repository.
     *
     * @param repository the {@link Repository} of configurations, must not be null
     * @throws NullPointerException if repository is null
     */
    public View(@Nonnull Repository<C, S> repository) {
        this.repository = Objects.requireNonNull(repository, "Repository cannot be null");
        this.selectedConfId = -1;
    }

    /**
     * Initializes the Lanterna terminal screen, configures fonts and themes, and displays the configuration
     * selection window. Closes the screen on exit.
     *
     * @throws RuntimeException if terminal setup or font loading fails
     */
    public void setup() {
        try {
            // Load custom font (work-in-progress)
            String fontPath="/fonts/jetbrains/JetBrainsMono-ExtraLight.ttf";
            Font font;
            try (InputStream is = View.class.getResourceAsStream(fontPath)) {
                if (is == null) {
                    throw new IOException("Font resource not found: " + fontPath);
                }
                font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, fontSize);
            } catch (Exception e) {
                throw new RuntimeException("Failed to load font: " + e.getMessage(), e);
            }

            // Fallback monospaced font
            final Font monospacedFont = new Font("Courier New", Font.PLAIN, fontSize);

            // Configure terminal
            final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
            terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
            terminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
            terminalFactory.setTerminalEmulatorFontConfiguration(
                    SwingTerminalFontConfiguration.newInstance(font)
            );

            screen = terminalFactory.createScreen();
            Terminal terminal = screen.getTerminal();

            // Configure Swing terminal frame
            if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
                swingTerminalFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                swingTerminalFrame.setResizable(false);
                swingTerminalFrame.setLocationRelativeTo(null);
            }

            screen.setCursorPosition(null);
            screen.startScreen();

            // Display configuration selection window
            showConfigurationWindow(screen);
            // exampleMultiWindow(screen);
            //exampleTableWindow(screen);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize terminal: " + e.getMessage(), e);
        } finally {
            close();
        }
    }

    /**
     * Displays the main configuration selection window, allowing users to browse configurations, view details,
     * and start a simulation. Work-in-progress; includes experimental multi-window and menu bar features.
     *
     * @param screen the {@link TerminalScreen} for rendering, must not be null
     */
    private void showConfigurationWindow(@Nonnull TerminalScreen screen) {
        Objects.requireNonNull(screen, "Screen cannot be null");
        final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        // textGUI.setTheme(LanternaThemes.getRegisteredTheme("blaster"));

        // ESC key to confirm exit
        textGUI.addListener((gui, key) -> {
            if (key.getKeyType() == KeyType.Escape) {
                confirmExit(textGUI);
            }
            return false;
        });

        final BasicWindow window = new BasicWindow("CAE");
        window.setHints(List.of(FULL_SCREEN, FIT_TERMINAL_WINDOW));

        // Root layout
        Panel rootPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rootPanel.setPreferredSize(new TerminalSize(80, 40));

        // Panels
        Panel topPanel = new Panel(new LinearLayout(Direction.VERTICAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Panel centerPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        // Header
        topPanel.addComponent(new EmptySpace());
        topPanel.addComponent(new Label("Terminal-based Cellular Automata Engine").addStyle(SGR.BOLD));
        topPanel.addComponent(new Label("Explore, configure, and run dynamic automata systems").addStyle(SGR.ITALIC));
        topPanel.addComponent(new Label("in a fully interactive text interface.").addStyle(SGR.ITALIC));
        topPanel.addComponent(new EmptySpace());
        topPanel.addComponent(new Label("Select an automaton and press Start to begin.").addStyle(SGR.ITALIC)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning)));

        // Configuration list
        RadioBoxList<String> configList = new RadioBoxList<>();
        configList.setPreferredSize(new TerminalSize(30, 35));
        for (Configuration<C, S> config : repository.getConfigurations()) {
            Descriptor descriptor = repository.getDescriptorByConfId(config.getId());
            configList.addItem(descriptor.getName());
        }

        // Details text area
        TextBox detailsBox = new TextBox(new TerminalSize(45, 35)).setReadOnly(true);

        // Listener for selection change: updates details or starts simulation
        configList.addListener((selectedIndex, prev) -> {
            if (selectedConfId == selectedIndex) {
                close();
            }
            Configuration<C, S> selectedConf = repository.getConfigurations().get(selectedIndex);
            selectedConfId = selectedIndex;
            setDetailsBoxText(selectedConf, detailsBox);
        });

        // Pre-select configuration if previously chosen
        if (selectedConfId > -1 && selectedConfId < repository.getConfigurations().size()) {
            Configuration<C, S> selectedConf = repository.getConfigurations().get(selectedConfId);
            configList.setSelectedIndex(selectedConfId);
            setDetailsBoxText(selectedConf, detailsBox);
        }

        centerPanel.addComponent(configList.withBorder(Borders.singleLine("Available Configurations")));
        centerPanel.addComponent(detailsBox.withBorder(Borders.singleLine("Configuration Details")));

        // Buttons
        buttonsPanel.addComponent(new Button("Start", () -> {
            if (selectedConfId >= 0) {
                close();
            } else {
                MessageDialog.showMessageDialog(textGUI, "", "Select a configuration first!", MessageDialogButton.OK);
            }
        }));

        buttonsPanel.addComponent(new Button("Controls", () ->
                MessageDialog.showMessageDialog(textGUI, "Controls", getControlsText(), MessageDialogButton.OK)));

        buttonsPanel.addComponent(new Button("Exit", () -> confirmExit(textGUI)));

        // Layout assembly
        mainPanel.addComponent(topPanel);
        mainPanel.addComponent(centerPanel);
        mainPanel.addComponent(buttonsPanel);
        rootPanel.addComponent(mainPanel);
        window.setComponent(rootPanel);

        // Launch the GUI
        textGUI.addWindowAndWait(window);
    }

    /**
     * Closes the terminal screen and releases resources.
     */
    private void close() {
        try {
            if (screen != null) {
                screen.stopScreen(true);
                screen.close();
            }
        } catch (IOException e) {
            System.exit(1);
        }
    }

    /**
     * Displays a confirmation dialog before exiting the application.
     *
     * @param textGUI the {@link WindowBasedTextGUI} for rendering the dialog
     */
    private void confirmExit(WindowBasedTextGUI textGUI) {
        MessageDialogButton result = MessageDialog.showMessageDialog(
                textGUI, "", "Exit?", MessageDialogButton.OK, MessageDialogButton.Cancel);
        if (result == MessageDialogButton.OK) {
            exit();
        }
    }

    /**
     * Updates the details text box with information about the selected configuration.
     *
     * @param selectedConf the selected {@link Configuration}
     * @param detailsBox   the {@link TextBox} to update
     */
    private void setDetailsBoxText(Configuration<C, S> selectedConf, TextBox detailsBox) {
        Objects.requireNonNull(selectedConf, "Selected configuration cannot be null");
        Objects.requireNonNull(detailsBox, "Details box cannot be null");
        Descriptor descriptorByConfId = repository.getDescriptorByConfId(selectedConf.getId());
        String s = descriptorByConfId.toString();
        detailsBox.setText(formatWithWrapping(s,44));
    }

    /**
     * Builds a formatted string listing keyboard and mouse controls with aligned descriptions.
     *
     * @return the formatted controls string
     */
    private String getControlsText() {
        Controls controls = new Controls();
        StringBuilder sb = new StringBuilder();

        // Split keyboard controls into two columns
        List<Controls.Control> keyboardControls = controls.controls;
        int totalControls = keyboardControls.size();
        int midPoint = (totalControls + 1) / 2;

        List<Controls.Control> leftControls = keyboardControls.subList(0, midPoint);
        List<Controls.Control> rightControls = keyboardControls.subList(midPoint, totalControls);

        // Compute max lengths for alignment
        int maxKeyLenLeft = leftControls.stream()
                .mapToInt(c -> c.control().length())
                .max()
                .orElse(1);
        int maxDescLenLeft = leftControls.stream()
                .mapToInt(c -> c.desc().length())
                .max()
                .orElse(1);
        int maxKeyLenRight = rightControls.stream()
                .mapToInt(c -> c.control().length())
                .max()
                .orElse(1);

        int leftKeyWidth = maxKeyLenLeft + 2;
        int leftDescWidth = maxDescLenLeft + 4;
        int rightKeyWidth = maxKeyLenRight + 2;

        sb.append("Keyboard Controls\n\n");

        // Format two-column layout
        int maxRows = Math.max(leftControls.size(), rightControls.size());
        for (int i = 0; i < maxRows; i++) {
            // Left column
            if (i < leftControls.size()) {
                Controls.Control left = leftControls.get(i);
                sb.append(String.format("%-" + leftKeyWidth + "s", left.control()));
                sb.append(String.format("%-" + leftDescWidth + "s", left.desc()));
            } else {
                sb.append(String.format("%-" + (leftKeyWidth + leftDescWidth) + "s", ""));
            }

            // Right column
            if (i < rightControls.size()) {
                Controls.Control right = rightControls.get(i);
                sb.append(String.format("%-" + rightKeyWidth + "s", right.control()));
                sb.append(right.desc());
            }
            sb.append("\n");
        }

        sb.append("\nMouse Controls\n\n");

        // Mouse controls
        int maxMouseLen = controls.mouseControls.stream()
                .mapToInt(c -> c.control().length())
                .max()
                .orElse(1);
        for (Controls.Control mc : controls.mouseControls) {
            String label = String.format("%-" + maxMouseLen + "s", mc.control());
            sb.append(label).append("  ").append(mc.desc()).append("\n");
        }

        return sb.toString();
    }

    /**
     * Exits the application, cleaning up terminal resources.
     */
    private void exit() {
        try {
            if (screen != null) {
                screen.stopScreen(true);
                screen.close();
            }
        } catch (IOException e) {
            System.exit(1);
        }
        System.exit(0);
    }

    /**
     * Returns the list of configurations from the repository.
     *
     * @return the list of {@link Configuration} objects
     */
    public List<Configuration<C, S>> getConfigurations() {
        return repository.getConfigurations();
    }

    /**
     * Returns the index of the currently selected configuration.
     *
     * @return the selected configuration ID, or -1 if none selected
     */
    public int getSelectedConfId() {
        return selectedConfId;
    }

    /**
     * Returns the currently selected configuration, if any.
     *
     * @return the selected {@link Configuration}, or null if none selected
     */
    public Configuration<C, S> getSelectedConfiguration() {
        if (selectedConfId > -1 && selectedConfId < repository.getConfigurations().size()) {
            return repository.getConfigurations().get(selectedConfId);
        }
        return null;
    }

    /**
     * Formats a string with word-wrapping for narrow Lanterna widgets.
     *
     * @param text     the text to wrap
     * @param maxWidth the maximum line width
     * @return the wrapped text
     */
    private String formatWithWrapping(String text, int maxWidth) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (line.length() + word.length() + 1 <= maxWidth) {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            } else {
                result.append(line).append("\n");
                line = new StringBuilder(word);
            }
        }
        if (!line.isEmpty()) {
            result.append(line);
        }
        return result.toString();
    }

    private void exampleTableWindow(TerminalScreen screen) {
        final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        //
        textGUI.setTheme(LanternaThemes.getRegisteredTheme("blaster"));
        //        textGUI.setTheme(LanternaThemes.getRegisteredTheme("conqueror"));
        //        textGUI.setTheme(LanternaThemes.getRegisteredTheme("bigsnake"));
//                textGUI.setTheme(LanternaThemes.getRegisteredTheme("businessmachine"));
//                textGUI.setTheme(LanternaThemes.getRegisteredTheme("defrost"));
        //textGUI.setTheme(LanternaThemes.getRegisteredTheme("blaster"));
        final BasicWindow rootwindow = new BasicWindow();
        rootwindow.setFixedSize(new TerminalSize(width, height));
        rootwindow.setHints(List.of(NO_DECORATIONS, NO_POST_RENDERING, CENTERED, FULL_SCREEN, CENTERED));

        final BasicWindow tableWindow = new BasicWindow("Window 2");
        tableWindow.setHints(List.of(NO_POST_RENDERING, FIXED_POSITION, NO_DECORATIONS));
        tableWindow.setFixedSize(new TerminalSize(width,height));
        tableWindow.setPosition(TerminalPosition.TOP_LEFT_CORNER);

        Panel tablePanel = new Panel(new AbsoluteLayout());

        Table<String> table = new Table<>("Automaton", "");
        Border borderedtable = table.withBorder(Borders.singleLine());
        borderedtable.setPosition(new TerminalPosition(0,0));
        borderedtable.setSize(new TerminalSize(width,height));

        for( Configuration configuration:repository.getConfigurations()){
            Descriptor descriptorByConfId = repository.getDescriptorByConfId(configuration.getId());
            table.getTableModel().addRow( descriptorByConfId.getName(),descriptorByConfId.getDescription().substring(0,150));
        }

        TextBox textBox = new TextBox("");
        textBox.setTextChangeListener((newText, changedByUserInteraction) -> {
            table.setSelectedRow(table.getSelectedRow()+1);
            table.takeFocus();
        });
        textBox.setPosition(TerminalPosition.TOP_LEFT_CORNER);
        textBox.setSize(new TerminalSize(width,1));

        //tablePanel.addComponent(textBox);
        tablePanel.addComponent(borderedtable);

        tableWindow.setComponent(tablePanel);

        textGUI.addWindow(rootwindow);
        textGUI.addWindow(tableWindow);
        textGUI.setActiveWindow(tableWindow);
        rootwindow.waitUntilClosed();

    }
    private void exampleMultiWindow(TerminalScreen screen) {
        final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(LanternaThemes.getRegisteredTheme("blaster"));

        final BasicWindow window = new BasicWindow();
        window.setFixedSize(new TerminalSize(width, height));
        window.setHints(List.of(NO_DECORATIONS, NO_POST_RENDERING, CENTERED, FULL_SCREEN));

        final BasicWindow window2 = new BasicWindow("Window 2");
        window2.setHints(List.of(NO_POST_RENDERING, FIXED_POSITION));
        window2.setFixedSize(new TerminalSize(width / 4, height / 4));
        window2.setPosition(new TerminalPosition(50, 10));

        final BasicWindow window3 = new BasicWindow("Window 3");
        window3.setFixedSize(new TerminalSize(width / 3, height / 3));
        window3.setPosition(new TerminalPosition(50, 15));
        // window.setHints(List.of(NO_DECORATIONS,NO_POST_RENDERING,CENTERED,FULL_SCREEN));

        Panel rootPanel = new Panel(new AbsoluteLayout());
        //rootPanel.setFillColorOverride(TextColor.ANSI.BLUE);
        rootPanel.setPosition(new TerminalPosition(10, 10));
        rootPanel.setSize(new TerminalSize(width / 2, height / 2));
        rootPanel.setPreferredSize(new TerminalSize(width / 2, height / 2));
        Panel topPanel = new Panel();
        topPanel.setPosition(new TerminalPosition(0,0 ));
        topPanel.setSize(new TerminalSize(80, 1));
        Panel centerPanel = new Panel();

        centerPanel.setSize(new TerminalSize(width, height));
        centerPanel.setPosition(new TerminalPosition(10, 10));
        centerPanel.setPosition(new TerminalPosition(5, 5));
        centerPanel.setPreferredSize(new TerminalSize(width, height));
        centerPanel.setFillColorOverride(TextColor.ANSI.BLUE);
        MenuBar menubar = new MenuBar();

        menubar.setPosition(new TerminalPosition(5,0));
        menubar.setSize(new TerminalSize(40,1));
        // "File" menu
        Menu menuFile = new Menu("File");

        menuFile.setSize(new TerminalSize(40,1));
        menubar.add(menuFile);
        MenuItem openItem = new MenuItem("Open...", new Runnable() {
            public void run() {
                File file = new FileDialogBuilder().build().showDialog(textGUI);
                if (file != null)
                    MessageDialog.showMessageDialog(
                            textGUI, "Open", "Selected file:\n" + file, MessageDialogButton.OK);
            }
        });

        menuFile.add(openItem);

        MenuItem exitItem = new MenuItem("Exit", new Runnable() {
            public void run() {
                System.exit(0);
            }
        });
        menuFile.add(exitItem);

        topPanel.addComponent(menubar);
        rootPanel.addComponent(topPanel);
        Border centerPanel1 = centerPanel.withBorder(Borders.singleLine("Center Panel"));
        centerPanel1.setPosition(new TerminalPosition(5, 5));
        centerPanel1.setSize(new TerminalSize(30, 8));
        rootPanel.addComponent(centerPanel1);

        // === Configuration List ===
        RadioBoxList<String> configList = new RadioBoxList<>();
        configList.setPreferredSize(new TerminalSize(30, 15));
        for (Configuration<C, S> config : getConfigurations()) {
            configList.addItem(config.getClass().getName());
        }

        configList.addListener(new RadioBoxList.Listener() {
            @Override
            public void onSelectionChanged(int selectedIndex, int previousSelection) {
                confirmExit(textGUI);
            }
        });
        // === Details Text Area ===
        TextBox detailsBox = new TextBox(new TerminalSize(40, 15)).setReadOnly(true);

        // Listener for selection change: updates detail text box
        // start simulation if selection is the same
        configList.addListener((selectedIndex, prev) -> {
            if (selectedConfId == selectedIndex) {
                close();
            }
            Configuration<C, S> selectedConf = getConfigurations().get(selectedIndex);
            selectedConfId = selectedIndex;

            setDetailsBoxText(selectedConf, detailsBox);
        });


        centerPanel.addComponent(configList.withBorder(Borders.singleLine("Available Configurations")));
        centerPanel.addComponent(detailsBox.withBorder(Borders.singleLine("Configuration Details")));

        window.setComponent(rootPanel);

        menubar.getMenu(0).setInputFilter(new InputFilter() {
            @Override
            public boolean onInput(Interactable interactable, KeyStroke keyStroke) {
                if(keyStroke.getKeyType()==KeyType.F1) {
                    interactable.takeFocus();
                    return true;
                }
                return true;
            }
        });

        // ESC key to confirm exit
        textGUI.addListener((gui, key) -> {
            if (key.getKeyType() == KeyType.Escape) {
                confirmExit(textGUI);
            }
            menubar.getMenu(0).handleInput(key);
            return false;
        });

        // Launch the GUI
        textGUI.addWindowAndWait(window);
        //        textGUI.addWindow(window2);
        //        textGUI.addWindowAndWait(window3);

    }

}