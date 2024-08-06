package ru.otus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.otus.card.Card;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Dto {
    private String name;

    private State state;

    private Boolean isSay;

    private Card card;

    private String message;
}
