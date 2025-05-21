package com.dungeoncode.ca.automa.rules;

import com.dungeoncode.ca.core.AbstractRule;
import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.Rule;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

/**
 * Implements the HGLASS rule for a cellular automaton, determining the next state of a cell based on
 * a 32-bit lookup table using the states of the cell and its four orthogonal neighbors (east, west, south,
 * north). The rule produces diverse behaviors, ranging from chaotic growth to structured patterns, depending
 * on initial conditions such as random distributions or localized blobs. This rule is described in Chapter 4,
 * Section 4.1 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see Rule
 * @see BooleanState
 */
public class RuleHGlass extends AbstractRule<BooleanCell, BooleanState> {

    /**
     * Lookup table for the HGLASS rule, mapping a 5-bit index (EWSNC: east, west, south, north, center) to
     * the next cell state. The table encodes the 32 possible neighborhood configurations and their outcomes.
     */
    private static final boolean[] RULE_TABLE = new boolean[32];

    static {
        // Initialize the rule table based on the HGLASS specification
        RULE_TABLE[0b00000] = false; // 00000 -> 0
        RULE_TABLE[0b00001] = true;  // 00001 -> 1
        RULE_TABLE[0b00010] = true;  // 00010 -> 1
        RULE_TABLE[0b00011] = true;  // 00011 -> 1
        RULE_TABLE[0b00100] = false; // 00100 -> 0
        RULE_TABLE[0b00101] = false; // 00101 -> 0
        RULE_TABLE[0b00110] = false; // 00110 -> 0
        RULE_TABLE[0b00111] = false; // 00111 -> 0

        RULE_TABLE[0b01000] = false; // 01000 -> 0
        RULE_TABLE[0b01001] = false; // 01001 -> 0
        RULE_TABLE[0b01010] = false; // 01010 -> 0
        RULE_TABLE[0b01011] = true;  // 01011 -> 1
        RULE_TABLE[0b01100] = false; // 01100 -> 0
        RULE_TABLE[0b01101] = false; // 01101 -> 0
        RULE_TABLE[0b01110] = false; // 01110 -> 0
        RULE_TABLE[0b01111] = false; // 01111 -> 0

        RULE_TABLE[0b10000] = false; // 10000 -> 0
        RULE_TABLE[0b10001] = false; // 10001 -> 0
        RULE_TABLE[0b10010] = false; // 10010 -> 0
        RULE_TABLE[0b10011] = false; // 10011 -> 0
        RULE_TABLE[0b10100] = false; // 10100 -> 0
        RULE_TABLE[0b10101] = true;  // 10101 -> 1
        RULE_TABLE[0b10110] = false; // 10110 -> 0
        RULE_TABLE[0b10111] = false; // 10111 -> 0

        RULE_TABLE[0b11000] = false; // 11000 -> 0
        RULE_TABLE[0b11001] = true;  // 11001 -> 1
        RULE_TABLE[0b11010] = false; // 11010 -> 0
        RULE_TABLE[0b11011] = false; // 11011 -> 0
        RULE_TABLE[0b11100] = false; // 11100 -> 0
        RULE_TABLE[0b11101] = true;  // 11101 -> 1
        RULE_TABLE[0b11110] = true;  // 11110 -> 1
        RULE_TABLE[0b11111] = true;  // 11111 -> 1
    }

    public RuleHGlass() {
        super(3);
//        super(
//                // Rule Type
//                Tag.DETERMINISTIC,
//
//                // Neighborhood Type
//                Tag.VON_NEUMANN,   // Uses 4 orthogonal neighbors
//
//                // Operation Types
//                Tag.LOOKUP_TABLE,
//                Tag.BITWISE,
//
//                // Behavior Types
//                Tag.CHAOTIC,       // Can exhibit chaotic behavior
//
//                // Source Types
//                Tag.BOOK,
//                Tag.CLASSIC
//        );
    }

    /**
     * Applies the HGLASS rule to a cell in the specified grid. Computes a 5-bit index from the boolean states
     * of the cell (center) and its four orthogonal neighbors (east, west, south, north), and uses the lookup
     * table to determine the cell’s next state. Supports diverse behaviors based on initial conditions.
     *
     * @param grid the grid containing the cell
     * @param cell the cell to evaluate
     * @return the next state of the cell
     */
    @Override
    public BooleanState apply(Grid<BooleanCell, BooleanState> grid, BooleanCell cell) {
        int x = cell.getPosition().getX();
        int y = cell.getPosition().getY();
        int width = grid.getWidth();
        int height = grid.getHeight();

        // Get the states of the 5 cells in the neighborhood (EWSNC)
        boolean center = cell.getState().getValue();

        // Calculate wrapped coordinates for the neighbors
        int eastX = (x + 1 + width) % width;
        int westX = (x - 1 + width) % width;
        int northY = (y - 1 + height) % height;
        int southY = (y + 1 + height) % height;

        boolean east = grid.getCell(eastX, y).getState().getValue();
        boolean west = grid.getCell(westX, y).getState().getValue();
        boolean south = grid.getCell(x, southY).getState().getValue();
        boolean north = grid.getCell(x, northY).getState().getValue();

        // Calculate the index into the rule table
        int index = 0;
        if (east) index |= 0b10000;
        if (west) index |= 0b01000;
        if (south) index |= 0b00100;
        if (north) index |= 0b00010;
        if (center) index |= 0b00001;

        // Compute the sum of live (true) states
        int liveCount = (center ? 1 : 0) + (north ? 1 : 0) + (south ? 1 : 0) + (west ? 1 : 0) + (east ? 1 : 0);

        // Look up the new state in the rule table
        grid.getIntermediateStates()[y][x].set(RULE_TABLE[index], cell.getState().getValue(), liveCount);
        return grid.getIntermediateStates()[y][x];
    }

}