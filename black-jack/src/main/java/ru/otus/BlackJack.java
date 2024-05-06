package ru.otus;

import ru.otus.card.CardDeck;
import ru.otus.card.impl.CardDeckImpl;
import ru.otus.card.scores.CardScores;
import ru.otus.card.scores.impl.CardScoresImpl;
import ru.otus.dealer.Dealer;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.player.Player;
import ru.otus.player.impl.PlayerComputer;
import ru.otus.player.impl.PlayerReal;
import ru.otus.service.DisplayPlayer;
import ru.otus.service.PlayerService;
import ru.otus.service.impl.DisplayPlayerConsole;
import ru.otus.service.impl.PlayerServiceImpl;
import ru.otus.table.Table;

import java.util.List;
import java.util.Map;

public class BlackJack {

    private final Table table;

    private final DisplayPlayer displayPlayer;

    private final PlayerService playerService;

    public BlackJack(
            Table table,
            DisplayPlayer displayPlayer,
            PlayerService playerService
    ) {
        this.table = table;
        this.displayPlayer = displayPlayer;
        this.playerService = playerService;
    }

    public void play() {
        CardDeck cardDeck = new CardDeckImpl();

        //Начало игры
        //1. Крупье сдаёт каждому игроку по две карты
        Dealer dealer = table.callDealer();

        int count = 2;
        while (count != 0) {
            for (Player player : table.callPlayers()) {
                player.takeCard(dealer.takeCardDeckAndGivePlayer(cardDeck));
            }

            count--;
        }

        //2. Крупье продолжает выдавать карты пока игроки просят или не перебрали
        while (true) {
            int stop = 0;

            for (Player player: table.callPlayers()) {
                //если не перебор, игрок говорит
                if (!player.isBust()) {
                    switch (player.say()) {
                        case MORE -> player.takeCard(dealer.takeCardDeckAndGivePlayer(cardDeck));
                        case PASS -> stop++;
                    }
                } else {
                    stop++;
                }
            }

            System.out.println("stop: " + stop + ", countPlayers: " + table.countPlayers());

            if (stop == table.countPlayers()) {
                break;
            }
        }

        //Определить победителя
        Map<Integer, List<Player>> groupPlayers = playerService.groupPlayerByScore(table.callPlayers());
        int pointWinner = playerService.findScoreWinner(groupPlayers);

        displayPlayer.winners(groupPlayers.remove(pointWinner));

        List<Player> otherPlayer = playerService.orderPlayerByScore(groupPlayers);

        displayPlayer.others(otherPlayer);

        //Отчистить результат игры у игроков
        for (Player player: table.callPlayers()) {
            player.giveCards();
        }
    }

    public static void main(String[] args) {
        PlayerService playerService = new PlayerServiceImpl();

        CardScores cardScores = new CardScoresImpl();

        Dealer dealer = new DealerImpl();

        Player player1 = new PlayerReal("Maxim", cardScores);
        Player computer2 = new PlayerComputer("computer2", cardScores);
        Player computer3 = new PlayerComputer("computer3", cardScores);
        Player computer4 = new PlayerComputer("computer4", cardScores);

        Table table = new Table(dealer);

        table.seat(player1);
        table.seat(computer2);
        table.seat(computer3);
        table.seat(computer4);

        DisplayPlayer displayPlayer = new DisplayPlayerConsole();

        BlackJack blackJack = new BlackJack(
                table,
                displayPlayer,
                playerService
        );

        while (true) {
            blackJack.play();
        }
    }
}
