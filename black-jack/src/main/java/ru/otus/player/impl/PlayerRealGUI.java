package ru.otus.player.impl;

import ru.otus.card.Card;
import ru.otus.card.scores.CardScores;
import ru.otus.facade.FacadeModel;
import ru.otus.player.Player;
import ru.otus.player.Say;

import java.util.ArrayList;
import java.util.List;

import static ru.otus.Constants.TWENTY_ONE;

public class PlayerRealGUI implements Player {

    private final List<Card> cards;

    private final CardScores cardScores;

    private final String name;

    private final FacadeModel facadeModel;

    public PlayerRealGUI(
            String name,
            CardScores cardScores,
            FacadeModel facadeModel
    ) {
        this.cards = new ArrayList<>();
        this.name = name;
        this.cardScores = cardScores;
        this.facadeModel = facadeModel;
    }

    @Override
    public void takeCard(Card card) {
        cards.add(card);
    }

    @Override
    public Say say() {
        return facadeModel.whoSay(name);
    }

    @Override
    public boolean isBust() {
        return getScore() > TWENTY_ONE;
    }

    @Override
    public void foldCards() {
        cards.clear();
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
