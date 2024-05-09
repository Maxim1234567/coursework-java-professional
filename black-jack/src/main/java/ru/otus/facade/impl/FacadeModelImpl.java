package ru.otus.facade.impl;

import ru.otus.facade.FacadeModel;
import ru.otus.facade.listener.CardListener;
import ru.otus.facade.listener.PlayerListener;
import ru.otus.facade.listener.SayListener;
import ru.otus.facade.listener.ScoredListener;
import ru.otus.facade.listener.SitListener;
import ru.otus.facade.listener.StatusListener;
import ru.otus.player.Say;

public class FacadeModelImpl implements FacadeModel {

    private volatile Say say;

    private SitListener sitListener;

    private CardListener cardListener;

    private ScoredListener scoredListener;

    private StatusListener statusListener;

    private PlayerListener playerListener;

    @Override
    public SayListener addSayListener(String name) {
        return new SayListener() {
            @Override
            public void take() {
                say = Say.MORE;
            }

            @Override
            public void pass() {
                say = Say.PASS;
            }
        };
    }

    @Override
    public void takeCard(String name, String card) {
        cardListener.addCard(name, card);
    }

    @Override
    public void foldCards(String name) {
        cardListener.removeCards(name);
    }

    @Override
    public Say whoSay(String name) {
        say = null;

        while (say == null)
            ;

        return say;
    }

    @Override
    public void updateScored(String name, int scored) {
        scoredListener.updateScores(name, String.valueOf(scored));
    }

    @Override
    public void updateStatus(String status) {
        this.statusListener.updateStatus(status);
    }

    @Override
    public void sitPlayerAtTable(String name) {
        this.sitListener.sitPlayerAtTable(name);
    }

    @Override
    public void movePlayer(String name) {
        this.playerListener.playerMove(name);
    }

    @Override
    public void addCardListener(CardListener cardListener) {
        this.cardListener = cardListener;
    }

    @Override
    public void addScoredListener(ScoredListener scoredListener) {
        this.scoredListener = scoredListener;
    }

    @Override
    public void addStatusListener(StatusListener statusListener) {
        this.statusListener = statusListener;
    }

    @Override
    public void addPlayerListener(PlayerListener playerListener) {
        this.playerListener = playerListener;
    }

    @Override
    public void addSitListener(SitListener sitListener) {
        this.sitListener = sitListener;
    }
}
