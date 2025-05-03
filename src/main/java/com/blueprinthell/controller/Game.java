package com.blueprinthell.controller;

import com.blueprinthell.view.MainMenuView;
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

        MainMenuView mainMenuView = new MainMenuView();

        Scene scene = new Scene(mainMenuView, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Blueprint Hell");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}