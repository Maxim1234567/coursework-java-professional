package ru.otus.card.impl;

import ru.otus.card.Card;
import ru.otus.card.CardService;

public class CardServiceImpl implements CardService {

    @Override
    public String getPathImage(Card card) {
        String prefix = "/card/";
        String postfix = ".png";
        return prefix + card.getCardValue().name() + "_" + card.getSuit().name() + postfix;
    }

}
