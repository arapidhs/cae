package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Configuration;
import com.dungeoncode.ca.core.Repository;
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
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.googlecode.lanterna.gui2.Window.Hint.*;

/**
 * Terminal-based user interface for selecting a cellular automata configuration before simulation starts.
 * This class builds and renders the interactive selection window using Lanterna's WindowBasedTextGUI.
 *
 * @param <C> Cell type
 * @param <S> Cell state type
 */
public class AutomaView<C extends Cell<S>, S extends CellState<?>> {

    private final int width = 140;
    private final int height = 40;
    private final int fontSize = 24;

    private final Repository<C, S> repository;
    private int selectedConfId;
    private TerminalScreen screen;

    public AutomaView(Repository<C, S> repository) {
        this.repository = repository;
        this.selectedConfId = -1;
    }

    /**
     * Initializes the terminal screen, sets up the Lanterna GUI system,
     * and invokes the configuration selection window.
     */
    public void setup() {
        try {

            Font font;
            try {
                final InputStream is = AutomaController.class.getResourceAsStream(
                        "/fonts/ibm/Ac437_IBM_VGA_9x14.ttf"
                );
                assert is != null;
                font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, fontSize);
                is.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to load fonts: " + e.getMessage(), e);
            }

            // Configure terminal settings
            final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
            terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
            terminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
            terminalFactory.setTerminalEmulatorFontConfiguration(
                    SwingTerminalFontConfiguration.newInstance(font)
            );

            screen = terminalFactory.createScreen();
            Terminal terminal = screen.getTerminal();

            if (terminal instanceof SwingTerminalFrame swingTerminalFrame) {
                swingTerminalFrame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
                swingTerminalFrame.setResizable(false);
                swingTerminalFrame.setLocationRelativeTo(null);
            }

            screen.setCursorPosition(null);
            screen.startScreen();

            window(screen);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Builds and shows the main configuration selection window.
     * Allows the user to browse configurations, view details, and launch simulation.
     */
    private void window(TerminalScreen screen) {
        final WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        textGUI.setTheme(LanternaThemes.getRegisteredTheme("conqueror"));

        // ESC key to confirm exit
        textGUI.addListener((gui, key) -> {
            if (key.getKeyType() == KeyType.Escape) {
                confirmExit(textGUI);
            }
            return false;
        });

        final BasicWindow window = new BasicWindow("Cellular Automata Simulator");
        window.setHints(List.of(FULL_SCREEN, FIT_TERMINAL_WINDOW));

        // === Root Layout ===
        Panel rootPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rootPanel.setFillColorOverride(TextColor.ANSI.BLUE);
        rootPanel.setPreferredSize(new TerminalSize(80, 40));

        // === Panels ===
        Panel topPanel = new Panel(new LinearLayout(Direction.VERTICAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
        Panel mainPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Panel centerPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Panel buttonsPanel = new Panel(new LinearLayout(Direction.HORIZONTAL))
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

        // === Header ===
        topPanel.addComponent(new EmptySpace());
        topPanel.addComponent(new Label("Terminal-based Cellular Automata Simulator").addStyle(SGR.BOLD));
        topPanel.addComponent(new Label("Explore, configure, and run dynamic automata systems").addStyle(SGR.ITALIC));
        topPanel.addComponent(new Label("in a fully interactive text interface.").addStyle(SGR.ITALIC));
        topPanel.addComponent(new EmptySpace());
        topPanel.addComponent(new Label("Select an automaton and press Start to begin the simulation.").addStyle(SGR.ITALIC)
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning)));

        // === Configuration List ===
        RadioBoxList<String> configList = new RadioBoxList<>();
        configList.setPreferredSize(new TerminalSize(30, 35));

        for (Configuration<C, S> config : repository.getConfigurations()) {
            configList.addItem(config.getClass().getSimpleName());
        }

        // === Details Text Area ===
        TextBox detailsBox = new TextBox(new TerminalSize(45, 35)).setReadOnly(true);

        // Listener for selection change: updates detail text box
        // start simulation if selection is the same
        configList.addListener((selectedIndex, prev) -> {
            if (selectedConfId == selectedIndex) {
                close();
            }
            Configuration<C, S> selectedConf = repository.getConfigurations().get(selectedIndex);
            selectedConfId = selectedIndex;
            setDetailsBoxText(selectedConf, detailsBox);
        });

        if (selectedConfId > -1) {
            Configuration<C, S> selectedConf = repository.getConfigurations().get(selectedConfId);
            configList.setSelectedIndex(selectedConfId);
            setDetailsBoxText(selectedConf, detailsBox);
        }

        centerPanel.addComponent(configList.withBorder(Borders.singleLine("Available Configurations")));
        centerPanel.addComponent(detailsBox.withBorder(Borders.singleLine("Configuration Details")));

        // === Buttons ===
        buttonsPanel.addComponent(new Button("Start", () -> {
            if (selectedConfId >= 0) {
                this.close(); // Close the GUI
            } else {
                MessageDialog.showMessageDialog(textGUI, "", "Select a configuration first!", MessageDialogButton.OK);
            }
        }));

        buttonsPanel.addComponent(new Button("Controls", () ->
                MessageDialog.showMessageDialog(textGUI, "Controls", getControlsText(), MessageDialogButton.OK)));

        buttonsPanel.addComponent(new Button("Exit", () -> confirmExit(textGUI)));

        // === Layout Assembly ===
        mainPanel.addComponent(topPanel);
        mainPanel.addComponent(centerPanel);
        mainPanel.addComponent(buttonsPanel);
        rootPanel.addComponent(mainPanel);
        window.setComponent(rootPanel);

        // Launch the GUI
        textGUI.addWindowAndWait(window);
    }

    /**
     * Shows confirmation dialog before exiting.
     */
    private void confirmExit(WindowBasedTextGUI textGUI) {
        MessageDialogButton result = MessageDialog.showMessageDialog(
                textGUI, "", "Exit?", MessageDialogButton.OK, MessageDialogButton.Cancel);
        if (result == MessageDialogButton.OK) {
            exit();
        }
    }

    /**
     * Closes the screen cleanly.
     */
    private void close() {
        try {
            screen.stopScreen(true);
            screen.close();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private void setDetailsBoxText(Configuration<C, S> selectedConf, TextBox detailsBox) {
        String name = selectedConf.getClass().getName();
//        String description = selectedConf.getDescription();
//        String citation = selectedConf.getCitation() == null ? "" : selectedConf.getCitation();

//        String formatted = formatWithWrapping(name, 38) + "\n\n"
//                + formatWithWrapping(description, 38) + "\n\n"
//                + formatWithWrapping(citation, 38);

        detailsBox.setText(name);
    }

    /**
     * Builds a formatted string listing all keyboard and mouse controls with aligned descriptions.
     *
     * @return formatted controls string
     */
    private String getControlsText() {
        Controls controls = new Controls();
        StringBuilder sb = new StringBuilder();

        // Split keyboard controls into two halves
        List<Controls.Control> keyboardControls = controls.controls;
        int totalControls = keyboardControls.size();
        int midPoint = (totalControls + 1) / 2;

        List<Controls.Control> leftControls = keyboardControls.subList(0, midPoint);
        List<Controls.Control> rightControls = keyboardControls.subList(midPoint, totalControls);

        // Compute max key length for each column
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

        // Define widths for formatting the two-column layout
        int leftKeyWidth = maxKeyLenLeft + 2; // Key + padding
        int leftDescWidth = maxDescLenLeft + 4; // Description + padding for separation
        int rightKeyWidth = maxKeyLenRight + 2; // Key + padding

        sb.append("Keyboard Controls\n\n");

        // Iterate through rows, displaying left and right controls side by side
        int maxRows = Math.max(leftControls.size(), rightControls.size());
        for (int i = 0; i < maxRows; i++) {
            // Left column
            if (i < leftControls.size()) {
                Controls.Control left = leftControls.get(i);
                sb.append(String.format("%-" + leftKeyWidth + "s", left.control()));
                sb.append(String.format("%-" + leftDescWidth + "s", left.desc()));
            } else {
                // Empty left column
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
     * Fully exits the application and cleans up terminal state.
     */
    private void exit() {
        try {
            screen.stopScreen(true);
            screen.close();
            CellAuto.running = false;
        } catch (IOException e) {
            System.exit(1);
        }
        System.exit(0);
    }

    /**
     * Word-wraps text manually for use in narrow Lanterna widgets.
     */
    private String formatWithWrapping(String text, int maxWidth) {
        if (text == null || text.isEmpty()) return "";

        StringBuilder result = new StringBuilder();
        String[] words = text.split("\\s+");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() + 1 <= maxWidth) {
                if (!line.isEmpty()) line.append(" ");
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

    public Configuration<C, S> getSelectedConfiguration() {
        if (getSelectedConfId() > -1) {
            return getConfigurations().get(getSelectedConfId());
        } else {
            return null;
        }
    }

    public int getSelectedConfId() {
        return selectedConfId;
    }

    public List<Configuration<C, S>> getConfigurations() {
        return repository.getConfigurations();
    }

    private void exampleMultiWindow(TerminalScreen screen) {
        final MultiWindowTextGUI textGUI = new MultiWindowTextGUI(screen);
        // textGUI.setTheme(LanternaThemes.getRegisteredTheme("conqueror"));

        // ESC key to confirm exit
        textGUI.addListener((gui, key) -> {
            if (key.getKeyType() == KeyType.Escape) {
                confirmExit(textGUI);
            }
            return false;
        });

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
        rootPanel.setFillColorOverride(TextColor.ANSI.BLUE);
        rootPanel.setPosition(new TerminalPosition(10, 10));
        rootPanel.setSize(new TerminalSize(width / 2, height / 2));
        rootPanel.setPreferredSize(new TerminalSize(width / 2, height / 2));
        Panel topPanel = new Panel();
        topPanel.setPosition(new TerminalPosition(2, 2));
        topPanel.setSize(new TerminalSize(35, 3));
        topPanel.setFillColorOverride(TextColor.ANSI.MAGENTA);
        Panel centerPanel = new Panel();

        centerPanel.setSize(new TerminalSize(width, height));
        centerPanel.setPosition(new TerminalPosition(10, 10));
        centerPanel.setPosition(new TerminalPosition(5, 5));
        centerPanel.setPreferredSize(new TerminalSize(width, height));
        centerPanel.setFillColorOverride(TextColor.ANSI.BLUE);
        MenuBar menubar = new MenuBar();

        // "File" menu
        Menu menuFile = new Menu("File");
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

        // Launch the GUI
        textGUI.addWindow(window);
        textGUI.addWindow(window2);
        textGUI.addWindowAndWait(window3);

    }

}
