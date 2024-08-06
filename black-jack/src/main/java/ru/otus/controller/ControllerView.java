package ru.otus.controller;

import ru.otus.facade.listener.EnableListener;
import ru.otus.facade.listener.EndGameListener;
import ru.otus.facade.listener.SayListener;
import ru.otus.facade.listener.SitListener;
import ru.otus.facade.listener.StatusListener;
import ru.otus.gui.PlayerComponent;

public interface ControllerView {
    SayListener addPlayerListener(String name);

    SayListener addPlayerListener();

    void addPlayer(String name);

    void addPlayerComputer(String name);

    void addSitListener(SitListener sitListener);

    void putPlayerComponent(PlayerComponent playerComponent);

    void addStatusListener(StatusListener statusListener);

    void addEnableListener(EnableListener enableListener);

    void play();

    void addEndGameListener(EndGameListener endGameListener);
}
