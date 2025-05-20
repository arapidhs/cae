package com.dungeoncode.ca.core.impl;

import com.dungeoncode.ca.core.Grid;
import com.dungeoncode.ca.core.GridInitializer;

import java.util.Random;

/**
 * Initializes a {@link Grid} for the Pulse Weaver cellular automaton by randomly assigning Dormant,
 * Charged, or Fading states to cells, with a probability distribution to create dynamic initial patterns.
 * Supports stochastic persistence for Fading cells to align with the automaton's pulsating behavior.
 *
 * @see GridInitializer
 * @see PulseWeaverCell
 * @see PulseWeaverState
 */
public class PulseWeaverRandomInitializer implements GridInitializer<PulseWeaverCell, PulseWeaverState> {

    /**
     * Random number generator for state assignment and persistence.
     */
    private final Random random = new Random();

    /**
     * Initializes the specified grid by assigning each cell a random state: Dormant (70% probability),
     * Charged (15% probability), or Fading (15% probability). Fading cells have a 20% chance of being
     * persistent to introduce stochastic behavior.
     *
     * @param grid the {@link Grid} to initialize
     */
    @Override
    public void initializeGrid(Grid<PulseWeaverCell, PulseWeaverState> grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();
        grid.setNextStates( new PulseWeaverState[height][width]);
        int centerX = width / 2;
        int centerY = height / 2;

        int halfSideLength=8;
        int startX = centerX - halfSideLength;
        int endX = centerX + halfSideLength;
        int startY = centerY - halfSideLength;
        int endY = centerY + halfSideLength;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                grid.getNextStates()[y][x] = new PulseWeaverState();

                PulseWeaverState state;
                boolean isActive = x >= startX && x < endX && y >= startY && y < endY;
                if(isActive) {
                    double rand = random.nextDouble();
                    if (rand < 0.70) {
                        state = new PulseWeaverState(PulseWeaverState.PulseState.DORMANT);
                    } else if (rand < 0.85) {
                        state = new PulseWeaverState(PulseWeaverState.PulseState.CHARGED);
                    } else {
                        boolean isPersistent = random.nextDouble() < 0.20;
                        state = new PulseWeaverState(PulseWeaverState.PulseState.FADING, isPersistent);
                    }
                } else {
                    state = new PulseWeaverState(PulseWeaverState.PulseState.DORMANT, false);
                }
                grid.setCell(x, y, new PulseWeaverCell(x, y, state));
            }
        }
    }
}