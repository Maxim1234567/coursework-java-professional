package ru.otus;

import ru.otus.gui.PlayFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Utils {

    public static final Color GREEN = new Color(0, 100, 0);

    public static ImageIcon loadImage(String name, int scaledWidth, int scaledHeight) {
        try {
            Image image = ImageIO.read(PlayFrame.class.getResourceAsStream(name));
            return new ImageIcon(image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_DEFAULT));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
