package com.dungeoncode.ca.view.render;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import java.util.HashMap;
import java.util.Map;

public class CellCharacter extends TextCharacter {

    static Map<TextColor, TextCharacter> map = new HashMap<>();
    static String SPACE = " ";

    @Deprecated
    public CellCharacter(char character) {
        super(character);
    }

    public static TextCharacter fromColor(TextColor color) {
        if (map.containsKey(color)) {
            return map.get(color);
        } else {
            TextCharacter textCharacter = TextCharacter.fromString(SPACE, color, null, SGR.REVERSE)[0];
            map.put(color, textCharacter);
            return textCharacter;
        }
    }

}
