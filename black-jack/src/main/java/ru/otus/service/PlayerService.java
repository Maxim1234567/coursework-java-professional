package ru.otus.service;

import ru.otus.player.Player;

import java.util.List;
import java.util.Map;

public interface PlayerService {

    Map<Integer, List<Player>> groupPlayerByScore(List<Player> players);

    int findScoreWinner(Map<Integer, List<Player>> groupPlayers);

    List<Player> orderPlayerByScore(Map<Integer, List<Player>> groupPlayers);
}
