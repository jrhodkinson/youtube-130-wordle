package com.youtube.wordle.solver;

import com.youtube.wordle.solver.WordleResponse.Color;
import com.youtube.wordle.solver.WordleResponse.ColoredCharacter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordleResponseFactoryTest
{
    @Test
    void onlyColorsOneCharacterYellowIfRepeated()
    {
        WordleResponse response = WordleResponseFactory.response(Word.of("TALLY"), Word.of("LANES"));

        List<ColoredCharacter> expectedResponse = List.of(
                new ColoredCharacter(0, Color.GREY, 'T'),
                new ColoredCharacter(1, Color.GREEN, 'A'),
                new ColoredCharacter(2, Color.YELLOW, 'L'),
                new ColoredCharacter(3, Color.GREY, 'L'),
                new ColoredCharacter(4, Color.GREY, 'Y')
        );

        assertEquals(expectedResponse, response.asList());
    }

    @Test
    void colorsAppropriateCharacterGreenIfCharacterIsRepeated()
    {
        WordleResponse response = WordleResponseFactory.response(Word.of("ABACK"), Word.of("CRAZY"));

        List<ColoredCharacter> expectedResponse = List.of(
                new ColoredCharacter(0, Color.GREY, 'A'),
                new ColoredCharacter(1, Color.GREY, 'B'),
                new ColoredCharacter(2, Color.GREEN, 'A'),
                new ColoredCharacter(3, Color.YELLOW, 'C'),
                new ColoredCharacter(4, Color.GREY, 'K')
        );

        assertEquals(expectedResponse, response.asList());
    }
}