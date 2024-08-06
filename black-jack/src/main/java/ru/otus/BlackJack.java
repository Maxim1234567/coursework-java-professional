package ru.otus;

import ru.otus.card.network.Network;

public interface BlackJack {
    void sitAtTable(String name);

    void sitAtTableComputer(String name);

    void play();

    void setNetwork(Network network);
}
