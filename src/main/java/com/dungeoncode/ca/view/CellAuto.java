package com.dungeoncode.ca.view;

import com.dungeoncode.ca.automa.*;
import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Main entry point for the Cellular Automata application. Initializes the terminal environment,
 * manages configuration selection, and launches the simulation view. Uses a loop to repeatedly
 * display the configuration selection screen until the application is terminated.
 */
@SuppressWarnings("rawtypes")
public class CellAuto {

    /**
     * Logger for recording application events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CellAuto.class);

    /**
     * The view for selecting automaton configurations.
     */
    static AutomaView automaView;

    /**
     * Flag indicating whether the application is running.
     */
    static boolean running;

    static List<Configuration> configurations;

    /**
     * Starts the Cellular Automata application. Initializes a list of available configurations,
     * creates a selection view, and enters a loop to display the selection screen and launch
     * simulations until the application is terminated.
     *
     * @param args command-line arguments (not used)
     * @throws RuntimeException if an error occurs during initialization or execution
     */
    public static void main(String[] args) {
        try {

            // Initialize available configurations
            configurations = new ArrayList<>();
            configurations.add(new ConfInkspot());
            configurations.add(new ConfGameOfLife());
            configurations.add(new ConfHglass());
            configurations.add(new ConfParity());
            configurations.add(new ConfSquares());
            configurations.add(new ConfDiamonds());
            configurations.add(new ConfTriangles());
            configurations.add(new ConfOneOutOfEight());
            configurations.add(new ConfLichens());
            configurations.add(new ConfLichensWithDeath());
            configurations.add(new ConfMajority());
            configurations.add(new ConfVichniacAnneal());
            configurations.add(new ConfBanks());
            configurations.add(new ConfBriansBrain());
            configurations.add(new ConfGreenberg());
            configurations.add(new ConfParityFlip());
            configurations.add(new ConfTimeTunnel());
            configurations.add(new ConfCandleRain());
            configurations.add(new ConfRandomAnneal());
            configurations.add(new ConfHistogram());
            configurations.add(new ConfTubeWorms());
            configurations.add(new ConfNaiveDiffusion());
            configurations.add(new ConfHandshakeDiffusion());

            // Sort the list by configuration name
            configurations.sort(Comparator.comparing((Configuration c) -> c.getPage())
                    .thenComparing(Configuration::getName));

            running = true; // Set application running state
            automaView = new AutomaView(configurations);
            while (running) {
                setup(); // Run configuration selection and simulation
            }
        } catch (Exception e) {
            LOGGER.error("Application error: {}", e.getMessage(), e);
            System.exit(1); // Exit with error status
        }
    }

    /**
     * Sets up the configuration selection view and launches the simulation if a configuration
     * is selected. Exits the application if no configuration is chosen.
     */
    private static void setup() {
        automaView.setup(); // Display configuration selection view
        if (automaView.getSelected() > -1) {
            // Launch simulation with selected configuration
            Configuration conf = automaView.getSelectedConfiguration();
            AutomaController<Cell<CellState<?>>, CellState<?>> automaController =
                    new AutomaController<>(1080, 720, 4, configurations, conf);
            automaController.run();
        } else {
            System.exit(0); // Exit if no configuration is selected
        }
    }
}