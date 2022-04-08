package com.youtube.wordle.solver;

public interface Wordle
{
    int MAXIMUM_GUESSES = 6;

    WordleState state();

    void guess(Word guess);
}
