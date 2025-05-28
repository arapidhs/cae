package com.dungeoncode.cae.terminal.gui.state;

import com.dungeoncode.cae.terminal.Message;
import com.dungeoncode.cae.terminal.ViewState;
import com.dungeoncode.cae.terminal.gui.*;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.List;

import static com.googlecode.lanterna.gui2.Window.Hint.*;
import static com.googlecode.lanterna.gui2.Window.Hint.FULL_SCREEN;

public class StateSetup implements ViewState {

    @Override
    public int getStateId() {
        return States.STATE_SETUP;
    }

    @Override
    public void enter(GUI gui) {
        final NamedWindow rootWindow = new NamedWindow(Windows.WINDOW_ROOT);
        rootWindow.setFixedSize(gui.getTerminalSize());
        rootWindow.setHints(List.of(NO_DECORATIONS, NO_POST_RENDERING, CENTERED, FULL_SCREEN));


        final Panel rootPanel = new Panel(new AbsoluteLayout());
        rootPanel.setPosition(TerminalPosition.TOP_LEFT_CORNER);
        rootPanel.setPreferredSize(gui.getTerminalSize());

        MenuPanel menuPanel = new MenuPanel();
        menuPanel.setPosition(TerminalPosition.TOP_LEFT_CORNER);
        menuPanel.setSize(new TerminalSize(gui.getWidth(),1));

        rootPanel.addComponent(menuPanel);

        rootWindow.setComponent(rootPanel);

        gui.addWindow(rootWindow);
        gui.setActiveWindow(rootWindow);
        gui.addEscListener();

    }

    @Override
    public void update(GUI gui) {
        MessageDialog.showMessageDialog(gui, "Welcome", "Select a configuration first!", MessageDialogButton.OK);
        gui.getStateManager().transitionTo(new StateMain());
    }

    @Override
    public void exit(GUI gui) {

    }

    @Override
    public void onMessage(GUI gui, Message message) {

    }

}
