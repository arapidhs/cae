package com.dungeoncode.ca.view.render;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Extends {@link TextCharacter} to provide cached, reverse-style text characters for cellular automaton rendering,
 * using a space symbol with a specified foreground color. Caches characters by color to optimize performance.
 */
public class CellCharacter extends TextCharacter {

    /**
     * Cache mapping {@link TextColor} to precomputed {@link TextCharacter} instances.
     */
    private static final Map<TextColor, TextCharacter> cache = new HashMap<>();

    /**
     * The space character used for rendering.
     */
    private static final String SPACE = " ";

    /**
     * Constructs a new {@link CellCharacter} with the specified character.
     * <p>
     * This constructor is deprecated; use {@link #fromColor(TextColor)} for rendering with cached, reverse-style characters.
     *
     * @param character the character to render
     * @deprecated Use {@link #fromColor(TextColor)} instead
     */
    @Deprecated
    public CellCharacter(char character) {
        super(character);
    }

    /**
     * Returns a reverse-style {@link TextCharacter} with a space symbol and the specified foreground color.
     * Retrieves from cache if available, otherwise creates and caches a new instance.
     *
     * @param color the foreground {@link TextColor}, must not be null
     * @return the cached or newly created {@link TextCharacter}
     * @throws NullPointerException if color is null
     */
    public static TextCharacter fromColor(@Nonnull TextColor color) {
        Objects.requireNonNull(color, "Color cannot be null");
        synchronized (cache) { // Thread-safe cache access
            return cache.computeIfAbsent(color, k ->
                    TextCharacter.fromString(SPACE, k, null, SGR.REVERSE)[0]
            );
        }
    }
}