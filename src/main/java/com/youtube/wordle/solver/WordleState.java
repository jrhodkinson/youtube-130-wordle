package com.youtube.wordle.solver;

public sealed interface WordleState permits WordleState.Solved, WordleState.Unsolved
{
    record Solved(Word solution) implements WordleState
    {
    }

    record Unsolved(Clues clues, int remainingGuesses) implements WordleState
    {
    }
}
