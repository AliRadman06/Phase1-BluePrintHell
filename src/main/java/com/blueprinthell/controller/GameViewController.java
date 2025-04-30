package com.blueprinthell.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class GameViewController {
    @FXML
    private AnchorPane gamePane;

    @FXML
    public void initialize() {
        gamePane.setPrefHeight(gamePane.getHeight());
        gamePane.setPrefWidth(gamePane.getWidth());

    }
}