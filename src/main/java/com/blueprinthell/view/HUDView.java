package com.blueprinthell.view;

import com.blueprinthell.logic.GameStats;
import javafx.animation.PauseTransition;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class HUDView extends HBox {
    private final Label wireLabel = new Label();
    private final Label timeLabel = new Label();
    private final Label lossLabel = new Label();
    private final Label coinsLabel = new Label();
    private GameStats gameStats;


    public HUDView() {
        setPadding(new Insets(10));
        setSpacing(30);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);");

        wireLabel.setTextFill(Color.WHITE);
        timeLabel.setTextFill(Color.WHITE);
        lossLabel.setTextFill(Color.WHITE);
        coinsLabel.setTextFill(Color.WHITE);

        Font font = new Font("Consolas", 16);
        wireLabel.setFont(font);
        timeLabel.setFont(font);
        lossLabel.setFont(font);
        coinsLabel.setFont(font);

        getChildren().addAll(wireLabel, timeLabel, lossLabel, coinsLabel);
        setVisible(false);
    }

    public void updateHUD(GameStats gameStats) {
        wireLabel.setText("Wire Left: " + gameStats.getRemainingWireLength());
        timeLabel.setText("Time: " + gameStats.getTemporalProgress() + "s");
        lossLabel.setText("Loss: " + gameStats.getLossPercent());
        coinsLabel.setText("Coins: " + gameStats.getCoins());
    }

    public void toggleVisibility(int durationMillis) {
        setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.millis(durationMillis));
        delay.setOnFinished(e -> setVisible(false));
        delay.play();
    }

    public void setGameStats(GameStats stats) {
        this.gameStats = stats;
    }

}
