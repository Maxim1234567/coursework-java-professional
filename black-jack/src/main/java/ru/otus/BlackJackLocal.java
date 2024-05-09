package ru.otus;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;
import ru.otus.card.CardService;
import ru.otus.card.impl.CardDeckImpl;
import ru.otus.card.impl.CardServiceImpl;
import ru.otus.card.scores.CardScores;
import ru.otus.card.scores.impl.CardScoresImpl;
import ru.otus.dealer.Dealer;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.facade.FacadeModel;
import ru.otus.facade.impl.FacadeModelImpl;
import ru.otus.player.Player;
import ru.otus.player.impl.PlayerComputer;
import ru.otus.player.impl.PlayerRealConsole;
import ru.otus.player.impl.PlayerRealGUI;
import ru.otus.service.DisplayPlayer;
import ru.otus.service.PlayerService;
import ru.otus.service.impl.DisplayPlayerConsole;
import ru.otus.service.impl.PlayerServiceImpl;
import ru.otus.table.Table;

import java.util.List;
import java.util.Map;

public class BlackJackLocal implements BlackJack {

    private final Table table;

    private final PlayerService playerService;

    private final FacadeModel facadeModel;

    private final CardService cardService;

    private final CardScores cardScores;

    public BlackJackLocal(
            Table table,
            PlayerService playerService,
            FacadeModel facadeModel,
            CardService cardService,
            CardScores cardScores
    ) {
        this.table = table;
        this.playerService = playerService;
        this.facadeModel = facadeModel;
        this.cardService = cardService;
        this.cardScores = cardScores;
    }

    @Override
    public void sitAtTable(String name) {
        table.seat(new PlayerRealGUI(name, cardScores, facadeModel));
    }

    @Override
    public void sitAtTableComputer(String name) {
        table.seat(new PlayerComputer(name, cardScores));
    }

    @Override
    public void play() {
        CardDeck cardDeck = new CardDeckImpl();

        facadeModel.updateStatus("Начало игры");

        //Начало игры
        //1. Крупье сдаёт каждому игроку по две карты
        Dealer dealer = table.callDealer();

        int count = 2;
        while (count != 0) {
            for (Player player : table.callPlayers()) {
                Card card = dealer.takeCardDeckAndGivePlayer(cardDeck);
                player.takeCard(card);
                facadeModel.updateStatus("Дилер сдаёт карту игроку " + player.getName());
                facadeModel.takeCard(player.getName(), cardService.getPathImage(card));
                facadeModel.updateScored(player.getName(), player.getScore());
            }

            count--;
        }

        //2. Крупье продолжает выдавать карты пока игроки просят или не перебрали
        while (true) {
            int stop = 0;

            for (Player player: table.callPlayers()) {
                //если не перебор, игрок говорит
                if (!player.isBust()) {
                    facadeModel.movePlayer(player.getName());
                    switch (player.say()) {
                        case MORE -> {
                            Card card = dealer.takeCardDeckAndGivePlayer(cardDeck);
                            player.takeCard(card);
                            facadeModel.takeCard(player.getName(), cardService.getPathImage(card));
                            facadeModel.updateScored(player.getName(), player.getScore());
                            facadeModel.updateStatus("Игрок " + player.getName() + " говорит ЕЩЁ");
                        }
                        case PASS -> {
                            stop++;
                            facadeModel.updateStatus("Игрок " + player.getName() + " говорит ВСЁ");
                        }
                    }
                } else {
                    stop++;
                    facadeModel.updateStatus("У игрока " + player.getName() + " перебор");
                }
            }

            if (stop == table.countPlayers()) {
                break;
            }
        }

        //Определить победителя
        Map<Integer, List<Player>> groupPlayers = playerService.groupPlayerByScore(table.callPlayers());
        int pointWinner = playerService.findScoreWinner(groupPlayers);

        List<Player> playerWinners = groupPlayers.get(pointWinner);

        for (Player player: playerWinners) {
            facadeModel.updateStatus("Победил игрок " + player.getName() + " набрал очков " + player.getScore());
        }

        //Отчистить результат игры у игроков
        for (Player player: table.callPlayers()) {
            player.foldCards();
            facadeModel.foldCards(player.getName());
        }
    }

    public static void main(String[] args) {
        PlayerService playerService = new PlayerServiceImpl();

        CardScores cardScores = new CardScoresImpl();

        Dealer dealer = new DealerImpl();

        Player player1 = new PlayerRealConsole("Maxim", cardScores);
        Player computer2 = new PlayerComputer("computer2", cardScores);
        Player computer3 = new PlayerComputer("computer3", cardScores);
        Player computer4 = new PlayerComputer("computer4", cardScores);

        Table table = new Table(dealer);

        table.seat(player1);
        table.seat(computer2);
        table.seat(computer3);
        table.seat(computer4);

        DisplayPlayer displayPlayer = new DisplayPlayerConsole();

        BlackJackLocal blackJack = new BlackJackLocal(
                table,
                playerService,
                new FacadeModelImpl(),
                new CardServiceImpl(),
                new CardScoresImpl()
        );

        while (true) {
            blackJack.play();
        }
    }
}
