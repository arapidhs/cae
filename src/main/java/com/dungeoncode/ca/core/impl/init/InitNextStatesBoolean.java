package com.dungeoncode.ca.core.impl.init;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;

public class InitNextStatesBoolean implements GridInitializer<BooleanCell, BooleanState> {

    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        if (grid.getNextStates() == null) {
            grid.setNextStates(new BooleanState[height][width]);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    BooleanState[][] intermediateStates = grid.getNextStates();
                    intermediateStates[y][x] = new BooleanState();
                }
            }
        }
    }
}
