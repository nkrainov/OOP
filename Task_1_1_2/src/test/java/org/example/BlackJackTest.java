package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
}