package com.blueprinthell.controller;

import com.blueprinthell.util.SoundManager;
import com.blueprinthell.util.StageProvider;
import com.blueprinthell.view.MainMenuView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import com.blueprinthell.util.Constants;

import java.net.URL;

public class Game extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        StageProvider.setStage(primaryStage);

        MainMenuView mainMenuView = new MainMenuView();

        Scene scene = new Scene(mainMenuView, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Blueprint Hell");
        primaryStage.setFullScreen(true);
        primaryStage.getIcons().add(new Image( getClass().getResourceAsStream("/images/icon.png")));
        primaryStage.show();
        SoundManager.getInstance().playMusic("/audio/lulled-to-sleep-143280.mp3");


    }


    public static void main(String[] args) {
        launch(args);
    }
}