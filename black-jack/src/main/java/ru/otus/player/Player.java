package ru.otus.player;

import ru.otus.card.Card;

import java.util.List;

public interface Player {

    void takeCard(Card card);

    Say say();

    boolean isBust();

    List<Card> giveCards();

}
