package com.youtube.wordle;

import com.youtube.wordle.solver.Word;
import com.youtube.wordle.youtube.VideoId;

import java.io.File;

import static java.nio.charset.StandardCharsets.UTF_8;

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
        return new String(
                new byte[] {
                        106, 97, 99, 107, 64, 106, 97, 99, 107, 104, 111, 100, 107, 105, 110, 115, 111, 110, 46, 99, 111, 109
                },
                UTF_8
        );
    }

    public VideoId videoId()
    {
        return new VideoId("j39gXQzsyBs");
    }

    public String title(Word word)
    {
        return "This video can solve today's Wordle (seriously. it's %s)".formatted(word);
    }

    public String wordleUrl()
    {
        return "https://www.nytimes.com/games/wordle/index.html";
    }
}
