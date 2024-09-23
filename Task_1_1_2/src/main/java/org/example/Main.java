package org.example;

import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        BlackJack game = new BlackJack();
        Scanner scan = new Scanner(System.in);
        game.startRound();
        printCards(game);

        if (game.getCurStatusOfGame() == statusOfGame.draw) {
            System.out.println("Ничья!");
        } else if (game.getCurStatusOfGame() == statusOfGame.playerWin) {
            System.out.println("У вас блэкджек!");
        } else if (game.getCurStatusOfGame() == statusOfGame.dealerWin) {
            System.out.println("У дилера блэкджек!");
        } else {
            int choice;
            while (game.getCurStatusOfGame() == statusOfGame.playing) {
                System.out.println("Enter 1 for taking card, else 0");
                choice = scan.nextInt();
                game.move(choice);
                printCards(game);
            }


            if (game.getCurStatusOfGame() == statusOfGame.draw) {
                System.out.println("Ничья!");
            } else if (game.getCurStatusOfGame() == statusOfGame.playerWin) {
                System.out.println("Вы победили!");
            } else if (game.getCurStatusOfGame() == statusOfGame.dealerWin) {
                System.out.println("Дилер победил!");
            }
        }

    }

    private static void printCards(BlackJack game) {
        Vector<String> playersCard = game.getCards(true);
        Vector<String> dealersCard = game.getCards(false);
        System.out.print("Ваши карты: " + playersCard);
        System.out.println("] => " + game.getSum(true));

        System.out.print("Карты дилера: " + dealersCard);
        if (game.getSum(false) != 0) {
            System.out.println(" => " + game.getSum(false));
        } else {
            System.out.println(" ");
        }
    }
}