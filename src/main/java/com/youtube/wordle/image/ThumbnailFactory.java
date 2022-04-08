package com.youtube.wordle.image;

import com.youtube.wordle.solver.Word;
import com.youtube.wordle.youtube.Thumbnail;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.awt.Font.BOLD;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.String.valueOf;

public class ThumbnailFactory
{
    public static Thumbnail thumbnail(LocalDate date, Word word)
    {
        BufferedImage image = new BufferedImage(1280, 720, TYPE_INT_RGB);

        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, image.getWidth(), image.getHeight());

        Font dateFont = new Font("Helvetica Neue", BOLD, 96);
        FontMetrics dateFontMetrics = g.getFontMetrics(dateFont);
        g.setFont(dateFont);

        int squareDimensions = image.getWidth() / 7;
        int lineMargin = dateFontMetrics.getDescent() + squareDimensions / 4;
        int squareMargin = squareDimensions / 6;

        String dateString = dateString(date);

        int dateX = (image.getWidth() - dateFontMetrics.stringWidth(dateString)) / 2;
        int dateY = (image.getHeight() - (dateFontMetrics.getHeight() + squareDimensions + lineMargin)) / 2 + dateFontMetrics.getAscent();
        g.setColor(Color.BLACK);
        g.drawString(dateString, dateX, dateY);

        Font wordleFont = new Font("SansSerif", BOLD, 124);
        FontMetrics wordleFontMetrics = g.getFontMetrics(wordleFont);
        g.setFont(wordleFont);

        int wordleStartingX = (image.getWidth() - (Word.LENGTH * (squareDimensions + squareMargin) - squareMargin)) / 2;
        int wordleY = dateY + lineMargin;
        for (int i = 0; i < Word.LENGTH; i++)
        {
            String character = valueOf(word.charAt(i));

            int squareX = wordleStartingX + (squareDimensions + squareMargin) * i;
            g.setColor(Color.decode("#6aaa64"));
            g.fillRect(squareX, wordleY, squareDimensions, squareDimensions);

            int charX = squareX + (squareDimensions - wordleFontMetrics.stringWidth(character)) / 2;
            int charY = wordleY + (squareDimensions - wordleFontMetrics.getHeight()) / 2 + wordleFontMetrics.getAscent();

            g.setColor(Color.WHITE);
            g.drawString(character, charX, charY);
        }

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(30));
        g.drawRect(0, 0, image.getWidth(), image.getHeight());

        return new Thumbnail(image);
    }

    static String dateString(LocalDate date)
    {
        String candidate = date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM"));

        if (candidate.length() > 18)
        {
            candidate = date.format(DateTimeFormatter.ofPattern("EEEE, d MMM"));
        }

        if (candidate.length() > 18)
        {
            candidate = date.format(DateTimeFormatter.ofPattern("EE, d MMM"));
        }

        return candidate;
    }
}
