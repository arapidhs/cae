package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the NAIVE-DIFFUSION rule for a cellular automaton, modeling diffusion of particles by either copying the
 * state of a neighboring cell or using a handshake protocol to move particles in one of four directions (north, south,
 * east, west). Optionally splits the grid into a 3x3 subgrid (9 subgrids), inhibiting movement across subgrid boundaries
 * if enabled, simulating isolated regions. This rule produces a diffusion pattern that breaks up into tongues of fire,
 * as described in Chapter 9, Section 9.4 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleNaiveDiffusion extends RuleBooleanNeighborCount {

    private final Random random = new Random();
    private final int subgridCountPerAxis = 3; // 3x3 subgrid (9 subgrids total)
    private boolean useGrid;
    private boolean useHandshake;

    /**
     * Constructs a new NAIVE-DIFFUSION rule with the specified grid splitting and handshake options.
     *
     * @param useGrid      if true, splits the grid into a 3x3 subgrid and inhibits movement across boundaries
     * @param useHandshake if true, uses a handshake protocol for particle movement instead of copying neighbor states
     */
    public RuleNaiveDiffusion(boolean useGrid, boolean useHandshake) {
        this();
        this.useGrid = useGrid;
        this.useHandshake = useHandshake;
    }

    /**
     * Constructs a new NAIVE-DIFFUSION rule with grid splitting and handshake disabled by default.
     */
    public RuleNaiveDiffusion() {
        super(22);
        this.useGrid = false;
        this.useHandshake = false;
    }

    /**
     * Applies the NAIVE-DIFFUSION rule to compute the new state of a given cell at position (x, y). If handshake is
     * disabled, randomly selects a direction (north, south, east, west) and copies the state (value) of the neighboring
     * cell in that direction, unless movement is inhibited by a subgrid boundary (if enabled). If handshake is enabled,
     * an active cell gives its state (becoming inactive) to an empty neighbor, and an inactive cell takes a state
     * (becoming active) from a neighbor with a particle, respecting subgrid boundaries. The new state is set in the
     * nextStates array for (x, y). Echo tracks the previous state for second-order dynamics.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        BooleanState currentState = cell.getState();
        boolean currentValue = currentState.getValue();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);

        // Subgrid boundaries (if enabled): split grid into 3x3 subgrids
        int subgridWidth = width / subgridCountPerAxis;
        int subgridHeight = height / subgridCountPerAxis;

        // Randomly choose a direction (0=north, 1=south, 2=west, 3=east)
        int direction = random.nextInt(4);

        int dx = 0;
        int dy = 0;
        switch (direction) {
            case 0: // North
                dy = -1;
                break;
            case 1: // South
                dy = 1;
                break;
            case 2: // West
                dx = -1;
                break;
            case 3: // East
                dx = 1;
                break;
        }
        int nx = (x + dx + width) % width;
        int ny = (y + dy + height) % height;
        BooleanState targetState = grid.getCell(nx, ny).getState();
        boolean targetValue = targetState.getValue();

        // Check if movement is inhibited by subgrid boundaries
        boolean canMove = !useGrid ||
                (x / subgridWidth == nx / subgridWidth && y / subgridHeight == ny / subgridHeight);

        boolean newValue = currentValue;
        boolean newEcho = currentValue;

        if (canMove) {
            if (useHandshake) {
                boolean canTake = !currentValue && targetValue;
                boolean canGive = currentValue && !targetValue;
                if (canTake) {
                    newValue = true;
                } else if (canGive) {
                    newValue = false;
                }
            } else {
                newValue = targetValue;
            }
        }

        // Update next states for the current cell
        grid.getIntermediateStates()[y][x].set(newValue, newEcho, liveSum);
        return grid.getIntermediateStates()[y][x];
    }

}