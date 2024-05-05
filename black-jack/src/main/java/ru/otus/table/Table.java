package ru.otus.table;

import ru.otus.dealer.Dealer;
import ru.otus.dealer.impl.DealerImpl;
import ru.otus.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final Dealer dealer;

    private final List<Player> players;


    public Table() {
        this.dealer = new DealerImpl();
        this.players = new ArrayList<>();
    }


    public void seat(Player player) {
        this.players.add(player);
    }

    public void seats(List<Player> players) {
        for (Player player: players) {
            seat(player);
        }
    }

    public Dealer callDealer() {
        return dealer;
    }

    public Player callPlayer(int index) {
        return players.get(index);
    }

    public int countPlayers() {
        return players.size();
    }

}
