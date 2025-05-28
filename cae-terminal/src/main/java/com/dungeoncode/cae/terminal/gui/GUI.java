package com.dungeoncode.cae.terminal.gui;

import com.dungeoncode.cae.terminal.StateManager;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;

import java.io.IOException;

public class GUI extends MultiWindowTextGUI {

    private StateManager stateManager;
    private final int width;
    private final int height;
    private final TerminalSize terminalSize;

    public GUI(Screen screen, int width, int height) {
        super(screen);
        this.width = width;
        this.height = height;
        this.terminalSize = new TerminalSize(width,height);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public TerminalSize getTerminalSize() {
        return terminalSize;
    }

    public NamedWindow getNamedWindow(String name) {
        return getWindows().stream()
                .filter(w -> w instanceof NamedWindow)
                .map(w -> (NamedWindow) w)
                .filter(w -> w.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Exits the application, cleaning up terminal resources.
     */
    private void exit() {
        try {
            if (getScreen() != null) {
                if ( getScreen() instanceof TerminalScreen ) {
                    ((TerminalScreen) getScreen()).stopScreen(true);
                } else {
                    getScreen().stopScreen();
                }
                getScreen().close();
            }
        } catch (IOException e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public StateManager getStateManager() {
        return stateManager;
    }

    public void setStateManager(StateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void addEscListener() {
        this.addListener(new EscGUIListener());
    }

    class EscGUIListener implements Listener {

        @Override
        public boolean onUnhandledKeyStroke(TextGUI textGUI, KeyStroke keyStroke) {
            if (keyStroke.isCtrlDown() && keyStroke.getKeyType() == KeyType.Character &&
             Character.toLowerCase(keyStroke.getCharacter()) == 'c') {
                confirmExit(textGUI);
                return true;
            }
            return false;
        }

        /**
         * Displays a confirmation dialog before exiting the application.
         *
         * @param gui the {@link WindowBasedTextGUI} for rendering the dialog
         */
        private void confirmExit(TextGUI gui) {
            MessageDialogButton result = MessageDialog.showMessageDialog(
                    (WindowBasedTextGUI) gui, "", "Exit?", MessageDialogButton.OK, MessageDialogButton.Cancel);
            if (result == MessageDialogButton.OK) {
                exit();
            }
        }

    }
}
