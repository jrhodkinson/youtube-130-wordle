package com.youtube.wordle.youtube;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public class Thumbnail
{
    private final BufferedImage image;

    public Thumbnail(BufferedImage image)
    {
        this.image = image;
    }

    AbstractInputStreamContent toMediaContent()
    {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream())
        {
            ImageIO.write(image, "png", os);

            return new ByteArrayContent("image/png", os.toByteArray());
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }
}
