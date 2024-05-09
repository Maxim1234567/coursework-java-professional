package ru.otus;

import ru.otus.card.Card;
import ru.otus.card.CardService;
import ru.otus.card.network.Network;
import ru.otus.card.scores.CardScores;
import ru.otus.facade.FacadeModel;
import ru.otus.model.Action;
import ru.otus.model.State;
import ru.otus.player.Player;
import ru.otus.player.Say;
import ru.otus.player.impl.PlayerComputer;
import ru.otus.player.impl.PlayerRealGUI;
import ru.otus.service.PlayerService;
import ru.otus.table.Table;

import java.util.List;
import java.util.Map;

public class BlackJackNetwork implements BlackJack {

    private final Table table;

    private final Network network;

    private final CardScores cardScores;

    private final CardService cardService;

    private final FacadeModel facadeModel;

    private final PlayerService playerService;

    public BlackJackNetwork(
            Table table,
            Network network,
            CardScores cardScores,
            FacadeModel facadeModel,
            CardService cardService,
            PlayerService playerService
    ) {
        this.table = table;
        this.network = network;
        this.cardScores = cardScores;
        this.facadeModel = facadeModel;
        this.cardService = cardService;
        this.playerService = playerService;
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
        while (true) {
            Action action = network.read();
            facadeModel.updateStatus(action.getMessage());

            if (action.getState() == State.START) {
                facadeModel.sitPlayerAtTable(action.getPlayer());
                break;
            }
        }

        int count = 2;
        while (count != 0) {
            for (Player player : table.callPlayers()) {
                Card card = takeCard(player.getName());
                player.takeCard(card);
                facadeModel.updateStatus("Дилер сдаёт карту игроку " + player.getName());
                facadeModel.takeCard(player.getName(), cardService.getPathImage(card));
                facadeModel.updateScored(player.getName(), player.getScore());
            }

            count--;
        }

        while (true) {
            int stop = 0;

            for (Player player: table.callPlayers()) {
                //если не перебор, игрок говорит
                if (!player.isBust()) {
                    facadeModel.movePlayer(player.getName());
                    switch (player.say()) {
                        case MORE -> {
                            Card card = takeCard(player.getName());
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

    private Card takeCard(String name) {
        network.send(new Action(name, Say.MORE, null, State.TAKE, null));
        Action response = network.read();
        return response.getCard();
    }

}
