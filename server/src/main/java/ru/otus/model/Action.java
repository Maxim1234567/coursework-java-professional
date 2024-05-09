package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.card.Card;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Action {
    private String player;
    private Say say;
    private String message;
    private State state;
    private Card card;
}