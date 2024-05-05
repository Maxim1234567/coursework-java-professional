package ru.otus.player.impl;

import ru.otus.card.Card;
import ru.otus.player.Player;
import ru.otus.player.Say;
import ru.otus.player.State;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.Constants.TWENTY_ONE;

public class PlayerComputer implements Player {

    private static final int LIMIT = 17;


    private final List<Card> cards;

    private State state;

    private int point = 0;

    public PlayerComputer() {
        cards = new ArrayList<>();
    }

    @Override
    public void takeCard(Card card) {
        cards.add(card);
    }

    @Override
    public Say say() {
        if (point >= LIMIT) {
            return Say.PASS;
        }

        return Say.MORE;
    }

    @Override
    public boolean isBust() {
        return point > TWENTY_ONE;
    }

    @Override
    public List<Card> giveCards() {
        List<Card> throwCards = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            throwCards.add(cards.removeFirst());
        }

        return throwCards;
    }
}
