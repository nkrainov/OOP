package org.example.snake;

import javafx.application.Platform;

import java.util.Random;

public class GameMain extends Thread {

    static private GameMain gameMain = null;

    public enum GameState {
        wait,
        playing
    }

    private GameState state = GameState.wait;
    private Field field;
    private Field.Direction curDirection = Field.Direction.Down;
    private Field.Direction oldDirection = Field.Direction.Down;
    private Controller controller;
    private int countFood = 0;
    private int xy;
    private final Random random = new Random();

    synchronized public void moveRight() {
        if (oldDirection != Field.Direction.Left) {
            curDirection = Field.Direction.Right;
        }
    }

    synchronized public void moveLeft() {
        if (oldDirection != Field.Direction.Right) {
            curDirection = Field.Direction.Left;
        }
    }

    synchronized public void moveUp() {
        if (oldDirection != Field.Direction.Down) {
            curDirection = Field.Direction.Up;
        }
    }

    synchronized public void moveDown() {
        if (oldDirection != Field.Direction.Up) {
            curDirection = Field.Direction.Down;
        }
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

    public void startGame(Controller controller, int xy) {
        if (state == GameState.playing) {
            return;
        }
        this.xy = xy;
        state = GameState.playing;
        this.controller = controller;
        random.setSeed(System.currentTimeMillis());

        synchronized (this) {
            notify();
        }
    }

    void initField() {
        field = new Field(this, xy);
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

                Field.Direction dir;
                synchronized (this) {
                    dir = curDirection;
                }

                if (!field.move(dir)) {
                    state = GameState.wait;
                }

                if (countFood == 0) {
                    countFood++;
                    int x = random.nextInt(xy);
                    int y = random.nextInt(xy);

                    while (field.getCells()[y][x].getState() == Field.Cell.CellState.Snake) {
                        x++;
                        if (x >= xy) {
                            x = 0;
                            y++;
                            if (y >= xy) {
                                y = 0;
                            }
                        }
                    }

                    field.setFood(x, y);
                    time = time - time / 8;
                }

                drawField(false);
                synchronized (this) {
                    oldDirection = curDirection;
                }

            }

            Platform.runLater(() -> {controller.gameOver();});

        }


    }

    private void drawField(boolean flag) {
        Platform.runLater(() -> {controller.drawField(field, flag);});
    }

}
