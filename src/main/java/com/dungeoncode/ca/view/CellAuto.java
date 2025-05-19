package com.dungeoncode.ca.view;

import com.dungeoncode.ca.automa.*;
import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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
            configurations.add(new InkspotConfiguration());
            configurations.add(new GameOfLifeConfiguration());
            configurations.add(new GameOfLifeWithEchoConfiguration());
            configurations.add(new GameOfLifeWithTracingConfiguration());
            configurations.add(new HGlassConfiguration());
            configurations.add(new ParityConfiguration());
            configurations.add(new SquaresConfiguration());
            configurations.add(new DiamondsConfiguration());
            configurations.add(new TrianglesConfiguration());
            configurations.add(new OneOutOfEightConfiguration());
            configurations.add(new LichensConfiguration());
            configurations.add(new LichensWithDeathConfiguration());
            configurations.add(new MajorityConfiguration());
            configurations.add(new AnnealConfiguration());
            configurations.add(new BanksConfiguration());
            configurations.add(new BriansBrainConfiguration());
            configurations.add(new GreenbergConfiguration());
            configurations.add(new ParityFlipConfiguration());
            configurations.add(new TimeTunnelConfiguration());
            configurations.add(new CandleRainConfiguration());
            configurations.add(new PulseWeaverConfiguration());

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