package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Configuration;
import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.bundle.LanternaThemes;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Terminal-based user interface for selecting a cellular automata configuration before simulation starts.
 * This class builds and renders the interactive selection window using Lanterna's WindowBasedTextGUI.
 *
 * @param <C> Cell type
 * @param <S> Cell state type
 */
public class SelectView<C extends Cell<S>, S extends CellState<?>> {

    private final int width = 80;
    private final int height = 25;
    private final int fontSize = 20;

    private final List<Configuration<C, S>> configurations;
    private int selected;
    private TerminalScreen screen;

    /**
     * Constructs the selection UI with a list of available configurations.
     *
     * @param configurations the list of available automata configurations
     */
    public SelectView(List<Configuration<C, S>> configurations) {
        this.configurations = configurations;
        this.selected = -1;
    }

    /**
     * Initializes the terminal screen, sets up the Lanterna GUI system,
     * and invokes the configuration selection window.
     */
    public void setup() {
        try {
            this.selected = -1;

            // Configure terminal settings
            final DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory(System.out, System.in, StandardCharsets.UTF_8);
            terminalFactory.setTerminalEmulatorTitle("Cellular Automata");
            terminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
            terminalFactory.setTerminalEmulatorFontConfiguration(
                    SwingTerminalFontConfiguration.newInstance(new Font("Courier New", Font.PLAIN, fontSize)));

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

        // === Root Layout ===
        Panel rootPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        rootPanel.setPreferredSize(new TerminalSize(76, 21));

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
        topPanel.addComponent(new Label("Select an automaton and press Start to begin the simulation.")
                .setLayoutData(LinearLayout.createLayoutData(LinearLayout.Alignment.Beginning)));

        // === Configuration List ===
        RadioBoxList<String> configList = new RadioBoxList<>();
        configList.setPreferredSize(new TerminalSize(30, 15));
        for (Configuration<C, S> config : configurations) {
            configList.addItem(config.getName());
        }

        // === Details Text Area ===
        TextBox detailsBox = new TextBox(new TerminalSize(40, 15)).setReadOnly(true);

        // Listener for selection change: updates detail text box
        // start simulation if selection is the same
        configList.addListener((selectedIndex, prev) -> {
            if (selected == selectedIndex) {
                close();
            }
            Configuration<C, S> selectedConf = configurations.get(selectedIndex);
            selected = selectedIndex;

            String name = selectedConf.getName();
            String description = selectedConf.getDescription();
            String citation = selectedConf.getCitation() == null ? "" : selectedConf.getCitation();

            String formatted = formatWithWrapping(name, 38) + "\n\n"
                    + formatWithWrapping(description, 38) + "\n\n"
                    + formatWithWrapping(citation, 38);

            detailsBox.setText(formatted);
        });

        centerPanel.addComponent(configList.withBorder(Borders.singleLine("Available Configurations")));
        centerPanel.addComponent(detailsBox.withBorder(Borders.singleLine("Configuration Details")));

        // === Buttons ===
        buttonsPanel.addComponent(new Button("Start", () -> {
            if (selected >= 0) {
                this.close(); // Close the GUI
            } else {
                MessageDialog.showMessageDialog(textGUI, "", "Select a configuration first!", MessageDialogButton.OK);
            }
        }));

        buttonsPanel.addComponent(new Button("Show Controls", () ->
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

    /**
     * Builds a formatted string listing all keyboard and mouse controls with aligned descriptions.
     *
     * @return formatted controls string
     */
    private String getControlsText() {
        Controls controls = new Controls();
        StringBuilder sb = new StringBuilder();

        // Compute max label width for keyboard controls
        int maxKeyLen = controls.controls.stream()
                .mapToInt(c -> String.valueOf(c.control()).length())
                .max()
                .orElse(1);

        sb.append("Keyboard Controls\n\n");
        for (Controls.Control c : controls.controls) {
            String label = String.format("%-" + maxKeyLen + "s", c.control());
            sb.append(label).append("  ").append(c.desc()).append("\n");
        }

        sb.append("\nMouse Controls\n\n");

        // Compute max label width for mouse controls
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

    public Configuration<C, S> getSelectedConfiguration() {
        if (getSelected() > -1) {
            return getConfigurations().get(getSelected());
        } else {
            return null;
        }
    }

    public int getSelected() {
        return selected;
    }

    public List<Configuration<C, S>> getConfigurations() {
        return configurations;
    }
}
