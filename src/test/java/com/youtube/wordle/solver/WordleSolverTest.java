package com.youtube.wordle.solver;

import com.youtube.wordle.solver.WordleState.Solved;
import org.junit.jupiter.api.Test;

import static com.youtube.wordle.solver.WordList.allPossibleAnswers;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WordleSolverTest
{
    @Test
    void canSolveAllPossibleWordles()
    {
        WordleSolver solver = new WordleSolver(new GreedyMinMax(allPossibleAnswers()));

        allPossibleAnswers().forEach(w -> {
            Wordle wordle = new InMemoryWordle(w);

            solver.solve(wordle);

            assertEquals(new Solved(w), wordle.state());
        });
    }
}
