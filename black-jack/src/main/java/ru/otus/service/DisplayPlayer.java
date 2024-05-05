package ru.otus.service;

import ru.otus.player.Player;

import java.util.List;

public interface DisplayPlayer {
    void winners(List<Player> players);

    void others(List<Player> players);
}
