package com.blueprinthell.view;

import com.blueprinthell.controller.WiringController;
import com.blueprinthell.util.Constants;
import com.blueprinthell.util.SoundManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LevelsMenuView extends AnchorPane {

    public LevelsMenuView() {

        ImageView bg = new ImageView(getClass().getResource("/images/MainMenuBackground.png").toExternalForm());
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(widthProperty());
        bg.fitHeightProperty().bind(heightProperty());
        getChildren().add(bg);

        Label title = new Label("LEVELS");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("X VOSTA" , 150));
        StackPane header = new StackPane(title);
        header.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(header, 50.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        getChildren().add(header);

        VBox vb = new VBox(Constants.SCREEN_HEIGHT / 70);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10));
        StackPane menuContainer = new StackPane(vb);

        menuContainer.prefWidthProperty().bind(widthProperty());
        menuContainer.prefHeightProperty().bind(heightProperty());

        StackPane.setAlignment(vb, Pos.CENTER);

        vb.translateYProperty().bind(heightProperty().multiply(0.325));
        vb.translateXProperty().bind(widthProperty().multiply(0.325));

        getChildren().add(menuContainer);

        String[] level = {"LEVEL 1", "LEVEL 2", "BACK"};
        for(String levelName : level) {

            Button b = new Button(levelName);
            b.setPrefSize(Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 10);
            b.setFont(Font.font("X Vosta", 75));
            b.setStyle("""
                -fx-background-color: rgba(2,3,14,0.6);
                -fx-text-fill: white;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-border-color: white;
            """);
            vb.getChildren().add(b);

            switch (levelName) {
                case "LEVEL 1" -> b.setOnAction(e -> {
                    switchToLevel1();
                    SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
                });
                case "LEVEL 2" -> b.setOnAction(e -> {
                    switchToLevel2();
                    SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
                });
                case "BACK" -> b.setOnAction(e -> {
                    backToMenu();
                    SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
                });
            }
        }

        getChildren().add(vb);

    }

    private void switchToLevel1() {
        Stage stage = (Stage) getScene().getWindow();
        GameView view = new GameView();
        stage.getScene().setRoot(view);

        // زمانی اجرا بشه که Scene کامل set شده
        javafx.application.Platform.runLater(() -> {
            WiringController wc = new WiringController(view);
            view.setWiringController(wc); // ← اگر نیاز داشتی دسترسی داخل view داشته باشی

        });
    }



    private void switchToLevel2() {}

    private void backToMenu() {
        Stage stage = (Stage) getScene().getWindow();
        stage.getScene().setRoot(new MainMenuView());
    }
}
