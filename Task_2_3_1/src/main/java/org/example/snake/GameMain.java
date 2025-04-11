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
        game.init();
        controller.init();
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
        while (game.tick()) {
            if (flag) {
                return;
            }
            Direction dir;
            dir = direction;

            Object info = game.update(dir);

            Platform.runLater(() -> gameController.drawField(info));
        }

        Platform.runLater(() -> gameController.gameOver());
    }
}
