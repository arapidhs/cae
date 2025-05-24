package com.dungeoncode.cae.view;

import com.dungeoncode.cae.automa.*;
import com.dungeoncode.cae.core.Cell;
import com.dungeoncode.cae.core.CellState;
import com.dungeoncode.cae.core.Configuration;
import com.dungeoncode.cae.core.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Main entry point for the Cellular Automata application. Initializes the terminal environment,
 * manages configuration selection, and launches simulations. Repeatedly displays the configuration
 * selection screen until the application terminates.
 */
public class ViewMain {

    /** Logger for application events and errors. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewMain.class);

    /** The view for selecting automaton configurations. */
    private static View view;

    /** Flag indicating whether the application is running. */
    static boolean running;

    /**
     * Starts the Cellular Automata application. Initializes the configuration repository,
     * creates a selection view, and enters a loop to display the selection screen and launch
     * simulations until termination.
     *
     * @param args command-line arguments (not used)
     * @throws RuntimeException if initialization or execution fails
     */
    @SuppressWarnings("rawtypes")
    public static void main(String[] args) {
        try {
            Repository repository = new Repository<>();
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
            repository.addConfiguration(new ConfParityFlip());
            repository.addConfiguration(new ConfTimeTunnel());
            repository.addConfiguration(new ConfCandleRain());
            repository.addConfiguration(new ConfRandomAnneal());
            repository.addConfiguration(new ConfHistogram());
            repository.addConfiguration(new ConfNaiveDiffusion());
            repository.addConfiguration(new ConfHandshakeDiffusion());
            repository.addConfiguration(new ConfGeneticDrift());
            repository.addConfiguration(new ConfSoilErosion());

            running = true;
            view = new View(repository);
            while (running) {
                setup(repository);
            }
        } catch (Exception e) {
            LOGGER.error("Application error: {}", e.getMessage(), e);
            System.exit(1);
        }
    }

    /**
     * Displays the configuration selection view and launches the simulation for the selected
     * configuration. Exits the application if no configuration is chosen.
     *
     * @param repository the {@link Repository} of configurations, must not be null
     * @throws NullPointerException if repository is null
     */
    @SuppressWarnings("rawtypes")
    private static void setup(@Nonnull Repository repository) {
        Objects.requireNonNull(repository, "Repository cannot be null");
        view.setup();
        if (view.getSelectedConfId() > -1) {
            Configuration conf = view.getSelectedConfiguration();
            ViewEngine<Cell<CellState<?>>, CellState<?>> viewEngine =
                    new ViewEngine<>(1080, 720, 4, repository.getConfigurations(), conf);
            viewEngine.run();
        } else {
            System.exit(0);
        }
    }
}