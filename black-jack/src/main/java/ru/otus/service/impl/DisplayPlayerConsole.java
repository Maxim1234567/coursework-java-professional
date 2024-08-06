package ru.otus.service.impl;

import ru.otus.player.Player;
import ru.otus.service.DisplayPlayer;

import java.util.List;

public class DisplayPlayerConsole implements DisplayPlayer {
    @Override
    public void winners(List<Player> players) {
        for (Player player: players) {
            System.out.println("Победитель: " + player.getName() + ", очки: " + player.getScore());
        }
    }

    @Override
    public void others(List<Player> players) {
        for (Player player: players) {
            System.out.println("Имя: " + player.getName() + ", очки: " + player.getScore());
        }
    }
}
