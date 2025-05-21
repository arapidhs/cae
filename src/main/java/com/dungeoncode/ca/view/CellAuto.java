package com.dungeoncode.ca.view;

import com.dungeoncode.ca.automa.*;
import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Configuration;
import com.dungeoncode.ca.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

            Repository repository = new Repository<>();
            // Initialize available configurations
            // configurations = new ArrayList<>();
            repository.addConfiguration(new ConfInkspot());
            repository.addConfiguration(new ConfGameOfLife());
            repository.addConfiguration(new ConfHglass());
            repository.addConfiguration(new ConfParity());
            repository.addConfiguration(new ConfSquares());
            repository.addConfiguration(new ConfDiamonds());
            repository.addConfiguration(new ConfTriangles());
            repository.addConfiguration(new ConfOneOutOfEight());
            repository.addConfiguration(new ConfLichens());
            repository.addConfiguration(new ConfLichensWithDeath());
            repository.addConfiguration(new ConfMajority());
            repository.addConfiguration(new ConfVichniacAnneal());
            repository.addConfiguration(new ConfBanks());
            repository.addConfiguration(new ConfBriansBrain());
            repository.addConfiguration(new ConfGreenberg());
            repository.addConfiguration(new ConfParityFlip());
            repository.addConfiguration(new ConfTimeTunnel());
            repository.addConfiguration(new ConfCandleRain());
            repository.addConfiguration(new ConfRandomAnneal());
            repository.addConfiguration(new ConfHistogram());
            repository.addConfiguration(new ConfNaiveDiffusion());
            repository.addConfiguration(new ConfHandshakeDiffusion());
            repository.addConfiguration(new ConfGeneticDrift());

            running = true; // Set application running state
            automaView = new AutomaView(repository);
            while (running) {
                setup(repository); // Run configuration selection and simulation
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
    private static void setup(Repository repository) {
        automaView.setup(); // Display configuration selection view
        if (automaView.getSelectedConfId() > -1) {
            // Launch simulation with selected configuration
            Configuration conf = automaView.getSelectedConfiguration();
            AutomaController<Cell<CellState<?>>, CellState<?>> automaController =
                    new AutomaController<>(960, 960, 4, repository.getConfigurations(), conf);
            automaController.run();
        } else {
            System.exit(0); // Exit if no configuration is selected
        }
    }

}