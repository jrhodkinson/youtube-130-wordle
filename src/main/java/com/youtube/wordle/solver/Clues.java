package com.youtube.wordle.solver;

import com.google.common.collect.ImmutableSet;
import com.youtube.wordle.solver.WordleResponse.ColoredCharacter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static com.youtube.wordle.solver.WordleResponse.Color.GREY;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

public class Clues
{
    private final Map<Character, CharacterPositions> clues;

    private Clues(Map<Character, CharacterPositions> clues)
    {
        this.clues = clues;
    }

    public static Clues noClues()
    {
        return new Clues(new HashMap<>());
    }

    public Clues afterResponse(WordleResponse response)
    {
        Map<Character, CharacterPositions> newClues = new HashMap<>(clues);

        Map<Character, List<ColoredCharacter>> groupedCharacters = response.asList().stream()
                .collect(groupingBy(ColoredCharacter::character, toList()));

        for (Map.Entry<Character, List<ColoredCharacter>> characters : groupedCharacters.entrySet())
        {
            CharacterPositions existingPositions = newClues.getOrDefault(characters.getKey(), CharacterPositions.any());
            newClues.put(characters.getKey(), existingPositions.with(characters.getValue()));
        }

        return new Clues(newClues);
    }

    public boolean match(Word word)
    {
        for (Map.Entry<Character, CharacterPositions> entry : clues.entrySet())
        {
            Character character = entry.getKey();
            CharacterPositions cluePositions = entry.getValue();

            if (word.countOf(character) < cluePositions.minimumCount())
            {
                return false;
            }

            Set<Integer> wordPositions = word.positionsOf(character);

            if (!wordPositions.containsAll(cluePositions.knownPositions()))
            {
                return false;
            }

            for (Integer position : wordPositions)
            {
                if (!cluePositions.isPossible(position))
                {
                    return false;
                }
            }
        }

        return true;
    }

    private static class CharacterPositions
    {
        private final int minimumCount;
        private final ImmutableSet<Integer> knownPositions;
        private final ImmutableSet<Integer> possiblePositions;

        private CharacterPositions(int minimumCount, ImmutableSet<Integer> knownPositions, ImmutableSet<Integer> possiblePositions)
        {
            this.knownPositions = knownPositions;
            this.possiblePositions = possiblePositions;
            this.minimumCount = minimumCount;
        }

        public static CharacterPositions any()
        {
            return new CharacterPositions(0, ImmutableSet.of(), range(0, Word.LENGTH).boxed().collect(toImmutableSet()));
        }

        public int minimumCount()
        {
            return minimumCount;
        }

        public Set<Integer> knownPositions()
        {
            return knownPositions;
        }

        public boolean isPossible(int position)
        {
            return possiblePositions.contains(position);
        }

        public CharacterPositions with(List<ColoredCharacter> coloredCharacters)
        {
            int matches = (int) coloredCharacters.stream().filter(cc -> cc.color() != GREY).count();
            int newMinimumCount = Integer.max(minimumCount, matches);

            Set<Integer> newKnownPositions = new HashSet<>(knownPositions);
            Set<Integer> newPossiblePositions = new HashSet<>(possiblePositions);

            for (ColoredCharacter coloredCharacter : coloredCharacters)
            {
                switch (coloredCharacter.color())
                {
                    case GREEN:
                        newKnownPositions.add(coloredCharacter.position());
                        break;

                    case YELLOW:
                        newPossiblePositions.remove(coloredCharacter.position());
                        break;

                    case GREY:
                        if (newMinimumCount > 0)
                        {
                            newPossiblePositions.remove(coloredCharacter.position());
                        }
                        else
                        {
                            newPossiblePositions.clear();
                        }
                        break;
                }
            }

            return new CharacterPositions(newMinimumCount, ImmutableSet.copyOf(newKnownPositions), ImmutableSet.copyOf(newPossiblePositions));
        }

        @Override
        public String toString()
        {
            return "CharacterPositions{" +
                    "minimumCount=" + minimumCount +
                    ", knownPositions=" + knownPositions +
                    ", possiblePositions=" + possiblePositions +
                    '}';
        }
    }

    @Override
    public String toString()
    {
        return "Clues{" +
                "clues=" + clues +
                '}';
    }
}
