package ru.otus.gui;

import javax.swing.*;
import java.awt.*;

public class CardComponent extends JComponent {

    private JLabel icon;

    public CardComponent(ImageIcon imageIcon) {
        setLayout(new BorderLayout());

        icon = new JLabel(imageIcon);

        add(icon);
    }
}
