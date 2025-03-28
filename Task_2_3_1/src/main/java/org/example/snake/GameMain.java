package org.example.snake;

import javafx.application.Platform;

import java.util.Random;

public class GameMain extends Thread {

    static private GameMain gameMain = null;

    static public enum GameState {
        wait,
        playing
    }

    private GameState state = GameState.wait;
    private Field field;
    private Field.Direction curDirection = Field.Direction.Down;
    private Controller controller;
    private int countFood = 0;
    private Random random = new Random();

    synchronized public void moveRight() {
        curDirection = Field.Direction.Right;
    }

    synchronized public void moveLeft() {
        curDirection = Field.Direction.Left;
    }

    synchronized public void moveUp() {
        curDirection = Field.Direction.Up;
    }

    synchronized public void moveDown() {
        curDirection = Field.Direction.Down;
    }

    static public GameMain getInstance() {
        if (gameMain == null) {
            gameMain = new GameMain();
            gameMain.setDaemon(true);
        }

        return gameMain;
    }

    private GameMain() {

    }

    public void startGame(Controller controller) {
        if (state == GameState.playing) {
            return;
        }
        state = GameState.playing;
        this.controller = controller;
        random.setSeed(System.currentTimeMillis());

        synchronized (this) {
            notify();
        }
    }

    void initField() {
        field = new Field(this);
        field.setSnake(0, 0);
    }

    void decrementFood() {
        countFood--;
    }

    @Override
    public void run() {
        while (true) {
            while (state != GameState.playing) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (Exception ignored) {
                    }
                }
            }

            initField();

            drawField(true);

            countFood = 0;

            long time = 1300;
            while (state == GameState.playing) {
                try {
                    sleep(time);
                } catch (InterruptedException ignored) {
                }


                if (!field.move(curDirection)) {
                    state = GameState.wait;
                }

                if (countFood == 0) {
                    countFood++;
                    int x = random.nextInt(10);
                    int y = random.nextInt(10);

                    while (field.getCells()[y][x].getState() == Field.Cell.CellState.Snake) {
                        x++;
                        if (x >= 10) {
                            x = 0;
                            y++;
                            if (y >= 10) {
                                y = 0;
                            }
                        }
                    }

                    field.setFood(x, y);
                    time = time / 2 + time / 4;
                }

                drawField(false);

            }

            Platform.runLater(() -> {controller.gameOver();});

        }


    }

    private void drawField(boolean flag) {
        Platform.runLater(() -> {controller.drawField(field, flag);});
    }

}
