package ru.otus.player;

import ru.otus.BlackJackServer;
import ru.otus.card.Card;
import ru.otus.card.scores.CardScores;
import ru.otus.card.scores.impl.CardScoresImpl;
import ru.otus.dto.Dto;
import ru.otus.dto.State;
import ru.otus.socket.WSocket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.Constants.TWENTY_ONE;

public class Player {

    private String name;

    private final WSocket socket;

    private final CardScores cardScores;

    private List<Card> cards;

    private final BlackJackServer server;

    public Player(BlackJackServer server, Socket socket) {
        this.server = server;
        this.socket = new WSocket(socket);
        this.cardScores = new CardScoresImpl();
        this.cards = new ArrayList<>();
        new Thread(this::communicate).start();
    }

    public void send(Dto dto) {
        socket.send(dto);
    }

    public Dto read() {
        return socket.read();
    }

    public String getName() {
        return name;
    }

    private void communicate() {
        Dto dto = socket.read();

        if (dto.getState() == State.CONNECTION) {
            name = dto.getName();
            server.sitAtTable(this);
        }
    }

    public void takeCard(Card card) {
        cards.add(card);
    }


    public boolean isBust() {
        return getScore() > TWENTY_ONE;
    }

    public int getScore() {
        return cardScores.score(cards);
    }

    public void disconnect() {
        socket.disconnect();
        server.remove(this);
    }
}
