package org.example;

import java.util.Vector;

/**
 * Перечисление возможных состояний игры.
 */
enum statusOfGame {
    stop,
    playing,
    dealerWin,
    playerWin,
    draw
}

/**
 * Класс, реализующий игру BlackJack.
 * Имеет 3 ключевые функции для игры.
 * Конструктор - устанавливает начальное состояние игры.
 * startGame - производит первичную раздачу карт.
 * move - реализует ход игрока, а также ход дилера после игрока.
 */
public class BlackJack {

    private statusOfGame curStatusOfGame;
    private StandartDeck deck;
    private int dealerScore;
    private int playerScore;
    private int dealerSum;
    private int playerSum;
    private Vector<String> dealersCard;
    private Vector<String> playersCard;
    private boolean hideDealersCard;

    /**
     * Конструктор. Инициализирует начальный счет и состояние.
     */
    public BlackJack() {
        curStatusOfGame = statusOfGame.stop;
        playerScore = 0;
        dealerScore = 0;
    }

    /**
     * Запускает раунд игры. Создает колоду для игры, инициализирует начальный суммы карты.
     * Проводит первичную раздачу карт.
     */
    public void startRound() {
        dealersCard = new Vector<String>();
        playersCard = new Vector<String>();
        curStatusOfGame = statusOfGame.playing;
        deck = new StandartDeck();
        hideDealersCard = true;

        dealersCard.add(deck.takeCard());
        dealersCard.add(deck.takeCard());
        playersCard.add(deck.takeCard());
        playersCard.add(deck.takeCard());

        dealerSum = 0;
        playerSum = 0;
        checkSituation();


    }

    /**
     * Подсчитывает сумму карт играющих.
     * Если сумма карт удовлетворяет какому-либо из концов игры, то в curStatusOfGame устанавливается исход игры,
     * а также изменяется счет.
     */
    private void checkSituation() {
        dealerSum = 0;
        playerSum = 0;
        for (String s : dealersCard) {
            dealerSum += getCardValue(s, true);
        }

        for (String s : playersCard) {
            playerSum += getCardValue(s, false);
        }
        if (playerSum == 21 && dealerSum == 21) {
            curStatusOfGame = statusOfGame.draw;
            hideDealersCard = false;
        } else if (playerSum == 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.playerWin;
            playerScore++;
        } else if (dealerSum == 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.dealerWin;
            dealerScore++;
        } else if (playerSum > 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.dealerWin;
            dealerScore++;
        } else if (dealerSum > 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.playerWin;
            playerScore++;
        } else if (dealerSum > 17 && dealerSum > playerSum) {
            curStatusOfGame = statusOfGame.dealerWin;
            dealerScore++;
        } else if (dealerSum > 17 && dealerSum < playerSum) {
            curStatusOfGame = statusOfGame.playerWin;
            playerScore++;
        } else if (dealerSum > 17) {
            curStatusOfGame = statusOfGame.draw;
        }
    }

    /**
     * Геттер для счета дилера.
     * @return возвращает счет дилера.
     */
    public int getDealerScore() {
        return dealerScore;
    }

    /**
     * Геттер для счета игрока.
     * @return возвращает счет игрока.
     */
    public int getPlayerScore() {
        return playerScore;
    }

    /**
     * Реализует взятие карты игроком. Если choice == 0, то завершает ход игрока
     * и запускает ход дилера. Если choice == 1, то карта будет добавлена в руку игрока,
     * затем будет проверены условия конца игры.
     *
     * @return при choice не равном 1 или 2, а также в случае, если игра не идет сейчас
     * (не установлен статус playing), возвращает 1, иначе ноль.
     */
    public int move(int choice) {
        if (curStatusOfGame != statusOfGame.playing) {
            return 1;
        }

        if (choice == 1) {
            playersCard.add(deck.takeCard());
            checkSituation();

        } else if (choice == 0) {
            dealerMove();
        } else {
            return 1;
        }


        return 0;
    }

    /**
     * Осуществляет ход дилера.
     */
    private void dealerMove() {
        hideDealersCard = false;
        while (dealerSum < 17) {
            dealersCard.add(deck.takeCard());
            checkSituation();
            if (curStatusOfGame != statusOfGame.playing) {
                break;
            }
        }
    }

    /**
     * Возвращает вектор с картами игрока или дилера.
     *
     * @return если ни разу не была начата игра, то возвращает null, при getPlayersCard возвращает карты игрока
     * иначе возвращает карты дилера.
     */
    public Vector<String> getCards(boolean getPlayersCard) {
        if (curStatusOfGame == statusOfGame.stop) {
            return null;
        }
        if (getPlayersCard) {
            return (Vector<String>) playersCard.clone();
        } else {
            if (hideDealersCard) {
                Vector<String> clone = (Vector<String>) dealersCard.clone();
                clone.remove(1);
                clone.add("<close card>");
                return clone;
            } else {
                return (Vector<String>) dealersCard.clone();
            }
        }
    }

    /**
     * Геттер для статуса игры.
     * @return возвращает статус игры.
     */
    public statusOfGame getCurStatusOfGame() {
        return curStatusOfGame;
    }

    /**
     * Геттер для сумм карты игрока или дилера.
     *
     * @return если player, то возвращает счет игрока, иначе возвращает счет дилера
     * (если ход ещё у игрока, то будет возвращен ноль).
     */
    public int getSum(boolean player) {
        if (player) {
            return playerSum;
        } else {
            if (hideDealersCard) {
                return 0;
            } else {
                return dealerSum;
            }
        }
    }

    /**
     * Функция подсчета стоимости карты.
     *
     * @return возвращает значение карты.
     */
    private int getCardValue(String card, boolean dealersCard) {
        int ans;
        if (card.contains("Двойка")) {
            ans = 2;
        } else if (card.contains("Тройка")) {
            ans = 3;
        } else if (card.contains("Четверка")) {
            ans = 4;
        } else if (card.contains("Пятерка")) {
            ans = 5;
        } else if (card.contains("Шестерка")) {
            ans = 6;
        } else if (card.contains("Семерка")) {
            ans = 7;
        } else if (card.contains("Восьмерка")) {
            ans = 8;
        } else if (card.contains("Девятка")) {
            ans = 9;
        } else if (card.contains("Десятка") || card.contains("Валет") || card.contains("Дама") || card.contains("Король")) {
            ans = 10;
        } else if (card.contains("Туз")) {
            if ((dealersCard && dealerSum + 11 < 22) || (!dealersCard && playerSum + 11 < 22)) {
                ans = 11;
            } else {
                ans = 1;
            }
        } else {
            ans = 0;
        }
        return ans;
    }

}
