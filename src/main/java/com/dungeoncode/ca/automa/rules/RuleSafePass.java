package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.RuleCategory;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the SAFE-PASS rule for a cellular automaton, simulating particle conservation by allowing tokens to fall
 * downward in columns and pile up above a baseline to form a histogram. Tokens move south via a handshake protocol,
 * inhibited by a baseline (echo=true), with the state (value) representing tokens (true = token, false = empty) and
 * the echo indicating the baseline. This rule is described in Chapter 9, Section 9.1 of <i>Cellular Automata Machines:
 * A New Environment for Modeling</i>.
 *
 * @see BooleanCell
 * @see BooleanState
 */
public class RuleSafePass extends RuleBooleanNeighborCount {

    /**
     * Applies the SAFE-PASS rule to compute the new state of a given cell. State (value): a cell takes a token from
     * the north if empty and the north has a token, or gives its token to the south if the south is empty, unless
     * inhibited by a baseline (north cell's echo=true for taking, or current cell's echo=true for giving). Echo
     * remains unchanged as the baseline is static.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return the new {@link BooleanState} of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int height = grid.getHeight();
        int liveSum = countLiveVonNeumannNeighbors(grid, x, y);
        BooleanState currentState = cell.getState();

        // Get states of current cell and neighbors
        boolean currentValue = currentState.getValue();
        boolean currentEcho = currentState.isEcho();

        // North neighbor
        int nxNorth = x;
        int nyNorth = (y - 1 + height) % height;
        BooleanState northState = grid.getCell(nxNorth, nyNorth).getState();
        boolean northValue = northState.getValue();
        boolean northEcho = northState.isEcho();

        // South neighbor
        int nxSouth = x;
        int nySouth = (y + 1) % height;
        BooleanState southState = grid.getCell(nxSouth, nySouth).getState();
        boolean southValue = southState.getValue();
        boolean southEcho = southState.isEcho();

        // Handshake protocol with inhibition
        boolean newValue = currentValue;
        // TAKE: If empty and north is full, take from north (unless north's echo=true)
        boolean canTake = !currentValue && northValue && !northEcho;
        if (canTake) {
            newValue = true; // Copy from north
        }
        // GIVE: If full and south is empty, give to south (unless current echo=true)
        boolean canGive = currentValue && !southValue && !currentEcho;
        if (canGive) {
            newValue = false; // Erase current token (south will take it)
        }

        // Echo remains unchanged (plane 1 is static)
        grid.getNextStates()[y][x].set(newValue, currentEcho, liveSum);
        return grid.getNextStates()[y][x];
    }

    @Override
    public RuleCategory getRuleCategory() {
        return RuleCategory.DETERMINISTIC;
    }
}