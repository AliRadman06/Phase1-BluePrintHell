package com.blueprinthell.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.blueprinthell.util.Constants;
import com.blueprinthell.logic.GameStats;

public class GameOverView extends AnchorPane {

    public static void showOverlay(Scene currentScene) {
        AnchorPane base = (AnchorPane) currentScene.getRoot();
        base.setEffect(new GaussianBlur(20));

        GameOverView overlay = new GameOverView();
        StackPane wrapper = new StackPane(base, overlay);
        wrapper.setPrefSize(currentScene.getWidth(), currentScene.getHeight());
        currentScene.setRoot(wrapper);
    }

    public GameOverView() {
        setPickOnBounds(false);

        Label title = new Label("GAME OVER");
        title.setTextFill(Color.RED);
        title.setFont(Font.font("X VOSTA", 150));
        StackPane header = new StackPane(title);
        header.setAlignment(Pos.TOP_CENTER);
        AnchorPane.setTopAnchor(header, 150.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        getChildren().add(header);

        int totalPackets = GameStats.getTotalSize();
        int lostPackets = GameStats.getLostCount();
        int deliveredPackets = totalPackets - lostPackets;
        float lossPercent = (totalPackets == 0) ? 0 : (100f * lostPackets / totalPackets);

        Label statsLabel = new Label("Delivered: " + deliveredPackets + "Loss: " + String.format("%.1f", lossPercent) + "%");
        GameStats.reset();
        statsLabel.setFont(Font.font("X VOSTA", 50));
        statsLabel.setTextFill(Color.WHITE);
        statsLabel.setAlignment(Pos.CENTER);

        Button mainMenuButton = new Button("Back to Menu");
        mainMenuButton.setPrefSize(Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 10);
        mainMenuButton.setFont(Font.font("X VOSTA", 50));
        mainMenuButton.setStyle("""
                -fx-background-color: rgba(7,7,7,0.6);
                -fx-text-fill: white;
                -fx-background-radius: 30;
                -fx-border-color: rgba(250, 250, 250) ;
                -fx-border-radius: 30;
            """);
        mainMenuButton.setOnAction(e -> goToMainMenu());

        VBox vb = new VBox(Constants.SCREEN_HEIGHT / 80);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10));
        vb.getChildren().addAll(statsLabel, mainMenuButton);

        StackPane centerPane = new StackPane(vb);
        centerPane.setTranslateY(50);
        centerPane.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(centerPane, 0.0);
        AnchorPane.setBottomAnchor(centerPane, 0.0);
        AnchorPane.setLeftAnchor(centerPane, 0.0);
        AnchorPane.setRightAnchor(centerPane, 0.0);
        getChildren().add(centerPane);
    }

    private static void goToMainMenu() {
        Stage stage = com.blueprinthell.util.StageProvider.getStage();
        stage.getScene().setRoot(new com.blueprinthell.view.MainMenuView());
    }


} 
