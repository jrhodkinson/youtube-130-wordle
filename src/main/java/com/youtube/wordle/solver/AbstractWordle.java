package com.youtube.wordle.solver;

import com.youtube.wordle.solver.WordleState.Solved;
import com.youtube.wordle.solver.WordleState.Unsolved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWordle implements Wordle
{
    private static final Logger logger = LoggerFactory.getLogger(AbstractWordle.class);

    private WordleState state;

    protected AbstractWordle()
    {
        this.state = new Unsolved(Clues.noClues(), MAXIMUM_GUESSES);
    }

    @Override
    public WordleState state()
    {
        return state;
    }

    @Override
    public void guess(Word guess)
    {
        logger.info("Received guess {}", guess);

        switch (state)
        {
            case Solved s -> throw new IllegalStateException();
            case Unsolved u && u.remainingGuesses() == 0 -> throw new IllegalStateException();
            case Unsolved u -> handleGuess(guess);
        }
    }

    private void handleGuess(Word guess)
    {
        WordleResponse response = responseTo(guess);

        if (response.isCorrect())
        {
            logger.info("Wordle is now solved");
            state = new Solved(guess);
        }
        else
        {
            Unsolved unsolved = (Unsolved) state;
            state = new Unsolved(unsolved.clues().afterResponse(response), unsolved.remainingGuesses() - 1);
        }
    }

    protected abstract WordleResponse responseTo(Word guess);
}

