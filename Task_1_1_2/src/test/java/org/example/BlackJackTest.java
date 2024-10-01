package org.example;

import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Класс для тестирования BlackJack.
 */
class BlackJackTest {

    /**
     * Тест на ход в ситуации, когда его сделать нельзя.
     * Ожидается 1 в качестве возвращаемого значения.
     */
    @Test
    void testMoveAfterEndOfGame() {
        BlackJack game = new BlackJack();
        game.startRound();
        while (game.getCurStatusOfGame() == StatusOfGame.playing) {
            game.move(1);
        }
        Assertions.assertEquals(game.move(1), 1);
    }

    /**
     * Проверка скрытия суммы и второй карты дилера до его хода.
     */
    @Test
    void testHideDealersCard() {
        while (true) {
            BlackJack game = new BlackJack();
            game.startRound();
            if (game.getCurStatusOfGame() != StatusOfGame.playing) {
                continue;
            }
            Assertions.assertEquals(game.getSum(false), 0);
            Assertions.assertTrue(game.getCards(false).size() == 1);
            while (game.getCurStatusOfGame() == StatusOfGame.playing) {
                game.move(1);
            }
            Assertions.assertFalse(game.getCards(false).size() == 1);
            break;
        }
    }

    /**
     * Проверка того, что карты действительно добавляются в руку игрока.
     */
    @Test
    void testGetPlayersCards() {
        BlackJack game = new BlackJack();
        Assertions.assertNull(game.getCards(true));
        game.startRound();
        Assertions.assertEquals(game.getCards(true).size(), 2);
        int i = 2;
        while (game.getCurStatusOfGame() == StatusOfGame.playing) {
            game.move(1);
            i++;
            Assertions.assertEquals(game.getCards(true).size(), i);
        }

    }

    /**
     * Проверяется сумма одного из игрока в конце игры при любом из вариантов.
     */
    @Test
    void testSum() {
        BlackJack game = new BlackJack();
        game.startRound();
        while (game.getCurStatusOfGame() == StatusOfGame.playing) {
            game.move(1);
        }
        if (game.getCurStatusOfGame() == StatusOfGame.dealerWin) {
            Assertions.assertTrue(game.getSum(false) <= 21);
        } else if (game.getCurStatusOfGame() == StatusOfGame.playerWin) {
            Assertions.assertTrue(game.getSum(true) <= 21);
        } else if (game.getCurStatusOfGame() == StatusOfGame.draw) {
            Assertions.assertTrue(game.getSum(true) <= 21);
            Assertions.assertTrue(game.getSum(false) <= 21);
        }
    }

    /**
     * Проверка работоспособности игры, а также изменения счета.
     */
    @Test
    void testGame() {
        BlackJack game = new BlackJack();
        int[] array = {1, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 1,
                       0, 1, 1, 1, 0, 1, 0, 0, 0, 1, 1, 1};

        int index = 0;
        int prevDealerScore;
        int prevPlayerScore;
        while (game.getDealerScore() < 2 && game.getPlayerScore() < 2) {
            prevPlayerScore = game.getPlayerScore();
            prevDealerScore = game.getDealerScore();
            game.startRound();
            while (game.getCurStatusOfGame() == StatusOfGame.playing) {
                game.move(array[index]);
                index++;
            }
            switch (game.getCurStatusOfGame()) {
                case draw:
                    Assertions.assertEquals(prevDealerScore, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore, game.getPlayerScore());
                    break;
                case dealerWin:
                    Assertions.assertEquals(prevDealerScore + 1, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore, game.getPlayerScore());
                    break;
                case playerWin:
                    Assertions.assertEquals(prevDealerScore, game.getDealerScore());
                    Assertions.assertEquals(prevPlayerScore + 1, game.getPlayerScore());
                    break;
                default:
                    break;
            }
        }

    }

    /**
     * Проводится обратная testSum проверка.
     */
    @Test
    void testPossibleWaysToWin() {
        BlackJack game = new BlackJack();
        Random rand = new Random();
        int prevDealerScore;
        int prevPlayerScore;
        boolean flagPlayerBlackJack = false;
        boolean flagDealerBlackJack = false;
        boolean flagDraw = false;
        boolean flagPlayersSumMore21 = false;
        boolean flagDealersSumMore21 = false;
        while (!(flagDealersSumMore21 && flagDraw && flagDealerBlackJack
                && flagPlayersSumMore21 && flagPlayerBlackJack)) {
            game.startRound();
            while (game.getCurStatusOfGame() == StatusOfGame.playing) {
                game.move(rand.nextInt(2));
            }
            if (!flagPlayerBlackJack && game.getSum(true) == 21) {
                Assertions.assertEquals(game.getCurStatusOfGame(), StatusOfGame.playerWin);
                flagPlayerBlackJack = true;
            } else if (!flagDealerBlackJack && game.getSum(false) == 21) {
                Assertions.assertEquals(game.getCurStatusOfGame(), StatusOfGame.dealerWin);
                flagDealerBlackJack = true;
            } else if (!flagDealersSumMore21 && game.getSum(false) > 21) {
                Assertions.assertEquals(game.getCurStatusOfGame(), StatusOfGame.playerWin);
                flagDealersSumMore21 = true;
            } else if (!flagPlayersSumMore21 && game.getSum(true) > 21) {
                Assertions.assertEquals(game.getCurStatusOfGame(), StatusOfGame.dealerWin);
                flagPlayersSumMore21 = true;
            } else if (!flagDraw && game.getSum(true) == game.getSum(false)) {
                Assertions.assertEquals(game.getCurStatusOfGame(), StatusOfGame.draw);
                flagDraw = true;
            }
        }

    }
}