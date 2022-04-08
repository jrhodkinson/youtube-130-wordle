package com.youtube.wordle.browser;

import com.youtube.wordle.WordleProperties;
import com.youtube.wordle.solver.GreedyMinMax;
import com.youtube.wordle.solver.WordList;
import com.youtube.wordle.solver.WordleSolver;
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
