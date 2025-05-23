package com.blueprinthell.util;

import javafx.stage.Stage;

public class StageProvider {
    private static Stage primaryStage;

    public static void setStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getStage() {
        return primaryStage;
    }
}
