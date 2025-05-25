package com.dungeoncode.cae.automa.rules;

import com.dungeoncode.cae.core.Grid;
import com.dungeoncode.cae.core.impl.BooleanCell;
import com.dungeoncode.cae.core.impl.BooleanState;

import java.util.Random;

/**
 * Implements the RANDOM-WALK rule for a one-dimensional cellular automaton, where particles in row t at step t move
 * left or right randomly, with no collision detection. A cell erases its particle, and copies a particle from the left
 * neighbor (if moving right) or right neighbor (if moving left) based on a random decision. The 2D grid stores spacetime
 * history, scrolling up when full of toroidal left/right wrapping. Described in Chapter 10, Section 10.1 of
 * <i>Cellular Automata Machines: A New Environment for Modeling</i> (MIT Press).
 *
 * @see RuleOneDScroll
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleRandomWalk extends RuleOneDScroll {

    /** Random number generator for left/right movement. */
    private final Random random = new Random();

    /**
     * Constructs a new RANDOM-WALK rule with a fixed ID.
     */
    public RuleRandomWalk() {
        super(29); // New ID
    }

    /**
     * Computes the new state for a cell in the target row. Erases the current particle (if any), and sets the state to
     * true if the left neighbor (row y-1) has a particle and moves right, or the right neighbor has a particle and moves
     * left, based on a random decision. Uses toroidal wrapping for left/right edges. No collision detection.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param x    the x-coordinate of the cell
     * @param y    the y-coordinate of the cell (target row)
     * @param step the current step
     * @return the new boolean state
     */
    @Override
    protected boolean computeNewState(Grid<BooleanCell, BooleanState> grid, int x, int y, int step) {

        int width = grid.getWidth();
        boolean currentValue = grid.getCell((x + width) % width, y).getState().getValue();
        if( currentValue ) {
            return false;
        }

        // Get neighbor states from row above (y-1)
        boolean left = grid.getCell((x - 1 + width) % width, y-1).getState().getValue();
        boolean right = grid.getCell((x + 1) % width, y-1).getState().getValue();

        // Random direction: true = right, false = left
        boolean moveRight = random.nextBoolean();

        // Set state to true if left neighbor moves right or right neighbor moves left
        return (left && moveRight) || (right && !moveRight);
    }
}