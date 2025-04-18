package org.example.snake;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameController {

    private GamePainter painter;
    private Stage stage;

    @FXML
    private Pane pane;

    @FXML
    private Canvas canvas;

    void setPainter(GamePainter painter) {
        this.painter = painter;
    }

    void setStage(Stage stage) {
        this.stage = stage;
    }

    void init(Object info) {
        pane.getChildren().remove(canvas);
        canvas = painter.init(info);
        canvas.snapshot(null, null); //we need it for forced rendering of canvas
        pane.getChildren().add(canvas);

    }

    void drawField(Object info) {
        painter.drawField(info, canvas);
    }

    void gameOver() {
        painter.gameOver(canvas);
    }
}
