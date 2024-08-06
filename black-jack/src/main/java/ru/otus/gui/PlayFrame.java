package ru.otus.gui;

import ru.otus.BlackJack;
import ru.otus.BlackJackNetwork;
import ru.otus.Utils;
import ru.otus.card.impl.CardServiceImpl;
import ru.otus.card.network.Network;
import ru.otus.card.scores.impl.CardScoresImpl;
import ru.otus.controller.ControllerView;
import ru.otus.controller.impl.ControllerViewImpl;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.facade.FacadeModel;
import ru.otus.facade.impl.FacadeModelImpl;
import ru.otus.model.Dto;
import ru.otus.model.State;
import ru.otus.service.impl.PlayerServiceImpl;
import ru.otus.table.Table;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

import static ru.otus.Utils.loadImage;

public class PlayFrame {
    public static void main(String[] args) {
        String playerName = "Maxim2";

        AtomicReference<Network> network = new AtomicReference<>(new Network("localhost", 8080));
        network.get().send(new Dto(playerName, State.CONNECTION, false, null, playerName + " подключился"));

        FacadeModel facadeModel = new FacadeModelImpl();

        BlackJack blackJack = new BlackJackNetwork(
                new Table(new DealerImpl()),
                network.get(),
                new CardScoresImpl(),
                facadeModel,
                new CardServiceImpl(),
                new PlayerServiceImpl()
        );

        ControllerView controllerView = new ControllerViewImpl(
                facadeModel,
                blackJack
        );

        ImageIcon avatar = loadImage("/player.png", 60, 60);
        ImageIcon shirt = loadImage("/card/SHIRT.png", 100, 100);

        controllerView.addPlayer(playerName);

        AtomicReference<PlayerComponent> playerReal2 = new AtomicReference<>();
//        PlayerComponent playerReal = new PlayerComponent("Maxim", avatar, false, controllerView.addPlayerListener("Maxim"));
        PlayerComponent playerReal = new PlayerComponent(playerName, avatar, false, controllerView.addPlayerListener());

        controllerView.putPlayerComponent(playerReal);
        controllerView.addEnableListener((name, enable) -> {
            if (playerName.equals(name))
                playerReal.setEnabled(enable);
        });

        CardComponent cardShirt = new CardComponent(shirt);

        JFrame frame = new JFrame("Black Jack");

        JLabel jStatus = new JLabel("Статус");

        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.X_AXIS));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(Utils.GREEN);

        frame.getContentPane().add(BorderLayout.SOUTH, playerReal);

        frame.getContentPane().add(BorderLayout.EAST, cardShirt);
        frame.getContentPane().add(BorderLayout.CENTER, jStatus);

        frame.setVisible(true);

        controllerView.addStatusListener(jStatus::setText);
        controllerView.addEndGameListener(() -> {
            network.get().disconnect();
            network.set(new Network("localhost", 8080));
            network.get().send(new Dto(playerName, State.CONNECTION, false, null, playerName + " подключился"));
            blackJack.setNetwork(network.get());
            frame.getContentPane().remove(playerReal2.get());
        });

        controllerView.addSitListener(name -> {
            playerReal2.set(new PlayerComputer(name, avatar, true, null));
            controllerView.addPlayer(name);
            controllerView.putPlayerComponent(playerReal2.get());
            frame.getContentPane().add(BorderLayout.NORTH, playerReal2.get());
        });

        while (true) {
            controllerView.play();
        }
    }
}
