package com.youtube.wordle.solver;

import java.util.Arrays;
import java.util.List;

public class WordleResponse
{
    private final ColoredCharacter[] coloredCharacters;

    WordleResponse(ColoredCharacter[] coloredCharacters)
    {
        this.coloredCharacters = coloredCharacters;
    }

    public static WordleResponse of(ColoredCharacter[] characters)
    {
        if (characters.length != Word.LENGTH)
        {
            throw new IllegalStateException();
        }

        return new WordleResponse(characters);
    }

    public List<ColoredCharacter> asList()
    {
        return Arrays.asList(coloredCharacters);
    }

    public boolean isCorrect()
    {
        for (ColoredCharacter coloredCharacter : coloredCharacters)
        {
            if (coloredCharacter.color != Color.GREEN)
            {
                return false;
            }
        }

        return true;
    }

    public record ColoredCharacter(int position, Color color, char character)
    {
    }

    public enum Color
    {
        GREEN, YELLOW, GREY;
    }
}
