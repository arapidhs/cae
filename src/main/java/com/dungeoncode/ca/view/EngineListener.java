package com.dungeoncode.ca.view;

import com.dungeoncode.ca.core.Automaton;
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

import javax.annotation.Nonnull;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Objects;
import java.util.Random;

/**
 * Handles mouse interactions with a cellular automaton grid in a terminal-based view, responding to clicks, drags,
 * and wheel movements to modify cell states. Supports left-click for random state changes, middle-click to activate
 * cells, right-click to deactivate cells, and mouse wheel to adjust the affected area's radius.
 *
 * @param <C> the type of cells, extending {@link Cell}
 * @param <S> the type of cell states, extending {@link CellState}
 */
public class EngineListener<C extends Cell<S>, S extends CellState<?>> extends MouseAdapter {

    /**
     * Logger for mouse interaction events and errors.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineListener.class);

    /**
     * The controller managing the automaton and terminal display.
     */
    private final ViewEngine<C, S> viewEngine;

    /**
     * The mouse button currently pressed (1 = left, 2 = middle, 3 = right).
     */
    private int button;

    /**
     * The radius of the affected grid area for mouse interactions.
     */
    private int radius;

    /**
     * The timestamp of the last mouse wheel scroll event.
     */
    private long lastScrollTime;

    /**
     * Constructs a new mouse listener for the specified controller.
     *
     * @param viewEngine the {@link ViewEngine} managing the automaton, must not be null
     * @throws NullPointerException if viewEngine is null
     */
    public EngineListener(@Nonnull ViewEngine<C, S> viewEngine) {
        this.viewEngine = Objects.requireNonNull(viewEngine, "Controller cannot be null");
        int width = viewEngine.getWidth();
        int height = viewEngine.getHeight();
        this.radius = (int) (Math.min(width, height) * 0.1);
    }

    /**
     * Handles mouse clicks by applying cell state changes at the clicked position.
     * Converts pixel coordinates to grid coordinates and updates cells based on the mouse button.
     *
     * @param e the {@link MouseEvent}, must not be null
     */
    @Override
    public void mouseClicked(@Nonnull MouseEvent e) {
        Objects.requireNonNull(e, "Mouse event cannot be null");
        int col = e.getX() / viewEngine.getCellFontSize();
        int row = e.getY() / viewEngine.getCellFontSize();
        applyChanges(col, row, e.getButton());
    }

    /**
     * Captures the mouse button pressed during a mouse event.
     *
     * @param e the {@link MouseEvent}, must not be null
     */
    @Override
    public void mousePressed(@Nonnull MouseEvent e) {
        Objects.requireNonNull(e, "Mouse event cannot be null");
        button = e.getButton();
    }

