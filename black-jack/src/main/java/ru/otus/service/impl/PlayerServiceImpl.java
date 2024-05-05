package ru.otus.service.impl;

import ru.otus.exception.PointWinnerNotFound;
import ru.otus.player.Player;
import ru.otus.service.PlayerService;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class PlayerServiceImpl implements PlayerService {
    @Override
    public Map<Integer, List<Player>> groupPlayerByScore(List<Player> players) {
        return players.stream().collect(groupingBy(Player::getScore));
    }

    @Override
    public int findScoreWinner(Map<Integer, List<Player>> groupPlayers) {
        return groupPlayers.keySet().stream().mapToInt(Integer::intValue).max().orElseThrow(PointWinnerNotFound::new);
    }

    @Override
    public List<Player> orderPlayerByScore(Map<Integer, List<Player>> groupPlayers) {
        return groupPlayers.values().stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(Player::getScore))
                .toList();
    }
}
