package com.blueprinthell.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenuController {

    @FXML
    Button StartButton;

    @FXML
    private void SwitchGameView(){
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/blueprinthell/game-view.fxml")
            );

            Parent gameRoot = loader.load();
            Stage stage = (Stage) StartButton.getScene().getWindow();
            stage.getScene().setRoot(gameRoot);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ExitApp(){
        System.exit(0);
    }
}
