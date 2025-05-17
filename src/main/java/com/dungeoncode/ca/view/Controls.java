package com.dungeoncode.ca.view;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages keyboard and mouse controls for the cellular automata simulator.
 * Provides lists of key bindings and mouse actions with their descriptions.
 */
public class Controls {

    /**
     * List of keyboard controls.
     */
    public List<Control> controls;

    /**
     * List of mouse controls.
     */
    public List<Control> mouseControls;

    /**
     * Initializes the controls with predefined keyboard and mouse actions.
     */
    public Controls() {
        controls = new ArrayList<>();
        controls.add(new Control("?", "Show controls"));
        controls.add(new Control("' '", "Pause/resume"));
        controls.add(new Control("p", "Pause/resume"));
        controls.add(new Control("r", "Restart simulation"));
        controls.add(new Control("s", "Step one tick"));
        controls.add(new Control("q", "Exit simulation"));
        controls.add(new Control(">", "Increase speed"));
        controls.add(new Control("<", "Decrease speed"));
        controls.add(new Control("+", "Increase grid resolution"));
        controls.add(new Control("-", "Decrease grid resolution"));

        mouseControls = new ArrayList<>();
        mouseControls.add(new Control("Left Click / Drag", "Paint random cells"));
        mouseControls.add(new Control("Middle Click / Drag", "Paint live cells"));
        mouseControls.add(new Control("Right Click / Drag", "Paint dead cells"));
        mouseControls.add(new Control("Scroll Up", "Larger brush size"));
        mouseControls.add(new Control("Scroll Down", "Smaller brush size"));
    }

    /**
     * Represents a keyboard control with a control (key/mouse) and its description.
     *
     * @param control   the control
     * @param desc the description of the key's action
     */
    public record Control(String control, String desc) {
    }

}