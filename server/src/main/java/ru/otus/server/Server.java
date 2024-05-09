package ru.otus.server;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;
import ru.otus.card.impl.CardDeckImpl;
import ru.otus.handler.PlayerHandler;
import ru.otus.model.Action;
import ru.otus.model.Say;
import ru.otus.model.State;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {

    private int port;

    private CardDeck cardDeck;

    private List<PlayerHandler> player;

    public Server(int port) {
        this.port = port;
        this.player = new ArrayList<>();
    }

    public void start() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new PlayerHandler(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void subscribe(PlayerHandler player) {
        this.player.add(player);

        if (this.player.size() == 1) {
            player.send(new Action(
                    null,
                    null,
                    player.getName() + " ждёт ещё одного игрока",
                    State.WAIT,
                    null
                    )
            );
        } else {
            for (PlayerHandler p: this.player) {
                Action action = new Action(
                        p.getName(),
                        null,
                        "Игрок " + p.getName() + " приветсвует тебя",
                        State.START,
                        null
                );

                broadcastMessage(p, action);
                cardDeck = new CardDeckImpl();
            }
        }
    }

    public synchronized void broadcastMessage(PlayerHandler playerHandler, Action action) {
        for (PlayerHandler play: player.stream().filter(p -> p != playerHandler).toList()) {
            play.send(action);
        }
    }

    public synchronized void broadcastMessage(Action a) {
        Card card = null;

        if (a.getState() == State.TAKE && Objects.nonNull(a.getSay())) {
            if (a.getSay() == Say.MORE) {
                card = cardDeck.give();
            }
        }

        Action action = new Action(
                a.getPlayer(),
                a.getSay(),
                a.getMessage(),
                a.getState(),
                card
        );

        for (PlayerHandler player: player) {
            player.send(action);
        }
    }

}
