package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the NAIVE-DIFFUSION rule for a cellular automaton, modeling diffusion of particles by randomly moving
 * them in one of four directions (north, south, east, west). Optionally splits the grid into a 3x3 subgrid (9 subgrids),
 * inhibiting movement across subgrid boundaries if enabled, simulating isolated regions. This rule produces a diffusion
 * pattern that breaks up into tongues of fire, as described in Chapter 9, Section 9.4 of <i>Cellular Automata Machines:
 * A New Environment for Modeling</i>.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleNaiveDiffusion extends RuleBooleanNeighborCount {

    private final Random random = new Random();
    private final boolean useGrid;
    private final int subgridCountPerAxis = 3; // 3x3 subgrid (9 subgrids total)

    /**
     * Constructs a new NAIVE-DIFFUSION rule with grid splitting disabled by default.
     */
    public RuleNaiveDiffusion() {
        this(false);
    }

    /**
     * Constructs a new NAIVE-DIFFUSION rule with the specified grid splitting option.
     *
     * @param useGrid if true, splits the grid into a 3x3 subgrid and inhibits movement across boundaries
     */
    public RuleNaiveDiffusion(boolean useGrid) {
        this.useGrid = useGrid;
    }

    /**
     * Applies the NAIVE-DIFFUSION rule to compute the new state of a given cell. Uses a handshake protocol: an active
     * cell randomly chooses a direction to give its state (becoming inactive), and an inactive cell takes from a neighbor
     * (becoming active), ensuring movement only occurs if the target is empty and not across a subgrid boundary (if
     * enabled). Echo tracks the previous state for second-order dynamics.
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

        // Subgrid boundaries (if enabled): split grid into 3x3 subgrids
        int subgridWidth = width / subgridCountPerAxis;
        int subgridHeight = height / subgridCountPerAxis;

        boolean newValue = currentState.getValue();
        boolean newEcho = currentState.getValue();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);

        // North neighbor
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        BooleanState northState = grid.getCell(nxNorth, nyNorth).getState();
        boolean northValue = northState.getValue();

        // South neighbor
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        BooleanState southState = grid.getCell(nxSouth, nySouth).getState();
        boolean southValue = southState.getValue();

        // West neighbor
        int nxWest = (x - 1 + width) % width;
        int nyWest = y;
        BooleanState westState = grid.getCell(nxWest, nyWest).getState();
        boolean westValue = westState.getValue();

        // East neighbor
        int nxEast = (x + 1) % width;
        int nyEast = y;
        BooleanState eastState = grid.getCell(nxEast, nyEast).getState();
        boolean eastValue = eastState.getValue();

        // Handshake protocol with grid inhibition
        // Randomly choose a direction (0=north, 1=south, 2=west, 3=east)
        int direction = random.nextInt(4);

        // GIVE: If active, attempt to give to a random neighbor
        if (newValue) {
            switch (direction) {
                case 0: // North
                    if (!northValue) { // North is empty
                        boolean canGive = !useGrid || (y - 1) / subgridHeight == y / subgridHeight;
                        if (canGive) {
                            newValue = false; // Give to north
                            grid.getCell(nxNorth, nyNorth).setState(new BooleanState(true, grid.getCell(nxNorth, nyNorth).getState().isEcho()));
                        }
                    }
                    break;
                case 1: // South
                    if (!southValue) { // South is empty
                        boolean canGive = !useGrid || (y + 1) / subgridHeight == y / subgridHeight;
                        if (canGive) {
                            newValue = false; // Give to south
                            grid.getCell(nxSouth, nySouth).setState(new BooleanState(true, grid.getCell(nxSouth, nySouth).getState().isEcho()));
                        }
                    }
                    break;
                case 2: // West
                    if (!westValue) { // West is empty
                        boolean canGive = !useGrid || (x - 1) / subgridWidth == x / subgridWidth;
                        if (canGive) {
                            newValue = false; // Give to west
                            grid.getCell(nxWest, nyWest).setState(new BooleanState(true, grid.getCell(nxWest, nyWest).getState().isEcho()));
                        }
                    }
                    break;
                case 3: // East
                    if (!eastValue) { // East is empty
                        boolean canGive = !useGrid || (x + 1) / subgridWidth == x / subgridWidth;
                        if (canGive) {
                            newValue = false; // Give to east
                            grid.getCell(nxEast, nyEast).setState(new BooleanState(true, grid.getCell(nxEast, nyEast).getState().isEcho()));
                        }
                    }
                    break;
            }
        }
        // TAKE: If inactive, attempt to take from a random neighbor
        else {
            switch (direction) {
                case 0: // North
                    if (northValue) { // North has a particle
                        boolean canTake = !useGrid || (y - 1) / subgridHeight == y / subgridHeight;
                        if (canTake) {
                            newValue = true; // Take from north
                            grid.getCell(nxNorth, nyNorth).setState(new BooleanState(false, grid.getCell(nxNorth, nyNorth).getState().isEcho()));
                        }
                    }
                    break;
                case 1: // South
                    if (southValue) { // South has a particle
                        boolean canTake = !useGrid || (y + 1) / subgridHeight == y / subgridHeight;
                        if (canTake) {
                            newValue = true; // Take from south
                            grid.getCell(nxSouth, nySouth).setState(new BooleanState(false, grid.getCell(nxSouth, nySouth).getState().isEcho()));
                        }
                    }
                    break;
                case 2: // West
                    if (westValue) { // West has a particle
                        boolean canTake = !useGrid || (x - 1) / subgridWidth == x / subgridWidth;
                        if (canTake) {
                            newValue = true; // Take from west
                            grid.getCell(nxWest, nyWest).setState(new BooleanState(false, grid.getCell(nxWest, nyWest).getState().isEcho()));
                        }
                    }
                    break;
                case 3: // East
                    if (eastValue) { // East has a particle
                        boolean canTake = !useGrid || (x + 1) / subgridWidth == x / subgridWidth;
                        if (canTake) {
                            newValue = true; // Take from east
                            grid.getCell(nxEast, nyEast).setState(new BooleanState(false, grid.getCell(nxEast, nyEast).getState().isEcho()));
                        }
                    }
                    break;
            }
        }

        // Update next states with the new state and echo
        grid.getNextStates()[y][x].set(newValue, newEcho, liveSum);
        return grid.getNextStates()[y][x];
    }
}