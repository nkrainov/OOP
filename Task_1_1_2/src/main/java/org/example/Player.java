package org.example;

import java.util.Vector;

/**
 * Класс, реализующий сущность игрока, у которого есть карты и текущая сумма.
 */
public class Player {
    private Vector<Card> cards;
    private int sum;

    /**
     * Инициализирует вектор, хранящий карты, и сумму, в начале равную нулю.
     */
    public Player() {
        sum = 0;
        cards = new Vector<Card>();
    }

    /**
     * Добавление карты в руку.
     */
    public void takeCard(Card card) {
        cards.add(card);
        sum += card.getValue(sum);
    }

    /**
     * Геттер для суммы.
     */
    public int getSum() {
        return sum;
    }

    /**
     * Геттер для карт.
     */
    public Vector<Card> getCards() {
        return cards;
    }
}
