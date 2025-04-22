package org.example.snake.defaultMod;

import org.example.snake.Direction;
import org.example.snake.Game;
import org.example.snake.wallMod.WallGame;

import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class DefaultGame implements Game {
    static class Cell {
        int x, y;

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final LinkedList<Cell> snake = new LinkedList<Cell>();
    private final int height = 10;
    private final int width = 10;
    private final BitSet snakeField = new BitSet(height * width);
    private int food_x = -1;
    private int food_y = -1;
    private int head_x;
    private int head_y;
    private long timeForTick = 1000;
    private final Random random = new Random();
    private long prevTime = 0;
    private boolean gameOver = false;
    private boolean win = false;
    private int length = 1;
    private final int winLength = 15;
    private Direction prev = null;


    @Override
    public Object init() {
        random.setSeed(System.currentTimeMillis());
        snake.add(new Cell(height / 2, width / 2));
        head_x = width / 2;
        head_y = height / 2;

        snakeField.set(head_y * width + head_x);

        return null;
    }

    @Override
    public boolean tick() {
        if (gameOver) {
            return false;
        }

        if (System.currentTimeMillis() - prevTime > timeForTick) {
            prevTime = System.currentTimeMillis();
            return true;
        }

        try {
            sleep(timeForTick - (System.currentTimeMillis() - prevTime));
        } catch (InterruptedException ignored) {
        }

        prevTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public Object update(Direction dir) {
        if (length > 1) {
            dir = oppositeDir(dir);
        }
        prev = dir;

        InfoBox infoBox = new InfoBox();
        Cell cell = snake.peek();

        moveHead(dir);
        infoBox.forRemove = new Cell(cell.x, cell.y);
        snakeField.set(head_y * width + head_x);

        boolean eaten = checkFoodEaten(infoBox, cell);
        if (eaten) {
            changeTimeForTick();

            if (generateFood()) {
                infoBox.food = new Cell(food_x, food_y);
            }
        } else if (food_y == -1 && food_x == -1) {
            generateFood();
            infoBox.food = new Cell(food_x, food_y);
        }

        infoBox.forPaint = new Cell(head_x, head_y);

        if (snakeField.cardinality() != length) {
            gameOver = true;
            infoBox.forPaint = null;
        } else if (length > winLength) {
            gameOver = true;
            win = true;
        }

        return infoBox;
    }

    @Override
    public boolean victory() {
        return win;
    }

    private void moveHead(Direction dir) {
        switch (dir) {
            case Up:
                head_y--;
                if (head_y < 0) {
                    head_y = height - 1;
                }
                break;
            case Down:
                head_y++;
                if (head_y >= height) {
                    head_y = 0;
                }
                break;
            case Left:
                head_x--;
                if (head_x < 0) {
                    head_x = width - 1;
                }
                break;
            case Right:
                head_x++;
                if (head_x >= width) {
                    head_x = 0;
                }
                break;
        }
    }

    private boolean generateFood() {
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        int coordN = snakeField.nextClearBit(y * width + x);
        if (coordN < width * height) {
            food_y = coordN / width;
            food_x = coordN - food_y * width;
            return true;
        }

        int coordP = snakeField.previousClearBit(y * width + x);
        if (coordP != -1) {
            food_y = coordP / width;
            food_x = coordP - food_y * width;
            return true;
        }

        return false;
    }

    private Direction oppositeDir(Direction dir) {
        if (dir == Direction.Up && prev == Direction.Down) {
            return Direction.Down;
        } else if (dir == Direction.Down && prev == Direction.Up) {
            return Direction.Up;
        } else if (dir == Direction.Left && prev == Direction.Right) {
            return Direction.Right;
        } else if (dir == Direction.Right && prev == Direction.Left) {
            return Direction.Left;
        }

        return dir;
    }

    private boolean checkFoodEaten(InfoBox infoBox, Cell cell) {
        if (head_x == food_x && head_y == food_y) {
            length++;
            infoBox.forRemove = null;
            snake.add(new Cell(head_x, head_y));
            return true;
        }

        snakeField.clear(cell.y * width + cell.x);
        snake.poll();
        cell.x = head_x;
        cell.y = head_y;
        snake.add(cell);
        return false;
    }

    private void changeTimeForTick() {
        timeForTick = (long) (1000 / (1 + 0.2 * (length - 1)));
        if (timeForTick < 100) {
            timeForTick = 100;
        }
    }
}
