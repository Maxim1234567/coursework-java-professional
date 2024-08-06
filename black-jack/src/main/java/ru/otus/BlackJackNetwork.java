package ru.otus;

import ru.otus.card.Card;
import ru.otus.card.CardService;
import ru.otus.card.network.Network;
import ru.otus.card.scores.CardScores;
import ru.otus.facade.FacadeModel;
import ru.otus.facade.listener.SayListener;
import ru.otus.model.Dto;
import ru.otus.model.State;
import ru.otus.player.Player;
import ru.otus.player.impl.PlayerComputer;
import ru.otus.player.impl.PlayerRealGUI;
import ru.otus.service.PlayerService;
import ru.otus.table.Table;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BlackJackNetwork implements BlackJack {

    private final Table table;

    private Network network;

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
        facadeModel.addSayListener(new SayListener() {
            @Override
            public void take() {
                network.send(
                        Dto.builder()
                                .name(name)
                                .state(State.TAKE)
                                .build()
                );
            }

            @Override
            public void pass() {
                network.send(
                        Dto.builder()
                                .name(name)
                                .state(State.PASS)
                                .build()
                );
            }
        });
        table.seat(new PlayerRealGUI(name, cardScores, facadeModel));
    }

    @Override
    public void sitAtTableComputer(String name) {
        table.seat(new PlayerComputer(name, cardScores));
    }

    @Override
    public void play() {
        while (true) {
            Dto dto = network.read();

            facadeModel.updateStatus(dto.getMessage());

            if (dto.getState() == State.START) {
                facadeModel.updateStatus(dto.getMessage());
                facadeModel.sitPlayerAtTable(dto.getName());
                break;
            }
        }

        while (true) {
            Dto dto = network.read();

            Player player = findPlayerByName(dto.getName());

            if (dto.getState() == State.END) {
                facadeModel.updateStatus(dto.getMessage());
                facadeModel.enabled(dto.getName(), false);
                break;
            }

            if (Objects.nonNull(dto.getCard())) {
                Card card = dto.getCard();
                player.takeCard(card);
                facadeModel.takeCard(player.getName(), cardService.getPathImage(card));
            }

            facadeModel.updateScored(player.getName(), player.getScore());
            facadeModel.updateStatus("Игрок " + player.getName() + " говорит " + dto.getState());
            facadeModel.enabled(dto.getName(), Objects.requireNonNullElse(dto.getIsSay(), false));
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

        facadeModel.end();
    }

    private Player findPlayerByName(String name) {
        return table.callPlayers().stream()
                .filter(p -> p.getName().equals(name))
                .findFirst()
                .orElseThrow(RuntimeException::new);

    }

    @Override
    public void setNetwork(Network network) {
        this.network = network;
    }
}
