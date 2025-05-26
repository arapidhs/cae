package com.dungeoncode.cae.automa.rule;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

/**
 * Implements a generic elementary cellular automaton (ECA) rule for a one-dimensional grid, where row t at step t
 * computes a cell's state based on the left, center, and right neighbors from row t-1, using a rule number (0-255)
 * whose binary representation defines the output for each of the 8 possible neighborhood configurations. The 2D grid
 * stores spacetime history, scrolling up when full of toroidal left/right wrapping.
 *
 * @see RuleOneDScroll
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleElementaryCA extends RuleOneDScroll {

    /**
     * The rule number (0-255) defining the ECA's output table.
     */
    private int ruleNumber;

    /**
     * Constructs a new elementary cellular automaton rule with the specified rule number.
     *
     * @param ruleNumber the rule number (0-255) defining the output table
     * @throws IllegalArgumentException if ruleNumber is not in [0, 255]
     */
    public RuleElementaryCA(int ruleNumber) {
        super(30); // New ID
        if (ruleNumber < 0 || ruleNumber > 255) {
            throw new IllegalArgumentException("Rule number must be between 0 and 255, got: " + ruleNumber);
        }
        this.ruleNumber = ruleNumber;
    }

    /**
     * Computes the new state for a cell in the target row based on the left, center, and right neighbors from row t-1.
     * The 3-bit neighborhood configuration (left, center, right) forms an index (0-7), and the corresponding bit in the
     * rule number's binary representation determines the new state. Uses toroidal wrapping for left/right edges.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param x    the x-coordinate of the cell
     * @param y    the y-coordinate of the cell (target row)
     * @param step the current step
     * @return the new boolean state
     */
    @Override
    protected boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int step) {
        // Get neighbor states from row above (y-1)
        int width = grid.getWidth();
        boolean left = grid.getCell((x - 1 + width) % width, y - 1).getState().getValue();
        boolean center = grid.getCell(x, y - 1).getState().getValue();
        boolean right = grid.getCell((x + 1) % width, y - 1).getState().getValue();

        // Compute 3-bit index (0-7) from neighborhood configuration
        int index = (left ? 4 : 0) + (center ? 2 : 0) + (right ? 1 : 0);

        // Get output from rule number's binary representation (bit at index)
        return ((ruleNumber >> index) & 1) == 1;
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public void setRuleNumber(int ruleNumber) {
        this.ruleNumber = ruleNumber;
    }
}