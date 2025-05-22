package com.dungeoncode.cae.view.render;

import com.dungeoncode.cae.core.Cell;
import com.dungeoncode.cae.core.CellState;
import com.dungeoncode.cae.core.Grid;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * A consumer that renders a {@link Grid} of cells to a Lanterna terminal screen.
 * Uses a {@link StateRenderer} to convert cell states into {@link com.googlecode.lanterna.TextCharacter} objects for display.
 *
 * @param <C> the type of cells in the grid, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class GridRenderer<C extends Cell<S>, S extends CellState<?>> implements Consumer<Grid<C, S>> {
    /**
     * Logger for recording rendering events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GridRenderer.class);

    /**
     * The Lanterna screen used for rendering the grid.
     */
    protected Screen screen;

    /**
     * The text graphics context for drawing characters on the screen.
     */
    protected TextGraphics textGraphics;

    /**
     * The renderer for converting cell states to display characters.
     */
    protected StateRenderer<S> stateRenderer;

    /**
     * Constructs a new grid consumer with the specified screen and state renderer.
     *
     * @param screen        the {@link Screen} to render the grid on
     * @param stateRenderer the {@link StateRenderer} to convert cell states to display characters
     */
    public GridRenderer(Screen screen, StateRenderer<S> stateRenderer) {
        this.screen = screen;
        this.stateRenderer = stateRenderer;
        this.textGraphics = screen.newTextGraphics();
    }

    /**
     * Renders the specified grid to the Lanterna screen.
     * Clears the screen, draws each cell's state using the state renderer, and refreshes the display.
     *
     * @param grid the {@link Grid} to render
     * @throws IllegalStateException if the screen is not initialized
     * @throws RuntimeException      if an I/O error occurs during rendering
     */
    @Override
    public void accept(Grid<C, S> grid) {
        try {
            for (int y = 0; y < grid.getHeight(); y++) {
                for (int x = 0; x < grid.getWidth(); x++) {
                    C cell = grid.getCell(x, y);
                    textGraphics.setCharacter(x, y, stateRenderer.render(cell.getState()));
                }
            }
            screen.refresh(Screen.RefreshType.DELTA);
        } catch (IOException e) {
            LOGGER.error("Failed to render grid: {}", e.getMessage(), e);
            throw new RuntimeException(String.format("Failed to render grid: %s", e.getMessage()), e);
        }
    }

    /**
     * Returns the state renderer responsible for provisioning the rendered character.
     *
     * @return the State renderer.
     */
    public StateRenderer<S> getStateRenderer() {
        return stateRenderer;
    }

}