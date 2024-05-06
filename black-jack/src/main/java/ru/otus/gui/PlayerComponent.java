package ru.otus.gui;

import javax.swing.*;

public class PlayerComponent extends JPanel {

    private String name;

    private JPanel cards;

    public PlayerComponent(String name, ImageIcon image, boolean up) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.name = name;

        JLabel jImage = new JLabel(image);
        JLabel jName = new JLabel(name);

        cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.X_AXIS));

        jImage.setAlignmentX(CENTER_ALIGNMENT);
        jName.setAlignmentX(CENTER_ALIGNMENT);

        if (up) {
            add(jName);
            add(jImage);
            add(cards);
        } else {
            add(cards);
            add(jImage);
            add(jName);
        }
    }

    public void add(CardComponent card) {
        cards.add(card);
    }

}
