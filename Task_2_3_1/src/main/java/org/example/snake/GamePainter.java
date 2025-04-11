package org.example.snake;

import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public interface GamePainter {
    Canvas init();

    void drawField(Object info, Canvas canvas);

    void gameOver(Canvas canvas);
}
