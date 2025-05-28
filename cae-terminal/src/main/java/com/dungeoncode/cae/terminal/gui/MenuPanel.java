package com.dungeoncode.cae.terminal.gui;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.menu.Menu;
import com.googlecode.lanterna.gui2.menu.MenuBar;
import com.googlecode.lanterna.gui2.menu.MenuItem;

public class MenuPanel extends Panel {

    public MenuPanel() {
        super(new LinearLayout(Direction.HORIZONTAL));
        MenuBar menuBar = createMenuBar();
        addComponent(menuBar);
    }

    private MenuBar createMenuBar() {
        final MenuBar menuBar = new MenuBar();

        Menu automataMenu = new Menu("Automata");

        Menu ecaMenu = new Menu("Elementary (ECA)");
        MenuItem rule255MenuItem = new MenuItem("Rules 0-255");
        ecaMenu.add(rule255MenuItem);

        Menu dimensionMenu = new Menu("Dimension");
        MenuItem oneDItem = new MenuItem("One Dimensional");
        MenuItem twoDItem = new MenuItem("Two Dimensional");
        dimensionMenu.add(oneDItem);
        dimensionMenu.add(twoDItem);
        ;
        automataMenu.add(ecaMenu);
        automataMenu.add(dimensionMenu);

        menuBar.add(automataMenu);

        Menu settingsMenu = new Menu("Settings");
        menuBar.add(settingsMenu);

        Menu helpMenu = new Menu("Help");
        helpMenu.add(new MenuItem("User Guide"));
        helpMenu.add(new MenuItem("Rule Reference"));
        helpMenu.add(new MenuItem("About"));
        menuBar.add(helpMenu);

        return menuBar;
    }

}
