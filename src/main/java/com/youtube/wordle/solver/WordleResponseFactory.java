package com.youtube.wordle.solver;

import com.youtube.wordle.solver.WordleResponse.Color;
import com.youtube.wordle.solver.WordleResponse.ColoredCharacter;

import java.util.Objects;

import static java.util.Arrays.stream;

public class WordleResponseFactory
{
    public static WordleResponse response(Word guess, Word correctWord)
    {
        ColoredCharacter[] coloredCharacters = new ColoredCharacter[Word.LENGTH];

        for (int i = 0; i < Word.LENGTH; i++)
        {
            char guessCharacter = guess.charAt(i);
            if (guessCharacter == correctWord.charAt(i))
            {
                coloredCharacters[i] = new ColoredCharacter(i, Color.GREEN, guessCharacter);
            }
        }

        for (int i = 0; i < Word.LENGTH; i++)
        {
            if (coloredCharacters[i] != null)
            {
                continue;
            }

            char guessCharacter = guess.charAt(i);
            char correctCharacter = correctWord.charAt(i);

            int correctCount = correctWord.countOf(guessCharacter);
            if (correctCount == 0)
            {
                coloredCharacters[i] = new ColoredCharacter(i, Color.GREY, guessCharacter);
                continue;
            }

            long totalExistingClues = stream(coloredCharacters)
                    .filter(Objects::nonNull)
                    .filter(c -> c.character() == guessCharacter)
                    .filter(c -> c.color() != Color.GREY)
                    .count();

            Color color = correctWord.countOf(guessCharacter) > totalExistingClues ? Color.YELLOW : Color.GREY;

            coloredCharacters[i] = new ColoredCharacter(i, color, guessCharacter);
        }

        return new WordleResponse(coloredCharacters);
    }
}
