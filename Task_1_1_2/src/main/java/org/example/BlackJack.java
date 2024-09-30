package org.example;

import java.util.Vector;

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
    private boolean hideDealersCard;
    private Player player;
    private Player dealer;

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
        player = new Player();
        dealer = new Player();
        curStatusOfGame = statusOfGame.playing;
        deck = new StandartDeck();
        hideDealersCard = true;

        player.takeCard(deck.takeCard());
        player.takeCard(deck.takeCard());
        dealer.takeCard(deck.takeCard());
        dealer.takeCard(deck.takeCard());

        checkSituation();


    }

    /**
     * Подсчитывает сумму карт играющих.
     * Если сумма карт удовлетворяет какому-либо из концов игры, то в curStatusOfGame устанавливается исход игры,
     * а также изменяется счет.
     */
    private void checkSituation() {
        int dealerSum = dealer.getSum();
        int playerSum = player.getSum();


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
        } else if (dealerSum >= 17 && dealerSum > playerSum && !hideDealersCard) {
            curStatusOfGame = statusOfGame.dealerWin;
            dealerScore++;
        } else if (dealerSum >= 17 && dealerSum < playerSum && !hideDealersCard) {
            curStatusOfGame = statusOfGame.playerWin;
            playerScore++;
        } else if (dealerSum >= 17 && !hideDealersCard) {
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
            player.takeCard(deck.takeCard());
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
        while (dealer.getSum() < 17) {
            dealer.takeCard(deck.takeCard());
            checkSituation();
            if (curStatusOfGame != statusOfGame.playing) {
                break;
            }
        }
        checkSituation();
    }

    /**
     * Возвращает вектор с картами игрока или дилера.
     *
     * @return если ни разу не была начата игра, то возвращает null, при getPlayersCard возвращает карты игрока
     * иначе возвращает карты дилера.
     */
    public Vector<Card> getCards(boolean getPlayersCard) {
        if (curStatusOfGame == statusOfGame.stop) {
            return null;
        }
        if (getPlayersCard) {
            return (Vector<Card>) player.getCards().clone();
        } else {
            if (hideDealersCard) {
                Vector<Card> clone = (Vector<Card>) dealer.getCards().clone();
                clone.remove(1);
                return clone;
            } else {
                return (Vector<Card>) dealer.getCards().clone();
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
    public int getSum(boolean p) {
        if (p) {
            return player.getSum();
        } else {
            if (hideDealersCard) {
                return 0;
            } else {
                return dealer.getSum();
            }
        }
    }
}
