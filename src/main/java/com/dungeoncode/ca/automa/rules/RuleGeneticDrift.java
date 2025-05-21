package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.RuleCategory;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the GENETIC-DRIFT rule, extending NAIVE-DIFFUSION. Models diffusion of particles (genes) with species IDs.
 * A cell can move to a neighbor if the neighbor has no species (ID 0) or the same species ID, using either a copy or handshake mechanism.
 * Optionally splits the grid into 3x3 subgrids to inhibit movement across boundaries.
 */
public class RuleGeneticDrift extends RuleBooleanNeighborCount {
    private final Random random = new Random();
    private final boolean useGrid;
    private final boolean useHandshake;
    private final int subgridCountPerAxis = 3;

    public RuleGeneticDrift() {
        this(false, false);
    }

    public RuleGeneticDrift(boolean useGrid, boolean useHandshake) {
        this.useGrid = useGrid;
        this.useHandshake = useHandshake;
    }

    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        BooleanState currentState = cell.getState();
        boolean currentValue = currentState.getValue();
        int currentId = currentState.getId();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);

        // Subgrid boundaries
        int subgridWidth = width / subgridCountPerAxis;
        int subgridHeight = height / subgridCountPerAxis;

        // Random direction (0=north, 1=south, 2=west, 3=east)
        int direction = random.nextInt(4);
        int dx = 0, dy = 0;
        switch (direction) {
            case 0: dy = -1; break; // North
            case 1: dy = 1; break; // South
            case 2: dx = -1; break; // West
            case 3: dx = 1; break; // East
        }
        int nx = (x + dx + width) % width;
        int ny = (y + dy + height) % height;
        BooleanState targetState = grid.getCell(nx, ny).getState();
        boolean targetValue = targetState.getValue();
        int targetId = targetState.getId();

        // Movement conditions
        boolean canMove = (!useGrid || (x / subgridWidth == nx / subgridWidth && y / subgridHeight == ny / subgridHeight))
                && currentId == 0 && targetId > 0;

        boolean newValue = currentValue;
        boolean newEcho = currentValue;
        int newId = currentId;

        if (canMove) {
            if (useHandshake) {
                boolean canTake = !currentValue && targetValue;
                boolean canGive = currentValue && !targetValue;
                if (canTake) {
                    newValue = true;
                    newId = targetId;
                } else if (canGive) {
                    newValue = false;
                }
            } else {
                newValue = true;
                newId = targetId;
            }
        }

        grid.getNextStates()[y][x].set(newValue, newEcho, liveSum, 0, newId);
        return grid.getNextStates()[y][x];
    }

    @Override
    public RuleCategory getRuleCategory() {
        return RuleCategory.PROBABILISTIC;
    }
}