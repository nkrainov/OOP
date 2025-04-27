package org.example.snake.wallMod;

import org.example.snake.Direction;
import org.example.snake.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestWallMod {

    @Test
    public void testWallGame() {
        Game game = new WallGame();
        game.init();

        int x = 5;
        int y = 5;
        int x_food = -1;
        int y_food = -1;
        Direction dir = Direction.Right;
        while (game.tick()) {

            InfoBox infoBox = (InfoBox) game.update(dir);

            switch (dir) {
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
            }

            if (x_food == x) {
                dir = Direction.Up;
            }

            if (x == x_food && y == y_food) {
                Assertions.assertNull(infoBox.forRemove);
                Assertions.assertNotNull(infoBox.special);
                return;
            }

            Assertions.assertNotNull(infoBox.forPaint);

            if (infoBox.special != null) {

                x_food = infoBox.special.get(0).x;
                y_food = infoBox.special.get(0).y;
            }
        }
    }

    @Test
    public void testNotEatYourself() {
        Game game = new WallGame();
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


            if (infoBox.special != null) {
                x_food = infoBox.special.get(0).x;
                y_food = infoBox.special.get(0).y;
            }
        }

        if (dir == Direction.Down) {
            game.update(Direction.Up);
        } else {
            game.update(Direction.Right);
        }

        Assertions.assertTrue(game.tick());

    }

    @Test
    public void testDieFromWall() {
        Game game = new WallGame();
        InfoBox info = (InfoBox) game.init();

        int x = 5;
        int y = 5;
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
                case Down:
                    y++;
                    if (y > 9) {
                        y = 0;
                    }
                    break;
            }


            boolean flag = false;
            for (WallGame.Cell cell : info.special) {
                if (cell.x == x && cell.y == y) {
                    flag = true;
                    break;
                }
            }

            if (flag) {
                break;
            }

            for (WallGame.Cell cell : info.special) {
                if (cell.x == x) {
                    dir = Direction.Down;
                }

                if (cell.y == y) {
                    dir = Direction.Left;
                }
            }

        }

        game.update(dir);

        Assertions.assertFalse(game.tick());

    }
}
