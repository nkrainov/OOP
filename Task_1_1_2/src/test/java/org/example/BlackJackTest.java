package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

class BlackJackTest {
    @Test
    void testMoveAfterEndOfGame() {
        BlackJack game = new BlackJack();
        game.startRound();
        while (game.getCurStatusOfGame() == statusOfGame.playing) {
            game.move(1);
        }
        Assertions.assertEquals(game.move(1), 1);
    }

    @Test
    void testHideDealersCard() {
        while (true) {
            BlackJack game = new BlackJack();
            game.startRound();
            if (game.getCurStatusOfGame() != statusOfGame.playing) {
                continue;
            }
            Assertions.assertEquals(game.getSum(false), 0);
            Assertions.assertTrue(game.getCards(false).contains("<close card>"));
            break;
        }
    }

    @Test
    void testGame(){
        BlackJack game = new BlackJack();
        int[] array = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1,
                       0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1};
        int index = 0;
        int prevDealerScore;
        int prevPlayerScore;

        while (game.getDealerScore() < 2 && game.getPlayerScore() < 2){
            prevPlayerScore = game.getPlayerScore();
            prevDealerScore = game.getDealerScore();
            game.startRound();
            while (game.getCurStatusOfGame() == statusOfGame.playing) {
                game.move(array[index]);
                index++;
            }
            switch (game.getCurStatusOfGame()){
                case draw:
                    Assertions.assertEquals(prevDealerScore, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore, game.getPlayerScore());
                    break;
                case dealerWin:
                    Assertions.assertEquals(prevDealerScore+1, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore, game.getPlayerScore());
                    break;
                case playerWin:
                    Assertions.assertEquals(prevDealerScore, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore+1, game.getPlayerScore());
                    break;
            }
        }

    }
}