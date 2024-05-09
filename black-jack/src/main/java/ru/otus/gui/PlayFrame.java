package ru.otus.gui;

import ru.otus.BlackJack;
import ru.otus.Utils;
import ru.otus.card.impl.CardServiceImpl;
import ru.otus.card.scores.impl.CardScoresImpl;
import ru.otus.controller.ControllerView;
import ru.otus.controller.impl.ControllerViewImpl;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.facade.FacadeModel;
import ru.otus.facade.impl.FacadeModelImpl;
import ru.otus.service.impl.PlayerServiceImpl;
import ru.otus.table.Table;

import javax.swing.*;
import java.awt.*;

import static ru.otus.Utils.loadImage;

public class PlayFrame {
    public static void main(String[] args) {
        FacadeModel facadeModel = new FacadeModelImpl();

        BlackJack blackJack = new BlackJack(
                new Table(new DealerImpl()),
                new PlayerServiceImpl(),
                facadeModel,
                new CardServiceImpl(),
                new CardScoresImpl()
        );

        ControllerView controllerView = new ControllerViewImpl(
                facadeModel,
                blackJack
        );

        ImageIcon avatar = loadImage("/player.png", 60, 60);
        ImageIcon shirt = loadImage("/card/SHIRT.png", 100, 100);

//        PlayerComponent playerReal = new PlayerComponent("Maxim", avatar, false, controllerView.addPlayerListener("Maxim"));
//        PlayerComponent playerReal2 = new PlayerComputer("Maxim2", avatar, true, null);

        PlayerComponent playerReal = new PlayerComputer("Maxim", avatar, true, null);
        PlayerComponent playerReal2 = new PlayerComponent("Maxim2", avatar, false, controllerView.addPlayerListener("Maxim2"));

//        controllerView.addPlayer("Maxim");
//        controllerView.addPlayer("Maxim2");

//        controllerView.addPlayerNetworkSend("Maxim");
//        controllerView.addPlayerNetworkGet("Maxim2");

        controllerView.addPlayerNetworkGet("Maxim");
        controllerView.addPlayerNetworkSend("Maxim2");

        controllerView.putPlayerComponent(playerReal);
        controllerView.putPlayerComponent(playerReal2);

        CardComponent cardShirt = new CardComponent(shirt);

        JFrame frame = new JFrame("Black Jack");

        JLabel jStatus = new JLabel("Статус");

        JPanel cards = new JPanel();
        cards.setLayout(new BoxLayout(cards, BoxLayout.X_AXIS));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(Utils.GREEN);

//        frame.getContentPane().add(BorderLayout.SOUTH, playerReal);
//        frame.getContentPane().add(BorderLayout.NORTH, playerReal2);

        frame.getContentPane().add(BorderLayout.NORTH, playerReal);
        frame.getContentPane().add(BorderLayout.SOUTH, playerReal2);

        frame.getContentPane().add(BorderLayout.EAST, cardShirt);
        frame.getContentPane().add(BorderLayout.CENTER, jStatus);

        frame.setVisible(true);

        controllerView.addStatusListener(jStatus::setText);

        while (true) {
            controllerView.play();
        }
    }
}
