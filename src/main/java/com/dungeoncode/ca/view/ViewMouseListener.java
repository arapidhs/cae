package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.Cell;
import com.dungeoncode.ca.core.CellState;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
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
public class ViewMouseListener<C extends Cell<S>, S extends CellState<?>> extends MouseAdapter {

    /**
     * Logger for recording mouse interaction events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ViewMouseListener.class);

    /**
     * The control view managing the automaton and terminal display.
     */
    private final ControlView<C, S> controlView;

    /**
     * The mouse button currently pressed (1 = left, 2 = middle, 3 = right).
     */
    private int button;

    /**
     * Multiplier for calculating the radius of the affected grid area, adjusted by mouse wheel.
     */
    private double radiusMultiplier;

    private int radius;

    /**
     * Constructs a new mouse listener for the specified control view.
     *
     * @param controlView the control view managing the automaton and terminal display
     */
    public ViewMouseListener(ControlView<C, S> controlView) {
        this.controlView = controlView;
        this.radiusMultiplier = 0.05; // Initial radius multiplier
        calculateRadius();
    }

    private void calculateRadius() {
        // Calculate radius based on grid size and multiplier
        int width = controlView.getWidth();
        int height = controlView.getHeight();
        radius = (int) Math.max(3, Math.min(width, height) * radiusMultiplier);
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
        int col = e.getX() / controlView.getCellFontSize();
        int row = e.getY() / controlView.getCellFontSize();
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
        if (e.getWheelRotation() == -1) {
            radiusMultiplier += 0.01; // Increase radius for upward scroll
        } else {
            radiusMultiplier -= 0.01; // Decrease radius for downward scroll
        }
        calculateRadius();

        if (controlView.getAutoma().isRunning()) {
            try {
                // Convert pixel coordinates to terminal grid coordinates
                int col = e.getX() / controlView.getCellFontSize();
                int row = e.getY() / controlView.getCellFontSize();

                controlView.getTextGraphics().drawRectangle(
                        new TerminalPosition(col - radius, row - radius),
                        new TerminalSize(2 * radius, 2 * radius),
                        TextCharacter.fromCharacter('-', TextColor.ANSI.WHITE, null)[0]
                );
                controlView.getScreen().refresh(Screen.RefreshType.DELTA);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to draw radius outline: " + ex.getMessage(), ex);
            }
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
        int col = e.getX() / controlView.getCellFontSize();
        int row = e.getY() / controlView.getCellFontSize();

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
        C cl = controlView.getAutoma().getGrid().getCell(0, 0);
        if(!(cl instanceof  BooleanCell)){
            return;
        }

        // Calculate radius based on grid size and multiplier
        int width = controlView.getWidth();
        int height = controlView.getHeight();

        // Iterate over a square area around the center
        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= radius) { // Only affect cells within circular radius
                    // Wrap coordinates to handle grid edges
                    int nx = (col + dx + width) % width;
                    int ny = (row + dy + height) % height;
                    if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                        BooleanCell cell = (BooleanCell) controlView.getAutoma().getGrid().getCell(nx, ny);
                        if (cell != null) {
                            try {
                                if (button == 1) {
                                    // Left button: set random state with distance-based probability
                                    Random rnd = new Random();
                                    double prob = Math.exp(-distance / radius); // Gaussian-like decay
                                    boolean b = rnd.nextDouble() < prob;
                                    cell.setState(new BooleanState(b, false));
                                } else {
                                    // Middle button activates, right button deactivates
                                    cell.setState(new BooleanState(button == 2, false));
                                }
                            } catch (Exception ex) {
                                LOGGER.error("Error updating cell at ({}, {}): {}", nx, ny, ex.getMessage());
                            }
                        }
                    }
                }
            }
        }

        // Update display if automaton is paused
        if (!controlView.getAutoma().isRunning()) {
            controlView.getRenderer().accept((Grid<Cell<BooleanState>, BooleanState>) controlView.getAutoma().getGrid());
        }
    }
}