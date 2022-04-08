package com.youtube.wordle.solver;

import static com.youtube.wordle.solver.WordleResponseFactory.response;

public class InMemoryWordle extends AbstractWordle
{
    private final Word solution;

    public InMemoryWordle(Word solution)
    {
        this.solution = solution;
    }

    @Override
    public WordleResponse responseTo(Word guess)
    {
        return response(guess, solution);
    }
}
