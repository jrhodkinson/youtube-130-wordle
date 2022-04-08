package com.youtube.wordle.solver;

import com.google.common.collect.ForwardingList;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static com.google.common.io.Resources.getResource;
import static com.google.common.io.Resources.readLines;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Collections.unmodifiableList;

public class WordList extends ForwardingList<Word>
{
    private final List<Word> words;

    public WordList(List<Word> words)
    {
        this.words = unmodifiableList(words);
    }

    public static WordList allPossibleAnswers()
    {
        try
        {
            List<String> wordLines = readLines(getResource("wordle-possible-answers.txt"), UTF_8);

            return new WordList(wordLines.stream().map(Word::of).toList());
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    protected List<Word> delegate()
    {
        return words;
    }

    public WordList filter(Clues clues)
    {
        return new WordList(words.stream().filter(clues::match).toList());
    }
}
