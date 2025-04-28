package com.blueprinthell.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Game extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        URL fxmlUrl = getClass().getResource("/com/blueprinthell/hello-view.fxml");
        if (fxmlUrl == null) {
            throw new RuntimeException("FXML file not found: /com/blueprinthell/main-menu.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Blueprint Hell");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
