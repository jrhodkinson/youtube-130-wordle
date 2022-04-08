package com.youtube.wordle.solver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordleSolver
{
    private static final Logger logger = LoggerFactory.getLogger(WordleSolver.class);

    private final GreedyMinMax greedyMinMax;

    public WordleSolver(GreedyMinMax greedyMinMax)
    {
        this.greedyMinMax = greedyMinMax;
    }

    public void solve(Wordle wordle)
    {
        logger.info("Running solver on wordle with state {}", wordle.state());

        wordle.guess(Word.of("SALET"));
        wordle.guess(Word.of("CRONY"));

        while (wordle.state() instanceof WordleState.Unsolved u && u.remainingGuesses() > 0)
        {
            wordle.guess(greedyMinMax.computeBestGuess(u));
        }
    }
}