    /**
     * Handles mouse wheel movements to adjust the radius of the affected grid area.
     * Increases radius for upward scrolls, decreases for downward scrolls, and draws a rectangular outline
     * around the cursor if the automaton is running.
     *
     * @param e the {@link MouseWheelEvent}, must not be null
     * @throws RuntimeException if drawing the radius outline fails
     */
    @Override
    public void mouseWheelMoved(@Nonnull MouseWheelEvent e) {
        Objects.requireNonNull(e, "Mouse wheel event cannot be null");

        long now = e.getWhen();
        if (lastScrollTime == 0 || now - lastScrollTime >= 20) {
            lastScrollTime = now;
        } else {
            return;
        }

        int dr = e.getWheelRotation() == -1 ? 3 : -3; // Increase for up, decrease for down
        updateRadius(dr);

        try {
            Automaton<C, S> automaton = viewEngine.getAutoma();
            boolean wasRunning = automaton.isRunning();
            if (wasRunning) {
                automaton.stop();
            }

            int col = e.getX() / viewEngine.getCellFontSize();
            int row = e.getY() / viewEngine.getCellFontSize();

            viewEngine.getTextGraphics().drawRectangle(
                    new TerminalPosition(col - radius, row - radius),
                    new TerminalSize(2 * radius, 2 * radius),
                    CellCharacter.fromColor(TextColor.ANSI.WHITE));
            viewEngine.getScreen().refresh(Screen.RefreshType.DELTA);

            if (wasRunning) {
                automaton.start();
            } else {
                viewEngine.getRenderer().accept(viewEngine.getAutoma().getGrid());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Failed to draw radius outline: " + ex.getMessage(), ex);
        }
    }

    /**
     * Updates the radius of the affected grid area, ensuring it stays within valid bounds.
     *
     * @param dr the radius change (positive to increase, negative to decrease)
     */
    private void updateRadius(int dr) {
        int width = viewEngine.getWidth();
        int height = viewEngine.getHeight();
        radius += dr;
        radius = Math.max(0, Math.min(radius, (width + height) / 8));
    }

    /**
     * Handles mouse drags by applying cell state changes at the dragged position.
     * Converts pixel coordinates to grid coordinates and updates cells based on the mouse button.
     *
     * @param e the {@link MouseEvent}, must not be null
     */
    @Override
    public void mouseDragged(@Nonnull MouseEvent e) {
        Objects.requireNonNull(e, "Mouse event cannot be null");
        int col = e.getX() / viewEngine.getCellFontSize();
        int row = e.getY() / viewEngine.getCellFontSize();
        applyChanges(col, row, button);
    }

    /**
     * Applies cell state changes to a circular area around the specified grid coordinates for {@link BooleanCell} grids.
     * Left-click sets random states with distance-based probability, middle-click activates cells, right-click
     * deactivates cells. Updates the display if the automaton is paused.
     *
     * @param col    the column coordinate in the grid
     * @param row    the row coordinate in the grid
     * @param button the mouse button (1 = left, 2 = middle, 3 = right)
     */
    private void applyChanges(int col, int row, int button) {
        C cell = viewEngine.getAutoma().getGrid().getCell(0, 0);
        if (!(cell instanceof BooleanCell)) {
            return;
        }

        int width = viewEngine.getWidth();
        int height = viewEngine.getHeight();

        if (radius > 0) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dx = -radius; dx <= radius; dx++) {
                    double distance = Math.sqrt(dx * dx + dy * dy);
                    if (distance <= radius) {
                        int nx = (col + dx + width) % width;
                        int ny = (row + dy + height) % height;
                        if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                            updateCell(button, nx, ny, distance);
                        }
                    }
                }
            }
        } else {
            if (col >= 0 && col < width && row >= 0 && row < height) {
                updateCell(button, col, row, 0);
            }
        }

        if (!viewEngine.getAutoma().isRunning()) {
            viewEngine.getRenderer().accept(viewEngine.getAutoma().getGrid());
        }
    }

    /**
     * Updates the state of a {@link BooleanCell} at the specified coordinates based on the mouse button.
     * Left-click sets a random state with probability decreasing with distance, middle-click activates,
     * right-click deactivates.
     *
     * @param button   the mouse button (1 = left, 2 = middle, 3 = right)
     * @param nx       the column coordinate
     * @param ny       the row coordinate
     * @param distance the distance from the center of the affected area
     */
    private void updateCell(int button, int nx, int ny, double distance) {
        BooleanCell cell = (BooleanCell) viewEngine.getAutoma().getGrid().getCell(nx, ny);
        if (cell != null) {
            try {
                if (button == 1) {
                    Random rnd = new Random();
                    double prob = radius > 0 ? Math.exp(-distance / radius) : 0.5;
                    boolean state = rnd.nextDouble() < prob;
                    cell.setState(state, false, 0);
                } else {
                    cell.setState(button == 2, false, 0);
                }
            } catch (Exception ex) {
                LOGGER.error("Error updating cell at ({}, {}): {}", nx, ny, ex.getMessage(), ex);
            }
        }
    }
}