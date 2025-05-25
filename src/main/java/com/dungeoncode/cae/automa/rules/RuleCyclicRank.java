package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Random;

/**
 * Implements the CYCLIC-RANK rule for a cellular automaton, where cells have one of four cyclic states (id=0,1,2,3).
 * A cell adopts the state of a randomly selected von Neumann neighbor if that neighbor's state is the next in the cycle
 * (0→1→2→3→0), creating self-organizing spiral patterns. The state is stored in the id field of BooleanState, with value=true
 * for active states. Described in Chapter 9, Section 9.6 of <i>Cellular Automata Machines: A New Environment for Modeling</i>
 * (MIT Press).
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleCyclicRank extends RuleBooleanNeighborCount {

    /** Random number generator for neighbor selection. */
    private final Random random = new Random();

    /**
     * Constructs a new CYCLIC-RANK rule with a fixed ID.
     */
    public RuleCyclicRank() {
        super(26); // New ID, adjust as needed
    }

    /**
     * Applies the CYCLIC-RANK rule to compute the new state of a cell. Selects a random von Neumann neighbor
     * (north, south, east, west) and checks if its state (id) is the next in the cyclic order (0→1→2→3→0) compared
     * to the current cell's state. If so, adopts the neighbor's state (value=true, id=neighbor's id); otherwise,
     * retains the current state. Updates the grid's intermediate state with the new state, echo, neighbor count,
     * and id.
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
        boolean echo = currentState.isEcho();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);

        // Randomly select a von Neumann neighbor (north, south, east, west)
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
        BooleanState neighborState = grid.getCell(nx, ny).getState();
        boolean neighborValue = neighborState.getValue();
        int neighborId = neighborState.getId();

        // Check if neighbor's state beats current state in cyclic order
        boolean beatsMe = currentId == 0 || neighborValue && // Neighbor must be active
                ((currentId == 1 && neighborId == 2) ||
                        (currentId == 2 && neighborId == 3) ||
                        (currentId == 3 && neighborId == 4) ||
                        (currentId == 4 && neighborId == 1));

        boolean newValue = currentValue;
        int newId = currentId;
        if (beatsMe) {
            newValue = true; // Adopt neighbor's state
            newId = neighborId;
        }

        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(newValue, echo, liveSum, newId);
        return intermediateStates[y][x];
    }
}