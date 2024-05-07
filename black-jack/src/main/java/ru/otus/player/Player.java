package ru.otus.player;

import ru.otus.card.Card;
import ru.otus.facade.FacadeModel;

import java.util.List;

public interface Player {

    void takeCard(Card card);

    Say say(FacadeModel facadeModel);

    boolean isBust();

    List<Card> foldCards();

    int getScore();

    String getName();

}
