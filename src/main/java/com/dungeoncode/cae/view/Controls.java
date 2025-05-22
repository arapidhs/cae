package com.dungeoncode.cae.view;

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
        controls.add(new Control("PgDn", "Start next automa"));
        controls.add(new Control("PgUp", "Start previous automa"));
        controls.add(new Control("i", "Show Automaton Info"));
        controls.add(new Control("s", "Step one tick"));
        controls.add(new Control("q", "Exit simulation"));
        controls.add(new Control(">", "Inc. speed"));
        controls.add(new Control("<", "Dec. speed"));
        controls.add(new Control("w", "Swap state with echo"));
        controls.add(new Control("+", "Inc. resolution"));
        controls.add(new Control("-", "Dec. resolution"));
        controls.add(new Control("→", "Next palette"));
        controls.add(new Control("←", "Previous palette"));
        controls.add(new Control("F1", "Invert palette"));
        controls.add(new Control("Ctrl+s", "Save screen as image"));

        mouseControls = new ArrayList<>();
        mouseControls.add(new Control("L. Clk Drag", "Paint random cells"));
        mouseControls.add(new Control("M. Clk Drag", "Paint live cells"));
        mouseControls.add(new Control("R. Clk Drag", "Paint dead cells"));
        mouseControls.add(new Control("Scroll Up", "Larger brush size"));
        mouseControls.add(new Control("Scroll Down", "Smaller brush size"));
    }

    /**
     * Represents a control entry with a label and description.
     *
     * @param control the key or mouse action label
     * @param desc    the description of the action
     */
    public record Control(String control, String desc) {
    }
}