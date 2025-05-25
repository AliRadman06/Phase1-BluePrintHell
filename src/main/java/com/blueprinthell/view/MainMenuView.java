package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import com.blueprinthell.util.SoundManager;
import eu.hansolo.tilesfx.addons.Switch;
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
import com.blueprinthell.util.Constants;
import javafx.stage.Stage;

public class MainMenuView extends AnchorPane {

    public MainMenuView() {
        // bg
        ImageView bg = new ImageView(getClass().getResource("/images/MainMenuBackground.png").toExternalForm());
        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(widthProperty());
        bg.fitHeightProperty().bind(heightProperty());
        getChildren().add(bg);

        Label title = new Label("BLUEPRINT HELL");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("X VOSTA" , 150));
        StackPane header = new StackPane(title);
        header.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(header, 50.0);
        AnchorPane.setLeftAnchor(header, 0.0);
        AnchorPane.setRightAnchor(header, 0.0);
        getChildren().add(header);

        VBox vb = new VBox(Constants.SCREEN_HEIGHT / 100);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(10));

        StackPane menuContainer = new StackPane(vb);

        menuContainer.prefWidthProperty().bind(widthProperty());
        menuContainer.prefHeightProperty().bind(heightProperty());

        StackPane.setAlignment(vb, Pos.CENTER);

        vb.translateYProperty().bind(heightProperty().multiply(0.325));
        vb.translateXProperty().bind(widthProperty().multiply(0.325));

        getChildren().add(menuContainer);


        String[] text = {"START", "SETTING", "EXIT"};
        for (String txt : text) {

            Button b = new Button(txt);
            b.setPrefSize(Constants.SCREEN_WIDTH / 3, Constants.SCREEN_HEIGHT / 10);
            b.setFont(Font.font("X Vosta", 75));
            b.setStyle("""
                -fx-background-color: rgba(2,3,14,0.6);
                -fx-text-fill: white;
                -fx-background-radius: 30;
                -fx-border-radius: 30;
                -fx-border-color: white ;
            """);
            vb.getChildren().add(b);

            switch (txt) {
                case "START" -> b.setOnAction(e -> {switchToLevelsMenu();
                    SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
                });
                case "SETTING" -> b.setOnAction(e -> {switchToSettingsMenu();
                    SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
                });
                case "EXIT" -> b.setOnAction(e -> {
                    System.exit(0);
                });
            }
        }

        getChildren().add(vb);


    }

    private void switchToLevelsMenu() {

        Stage stage = (Stage) getScene().getWindow();
        stage.getScene().setRoot(new LevelsMenuView());
    }

    private void switchToSettingsMenu() {
        Stage stage = (Stage) getScene().getWindow();
        stage.getScene().setRoot(new GameSettingsView());
    }
}
