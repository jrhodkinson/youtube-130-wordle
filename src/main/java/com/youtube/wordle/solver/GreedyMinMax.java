package com.youtube.wordle.solver;

import static com.youtube.wordle.solver.WordleResponseFactory.response;
import static java.lang.Integer.max;

public class GreedyMinMax
{
    private final WordList allWords;

    public GreedyMinMax(WordList allWords)
    {
        this.allWords = allWords;
    }

    public Word computeBestGuess(WordleState.Unsolved unsolved)
    {
        Clues initialClues = unsolved.clues();
        WordList possibleWords = allWords.filter(initialClues);

        if (possibleWords.size() == 1)
        {
            return possibleWords.get(0);
        }

        int bestMaximum = Integer.MAX_VALUE;
        Word bestGuess = null;

        for (Word guess : allWords)
        {
            int maximumPossibleCountAfterGuess = Integer.MIN_VALUE;

            for (Word potentialSolution : possibleWords)
            {
                WordleResponse response = response(guess, potentialSolution);

                Clues newClues = initialClues.afterResponse(response);

                maximumPossibleCountAfterGuess = max(maximumPossibleCountAfterGuess, possibleWords.filter(newClues).size());
            }

            if (maximumPossibleCountAfterGuess < bestMaximum)
            {
                bestMaximum = maximumPossibleCountAfterGuess;
                bestGuess = guess;
            }
        }

        return bestGuess;
    }
}
