package com.dungeoncode.cae.terminal;

import com.dungeoncode.cae.terminal.gui.GUI;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

import javax.annotation.Nonnull;
import java.util.Objects;

public class StateManager {

    private final GUI gui;
    private ViewState currentState;
    private ViewState previousState;

    public StateManager(@Nonnull final GUI gui) {
        Objects.requireNonNull(gui);
        this.gui = gui;
        this.currentState = null;
        this.previousState = null;
        gui.setStateManager(this);
    }

    public void transitionTo(ViewState state) {

        // Exit current state
        if (currentState != null) {
            currentState.exit(gui);
        }

        previousState = currentState;
        currentState = state;

        // Enter new state
        currentState.enter(gui);
    }

    public void update() {
        if (currentState != null) {
            currentState.update(gui);
        }
    }

    public void onMessage(@Nonnull final Message message) {
        if (currentState != null) {
            currentState.onMessage(gui, message);
        }
    }

}
