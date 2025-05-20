package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;

public class NextStatesBooleanInitializer implements GridInitializer<BooleanCell, BooleanState> {

    @Override
    public void initializeGrid(Grid<BooleanCell, BooleanState> grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        grid.setNextStates( new BooleanState[height][width]);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BooleanState[][] intermediateStates = grid.getNextStates();
                intermediateStates[y][x]=new BooleanState();
            }
        }
    }
}
