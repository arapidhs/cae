package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * Abstract base class for one-dimensional cellular automaton rules in a 2D grid, managing scrolling spacetime history.
 * Computes row t at step t using a rule-specific state calculation, with no shifting until step >= height, then shifts
 * rows up to scroll the history. Uses toroidal wrapping for left/right edges. Subclasses implement the state computation.
 */
public abstract class RuleOneDScroll extends RuleBooleanNeighborCount {

    /**
     * Constructs a new one-dimensional scrolling rule with the specified ID.
     *
     * @param id the unique identifier for the rule
     */
    protected RuleOneDScroll(int id) {
        super(id);
    }

    /**
     * Applies the one-dimensional scrolling rule to compute the new state of a cell. At step t, computes row t using
     * the rule-specific state calculation from the row above (t-1). If step >= height, shifts rows 0 to height-2 up,
     * then computes the bottom row. Updates the grid's intermediate state.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors, must not be null
     * @param cell the {@link BooleanCell} to update, must not be null
     * @param step the current step (1-based, step t computes row t)
     * @return the new {@link BooleanState} of the cell
     * @throws NullPointerException if grid or cell is null
     */
    @Override
    public BooleanState apply(@Nonnull Grid<BooleanCell, BooleanState> grid, @Nonnull BooleanCell cell, int step) {
        Objects.requireNonNull(grid, "Grid cannot be null");
        Objects.requireNonNull(cell, "Cell cannot be null");

        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();
        BooleanState currentState = cell.getState();
        boolean currentValue = currentState.getValue();
        boolean echo = currentState.isEcho();
        int liveSum = countLiveMooreNeighbors(grid, x, y);
        int id = currentState.getId();

        boolean newValue = currentValue;

        // Determine the row to compute (row t at step t)
        int targetRow = Math.min(step, height - 1);

        if (step < height) {
            // Early steps: compute row t (step t) using row t-1, no shifting
            if (y == targetRow && targetRow > 0) {
                newValue = computeNewState(grid, x, y-1, width);
            } else if (y > targetRow) {
                // Rows below target are inactive
                newValue = false;
            }
            // Rows above target retain their state (no shift yet)
        } else {
            // Step >= height: shift rows 0 to height-2 up, compute bottom row
            if (y < height - 1) {
                // Shift: row y gets row y+1's state
                newValue = grid.getCell(x, y + 1).getState().getValue();
            } else if (y == height - 1) {
                // Compute bottom row using row height-1
                newValue = computeNewState(grid, x, y, width);
            }
        }

        BooleanState[][] intermediateStates = grid.getNextStates();
        intermediateStates[y][x].set(newValue, echo, liveSum, id);
        return intermediateStates[y][x];
    }

    /**
     * Computes the new state for a cell in the target row using the row above (y-1). Subclasses implement the
     * specific rule logic, accessing neighbors with toroidal wrapping for left/right edges.
     *
     * @param grid  the {@link Grid} containing the cell and its neighbors
     * @param x     the x-coordinate of the cell
     * @param y     the y-coordinate of the cell (target row)
     * @param width the grid width for toroidal wrapping
     * @return the new boolean state for the cell
     */
    protected abstract boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int width);
}