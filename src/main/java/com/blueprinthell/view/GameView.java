package com.blueprinthell.view;

import com.blueprinthell.model.NetworkDevice;
import com.blueprinthell.model.SystemFactory;
import com.blueprinthell.model.SystemType;
import com.blueprinthell.util.Constants;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameView extends AnchorPane {
    private final Canvas gridCanvas;
    private final Button backButton;


    public GameView() {

        gridCanvas = new Canvas();
        gridCanvas.widthProperty().bind(widthProperty());
        gridCanvas.heightProperty().bind(heightProperty());

        getChildren().add(gridCanvas);

        ChangeListener<Number> redraw = (obs, oldV, newV) -> drawGrid();
        widthProperty().addListener(redraw);
        heightProperty().addListener(redraw);

        drawGrid();

        AnchorPane gamePane = new AnchorPane();
        AnchorPane.setTopAnchor   (gamePane, 0.0);
        AnchorPane.setBottomAnchor(gamePane, 0.0);
        AnchorPane.setLeftAnchor  (gamePane, 0.0);
        AnchorPane.setRightAnchor (gamePane, 0.0);
        getChildren().add(gamePane);

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
        s1.initialize();
        AbstractDeviceView v1 = DeviceViewFactory.create(s1);
        gamePane.getChildren().add(v1);

        // — نمونهٔ ProcessingSystem —
        NetworkDevice p1 = SystemFactory.createSystem(SystemType.PROCESSING, "p1", 600, 320);
        p1.initialize();
        AbstractDeviceView v2 = DeviceViewFactory.create(p1);
        gamePane.getChildren().add(v2);


        // — نمونهٔ EndSystem —
        NetworkDevice e1 = SystemFactory.createSystem(SystemType.END, "e1", 1000, 440);
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
}
