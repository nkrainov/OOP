package org.example.snake.defaultMod;

import org.example.snake.Direction;
import org.example.snake.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestDefaultMod {

    @Test
    public void testDefaultGame() {
        Game game = new DefaultGame();
        game.init();

        int x = 5;
        int y = 5;
        int x_food = -1;
        int y_food = -1;
        Direction dir = Direction.Right;
        while (game.tick()) {

            InfoBox infoBox = (InfoBox) game.update(dir);

            switch (dir) {
                case Left:
                    x--;
                    if (x < 0) {
                        x = 9;
                    }
                    break;
                case Right:
                    x++;
                    if (x > 9) {
                        x = 0;
                    }
                    break;
                case Up:
                    y--;
                    if (y < 0) {
                        y = 9;
                    }
                    break;
                case Down:
                    y++;
                    if (y > 9) {
                        y = 0;
                    }
                    break;
            }

            if (x_food == x) {
                dir = Direction.Up;
            }

            if (x == x_food && y == y_food) {
                Assertions.assertNull(infoBox.forRemove);
                Assertions.assertNotNull(infoBox.food);
                return;
            }

            Assertions.assertNotNull(infoBox.forPaint);

            if (infoBox.food != null) {
                x_food = infoBox.food.x;
                y_food = infoBox.food.y;
            }
        }
    }

    @Test
    public void testNotEatYourself() {
        Game game = new DefaultGame();
        game.init();

        int x = 5;
        int y = 5;
        int x_food = -1;
        int y_food = -1;
        int count = 0;
        Direction dir = Direction.Left;
        while (game.tick()) {

            InfoBox infoBox = (InfoBox) game.update(dir);

            switch (dir) {
                case Left:
                    x--;
                    if (x < 0) {
                        x = 9;
                    }
                    break;
                case Right:
                    x++;
                    if (x > 9) {
                        x = 0;
                    }
                    break;
                case Up:
                    y--;
                    if (y < 0) {
                        y = 9;
                    }
                    break;
                case Down:
                    y++;
                    if (y > 9) {
                        y = 0;
                    }
                    break;
            }

            if (x_food == x) {
                dir = Direction.Down;
            }

            if (x == x_food && y == y_food) {
                count++;
                if (count == 2) {
                    break;
                }
                dir = Direction.Left;
            }


            if (infoBox.food != null) {
                x_food = infoBox.food.x;
                y_food = infoBox.food.y;
            }
        }

        if (dir == Direction.Down) {
            game.update(Direction.Up);
        } else {
            game.update(Direction.Right);
        }

        Assertions.assertTrue(game.tick());

    }
}
