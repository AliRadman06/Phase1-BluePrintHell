package com.blueprinthell.view;

import com.blueprinthell.controller.PacketController;
import com.blueprinthell.controller.WiringController;
import com.blueprinthell.logic.GameStats;
import com.blueprinthell.model.*;
import com.blueprinthell.util.GameSession;
import com.blueprinthell.util.SoundManager;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameViewL2 extends AnchorPane {
    private final AnchorPane gamePane;
    private final Canvas gridCanvas;
    private final Button backButton;
    private Pane wiringLayer = new Pane();
    private final PacketController packetController;
    public static final double TOTAL_WIRE = 1500.0;
    private GameStats gameStats;
    private WiringController wiringController;
    private Label wireLabel;
    private Label coinLabel;
    private Label packetLossLabel;
    private Timeline hudTimeline;
    private final ShopView shopView;
    private  Button shopButton;
    private final VBox skillsBox ;
    private final Button skillsButton;





    public GameViewL2() {
        GameSession.setCurrentLevel(1);
        GameStats.resetCoins();


        gridCanvas = new Canvas();
        gridCanvas.widthProperty().bind(widthProperty());
        gridCanvas.heightProperty().bind(heightProperty());

        getChildren().add(gridCanvas);

        ChangeListener<Number> redraw = (obs, oldV, newV) -> drawGrid();
        widthProperty().addListener(redraw);
        heightProperty().addListener(redraw);

        drawGrid();

        this.gamePane = new AnchorPane();
        AnchorPane.setTopAnchor   (gamePane, 0.0);
        AnchorPane.setBottomAnchor(gamePane, 0.0);
        AnchorPane.setLeftAnchor  (gamePane, 0.0);
        AnchorPane.setRightAnchor (gamePane, 0.0);
        getChildren().add(gamePane);
        getChildren().add(1,  wiringLayer);
        packetController = new PacketController(gamePane);
        packetController.setWiringLayer(wiringLayer);
        packetController.start();

        // — نمونهٔ StartSystem —
        StartSystem s1 = (StartSystem) SystemFactory.createSystem(SystemType.START, "s1", 80, 120);
        s1.setX(80);
        s1.setY(120);


        Port s1out1 = new Port(s1, Port.Direction.OUT, Port.Shape.SQUARE);
        Port s1out2 = new Port(s1, Port.Direction.OUT, Port.Shape.TRIANGLE);

        s1out1.setRelativeY(0.7);
        s1out2.setRelativeY(0.3);

        s1.getOutPorts().add(s1out1);
        s1.getOutPorts().add(s1out2);

        s1.setSquarePacket(2);
        s1.setTrianglePacket(2);

        List<Point2D> dummyPath = new ArrayList<>();
        dummyPath.add(new Point2D(0, 0));
        dummyPath.add(new Point2D(100, 0));
        s1.generateInitialPackets(dummyPath);
        packetController.setTotalToBeSpawned(s1.getSquarePacket() + s1.getTrianglePacket());



        s1.initialize();

        StartSystemView v1 = (StartSystemView) DeviceViewFactory.create(s1);
        v1.setPacketController(this.packetController);
        v1.setWiringLayer(this.wiringLayer);
        v1.renderBufferedPackets();
        gamePane.getChildren().add(v1);

        // — نمونهٔ ProcessingSystem —
        NetworkDevice p1 = SystemFactory.createSystem(SystemType.PROCESSING, "p1", 520, 120);
        p1.setX(520);
        p1.setY(120);
        Port p1in1 = new Port(p1, Port.Direction.IN, Port.Shape.SQUARE);
        Port p1in2 = new Port(p1, Port.Direction.IN, Port.Shape.TRIANGLE);

        p1in1.setRelativeY(0.3);
        p1in2.setRelativeY(0.7);

        p1.getInPorts().add(p1in1);
        p1.getInPorts().add(p1in2);

        Port p1out1 = new Port(p1, Port.Direction.OUT, Port.Shape.SQUARE);
        Port p1out2 = new Port(p1, Port.Direction.OUT, Port.Shape.TRIANGLE);

        p1out1.setRelativeY(0.3);
        p1out2.setRelativeY(0.7);

        p1.getOutPorts().add(p1out1);
        p1.getOutPorts().add(p1out2);

        p1.initialize();

        AbstractDeviceView v2 = DeviceViewFactory.create(p1);
        gamePane.getChildren().add(v2);


        // — نمونهٔ ProcessingSystem —
        NetworkDevice p2 = SystemFactory.createSystem(SystemType.PROCESSING, "p2", 840, 120);
        p2.setX(840);
        p2.setY(120);
        Port p2in1 = new Port(p2, Port.Direction.IN, Port.Shape.SQUARE);
        Port p2in2 = new Port(p2, Port.Direction.IN, Port.Shape.TRIANGLE);

        p2in1.setRelativeY(0.3);
        p2in2.setRelativeY(0.7);

        p2.getInPorts().add(p2in1);
        p2.getInPorts().add(p2in2);

        Port p2out1 = new Port(p2, Port.Direction.OUT, Port.Shape.SQUARE);
        Port p2out2 = new Port(p2, Port.Direction.OUT, Port.Shape.TRIANGLE);

        p2out1.setRelativeY(0.7);
        p2out2.setRelativeY(0.3);

        p2.getOutPorts().add(p2out1);
        p2.getOutPorts().add(p2out2);

        p2.initialize();

        AbstractDeviceView v3 = DeviceViewFactory.create(p2);
        gamePane.getChildren().add(v3);


        // — نمونهٔ EndSystem —
        EndSystem e1 = (EndSystem) SystemFactory.createSystem(SystemType.END, "e1", 1200, 120);
        e1.setX(1200);
        e1.setY(120);

        Port e1in1 = new Port(e1, Port.Direction.IN, Port.Shape.SQUARE);
        Port e1in2 = new Port(e1, Port.Direction.IN, Port.Shape.TRIANGLE);

        e1in1.setRelativeY(0.3);
        e1in2.setRelativeY(0.7);

        e1.getInPorts().add(e1in1);
        e1.getInPorts().add(e1in2);

        e1.initialize();
        AbstractDeviceView v4 = DeviceViewFactory.create(e1);
        packetController.setEndSystem(e1);
        gamePane.getChildren().add(v4);

        // --- HUD Wire Label ---
        HBox hudBar = new HBox(40); // فاصله بین آیتم‌ها
        hudBar.setAlignment(Pos.TOP_CENTER);
        hudBar.setPadding(new Insets(10));
        hudBar.setStyle("-fx-background-color: rgba(0,0,0,0.9);");
        hudBar.setPrefHeight(40);
        hudBar.setPrefWidth(USE_COMPUTED_SIZE);

        wireLabel = new Label("Wire Left: 0");
        coinLabel = new Label("Coins: 0");
        packetLossLabel = new Label("Loss: 0%");

        List<Label> labels = List.of(wireLabel, coinLabel, packetLossLabel);
        for (Label lbl : labels) {
            lbl.setTextFill(Color.WHITE);
            lbl.setFont(Font.font(18));
            lbl.setStyle(
                    "-fx-font-family: 'X Vosta' "
            );
        }

        hudBar.getChildren().addAll(wireLabel, coinLabel, packetLossLabel);
        AnchorPane.setTopAnchor(hudBar, 0.0);
        AnchorPane.setLeftAnchor(hudBar, 0.0);
        AnchorPane.setRightAnchor(hudBar, 0.0);
        getChildren().add(hudBar);

        // --- دکمهٔ Back ---
        backButton = new Button("Back");
        backButton.setTextFill(Color.WHITE);
        backButton.setPrefHeight(39);
        backButton.setPrefWidth(79);
        backButton.setStyle("""
                               -fx-background-color: rgb(110,110,110);
                               -fx-font-family: "X VOSTA";
                               -fx-font-size: 20;
                            """);
        backButton.setLayoutX(1);
        backButton.setLayoutY(1);
        backButton.setOnAction(e -> {
            goBackToMenu();
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
        });
        getChildren().add(backButton);

        // --- Skills Box ---
        skillsBox = new VBox(10);
        skillsBox.setPadding(new Insets(10));
        skillsBox.setLayoutX(134);
        skillsBox.setLayoutY(45);
        skillsBox.setStyle("""
                -fx-background-color: rgba(50, 50, 50, 0.95);
                            -fx-background-radius: 10;
                            -fx-border-color: white;
                            -fx-border-width: 1;
                            -fx-border-radius: 10;
                """);
        skillsBox.setVisible(false);
        getChildren().add(skillsBox);


        // --- Skills Button ---
        skillsButton = new Button("Skills");
        skillsButton.setTextFill(Color.WHITE);
        skillsButton.setPrefWidth(79);
        skillsButton.setPrefHeight(39);
        skillsButton.setLayoutX(161);
        skillsButton.setLayoutY(1);
        skillsButton.setStyle("""
            -fx-background-color: rgb(110,110,110);
            -fx-font-family: 'X VOSTA';
            -fx-font-size: 20;
        """);
        skillsButton.setTextFill(Color.WHITE);

        skillsButton.setOnAction(e -> {
            if (!skillsBox.isVisible()) {
                updateSkillsBox();
                skillsBox.setVisible(true);
            } else {
                skillsBox.setVisible(false);
            }
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
        });
        getChildren().add(skillsButton);



        // --- Shop View ---
        shopView = new ShopView(() ->
                packetController.resume(),
                packetController
        );
        shopView.setVisible(false);


        AnchorPane.setTopAnchor(shopView, 0.0);
        AnchorPane.setBottomAnchor(shopView, 0.0);
        AnchorPane.setLeftAnchor(shopView, 0.0);
        AnchorPane.setRightAnchor(shopView, 0.0);

        getChildren().add(shopView);



        // --- Shop Button ---
        shopButton = new Button("Shop");
        shopButton.setTextFill(Color.WHITE);
        shopButton.setPrefWidth(79);
        shopButton.setPrefHeight(39);
        shopButton.setStyle("""
                                -fx-background-color: rgb(110,110,110);
                                -fx-font-family: "X VOSTA";
                                -fx-font-size: 20;
                            """);

        shopButton.setLayoutX(81);
        shopButton.setLayoutY(1);

        shopButton.setOnAction(e -> {
            SoundManager.getInstance().playEffect("click", "/audio/click.mp3");
            shopView.updateBuyButtons();
            shopView.setVisible(true);
            packetController.pause();
        });

        getChildren().add(shopButton);

//      --- Timeline آپدیت HUD ---
        hudTimeline = new Timeline(
                new KeyFrame(Duration.millis(200), e -> {
                    wireLabel.setText("Wire Left: " + (int) wiringController.getRemainingWires());
                    coinLabel.setText("Coins: " + GameStats.getCoins());
                    packetLossLabel.setText("Loss: " + (int) GameStats.getLossPercent() + "%");
                })
        );
        hudTimeline.setCycleCount(Timeline.INDEFINITE);
        hudTimeline.play();


    }

    private void drawGrid() {
        double w = getWidth();
        double h = getHeight();
        double cell = 40;

        GraphicsContext gc = gridCanvas.getGraphicsContext2D();

        gc.setFill(Color.rgb(3, 3, 3));
        gc.fillRect(0, 0, w, h);

        gc.setStroke(Color.GRAY.darker());
        gc.setLineWidth(2);

        for (double x = 0; x <= w; x += cell) {
            gc.strokeLine(x, 0, x, h);
        }

        for (double y = 0; y <= h; y += cell) {
            gc.strokeLine(0, y, w, y);
        }
    }

    private void goBackToMenu() {
        Stage stage = (Stage) getScene().getWindow();
        stage.getScene().setRoot(new MainMenuView());
    }

    public Pane getWiringLayer() {
        return wiringLayer;
    }

    public AnchorPane getGamePane() {
        return gamePane;
    }

    public PacketController getPacketController() {
        return packetController;
    }

    public void setWiringController(WiringController wiringController) {
        this.wiringController = wiringController;
    }

    private void updateSkillsBox() {
        skillsBox.getChildren().clear();
        List<String> items = packetController.boughtItems();
        Set<String> addedItems = new HashSet<>();

        for (String item : items) {
            if (addedItems.contains(item)) continue;
            addedItems.add(item);

            final String itemFinal = item;
            final Runnable action;
            final boolean isAvailable;

            if (itemFinal.equals("O Atar")) {
                action = () -> {
                    packetController.activateOAtarSkill();
                    packetController.setOAtarCount(packetController.getOAtarCount()-1);
                };
                isAvailable = packetController.isOAtarBought();
            } else if (itemFinal.equals("O Airyaman")) {
                action = () -> {
                    packetController.activateOAiryamanSkill();
                    packetController.setOAiryamanCount(packetController.getOAiryamanCount()-1);
                };
                isAvailable = packetController.isOAiryamanBought();
            } else if (itemFinal.equals("O Anahita")) {
                action = () -> {
                    packetController.activeOAnahitaSkill();
                    packetController.setOAnahitaCount(packetController.getOAnahitaCount()-1);
                };
                isAvailable = packetController.isOAnahitaBought();
            } else {
                continue;
            }

            if (isAvailable) {
                Button skillBtn = new Button();
                skillBtn.setStyle("""
                        -fx-background-color: gold;
                        -fx-font-family: 'X Vosta';
                        -fx-font-size: 15;
                        -fx-background-radius: 20;
                        """);

                skillBtn.setOnAction(ev -> {
                    action.run();


                    String newLabel;
                    if (itemFinal.equals("O Atar")) newLabel = "O Atar (" + packetController.getOAtarCount() + ")";
                    else if (itemFinal.equals("O Airyaman")) newLabel = "O Airyaman (" + packetController.getOAiryamanCount() + ")";
                    else newLabel = "O Anahita (" + packetController.getOAnahitaCount() + ")";

                    skillBtn.setText(newLabel);


                    if ((itemFinal.equals("O Atar") && !packetController.isOAtarBought()) ||
                            (itemFinal.equals("O Airyaman") && !packetController.isOAiryamanBought()) ||
                            (itemFinal.equals("O Anahita") && !packetController.isOAnahitaBought())) {

                        skillBtn.setDisable(true);
                        skillBtn.setStyle("""
                                            -fx-background-color: gray;
                                            -fx-font-family: 'X Vosta';
                                            -fx-font-size: 15;
                                            -fx-background-radius: 20;
                                        """);
                    }
                });


                if (itemFinal.equals("O Atar")) skillBtn.setText("O Atar (" + packetController.getOAtarCount() + ")");
                else if (itemFinal.equals("O Airyaman")) skillBtn.setText("O Airyaman (" + packetController.getOAiryamanCount() + ")");
                else skillBtn.setText("O Anahita (" + packetController.getOAnahitaCount() + ")");

                skillsBox.getChildren().add(skillBtn);
            }
        }
    }

}
