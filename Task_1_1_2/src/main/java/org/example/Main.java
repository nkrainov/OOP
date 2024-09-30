package org.example;

import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        Scanner scan = new Scanner(System.in);
        game.startRound();
        printCards(game);

        if (game.getCurStatusOfGame() == StatusOfGame.draw) {
            System.out.println("Ничья!");
        } else if (game.getCurStatusOfGame() == StatusOfGame.playerWin) {
            System.out.println("У вас блэкджек!");
        } else if (game.getCurStatusOfGame() == StatusOfGame.dealerWin) {
            System.out.println("У дилера блэкджек!");
        } else {
            int choice;
            while (game.getCurStatusOfGame() == StatusOfGame.playing) {
                System.out.println("Введите 1 для взятия карты, иначе ноль");
                choice = scan.nextInt();
                game.move(choice);
                printCards(game);
            }


            if (game.getCurStatusOfGame() == StatusOfGame.draw) {
                System.out.println("Ничья!");
            } else if (game.getCurStatusOfGame() == StatusOfGame.playerWin) {
                System.out.println("Вы победили!");
            } else if (game.getCurStatusOfGame() == StatusOfGame.dealerWin) {
                System.out.println("Дилер победил!");
            }
        }

    }

    private static void printCards(BlackJack game) {
        Vector<Card> playersCard = game.getCards(true);
        Vector<Card> dealersCard = game.getCards(false);
        System.out.print("Ваши карты: [");
        for (Card card : game.getCards(true)){
            System.out.print(card.getName() + " ");
        }
        System.out.println("] => " + game.getSum(true));

        System.out.print("Карты дилера: [");
        for (Card card : game.getCards(false)){
            System.out.print(card.getName() + " ");
        }

        if (game.getSum(false) != 0) {
            System.out.println("] => " + game.getSum(false));
        } else {
            System.out.println(" <закрытая карта>]");
        }
    }
}