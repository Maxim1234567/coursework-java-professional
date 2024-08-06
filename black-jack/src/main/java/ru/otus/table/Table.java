package ru.otus.table;

import ru.otus.dealer.Dealer;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final Dealer dealer;

    private final List<Player> players;


    public Table(
            Dealer dealer
    ) {
        this.dealer = dealer;
        this.players = new ArrayList<>();
    }


    public void seat(Player player) {
        this.players.add(player);
    }

    public Dealer callDealer() {
        return dealer;
    }

    public List<Player> callPlayers() {
        return players;
    }

    public int countPlayers() {
        return players.size();
    }

}
