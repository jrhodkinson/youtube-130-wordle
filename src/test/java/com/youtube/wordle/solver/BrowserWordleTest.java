package com.youtube.wordle.solver;

import com.youtube.wordle.WordleProperties;
import com.youtube.wordle.browser.BrowserWordle;
import com.youtube.wordle.solver.WordleState.Solved;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BrowserWordleTest
{
    @Test
    void solvesWordle()
    {
        try (BrowserWordle wordle = BrowserWordle.connect(new WordleProperties()))
        {
            WordleSolver solver = new WordleSolver(new GreedyMinMax(WordList.allPossibleAnswers()));

            solver.solve(wordle);

            assertTrue(wordle.state() instanceof Solved);
        }
    }
}
