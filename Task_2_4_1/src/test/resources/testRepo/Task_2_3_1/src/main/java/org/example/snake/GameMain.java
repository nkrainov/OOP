package org.example.snake;

import javafx.application.Platform;

public class GameMain extends Thread {

    private Game game;
    private GameController gameController;
    private Direction direction = Direction.Right;
    private boolean flag = false;

    public GameMain(Game game, GamePainter painter, GameController controller) {
        controller.setPainter(painter);
        this.game = game;
        this.gameController = controller;
        Object info = game.init();
        controller.init(info);
    }

    void startGame() {
        this.start();
    }

    void setDirection(Direction dir) {
        direction = dir;
    }

    @Override
    synchronized public void interrupt() {
        flag = true;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
        } catch (InterruptedException ignored) {
        }
        while (game.tick()) {
            if (flag) {
                return;
            }
            Direction dir;
            dir = direction;

            Object info = game.update(dir);

            Platform.runLater(() -> gameController.drawField(info));
        }

        if (game.victory()) {
            Platform.runLater(() -> gameController.victory());
        } else {
            Platform.runLater(() -> gameController.gameOver());
        }

    }
}
