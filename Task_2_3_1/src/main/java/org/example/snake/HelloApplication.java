package org.example.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("game.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 760, 480);

        GameMain.getInstance().start();
        stage.setResizable(false);
        stage.setTitle("Snake");
        stage.setScene(scene);

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    GameMain.getInstance().moveUp();
                    break;
                case S:
                    GameMain.getInstance().moveDown();
                    break;
                case A:
                    GameMain.getInstance().moveLeft();
                    break;
                case D:
                    GameMain.getInstance().moveRight();
                    break;
                }
            }
        );
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}