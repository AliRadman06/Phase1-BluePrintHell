package com.blueprinthell.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.blueprinthell.util.Constants;

import java.net.URL;

public class Game extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Update screen dimensions
        Constants.SCREEN_WIDTH = Screen.getPrimary().getVisualBounds().getWidth();
        Constants.SCREEN_HEIGHT = Screen.getPrimary().getVisualBounds().getHeight();

        URL fxmlUrl = getClass().getResource("/com/blueprinthell/hello-view.fxml");
        if (fxmlUrl == null) {
            throw new RuntimeException("FXML file not found: /com/blueprinthell/hello-view.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Scene scene = new Scene(root, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Blueprint Hell");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}