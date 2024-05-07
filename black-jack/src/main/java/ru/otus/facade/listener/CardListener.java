package ru.otus.facade.listener;

import java.util.List;

public interface CardListener {
    void addCard(String name, String card);

    void removeCards(String name);
}
