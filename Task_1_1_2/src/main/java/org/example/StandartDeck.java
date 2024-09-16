package org.example;

import java.util.Random;

public class StandartDeck {

    private int curTop;
    static private String[] cardNames =
            {"Двойка треф", "Двойка бубен", "Двойка червей", "Двойка пик",
            "Тройка треф", "Тройка бубен", "Тройка червей", "Тройка пик",
            "Четверка треф", "Четверка бубен", "Четверка червей", "Четверка пик",
            "Пятерка треф", "Пятерка бубен", "Пятерка червей", "Пятерка пик",
            "Шестерка треф", "Шестерка бубен", "Шестерка червей", "Шестерка пик",
            "Семерка треф", "Семерка бубен", "Семерка червей", "Семерка пик",
            "Восьмерка треф", "Восьмерка бубен", "Восьмерка червей", "Восьмерка пик",
            "Девятка треф", "Девятка бубен", "Девятка червей", "Девятка пик",
            "Десятка треф", "Десятка бубен", "Десятка червей", "Десятка пик",
            "Валет треф", "Валет бубен", "Валет червей", "Валет пик",
            "Дама треф", "Дама бубен", "Дама червей", "Дама пик",
            "Король треф", "Король бубен", "Король червей", "Король пик",
            "Туз треф", "Туз бубен", "Туз червей", "Туз пик"};
    private String[] deck;

    public StandartDeck() {
        deck = new String[cardNames.length];
        curTop = cardNames.length;
        System.arraycopy(cardNames, 0, deck, 0, cardNames.length);
        shuffleDeck();
    }

    private void shuffleDeck() {
        Random rnd = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            String temp = deck[index];
            deck[index] = deck[i];
            deck[i] = temp;
        }
    }

    public String takeCard() {
        if (curTop == -1) {
            return null;
        } else {
            String ans = new String(deck[curTop-1]);
            curTop--;
            return ans;
        }
    }
}
