package org.example.snake.wallMod;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.snake.GamePainter;

public class WallPainter implements GamePainter {

    private final int width = 10;
    private final int height = 10;
    private final int size = 500;

    @Override
    public Canvas init(Object object) {
        if (!(object instanceof InfoBox)) {
            return null;
        }
        InfoBox infoBox = (InfoBox) object;

        Canvas canvas = new Canvas();

        canvas.setWidth(size);
        canvas.setHeight(size);

        drawGrid(canvas);

        drawCell(infoBox.forPaint, canvas);

        for (WallGame.Cell cell : infoBox.special) {
            drawWall(cell, canvas);
        }

        return canvas;
    }

    @Override
    public void drawField(Object info, Canvas canvas) {
        if (!(info instanceof InfoBox)) {
            return;
        }

        InfoBox infoBox = (InfoBox) info;

        if (infoBox.forRemove != null) {
            removeCell(infoBox.forRemove, canvas);
        }

        if (infoBox.forPaint != null) {
            drawCell(infoBox.forPaint, canvas);
        }

        if (infoBox.special != null) {
            for (WallGame.Cell cell : infoBox.special) {
                drawFood(cell, canvas);
            }
        }
    }

    private void drawCell(WallGame.Cell cell, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.ORANGE);
        context.fillRect(cell.x * (double) size / width, cell.y * (double) size / width,
                (double) size / width, (double) size / height);

        drawBounds(canvas, cell.x, cell.y);
    }

    private void drawWall(WallGame.Cell cell, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.GRAY);
        context.fillRect(cell.x * (double) size / width, cell.y * (double) size / width,
                (double) size / width, (double) size / height);

        drawBounds(canvas, cell.x, cell.y);
    }

    private void removeCell(WallGame.Cell cell, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        double x = cell.x * (double) size / width;
        double y = cell.y * (double) size / height;

        context.setFill(new Color(0, 0.9, 0, 1));

        context.fillRect(x, y,
                (double) size / width, (double) size / height);

        drawBounds(canvas, cell.x, cell.y);
    }

    @Override
    public void gameOver(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setTextAlign(TextAlignment.CENTER);
        context.setFont(Font.font(100));
        context.fillText("GAME OVER", (double) size / 2, (double) size / 2, 300);
    }

    @Override
    public void victory(Canvas canvas) {
        //
    }

    private void drawGrid(Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(new Color(0, 0.9, 0, 1));
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        context.setFill(Color.BLACK);
        context.setLineWidth(1.0);
        for (int x = 0; x < width + 1; x++) {
            context.moveTo((double) (x * size) / width, 0);
            context.lineTo((double) (x * size) / width, size);
            context.stroke();
        }

        for (int y = 0; y < height + 1; y++) {
            context.moveTo(0, (double) (y * size) / height);
            context.lineTo(size, (double) (y * size) / height);
            context.stroke();
        }
    }

    private void drawFood(WallGame.Cell cell, Canvas canvas) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.RED);
        context.fillRect(cell.x * (double) size / width, cell.y * (double) size / width,
                (double) size / width, (double) size / height);

        drawBounds(canvas, cell.x, cell.y);
    }

    private void drawBounds(Canvas canvas, int X, int Y) {
        GraphicsContext context = canvas.getGraphicsContext2D();
        context.setFill(Color.BLACK);

        double x = X * (double) size / width;
        double y = Y * (double) size / width;

        context.moveTo(x, y);
        context.lineTo(x, y + (double) size / height);
        context.stroke();

        context.moveTo(x, y + (double) size / height);
        context.lineTo(x + (double) size / width, y + (double) size / height);
        context.stroke();

        context.moveTo(x + (double) size / width, y + (double) size / height);
        context.lineTo(x + (double) size / width, y);
        context.stroke();

        context.moveTo(x + (double) size / width, y);
        context.lineTo(x, y);
        context.stroke();
    }
}
