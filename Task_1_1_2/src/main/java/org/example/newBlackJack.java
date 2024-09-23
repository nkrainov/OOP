package org.example;

import java.util.Vector;

enum statusOfGame{
    stop,
    playing,
    dealerWin,
    playerWin,
    draw
}

public class newBlackJack {

    private statusOfGame curStatusOfGame;
    private StandartDeck deck;
    private int dealerScore;
    private int playerScore;
    private int dealerSum;
    private int playerSum;
    private Vector<String> dealersCard;
    private Vector<String> playersCard;
    private boolean hideDealersCard;

    public newBlackJack(){
        curStatusOfGame = statusOfGame.stop;
    }

    public void startRound(){
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

    private void checkSituation(){
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
        } else if (dealerSum == 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.dealerWin;;
        } else if (playerSum > 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.dealerWin;
        } else if (dealerSum > 21) {
            hideDealersCard = false;
            curStatusOfGame = statusOfGame.playerWin;
        } else if (dealerSum > 17 && dealerSum > playerSum){
            curStatusOfGame = statusOfGame.dealerWin;
        } else if (dealerSum > 17 && dealerSum < playerSum){
            curStatusOfGame = statusOfGame.playerWin;
        } else if (dealerSum > 17){
            curStatusOfGame = statusOfGame.draw;
        }
    }

    public int move(int choice){
        if (curStatusOfGame != statusOfGame.playing){
            return 1;
        }

        if (choice == 1) {
            playersCard.add(deck.takeCard());
            checkSituation();

        } else if (choice == 0) {
            dealerMove();
        }


        return 0;
    }

    private void dealerMove(){
        hideDealersCard = false;
        while (dealerSum < 17) {
            dealersCard.add(deck.takeCard());
            checkSituation();
            if (curStatusOfGame != statusOfGame.playing) {
                break;
            }
        }
    }

    public Vector<String> getCards(boolean getPlayersCard){
        if (curStatusOfGame == statusOfGame.stop){
            return null;
        }
        if (getPlayersCard){
            return (Vector<String>)playersCard.clone();
        }
        else{
            if (hideDealersCard){
                Vector<String> clone = (Vector<String>)dealersCard.clone();
                clone.remove(1);
                clone.add("<close card>");
                return clone;
            }
            else {
                return (Vector<String>)dealersCard.clone();
            }
        }
    }

    public statusOfGame getCurStatusOfGame(){
        return curStatusOfGame;
    }

    public int getSum(boolean player){
        if (player){
            return playerSum;
        }
        else {
            if (hideDealersCard){
                return 0;
            }
            else {
                return dealerSum;
            }
        }
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

}
