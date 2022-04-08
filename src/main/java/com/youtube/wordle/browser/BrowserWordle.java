package com.youtube.wordle.browser;

import com.youtube.wordle.WordleProperties;
import com.youtube.wordle.browser.ShadowWebDriver.Element;
import com.youtube.wordle.solver.AbstractWordle;
import com.youtube.wordle.solver.Word;
import com.youtube.wordle.solver.WordleResponse;
import com.youtube.wordle.solver.WordleResponse.Color;
import com.youtube.wordle.solver.WordleResponse.ColoredCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static com.youtube.wordle.browser.CssSelector.BODY;
import static com.youtube.wordle.browser.CssSelector.COOKIE_DIALOG_CLOSE_BUTTON;
import static com.youtube.wordle.browser.CssSelector.GAME_ROW;
import static com.youtube.wordle.browser.CssSelector.GAME_TILE;
import static com.youtube.wordle.browser.CssSelector.TUTORIAL_CLOSE_BUTTON;
import static com.youtube.wordle.solver.WordleResponse.Color.GREEN;
import static com.youtube.wordle.solver.WordleResponse.Color.GREY;
import static com.youtube.wordle.solver.WordleResponse.Color.YELLOW;

public class BrowserWordle extends AbstractWordle implements AutoCloseable
{
    private static final Logger logger = LoggerFactory.getLogger(BrowserWordle.class);

    private static final String TILE_STATE_ATTRIBUTE = "data-state";
    private static final Map<String, Color> TILE_STATE_COLOR_MAP = Map.of(
            "correct", GREEN,
            "present", YELLOW,
            "absent", GREY
    );

    private final ShadowWebDriver driver;

    private BrowserWordle(ShadowWebDriver driver)
    {
        this.driver = driver;
    }

    public static BrowserWordle connect(WordleProperties properties)
    {
        ShadowWebDriver driver = ShadowWebDriver.open();

        driver.get(properties.wordleUrl());

        BrowserWordle wordle = new BrowserWordle(driver);

        wordle.dismiss(COOKIE_DIALOG_CLOSE_BUTTON);
        wordle.dismiss(TUTORIAL_CLOSE_BUTTON);

        return wordle;
    }

    @Override
    public WordleResponse responseTo(Word guess)
    {
        driver.waitForElement(BODY).sendWord(guess);

        Element responseRow = driver.waitForElement(GAME_ROW.apply(guess));

        List<Element> tiles = responseRow.findChildren(GAME_TILE);

        if (tiles.size() != Word.LENGTH)
        {
            throw new IllegalStateException("Couldn't find " + Word.LENGTH + " tiles, actually found " + tiles.size());
        }

        ColoredCharacter[] coloredCharacters = new ColoredCharacter[Word.LENGTH];

        for (int i = 0; i < Word.LENGTH; i++)
        {
            coloredCharacters[i] = new ColoredCharacter(i, tileColor(tiles.get(i)), guess.charAt(i));
        }

        return WordleResponse.of(coloredCharacters);
    }

    @Override
    public void close()
    {
        driver.close();
    }

    private Color tileColor(Element tile)
    {
        Color color = TILE_STATE_COLOR_MAP.get(tile.getAttribute(TILE_STATE_ATTRIBUTE, TILE_STATE_COLOR_MAP::containsKey));

        if (color == null)
        {
            throw new IllegalStateException("Failed to get color of element " + tile);
        }

        return color;
    }

    private void dismiss(CssSelector closeButton)
    {
        driver.findElement(closeButton).ifPresentOrElse(
                Element::click,
                () -> logger.info("Couldn't find close button by css=[{}], skipping", closeButton)
        );
    }
}
