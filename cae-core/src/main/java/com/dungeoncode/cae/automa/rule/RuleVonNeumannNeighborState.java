package com.dungeoncode.cae.automa.rule;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Implements a two-dimensional cellular automaton rule where a cell’s next state is determined by the number of active
 * von Neumann neighbors (0-4) and its own state, using a 10-bit code number’s binary digits to define outputs for each
 * configuration. Bits 0-1 set outputs for 4 neighbors (center active/inactive), bits 2-3 for 3 neighbors, bits 4-5 for
 * 2 neighbors, bits 6-7 for 1 neighbor, and bits 8-9 for 0 neighbors. The grid stores patterns, starting from a single
 * active cell. Inspired by <i>A New Kind of Science</i> by Stephen Wolfram.
 *
 * @see RuleBooleanNeighborCount
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleVonNeumannNeighborState extends RuleBooleanNeighborCount {

    /** The code number (0-1023) defining the rule’s output table via its 10 binary digits. */
    private int codeNumber;

    /**
     * Constructs a new von Neumann neighbor-state rule with the specified code number.
     *
     * @param codeNumber the code number (0-1023) defining the output table for 10 configurations
     * @throws IllegalArgumentException if codeNumber is not in [0, 1023]
     */
    public RuleVonNeumannNeighborState(int codeNumber) {
        super(32); // Rule ID
        if (codeNumber < 0 || codeNumber > 1023) {
            throw new IllegalArgumentException("Code number must be between 0 and 1023, got: " + codeNumber);
        }
        this.codeNumber = codeNumber;
    }

    /**
     * Applies the rule to compute the new state of a cell. Counts active neighbors in the von Neumann neighborhood
     * (north, south, east, west). Uses the code number’s binary digits to determine the output: bits 0-1 for 4 active
     * neighbors (center active/inactive), bits 2-3 for 3 neighbors, bits 4-5 for 2 neighbors, bits 6-7 for 1 neighbor,
     * and bits 8-9 for 0 neighbors. Updates the grid’s intermediate state with the new state, echo, and neighbor count.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @param step the current step
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell, int step) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        boolean currentState = cell.getState().getValue();

        // Count active neighbors in the von Neumann neighborhood (north, south, east, west)
        int activeNeighbors = countLiveVonNeumannNeighbors(grid, x, y);

        // Count active neighbors in the Moore neighborhood for liveSum (used in rendering)
        int liveSum = countLiveMooreNeighbors(grid, x, y);

        // Determine index based on neighbor count and center state
        // Bit mapping: 0-1 (4 neighbors, active/inactive center), 2-3 (3 neighbors), 4-5 (2 neighbors),
        // 6-7 (1 neighbor), 8-9 (0 neighbors)
        int index = (4 - activeNeighbors) * 2 + (currentState ? 0 : 1);

        // Get output from code number’s binary representation (bit at index)
        // 1 = active (true), 0 = inactive (false)
        boolean isActive = ((codeNumber >> index) & 1) == 1;

        // Update the cell’s state in the intermediate state array
        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(isActive, cell.getState().getValue(), liveSum);
        return intermediateStates[y][x];
    }

    /**
     * Returns the code number defining the rule’s output table.
     *
     * @return the code number (0-1023)
     */
    public int getCodeNumber() {
        return codeNumber;
    }

    /**
     * Sets a new code number for the rule’s output table.
     *
     * @param codeNumber the new code number (0-1023)
     * @throws IllegalArgumentException if codeNumber is not in [0, 1023]
     */
    public void setCodeNumber(int codeNumber) {
        if (codeNumber < 0 || codeNumber > 1023) {
            throw new IllegalArgumentException("Code number must be between 0 and 1023, got: " + codeNumber);
        }
        this.codeNumber = codeNumber;
    }
}