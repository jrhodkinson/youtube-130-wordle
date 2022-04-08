package com.youtube.wordle.browser;

import com.youtube.wordle.solver.Word;

import java.util.Locale;
import java.util.function.Function;

public class CssSelector
{
    public static final CssSelector BODY = new CssSelector("body");

    public static final Function<Word, CssSelector> GAME_ROW = w -> new CssSelector("game-row[letters=\\\"%s\\\"]".formatted(w.toString().toLowerCase(Locale.UK)));
    public static final CssSelector GAME_TILE = new CssSelector(".tile");

    public static final CssSelector COOKIE_DIALOG_CLOSE_BUTTON = new CssSelector("#pz-gdpr-btn-reject");
    public static final CssSelector TUTORIAL_CLOSE_BUTTON = new CssSelector(".overlay .close-icon");

    private final String selector;

    public CssSelector(String selector)
    {
        this.selector = selector;
    }

    @Override
    public String toString()
    {
        return selector;
    }
}
