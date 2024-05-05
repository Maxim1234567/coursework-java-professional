package ru.otus;

import ru.otus.card.CardDeck;
import ru.otus.dealer.Dealer;
import ru.otus.player.Player;
import ru.otus.table.Table;

public class BlackJack {

    private final Table table;

    private final CardDeck cardDeck;

    public BlackJack(
            Table table,
            CardDeck cardDeck
    ) {
        this.table = table;
        this.cardDeck = cardDeck;
    }

    public void play() {
        //Начало игры
        //1. Крупье сдаёт каждому игроку по две карты
        Dealer dealer = table.callDealer();

        for (int i = 0; i < table.countPlayers(); i++) {
            Player player = table.callPlayer(i);

            player.takeCard(dealer.takeCardDeckAndGivePlayer(cardDeck));
            player.takeCard(dealer.takeCardDeckAndGivePlayer(cardDeck));
        }

        //2. Крупье продолжает выдавать карты пока игроки просят или не перебрали
        while (true) {
            int stop = 0;

            for (int i = 0; i < table.countPlayers(); i++) {
                Player player = table.callPlayer(i);

                //если не перебор, игрок говорит
                if (!player.isBust()) {
                    switch (player.say()) {
                        case MORE -> player.takeCard(dealer.takeCardDeckAndGivePlayer(cardDeck));
                        case PASS -> stop++;
                    }
                } else {
                    stop++;
                }
            }

            if (stop == table.countPlayers()) {
                break;
            }
        }

        //Определить победителя

    }

}
