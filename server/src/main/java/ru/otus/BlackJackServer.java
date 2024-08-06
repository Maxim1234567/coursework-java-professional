package ru.otus;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;
import ru.otus.card.impl.CardDeckImpl;
import ru.otus.dto.Dto;
import ru.otus.dto.State;
import ru.otus.player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static ru.otus.Constants.PLAYERS;

public class BlackJackServer {

    private int port;

    private List<Player> players;

    public BlackJackServer(int port) {
        this.port = port;
        this.players = new ArrayList<>();
    }

    public void start() {
        try(ServerSocket server = new ServerSocket(port)) {
            while (true) {
                Socket socket = server.accept();
                new Player(this, socket);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void sitAtTable(Player player) {
        players.add(player);

        if (players.size() < PLAYERS) {
            for (Player p: players) {
                p.send(
                        Dto.builder()
                                .state(State.WAIT)
                                .message("Присоединился игрок " + player.getName())
                                .build()
                );
            }
        } else if (players.size() == PLAYERS) {
            players.get(0).send(
                    Dto.builder()
                            .state(State.START)
                            .name(players.get(1).getName())
                            .message("Игра началась")
                            .build()
            );

            players.get(1).send(
                    Dto.builder()
                            .state(State.START)
                            .name(players.get(0).getName())
                            .message("Игра началась")
                            .build()
            );

            play();
        }
    }

    public synchronized void remove(Player player) {
        players.remove(player);
    }

    public synchronized void broadcastSend(Dto dto) {
        for (Player player: players) {
            player.send(dto);
        }
    }

    public void play() {
        CardDeck cardDeck = new CardDeckImpl();

        int count = PLAYERS;

        while (count != 0) {
            for (Player player: players) {
                Card card = cardDeck.give();
                player.takeCard(card);
                broadcastSend(Dto.builder().name(player.getName()).card(card).build());
            }

            count--;
        }

        for (int i = 0; i < players.size(); i++) {
            if (i == 0) {
                players.get(i).send(
                        Dto.builder()
                                .name(players.get(i).getName())
                                .isSay(true)
                                .build()
                );
            } else {
                players.get(i).send(
                        Dto.builder()
                                .name(players.get(i).getName())
                                .isSay(false)
                                .build()
                );
            }
        }

        while (true) {
            int stop = 0;

            for (Player player: players) {
                if (!player.isBust()) {
                    player.send(
                            Dto.builder()
                                    .name(player.getName())
                                    .isSay(true)
                                    .build()
                    );

                    Dto dto = player.read();

                    switch (dto.getState()) {
                        case TAKE -> {
                            Card card = cardDeck.give();
                            player.takeCard(card);
                            broadcastSend(Dto.builder()
                                    .name(player.getName())
                                    .card(card)
                                    .isSay(false)
                                    .build());
                        }

                        case PASS -> {
                            broadcastSend(Dto.builder()
                                    .name(player.getName())
                                    .isSay(false)
                                    .build());
                            stop++;
                        }
                    }
                } else {
                    stop++;
                    player.send(
                            Dto.builder()
                                    .name(player.getName())
                                    .isSay(false)
                                    .build()
                    );
                }
            }

            if (stop == PLAYERS)
                break;
        }

        for (Player player: players) {
            System.out.println(player.getName() + " send and disconnect");

            player.send(
                    Dto.builder()
                            .name(player.getName())
                            .message("Игра закончилась")
                            .state(State.END)
                            .build()
            );
        }

        players.getFirst().disconnect();
        players.getFirst().disconnect();

        System.out.println("End: " + players.size());
    }
}
