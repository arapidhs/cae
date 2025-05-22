package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Implements the GENETIC-DRIFT rule for a cellular automaton, extending NAIVE-DIFFUSION to model diffusion of
 * particles (genes) with species IDs. A cell can move to a neighbor with no species (ID 0) or the same species ID,
 * using either a copy or handshake mechanism. Optionally splits the grid into 3x3 subgrids to inhibit movement
 * across boundaries. Described in Chapter 9, Section 9.4 of <i>Cellular Automata Machines: A New Environment for
 * Modeling</i> (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleGeneticDrift extends RuleBooleanNeighborCount {

    /** Random number generator for neighbor selection. */
    private final Random random = new Random();

    /** Indicates whether subgrid boundaries are enforced. */
    private final boolean useGrid;

    /** Indicates whether handshake mechanism is used for diffusion. */
    private final boolean useHandshake;

    /** Number of subgrids per axis (3x3 grid). */
    private final int subgridCountPerAxis = 3;

    /**
     * Constructs a new GENETIC-DRIFT rule with default settings (no subgrid, no handshake).
     */
    public RuleGeneticDrift() {
        this(false, false);
    }

    /**
     * Constructs a new GENETIC-DRIFT rule with specified settings.
     *
     * @param useGrid      true to enforce 3x3 subgrid boundaries, false otherwise
     * @param useHandshake true to use handshake mechanism for diffusion, false for copy mechanism
     */
    public RuleGeneticDrift(boolean useGrid, boolean useHandshake) {
        super(23);
        this.useGrid = useGrid;
        this.useHandshake = useHandshake;
    }

    /**
     * Applies the GENETIC-DRIFT rule to compute the new state of a cell. Selects a random von Neumann neighbor
     * (north, south, east, west) and allows movement if the neighbor has no species (ID 0) or the same species ID,
     * respecting subgrid boundaries if enabled. Uses a copy mechanism (direct transfer) or handshake mechanism
     * (mutual state exchange) for diffusion. Updates the grid's intermediate state with the new state, echo,
     * neighbor count, and species ID.
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
        BooleanState currentState = cell.getState();
        boolean currentValue = currentState.getValue();
        int currentId = currentState.getId();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);

        // Subgrid boundaries
        int subgridWidth = width / subgridCountPerAxis;
        int subgridHeight = height / subgridCountPerAxis;

        // Random direction (0=north, 1=south, 2=west, 3=east)
        int direction = random.nextInt(4);
        int dx = switch (direction) {
            case 0 -> 0;    // North
            case 1 -> 0;    // South
            case 2 -> -1;   // West
            case 3 -> 1;    // East
            default -> 0;
        };
        int dy = switch (direction) {
            case 0 -> -1;   // North
            case 1 -> 1;    // South
            case 2, 3 -> 0; // West, East
            default -> 0;
        };

        int nx = (x + dx + width) % width;
        int ny = (y + dy + height) % height;
        BooleanState targetState = grid.getCell(nx, ny).getState();
        boolean targetValue = targetState.getValue();
        int targetId = targetState.getId();

        // Movement conditions
        boolean canMove = (!useGrid || (x / subgridWidth == nx / subgridWidth && y / subgridHeight == ny / subgridHeight))
                && (currentId == 0 || currentId == targetId);

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
                if (!currentValue && targetValue) {
                    newValue = true;
                    newId = targetId;
                }
            }
        }

        BooleanState[][] intermediateStates = grid.getIntermediateStates();
        intermediateStates[y][x].set(newValue, newEcho, liveSum, newId);
        return intermediateStates[y][x];
    }
}