package ru.otus.player.impl;

import ru.otus.card.Card;
import ru.otus.card.scores.CardScores;
import ru.otus.player.Player;
import ru.otus.player.Say;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.Constants.TWENTY_ONE;

public class PlayerComputer implements Player {

    private static final int LIMIT = 17;


    private final List<Card> cards;

    private final CardScores cardScores;

    private final String name;

    public PlayerComputer(
            String name,
            CardScores cardScores
    ) {
        this.cards = new ArrayList<>();
        this.name = name;
        this.cardScores = cardScores;
    }

    @Override
    public void takeCard(Card card) {
        cards.add(card);
    }

    @Override
    public Say say() {
        if (getScore() >= LIMIT) {
            return Say.PASS;
        }
        return Say.MORE;
    }

    @Override
    public boolean isBust() {
        return getScore() > TWENTY_ONE;
    }

    @Override
    public List<Card> foldCards() {
        List<Card> throwCards = new ArrayList<>();

        for (int i = 0; i < cards.size(); i++) {
            throwCards.add(cards.removeFirst());
        }

        return throwCards;
    }

    @Override
    public int getScore() {
        return cardScores.score(cards);
    }

    @Override
    public String getName() {
        return name;
    }
}
