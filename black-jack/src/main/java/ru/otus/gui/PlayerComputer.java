package ru.otus.gui;

import ru.otus.facade.listener.SayListener;

import javax.swing.*;

public class PlayerComputer extends PlayerComponent {

    public PlayerComputer(String name, ImageIcon image, boolean up, SayListener sayListener) {
        super(name, image, up, sayListener);
        pass.setVisible(false);
        take.setVisible(false);
    }

}
