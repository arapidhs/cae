package com.dungeoncode.cae.view;

import com.dungeoncode.cae.view.gui.GUI;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;

public interface ViewState {

    int getStateId();

    void enter(GUI gui);

    void update(GUI gui);

    void exit(GUI gui);

    void onMessage(GUI gui, Message message);

}
