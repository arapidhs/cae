package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements the SOIL-EROSION rule for a cellular automaton, where a cell (soil, active=true) remains active if it has
 * at least one active neighbor in each of the north, south, west, and east directions in its 3x3 Moore neighborhood,
 * and becomes inactive (eroded) otherwise. This rule models soil stability and erosion, producing stable or growing
 * holes based on initial conditions. Described in Chapter 9, Section 9.5 of <i>Cellular Automata Machines: A New
 * Environment for Modeling</i> (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleSoilErosion extends RuleBooleanNeighborCount {

    /**
     * Constructs a new SOIL-EROSION rule with a fixed ID.
     */
    public RuleSoilErosion() {
        super(24);
    }

    /**
     *
     * @param id the id of the configuration.
     */
    public RuleSoilErosion(final int id ) {
        super((id));
    }

    /**
     * Applies the SOIL-EROSION rule to compute the new state of a given cell. Checks the 3x3 Moore neighborhood
     * for at least one active neighbor in each direction (north: NW, N, NE; south: SW, S, SE; west: NW, W, SW;
     * east: NE, E, SE). If all directions have at least one active neighbor, the cell remains active (stable);
     * otherwise, it becomes inactive (eroded). Updates the grid's intermediate state with the new state, echo,
     * and neighbor count.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        boolean currentState = cell.getState().getValue();
        boolean echo = currentState;

        // Get neighbor states in 3x3 Moore neighborhood (toroidal wrapping)
        boolean[][] neighbors = new boolean[3][3];
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int nx = (x + dx + width) % width;
                int ny = (y + dy + height) % height;
                neighbors[dy + 1][dx + 1] = grid.getCell(nx, ny).getState().getValue();
            }
        }

        // Check for at least one active neighbor in each direction
        boolean hasNorth = neighbors[0][0] || neighbors[0][1] || neighbors[0][2]; // NW, N, NE
        boolean hasSouth = neighbors[2][0] || neighbors[2][1] || neighbors[2][2]; // SW, S, SE
        boolean hasWest = neighbors[0][0] || neighbors[1][0] || neighbors[2][0];  // NW, W, SW
        boolean hasEast = neighbors[0][2] || neighbors[1][2] || neighbors[2][2];  // NE, E, SE

        // Cell is stable (remains active) if all directions have at least one active neighbor
        boolean nextState = currentState && hasNorth && hasSouth && hasWest && hasEast;

        // Count live neighbors (excluding center) for liveSum
        int liveNeighbors = countLiveMooreNeighbors(grid, x, y);

        BooleanState[][] nextStates = grid.getNextStates();
        nextStates[y][x].set(nextState, echo, liveNeighbors);
        return nextStates[y][x];
    }
}