package ru.otus.gui;

import ru.otus.Utils;
import ru.otus.facade.listener.SayListener;

import javax.swing.*;
import java.awt.*;

public class PlayerComponent extends JPanel {

    private final String name;

    private final JPanel cards;

    private final JLabel jScores;

    protected JButton pass;

    protected JButton take;

    public PlayerComponent(String name, ImageIcon image, boolean up, SayListener sayListener) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        this.name = name;

        pass = new JButton("Pass");
        take = new JButton("Take");

        pass.addActionListener(e -> {
            sayListener.pass();
            setEnabled(false);
        });
        take.addActionListener(e -> {
            sayListener.take();
            setEnabled(false);
        });

        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons, BoxLayout.X_AXIS));
        buttons.add(pass);
        buttons.add(take);

        JLabel jTextScores = new JLabel("Очки: ");
        jScores = new JLabel("0");

        JPanel scores = new JPanel();
        scores.setLayout(new BoxLayout(scores, BoxLayout.X_AXIS));

        scores.add(jTextScores);
        scores.add(jScores);

        JLabel jImage = new JLabel(image);
        JLabel jName = new JLabel(name);

        cards = new JPanel();
//        cards.setLayout(new BoxLayout(cards, BoxLayout.X_AXIS));
        cards.setLayout(new GridLayout());
        cards.setBackground(Utils.GREEN);

        jImage.setAlignmentX(CENTER_ALIGNMENT);
        jName.setAlignmentX(CENTER_ALIGNMENT);

        if (up) {
            add(buttons);
            add(scores);
            add(jName);
            add(jImage);
            add(cards);
        } else {
            add(cards);
            add(jImage);
            add(jName);
            add(scores);
            add(buttons);
        }

        setEnabled(false);

        setBackground(Utils.GREEN);
    }

    public void add(CardComponent card) {
        cards.add(card);
    }

    public void remove() {
        cards.removeAll();
    }

    public void setScores(String scores) {
        jScores.setText(scores);
    }

    public String getName() {
        return name;
    }

    public void setEnabled(boolean isEnabled) {
        pass.setEnabled(isEnabled);
        take.setEnabled(isEnabled);
    }

}
