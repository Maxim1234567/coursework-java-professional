package ru.otus.facade;

import ru.otus.facade.listener.CardListener;
import ru.otus.facade.listener.PlayerListener;
import ru.otus.facade.listener.SayListener;
import ru.otus.facade.listener.ScoredListener;
import ru.otus.facade.listener.StatusListener;
import ru.otus.player.Say;

public interface FacadeModel {

    SayListener addSayListener(String name);

    void takeCard(String name, String card);

    void foldCards(String name);

    Say whoSay(String name);

    void updateScored(String name, int scored);

    void updateStatus(String status);

    void movePlayer(String name);

    void addCardListener(CardListener cardListener);

    void addScoredListener(ScoredListener scoredListener);

    void addStatusListener(StatusListener statusListener);

    void addPlayerListener(PlayerListener playerListener);

}
