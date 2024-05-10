package ru.otus.dealer.impl;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;
import ru.otus.dealer.Dealer;

public class DealerImpl implements Dealer {

    @Override
    public Card takeCardDeckAndGivePlayer(CardDeck cardDeck) {
        return cardDeck.give();
    }

}
