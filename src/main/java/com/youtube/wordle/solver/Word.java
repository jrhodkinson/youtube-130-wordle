package com.youtube.wordle.solver;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.emptySet;
import static java.util.Collections.unmodifiableSet;

public class Word
{
    public static final int LENGTH = 5;

    private final List<Character> characters;
    private final Map<Character, Set<Integer>> characterPositions;

    private Word(List<Character> characters)
    {
        this.characters = characters;
        this.characterPositions = characterPositions(characters);
    }

    public static Word of(String word)
    {
        if (word.length() != LENGTH)
        {
            throw new IllegalStateException();
        }

        List<Character> characters = new ArrayList<>();
        for (char c : word.toUpperCase(Locale.UK).toCharArray())
        {
            characters.add(c);
        }

        return new Word(characters);
    }

    public char charAt(int index)
    {
        return characters.get(index);
    }

    public int countOf(char character)
    {
        return characterPositions.getOrDefault(character, emptySet()).size();
    }

    public Set<Integer> positionsOf(char character)
    {
        return unmodifiableSet(characterPositions.getOrDefault(character, emptySet()));
    }

    private static Map<Character, Set<Integer>> characterPositions(List<Character> characters)
    {
        Map<Character, Set<Integer>> positions = new HashMap<>();

        for (int i = 0; i < characters.size(); i++)
        {
            positions.computeIfAbsent(characters.get(i), __ -> new HashSet<>()).add(i);
        }

        return positions;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Word word = (Word) o;

        return characters != null ? characters.equals(word.characters) : word.characters == null;
    }

    @Override
    public int hashCode()
    {
        return characters != null ? characters.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return Joiner.on("").join(characters);
    }
}
