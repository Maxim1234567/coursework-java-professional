package ru.otus.card.scores.impl;

import ru.otus.card.Card;
import ru.otus.card.Suit;
import ru.otus.card.scores.CardScores;

import java.util.List;

public class CardScoresImpl implements CardScores {
    @Override
    public int score(List<Card> cards) {
        return cards.stream()
                .map(Card::getSuit)
                .mapToInt(Suit::getValue)
                .sum();
    }
}
