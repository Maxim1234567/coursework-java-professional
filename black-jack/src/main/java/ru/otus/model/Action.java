package ru.otus.model;

import ru.otus.card.Card;

public record Action(String player, Say say, String message, State state, Card card) {
}
