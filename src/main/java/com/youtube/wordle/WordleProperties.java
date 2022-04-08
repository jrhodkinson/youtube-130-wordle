package com.youtube.wordle;

import com.youtube.wordle.solver.Word;
import com.youtube.wordle.youtube.VideoId;

import java.io.File;

public class WordleProperties
{
    public String applicationName()
    {
        return "Wordle Video";
    }

    public File credentialsFile()
    {
        return new File("youtube-credentials.json");
    }

    public String email()
    {
        return "jack@jackhodkinson.com";
    }

    public VideoId videoId()
    {
        return new VideoId("M50pe3giieI");
    }

    public String title(Word word)
    {
        return "This video can solve Wordle (seriously, today's is %s)".formatted(word);
    }

    public String wordleUrl()
    {
        return "https://www.nytimes.com/games/wordle/index.html";
    }
}
