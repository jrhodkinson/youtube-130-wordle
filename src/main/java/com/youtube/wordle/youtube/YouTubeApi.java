package com.youtube.wordle.youtube;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ThumbnailSetResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;
import com.google.api.services.youtube.model.VideoSnippet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YouTubeApi
{
    private static final Logger logger = LoggerFactory.getLogger(YouTubeApi.class);

    private final YouTube youTube;

    YouTubeApi(YouTube youTube)
    {
        this.youTube = youTube;
    }

    public void setTitle(VideoId videoId, String title)
    {
        logger.info("Setting video {} title to {}", videoId, title);

        try
        {
            VideoListResponse existingVideoResponse = youTube.videos()
                    .list("snippet")
                    .setId(videoId.id())
                    .execute();

            if (existingVideoResponse.getItems().size() != 1)
            {
                logger.error("Failed to find existing video: {}", existingVideoResponse);
                return;
            }

            Video video = new Video();
            video.setId(videoId.id());

            VideoSnippet snippet = existingVideoResponse.getItems().get(0).getSnippet();
            snippet.setTitle(title);
            video.setSnippet(snippet);

            Video response = youTube.videos().update("snippet", video).execute();

            logger.info("Video updated {}", response);
        }
        catch (Exception e)
        {
            logger.error("Failed to update video {} with title {}", videoId, title, e);
        }
    }

    public void setThumbnail(VideoId videoId, Thumbnail thumbnail)
    {
        logger.info("Updating thumbnail for video {}", videoId);

        try
        {
            ThumbnailSetResponse response = youTube.thumbnails()
                    .set(videoId.id(), thumbnail.toMediaContent())
                    .execute();

            logger.info("ThumbnailSetResponse={}", response);
        }
        catch (Exception e)
        {
            logger.error("Failed to set thumbnail for {}", videoId, e);
        }
    }
}
