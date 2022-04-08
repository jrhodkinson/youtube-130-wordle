package com.youtube.wordle.youtube;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.youtube.wordle.WordleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class YouTubeApiFactory
{
    private static final Logger logger = LoggerFactory.getLogger(YouTubeApiFactory.class);

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final Collection<String> SCOPES = List.of("https://www.googleapis.com/auth/youtube.force-ssl");

    public static YouTubeApi api(WordleProperties properties) {
        try
        {
            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            YouTube youTube = new YouTube.Builder(httpTransport, JSON_FACTORY, credential(properties))
                    .setApplicationName(properties.applicationName())
                    .build();

            return new YouTubeApi(youTube);
        }
        catch (IOException | GeneralSecurityException e)
        {
            logger.error("Failed to create YouTubeApi", e);
            System.exit(1);

            return null; // satisfy compiler
        }
    }

    public static GoogleCredential credential(WordleProperties properties) throws IOException {
        return GoogleCredential.fromStream(new FileInputStream(properties.credentialsFile()))
                .createScoped(Collections.singleton(YouTubeScopes.YOUTUBE_FORCE_SSL))
                .createDelegated(properties.email());
    }
}
