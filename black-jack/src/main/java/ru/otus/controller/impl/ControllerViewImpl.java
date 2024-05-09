package ru.otus.controller.impl;

import ru.otus.BlackJack;
import ru.otus.BlackJackLocal;
import ru.otus.controller.ControllerView;
import ru.otus.facade.FacadeModel;
import ru.otus.facade.listener.CardListener;
import ru.otus.facade.listener.SayListener;
import ru.otus.facade.listener.SitListener;
import ru.otus.facade.listener.StatusListener;
import ru.otus.gui.CardComponent;
import ru.otus.gui.PlayerComponent;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static ru.otus.Utils.loadImage;

public class ControllerViewImpl implements ControllerView {

    private final FacadeModel facadeModel;
    private final Map<String, PlayerComponent> players;

    private final BlackJack blackJack;

    public ControllerViewImpl(
            FacadeModel facadeModel,
            BlackJack blackJack
    ) {
        this.facadeModel = facadeModel;
        this.players = new HashMap<>();
        this.blackJack = blackJack;

        this.facadeModel.addCardListener(new CardListener() {
            @Override
            public void addCard(String name, String card) {
                PlayerComponent player = ControllerViewImpl.this.players.get(name);

                ImageIcon imageCard = loadImage(card, 100, 100);
                CardComponent cardComponent = new CardComponent(imageCard);

                player.add(cardComponent);
            }

            @Override
            public void removeCards(String name) {
                PlayerComponent player = ControllerViewImpl.this.players.get(name);
                player.remove();
            }
        });

        this.facadeModel.addScoredListener((name, scores) -> {
            PlayerComponent player = ControllerViewImpl.this.players.get(name);
            player.setScores(scores);
        });

        this.facadeModel.addPlayerListener((name) -> {
            PlayerComponent player = ControllerViewImpl.this.players.get(name);
            player.setEnabled(true);
        });
    }

    @Override
    public SayListener addPlayerListener(String name) {
        return facadeModel.addSayListener(name);
    }

    @Override
    public void addPlayer(String name) {
        blackJack.sitAtTable(name);
    }

    @Override
    public void addPlayerComputer(String name) {
        blackJack.sitAtTableComputer(name);
    }

    @Override
    public void addSitListener(SitListener sitListener) {
        facadeModel.addSitListener(sitListener);
    }

    @Override
    public void putPlayerComponent(PlayerComponent playerComponent) {
        players.put(playerComponent.getName(), playerComponent);
    }

    @Override
    public void addStatusListener(StatusListener statusListener) {
        facadeModel.addStatusListener(statusListener);
    }

    @Override
    public void play() {
        blackJack.play();
    }
}
