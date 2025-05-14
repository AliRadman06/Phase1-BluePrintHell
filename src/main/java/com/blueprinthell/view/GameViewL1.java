package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import com.blueprinthell.model.Port;
import com.blueprinthell.model.SystemFactory;
import com.blueprinthell.model.SystemType;
import javafx.beans.value.ChangeListener;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameViewL1 extends AnchorPane {
    private final AnchorPane gamePane;
    private final Canvas gridCanvas;
    private final Button backButton;
    private Pane wiringLayer = new Pane();
    public static final double TOTAL_WIRE = 1000.0;  // کل طول سیم مجاز



    public GameViewL1() {

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
        getChildren().add(0, wiringLayer);

        // --- دکمهٔ Back ---
        backButton = new Button("Back");
        backButton.setTextFill(Color.WHITE);
        backButton.setPrefHeight(39);
        backButton.setPrefWidth(79);
        backButton.setStyle("-fx-background-color: rgb(110,110,110)");
        backButton.setLayoutX(1);
        backButton.setLayoutY(1);
        backButton.setOnAction(e -> goBackToMenu());
        getChildren().add(backButton);


        // — نمونهٔ StartSystem —
        NetworkDevice s1 = SystemFactory.createSystem(SystemType.START, "s1", 200, 200);
        s1.setX(200);
        s1.setY(200);
        Port s1out1 = new Port(s1, Port.Direction.OUT, Port.Shape.SQUARE);
        Port s1out2 = new Port(s1, Port.Direction.OUT, Port.Shape.TRIANGLE);

        s1out1.setRelativeY(0.7);
        s1out2.setRelativeY(0.3);

        s1.getOutPorts().add(s1out1);
        s1.getOutPorts().add(s1out2);

        s1.initialize();

        AbstractDeviceView v1 = DeviceViewFactory.create(s1);
        gamePane.getChildren().add(v1);

        // — نمونهٔ ProcessingSystem —
        NetworkDevice p1 = SystemFactory.createSystem(SystemType.PROCESSING, "p1", 600, 320);
        p1.setX(600);
        p1.setY(320);
        Port p1in1 = new Port(p1, Port.Direction.IN, Port.Shape.SQUARE);
        Port p1in2 = new Port(p1, Port.Direction.IN, Port.Shape.TRIANGLE);

        p1in1.setRelativeY(0.7);
        p1in2.setRelativeY(0.3);

        p1.getInPorts().add(p1in1);
        p1.getInPorts().add(p1in2);

        Port p1out1 = new Port(p1, Port.Direction.OUT, Port.Shape.SQUARE);
        Port p1out2 = new Port(p1, Port.Direction.OUT, Port.Shape.TRIANGLE);

        p1out1.setRelativeY(0.7);
        p1out2.setRelativeY(0.3);

        p1.getOutPorts().add(p1out1);
        p1.getOutPorts().add(p1out2);

        p1.initialize();

        AbstractDeviceView v2 = DeviceViewFactory.create(p1);
        gamePane.getChildren().add(v2);


        // — نمونهٔ EndSystem —
        NetworkDevice e1 = SystemFactory.createSystem(SystemType.END, "e1", 1000, 440);
        e1.setX(1000);
        e1.setY(440);
        System.out.println(e1.getX());

        Port e1in1 = new Port(e1, Port.Direction.IN, Port.Shape.SQUARE);
        Port e1in2 = new Port(e1, Port.Direction.IN, Port.Shape.TRIANGLE);

        e1in1.setRelativeY(0.7);
        e1in2.setRelativeY(0.3);

        e1.getInPorts().add(e1in1);
        e1.getInPorts().add(e1in2);

        e1.initialize();
        AbstractDeviceView v3 = DeviceViewFactory.create(e1);
        gamePane.getChildren().add(v3);

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


}
