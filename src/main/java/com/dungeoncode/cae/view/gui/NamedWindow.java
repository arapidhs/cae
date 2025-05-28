package com.dungeoncode.cae.view.gui;

import com.googlecode.lanterna.gui2.BasicWindow;

public class NamedWindow extends BasicWindow {

    private final String name;

    public NamedWindow(String name) {
        this.name = name;
    }

    public NamedWindow(String title, String name) {
        super(title);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
