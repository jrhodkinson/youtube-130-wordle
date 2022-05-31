package com.youtube.wordle;

import com.youtube.wordle.browser.BrowserWordle;
import com.youtube.wordle.solver.GreedyMinMax;
import com.youtube.wordle.solver.Word;
import com.youtube.wordle.solver.WordList;
import com.youtube.wordle.solver.WordleSolver;
import com.youtube.wordle.solver.WordleState;
import com.youtube.wordle.solver.WordleState.Solved;
import com.youtube.wordle.youtube.Thumbnail;
import com.youtube.wordle.youtube.YouTubeApi;
import com.youtube.wordle.youtube.YouTubeApiFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static com.youtube.wordle.image.ThumbnailFactory.thumbnail;

public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args)
    {
        new Main().run(new WordleProperties());
    }

    private void run(WordleProperties properties)
    {
        if (solve(properties) instanceof Solved s)
        {
            Word solution = s.solution();
            logger.info("Solved Wordle with solution {}", solution);

            Thumbnail thumbnail = thumbnail(LocalDate.now(), solution);

            YouTubeApi api = YouTubeApiFactory.api(properties);
//            api.setTitle(properties.videoId(), properties.title(solution));
            api.setThumbnail(properties.videoId(), thumbnail);
        }
    }

    private WordleState solve(WordleProperties properties)
    {
        try (BrowserWordle wordle = BrowserWordle.connect(properties))
        {
            WordleSolver solver = new WordleSolver(new GreedyMinMax(WordList.allPossibleAnswers()));

            solver.solve(wordle);

            return wordle.state();
        }
    }
}
