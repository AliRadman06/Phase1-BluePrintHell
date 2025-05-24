package com.blueprinthell.view;

import com.blueprinthell.controller.PacketController;
import com.blueprinthell.logic.GameStats;
import com.blueprinthell.model.ShopItem;
import com.blueprinthell.util.Constants;
import com.google.gson.Gson;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ShopView extends AnchorPane {
    private final List<Button> buyButtons = new ArrayList<>();
    private final Label sharedTooltip = new Label();
    private final PacketController packetController;


    public ShopView(Runnable onClose, PacketController packetController) {
        this.packetController = packetController;
        setPrefSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        VBox card = new VBox(30);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPadding(new Insets(40, 60, 40, 60));
        card.setPrefSize(460, 500);
        card.setStyle("""
            -fx-background-color: #222;
            -fx-border-color: white;
            -fx-border-radius: 20;
            -fx-background-radius: 20;
        """);

        Label title = new Label("Shop");
        title.setFont(Font.font("X VOSTA", 40));
        title.setTextFill(Color.WHITE);

        VBox items = new VBox(30);
        items.setPrefWidth(Double.MAX_VALUE);

        for (ShopItem item : loadShopItemsFromJson()) {
            items.getChildren().add(createShopItem(item.name, item.description, item.price));
        }

        Button close = new Button("Close");
        close.setPrefWidth(80);
        close.setPrefHeight(30);
        close.setTranslateY(20);
        close.setTextFill(Color.WHITE);
        close.setStyle("""
            -fx-background-color: rgb(110,110,110);
            -fx-font-family: "X VOSTA";
            -fx-border-color: white;
            -fx-border-radius: 10;
            -fx-background-radius: 10;
            -fx-font-size: 20;
        """);
        close.setOnAction(e -> {
            setVisible(false);
            onClose.run();
        });

        // Tooltip مشترک برای همه
        sharedTooltip.setWrapText(true);
        sharedTooltip.setVisible(false);
        sharedTooltip.setManaged(false);
        sharedTooltip.setFont(Font.font("X VOSTA", 18));
        sharedTooltip.setPrefWidth(300);
        sharedTooltip.setPadding(new Insets(10));
        sharedTooltip.setStyle("""
            -fx-background-color: rgba(255,255,255,0.9);
            -fx-text-fill: black;
            -fx-background-radius: 10;
            -fx-border-radius: 10;
            -fx-border-color: gray;
            -fx-font-family: 'X Vosta';
        """);


        card.getChildren().addAll(title, items, close);
        getChildren().add(card);
        getChildren().add(sharedTooltip);


        widthProperty().addListener((obs, oldV, newV) ->
                card.setLayoutX((getWidth() - card.getPrefWidth()) / 2));
        heightProperty().addListener((obs, oldV, newV) ->
                card.setLayoutY((getHeight() - card.getPrefHeight()) / 2));
    }

    private VBox createShopItem(String itemName, String description, int price) {
        Label name = new Label(itemName);
        name.setFont(Font.font("X VOSTA", 24));
        name.setTextFill(Color.WHITE);

        name.setOnMouseClicked(e -> {
            System.out.println("Clicked on: " + itemName);
            Point2D localPos = name.localToScene(0, 0);
            Point2D tooltipPos = sharedTooltip.getParent().sceneToLocal(localPos);
            System.out.println("Scene coords: " + localPos);
            System.out.println("Converted to local: " + tooltipPos);

            sharedTooltip.setText(description);
            sharedTooltip.setLayoutX(tooltipPos.getX() + 200);
            sharedTooltip.setLayoutY(tooltipPos.getY());

            boolean newVisible = !sharedTooltip.isVisible();
            sharedTooltip.setVisible(newVisible);
            if (newVisible) sharedTooltip.toFront();

            System.out.println("Tooltip visibility: " + newVisible);
        });



        Button buy = new Button(price + " Coin");
        buy.setFont(Font.font("X VOSTA", 18));
        buy.setPrefHeight(30);
        buy.setPrefWidth(120);
        buyButtons.add(buy);

        buy.setOnAction(e -> {
            if (GameStats.getCoins() >= price) {
                GameStats.removeCoins(price);
                System.out.println("Bought: " + itemName);

                switch (itemName) {
                    case "O' Atar"     -> packetController.addOAtar();
                    case "O' Airyaman" -> packetController.addOAiryaman();
                    case "O' Anahita"  -> packetController.addOAnahita();
                }
                updateBuyButtons();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox box = new HBox(30, name, spacer, buy);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(0, 20, 0, 20));

        Separator sep = new Separator();
        sep.setPrefWidth(360);
        sep.setStyle("-fx-background-color: white;");
        VBox.setMargin(sep, new Insets(10, 0, 0, 0));

        VBox vbox = new VBox(box, sep);
        vbox.setUserData(price);
        return vbox;
    }

    public void updateBuyButtons() {
        int coins = (int) GameStats.getCoins();
        for (Button btn : buyButtons) {
            Node container = btn.getParent().getParent();
            if (container instanceof VBox itemBox) {
                int price = (int) itemBox.getUserData();
                if (coins >= price) {
                    btn.setDisable(false);
                    btn.setStyle(enabledStyle());
                } else {
                    btn.setDisable(true);
                    btn.setStyle(disabledStyle());
                }
            }
        }
    }

    private String enabledStyle() {
        return """
            -fx-background-color: gold;
            -fx-font-family: 'X Vosta';
            -fx-font-size: 18;
            -fx-background-radius: 20;
        """;
    }

    private String disabledStyle() {
        return """
            -fx-background-color: gray;
            -fx-font-family: 'X Vosta';
            -fx-font-size: 18;
            -fx-background-radius: 20;
        """;
    }

    private List<ShopItem> loadShopItemsFromJson() {
        try (InputStream is = getClass().getResourceAsStream("/data/shop_item.json");
             InputStreamReader reader = new InputStreamReader(is)) {
            Gson gson = new Gson();
            return List.of(gson.fromJson(reader, ShopItem[].class));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
