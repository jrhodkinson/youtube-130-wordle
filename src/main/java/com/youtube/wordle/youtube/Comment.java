package com.youtube.wordle.youtube;

import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;

public record Comment(String user, String text)
{
    public static Comment fromCommentThread(CommentThread commentThread)
    {
        CommentSnippet commentSnippet = commentThread.getSnippet().getTopLevelComment().getSnippet();

        return new Comment(commentSnippet.getAuthorDisplayName(), commentSnippet.getTextOriginal());
    }
}
