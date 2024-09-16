package org.example;

import java.util.Scanner;
import java.util.Vector;

public class BlackJack {
    private int countOfRounds;
    private final StandartDeck standartDeck;
    private int playerSum;
    private int dealerSum;


    public BlackJack(int countOfRounds) {
        this.countOfRounds = countOfRounds;
        this.standartDeck = new StandartDeck();
    }

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

    private int checkSituation(Vector<String> dealersCard, Vector<String> playersCard) {
        dealerSum = 0;
        playerSum = 0;
        for (String s : dealersCard) {
            dealerSum += getCardValue(s, true);
        }

        for (String s : playersCard) {
            playerSum += getCardValue(s, false);
        }
        if (playerSum == dealerSum && dealerSum == 21) {
            return 3;
        } else if (playerSum == 21) {
            return 2;
        } else if (dealerSum == 21) {
            return 1;
        } else if (playerSum > 21) {
            return 4;
        } else if (dealerSum > 21) {
            return 5;
        }
        return 0;
    }

    private void printCards(Vector<String> dealersCard, Vector<String> playersCard, boolean beforeDealersMove) {
        System.out.print("Ваши карты: [");
        for (String elem : playersCard) {
            System.out.print(" " + elem + "(" + getCardValue(elem, false) + ")" + " ");
        }
        System.out.println("] => " + playerSum);

        System.out.print("Карты дилера: [");
        if (beforeDealersMove) {
            System.out.println(" " + playersCard.get(0) + " <закрытая карта> ]");
        } else {
            for (String elem : dealersCard) {
                System.out.print(" " + elem + "(" + getCardValue(elem, true) + ")" + " ");
            }
            System.out.println("] => " + dealerSum);
        }
    }

    public void play() {
        System.out.println("Старт игры");
        Vector<String> dealersCard = new Vector<String>();
        Vector<String> playersCard = new Vector<String>();
        Scanner scan = new Scanner(System.in);
        int playerScore = 0;
        int dealerScore = 0;
        int choice;

        outerloop:
        for (int i = 0; i < countOfRounds; i++) {
            System.out.printf("Счет: %d:%d\n", playerScore, dealerScore);
            dealersCard.removeAllElements();
            playersCard.removeAllElements();
            System.out.println("Раунд " + (i + 1));

            dealersCard.add(standartDeck.takeCard());
            dealersCard.add(standartDeck.takeCard());
            playersCard.add(standartDeck.takeCard());
            playersCard.add(standartDeck.takeCard());

            int res = checkSituation(dealersCard, playersCard);
            printCards(dealersCard, playersCard, true);
            if (res == 3) {
                System.out.println("Ничья!");
                continue;
            } else if (res == 2) {
                System.out.println("У вас блэкджек, вы победили!");
                playerScore++;
                continue;
            } else if (res == 1) {
                System.out.println("Дилер откурывает свою карту");
                printCards(dealersCard, playersCard, false);
                System.out.println("У дилера блэкджек, вы проиграли!");
                dealerScore++;
                continue;
            }

            System.out.println("Ваш ход\n-------");

            while (true) {
                System.out.println("Введите “1”, чтобы взять карту, и “0”, чтобы остановиться");
                choice = scan.nextInt();
                if (choice == 1) {
                    playersCard.add(standartDeck.takeCard());
                    res = checkSituation(dealersCard, playersCard);
                    printCards(dealersCard, playersCard, true);
                    if (res == 2) {
                        System.out.println("У вас блэкджек, вы победили!");
                        playerScore++;
                        continue outerloop;
                    } else if (res == 4) {
                        System.out.println("Вы набрали больше 21 очка, вы проиграли(!");
                        dealerScore++;
                        continue outerloop;
                    }
                } else if (choice == 0) {
                    break;
                }
            }

            System.out.println("Ход дилера\n-------");

            System.out.println("Дилер открывает свои карты");

            printCards(dealersCard, playersCard, false);

            while (dealerSum < 17) {
                dealersCard.add(standartDeck.takeCard());
                res = checkSituation(dealersCard, playersCard);
                printCards(dealersCard, playersCard, false);
                if (res == 1) {
                    System.out.println("У дилера блэкджек, вы проиграли(");
                    dealerScore++;
                    continue outerloop;
                } else if (res == 5) {
                    System.out.println("У дилера больше 21 очка, вы выиграли!");
                    playerScore++;
                    continue outerloop;
                }
            }

            if (playerSum > dealerSum) {
                System.out.println("У вас больше очков, вы выиграли!");
                playerScore++;
            } else if (playerSum < dealerSum) {
                System.out.println("У вас меньше очков, вы проиграли!");
                dealerScore++;
            } else {
                System.out.println("У вас поровну очков, ничья!");
            }
        }

    }


}
