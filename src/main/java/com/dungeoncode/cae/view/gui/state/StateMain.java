package com.dungeoncode.cae.view.gui.state;

import com.dungeoncode.cae.view.Message;
import com.dungeoncode.cae.view.ViewState;
import com.dungeoncode.cae.view.gui.GUI;
import com.dungeoncode.cae.view.gui.NamedWindow;
import com.dungeoncode.cae.view.gui.States;
import com.dungeoncode.cae.view.gui.Windows;

import java.awt.*;

public class StateMain implements ViewState {

    @Override
    public int getStateId() {
        return States.STATE_MAIN;
    }

    @Override
    public void enter(GUI gui) {
        final NamedWindow rootWindow = gui.getNamedWindow(Windows.WINDOW_ROOT);
        rootWindow.waitUntilClosed();
    }

    @Override
    public void update(GUI gui) {

    }

    @Override
    public void exit(GUI gui) {

    }

    @Override
    public void onMessage(GUI gui, Message message) {

    }

}
