package ru.otus.card.impl;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;
import ru.otus.card.CardValue;
import ru.otus.card.Suit;

import java.util.Collections;
import java.util.Stack;

public class CardDeckImpl implements CardDeck {

    private final Stack<Card> cards;

    public CardDeckImpl() {
        cards = new Stack<>();

        for (CardValue value: CardValue.values()) {
            for (Suit suit: Suit.values()) {
                cards.add(new Card(suit, value));
            }
        }

        Collections.shuffle(cards);
    }

    @Override
    public Card give() {
        return cards.pop();
    }

}
