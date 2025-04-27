package org.example.snake;

import javafx.scene.canvas.Canvas;

public interface GamePainter {
    Canvas init(Object object);

    void drawField(Object info, Canvas canvas);

    void gameOver(Canvas canvas);

    void victory(Canvas canvas);
}
