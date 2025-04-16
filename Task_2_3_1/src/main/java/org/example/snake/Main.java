package org.example.snake;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("menu.fxml"));
        Menu menu = new Menu();
        fxmlLoader.setController(menu);
        Scene scene = new Scene(fxmlLoader.load(), 400, 400);

        for (Node node : scene.getRoot().getChildrenUnmodifiable()) {
            if (node instanceof CheckBox) {
                node.setOnMouseClicked(event -> {
                    menu.check();
                });
            } else if (node instanceof Button) {
                node.setOnMouseClicked(event -> {
                    try {
                        menu.onStart();
                    } catch (Exception ignored) {
                    }
                });
            }
        }

        menu.setStage(stage);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}