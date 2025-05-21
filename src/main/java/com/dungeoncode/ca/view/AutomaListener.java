package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.Automa;
import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.view.render.CellCharacter;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Random;

/**
 * A mouse listener for handling user interactions with a cellular automaton grid in a terminal-based view.
 * Responds to mouse clicks, drags, and wheel movements to modify cell states in the automaton's grid.
 * Supports left-click for random state changes, middle-click to activate cells, right-click to deactivate cells,
 * and mouse wheel to adjust the radius of the affected area.
 *
 * @param <C> the type of cells in the automaton, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class AutomaListener<C extends Cell<S>, S extends CellState<?>> extends MouseAdapter {

    /**
     * Logger for recording mouse interaction events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AutomaListener.class);

    /**
     * The control view managing the automaton and terminal display.
     */
    private final AutomaController<C, S> automaController;

    /**
     * The mouse button currently pressed (1 = left, 2 = middle, 3 = right).
     */
    private int button;

    private int radius;
    private long lastScrollTime;

    /**
     * Constructs a new mouse listener for the specified control view.
     *
     * @param automaController the control view managing the automaton and terminal display
     */
    public AutomaListener(AutomaController<C, S> automaController) {
        this.automaController = automaController;

        int width = automaController.getWidth();
        int height = automaController.getHeight();
        radius = (int) (Math.min(width, height) * 0.1);
    }

    /**
     * Handles mouse click events by applying cell state changes at the clicked position.
     * Converts pixel coordinates to grid coordinates and updates cells based on the mouse button.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Convert pixel coordinates to terminal grid coordinates
        int col = e.getX() / automaController.getCellFontSize();
        int row = e.getY() / automaController.getCellFontSize();
        int button = e.getButton();

        applyChanges(col, row, button); // Apply changes to the grid
    }

    /**
     * Captures the mouse button pressed during a mouse event.
     *
     * @param e the mouse event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        button = e.getButton(); // Store the pressed button
    }

    /**
     * Handles mouse wheel movements to adjust the radius of the affected grid area for mouse interactions.
     * Increases the radius for upward scrolls and decreases it for downward scrolls, updating the radius
     * and drawing a rectangular outline around the cursor position if the automaton is running.
     *
     * @param e the mouse wheel event
     * @throws RuntimeException if an error occurs while drawing the radius outline
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        if (lastScrollTime == 0) {
            lastScrollTime = e.getWhen();
            return;
        }
        long now = e.getWhen();
        if (now - lastScrollTime < 20) {
            lastScrollTime = now;
            return;
        } else {
            lastScrollTime = now;
        }

        int dr;
        if (e.getWheelRotation() == -1) {
            dr = 3; // Increase radius for upward scroll
        } else {
            dr = -3; // Decrease radius for downward scroll
        }
        updateRadius(dr);
        try {
            Automa<C, S> automa = automaController.getAutoma();
            boolean wasRunning = automa.isRunning();
            if (automa.isRunning()) {
                automa.stop();
            }
            // Convert pixel coordinates to terminal grid coordinates
            int col = e.getX() / automaController.getCellFontSize();
            int row = e.getY() / automaController.getCellFontSize();

            automaController.getTextGraphics().drawRectangle(
                    new TerminalPosition(col - radius, row - radius),
                    new TerminalSize(2 * radius, 2 * radius),
                    CellCharacter.fromColor(TextColor.ANSI.WHITE));
            automaController.getScreen().refresh(Screen.RefreshType.DELTA);

            if (wasRunning) {
                automa.start();
            } else {
                automaController.getRenderer().accept(automaController.getAutoma().getGrid());
            }


        } catch (Exception ex) {
            throw new RuntimeException("Failed to draw radius outline: " + ex.getMessage(), ex);
        }

    }

    private void updateRadius(int dr) {
        int width = automaController.getWidth();
        int height = automaController.getHeight();
        radius += dr;
        if (dr > 0) {
            radius = Math.min(radius, (width + height) / 8);
        } else {
            radius = Math.max(radius, 0);
        }
    }

    /**
     * Handles mouse drag events by applying cell state changes at the dragged position.
     * Converts pixel coordinates to grid coordinates and updates cells based on the mouse button.
     *
     * @param e the mouse event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // Convert pixel coordinates to terminal grid coordinates
        int col = e.getX() / automaController.getCellFontSize();
        int row = e.getY() / automaController.getCellFontSize();

        applyChanges(col, row, button); // Apply changes to the grid
    }

    /**
     * Applies cell state changes to a circular area around the specified grid coordinates.
     * Uses the mouse button to determine the state change: left-click sets random states with
     * probability based on distance, middle-click activates cells, and right-click deactivates cells.
     * Updates the grid display if the automaton is not running.
     *
     * @param col    the column coordinate in the grid
     * @param row    the row coordinate in the grid
     * @param button the mouse button (1 = left, 2 = middle, 3 = right)
     */
    private void applyChanges(int col, int row, int button) {

        // If the grid is not a grid of Boolean Cells, return.
        C cl = automaController.getAutoma().getGrid().getCell(0, 0);
        if (!(cl instanceof BooleanCell)) {
            return;
        }

        // Calculate radius based on grid size and multiplier
        int width = automaController.getWidth();
        int height = automaController.getHeight();

        if (radius > 0) {
            // Iterate over a square area around the center
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance <= radius) { // Only affect cells within circular radius
                        // Wrap coordinates to handle grid edges
                        int nx = (col + dx + width) % width;
                        int ny = (row + dy + height) % height;
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            updateCell(button, nx, ny, distance);
                        }
                    }
                }
            }
        } else {
            updateCell(button, col, row, 0);
        }

        // Update display if automaton is paused
        if (!automaController.getAutoma().isRunning()) {
            automaController.getRenderer().accept(automaController.getAutoma().getGrid());
        }
    }

    private void updateCell(int button, int nx, int ny, double distance) {
        BooleanCell cell = (BooleanCell) automaController.getAutoma().getGrid().getCell(nx, ny);
        if (cell != null) {
            try {
                if (button == 1) {
                    // Left button: set random state with distance-based probability
                    Random rnd = new Random();
                    double prob = Math.exp(-distance / radius); // Gaussian-like decay
                    boolean b = rnd.nextDouble() < prob;
                    cell.setState(b, false, 0);
                } else {
                    // Middle button activates, right button deactivates
                    cell.setState(button == 2, false, 0);
                }
            } catch (Exception ex) {
                LOGGER.error("Error updating cell at ({}, {}): {}", nx, ny, ex.getMessage());
            }
        }
    }
}