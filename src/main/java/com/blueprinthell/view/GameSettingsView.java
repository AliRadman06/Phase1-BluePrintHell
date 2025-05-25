package com.blueprinthell.view;

import com.blueprinthell.util.SoundManager;
import com.blueprinthell.util.Constants;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class GameSettingsView extends StackPane {

    private final VBox mainMenuBox = new VBox(Constants.SCREEN_HEIGHT / 100);
    private final VBox soundSettingsBox = new VBox(Constants.SCREEN_HEIGHT / 100);
    private final StackPane rootContainer = new StackPane();

    public GameSettingsView() {
        setPadding(new Insets(0));

        ImageView background = new ImageView(new Image(getClass().getResource("/images/SettingMenuBackground.png").toExternalForm()));
        background.fitWidthProperty().bind(widthProperty());
        background.fitHeightProperty().bind(heightProperty());
        background.setPreserveRatio(false);

        setupMainMenu();
        setupSoundSettings();

        rootContainer.setAlignment(Pos.CENTER);
        rootContainer.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane centerWrapper = new StackPane(rootContainer);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.prefWidthProperty().bind(widthProperty());
        centerWrapper.prefHeightProperty().bind(heightProperty());

        rootContainer.getChildren().add(mainMenuBox);
        getChildren().addAll(background, centerWrapper);
    }

    private void setupMainMenu() {
        Label title = new Label("SETTINGS");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("X VOSTA", 100));

        StackPane header = new StackPane(title);
        header.setAlignment(Pos.CENTER);
        VBox.setMargin(header, new Insets(0, 0, 50, 0));

        Button soundsButton = createMenuButton("SOUNDS");
        Button keysButton = createMenuButton("KEYS");

        soundsButton.setOnAction(e -> {
            switchTo(soundSettingsBox);
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
        });
        keysButton.setOnAction(e -> {
            System.out.println("TODO: open keys config");
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");

        });

        mainMenuBox.setAlignment(Pos.CENTER);
        mainMenuBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
        mainMenuBox.setPrefHeight(Region.USE_COMPUTED_SIZE);
        Button backToMainButton = createMenuButton("BACK");
        backToMainButton.setOnAction(e -> {
            Stage stage = (Stage) getScene().getWindow();
            stage.getScene().setRoot(new MainMenuView());
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
        });

        mainMenuBox.getChildren().addAll(header, soundsButton, keysButton, backToMainButton);
    }

    private void setupSoundSettings() {
        soundSettingsBox.setAlignment(Pos.CENTER);
        soundSettingsBox.setPadding(new Insets(20));

        Label title = new Label("SOUND SETTINGS");
        title.setFont(Font.font("X VOSTA", 48));
        title.setTextFill(Color.WHITE);

        Label bgLabel = new Label("Background Music");
        bgLabel.setTextFill(Color.WHITE);
        bgLabel.setFont(Font.font("X VOSTA", 24));

        Slider bgSlider = new Slider(0, 1, SoundManager.getInstance().getVolume());
        bgSlider.getStyleClass().add("custom-slider");
        Platform.runLater(() -> {
            getScene().getStylesheets().add(getClass().getResource("/css/slider.css").toExternalForm());
        });

        bgSlider.setMajorTickUnit(0.1);
        bgSlider.setBlockIncrement(0.05);
        bgSlider.setPrefWidth(400);

        bgSlider.valueProperty().addListener((obs, oldV, newV) -> {
            SoundManager.getInstance().setVolume(newV.doubleValue());
        });

        Button backBtn = createMenuButton("BACK");
        backBtn.setOnAction(e -> {
            switchTo(mainMenuBox);
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
        });

        soundSettingsBox.getChildren().addAll(title, bgLabel, bgSlider, backBtn);
    }

    private Button createMenuButton(String text) {
        Button b = new Button(text);
        b.setPrefSize(Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 10);
        b.setFont(Font.font("X VOSTA", 75));
        b.setStyle("""
            -fx-background-color: rgba(2,3,14,0.6);
            -fx-text-fill: white;
            -fx-background-radius: 30;
            -fx-border-radius: 30;
            -fx-border-color: white ;
        """);
        return b;
    }

    private void switchTo(VBox targetBox) {
        rootContainer.getChildren().clear();
        rootContainer.getChildren().add(targetBox);
    }
}
