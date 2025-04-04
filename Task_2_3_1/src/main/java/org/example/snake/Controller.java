package org.example.snake;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;

public class Controller {
    @FXML
    private GridPane root;

    @FXML
    private GridPane field;

    @FXML
    private Label label;

    @FXML
    private Spinner<Integer> spinner;

    public void onStart() {
        int xy = spinner.getValue();
        GameMain.getInstance().startGame(this, xy);
    }

    public Node getNodeByCell(GridPane gridPane, int column, int row) {
        for (Node node : gridPane.getChildren()) {
            Integer nodeColumn = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);

            if (nodeColumn == null) nodeColumn = 0;
            if (nodeRow == null) nodeRow = 0;

            if (nodeColumn == column && nodeRow == row) {
                return node;
            }
        }
        return null; // Если ячейка пуста
    }

    private void initField(Field field) {
        label.setText("Game in progress");
        this.field.getChildren().clear();
        this.field.getColumnConstraints().clear();
        this.field.getRowConstraints().clear();
        this.field.setStyle("-fx-background-color:  #34C924; ");

        int rows = field.getCells().length;
        int cols = field.getCells()[0].length;

        this.field.prefHeightProperty().bind(root.heightProperty());
        this.field.prefHeightProperty().bind(this.field.widthProperty());
        for (int i = 0; i < cols; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100.0 / cols); // Равномерное распределение ширины
            this.field.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100.0 / rows); // Равномерное распределение высоты
            this.field.getRowConstraints().add(row);
        }

        for (int x = 0; x < field.getCells().length; x++) {
            ImageView[] images = new ImageView[field.getCells()[x].length];
            for (int y = 0; y < field.getCells()[x].length; y++) {
                images[y] = new ImageView();
                images[y].fitWidthProperty().bind(
                        this.field.widthProperty()
                                .divide(this.field.getColumnConstraints().size()) // Ширина одной ячейки
                );

                images[y].fitHeightProperty().bind(
                        this.field.heightProperty()
                                .divide(this.field.getRowConstraints().size()) // Высота одной ячейки
                );
            }

            this.field.addRow(x, images);
        }
        this.field.setGridLinesVisible(false);
        this.field.setGridLinesVisible(true);
    }

    void gameOver() {
        label.setText("Game Over");
    }

    public void drawField(Field field, boolean flagStart) {
        if (flagStart) {
            initField(field);
        }

        for (int x = 0; x < field.getCells().length; x++) {
            for (int y = 0; y < field.getCells()[x].length; y++) {
                Node node = getNodeByCell(this.field, y, x);
                if (node instanceof ImageView) {
                    switch (field.getCells()[y][x].getState()) {
                        case Snake:
                            ((ImageView) node).setImage(new Image(Controller.class.getResource("snake.png").toString()));
                            break;
                        case Empty:
                            ((ImageView) node).setImage(null);
                            break;
                        case Food:
                            ((ImageView) node).setImage(new Image(Controller.class.getResource("food.png").toString()));
                    }
                }
            }
        }

        this.field.requestLayout();
    }
}