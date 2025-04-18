package org.example.snake;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.snake.defaultMod.DefaultGame;
import org.example.snake.defaultMod.DefaultPainter;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

public class Menu {

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private Label label;

    @FXML
    private Label errorLabel;

    @FXML
    private Button start;

    @FXML
    private TextField path;

    @FXML
    private CheckBox checkBox;

    @FXML
    void check() {
        path.setVisible(!checkBox.isSelected());
        label.setVisible(!checkBox.isSelected());
    }

    @FXML
    void onStart() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(GameController.class.getResource("game.fxml"));
            Stage stage = new Stage();

            Game game = null;
            GamePainter painter = null;

            if (checkBox.isSelected()) {
                game = new DefaultGame();
                painter = new DefaultPainter();
            } else {
                File file = new File(path.getText());
                Config config = new ObjectMapper().readValue(file, Config.class);

                try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{})) {
                    Class<?> loadedClassGame = classLoader.loadClass(config.class_name_game);
                    game = (Game) loadedClassGame.getConstructors()[0].newInstance();
                }

                try (URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{})) {
                    Class<?> loadedClassGame = classLoader.loadClass(config.class_name_painter);
                    painter = (GamePainter) loadedClassGame.getConstructors()[0].newInstance();
                }
            }

            GameController controller = new GameController();
            controller.setStage(stage);
            fxmlLoader.setController(controller);

            stage.setScene(new Scene(fxmlLoader.load(), 500, 500));
            GameMain gameMain = new GameMain(game, painter, controller);
            stage.setResizable(false);
            stage.sizeToScene();
            stage.getScene().setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case UP:
                    case W:
                        gameMain.setDirection(Direction.Up);
                        break;
                    case DOWN:
                    case S:
                        gameMain.setDirection(Direction.Down);
                        break;
                    case LEFT:
                    case A:
                        gameMain.setDirection(Direction.Left);
                        break;
                    case RIGHT:
                    case D:
                        gameMain.setDirection(Direction.Right);
                        break;
                }
            });

            stage.setOnCloseRequest(event -> {
                gameMain.interrupt();
                this.stage.show();
            });
            label.getScene().getWindow().hide();
            stage.show();
            gameMain.setDaemon(true);
            gameMain.startGame();

            errorLabel.setVisible(false);
        } catch (Exception e) {
            errorLabel.setText("Произошла ошибка при загрузке мода");
        }


    }
}
