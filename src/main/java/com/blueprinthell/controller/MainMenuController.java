package com.blueprinthell.controller;

import com.blueprinthell.util.Constants;
import com.blueprinthell.view.GameView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    private AnchorPane gamePane;
    @FXML
    private Button StartButton;
    @FXML
    private ImageView backgroundImage;

    @FXML
    private void initialize() {
        gamePane.setPrefHeight(Constants.SCREEN_HEIGHT);
        gamePane.setPrefWidth(Constants.SCREEN_WIDTH);
        backgroundImage.fitWidthProperty().bind(gamePane.widthProperty());
        backgroundImage.fitHeightProperty().bind(gamePane.heightProperty());
    }

    @FXML
    private void SwitchGameView() {

        Stage stage = (Stage) StartButton.getScene().getWindow();
        GameView gameView = new GameView();
        stage.getScene().setRoot(gameView);
    }

    @FXML
    private void ExitApp() {
        System.exit(0);
    }
}