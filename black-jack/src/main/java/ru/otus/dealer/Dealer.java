package ru.otus.dealer;

import ru.otus.card.Card;
import ru.otus.card.CardDeck;

public interface Dealer {
    Card takeCardDeckAndGivePlayer(CardDeck cardDeck);
}
