package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * A rule for the Inkspot cellular automaton, where a cell becomes black ({@code true}) if it has exactly three black
 * neighbors in its 3x3 neighborhood or is already black. The grid uses wrap-around boundaries, connecting opposite edges
 * (right to left, top to bottom) to form a toroidal surface, eliminating special cases for edge cells.
 * This rule is inspired by the "INKSPOT" recipe in Chapter 1, Section 1.2, page 6 of
 * "Cellular Automata Machines: A New Environment for Modeling" by Toffoli and Margolus.
 *
 * @see BooleanCell
 * @see BooleanState
 * @see <a href="https://doi.org/10.7551/mitpress/1763.001.0001">Toffoli, T., & Margolus, N. (1987). MIT Press.</a>
 */
public class RuleInkspot extends RuleBooleanNeighborCount {

    /**
     * Applies the Inkspot rule to compute the new state of a cell. The cell becomes black ({@code true}) if it has
     * exactly three black neighbors in its 3x3 neighborhood or is already black.
     *
     * @param grid the {@link Grid} containing the cell and its neighbors
     * @param cell the {@link BooleanCell} whose state is to be updated
     * @return a new {@link BooleanState} representing the updated state
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int liveCount = countLiveMooreNeighbors(grid, x, y);

        boolean echo = cell.getState().getValue();
        boolean isLive = cell.getState() != null && cell.getState().getValue();
        grid.getNextStates()[y][x].set(liveCount == 3 || isLive, echo, liveCount);
        return grid.getNextStates()[y][x];
    }

}