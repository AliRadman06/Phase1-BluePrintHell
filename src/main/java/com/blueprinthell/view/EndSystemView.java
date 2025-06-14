package com.blueprinthell.view;

import com.blueprinthell.model.EndSystem;
import com.blueprinthell.model.NetworkDevice;
import com.blueprinthell.model.Packet;
import com.blueprinthell.model.ProcessingSystem;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class EndSystemView extends AbstractDeviceView {
    private Rectangle body;
    private Pane innerBody;
    private Circle lamp;

    public EndSystemView(NetworkDevice model) {
        super(model);
    }

    @Override
    protected void initializeGraphics() {
        body = new Rectangle(0, 0, 200, 200);
        innerBody = new Pane();
        innerBody.setLayoutX(5);
        innerBody.setLayoutY(50);
        innerBody.setPrefWidth(190);
        innerBody.setPrefHeight(140);

        lamp = new Circle(180, 25, 15);
        lamp.setFill(Color.rgb(40, 40, 40));
        lamp.setStroke(Color.rgb(40, 40, 40).darker().darker().darker());

        body.setArcWidth(10);
        body.setArcHeight(10);
        body.setFill(Color.rgb(80, 80, 80));
        body.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
        body.setStrokeWidth(2);

        innerBody.setStyle("""
                -fx-background-color: rgb(40, 40, 40); 
                -fx-border-color: rgb(20, 20, 20); 
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                """ );

        getChildren().addAll(body, innerBody, lamp);
        drawPorts();
        renderBufferedPackets();
    }

    public void renderBufferedPackets() {
        innerBody.toFront();
        innerBody.getChildren().clear();

        EndSystem endSystem = (EndSystem) model;
        double y = 10;
        for (Packet p : endSystem.getPacketBuffer()) {
            if (p.getShape() == Packet.ShapeType.SQUARE) {
                Rectangle r = new Rectangle(20, 20);
                r.setFill(Color.YELLOW);
                r.setStroke(Color.rgb(140, 112, 5));
                r.setStrokeWidth(2);
                r.setLayoutX(10);
                r.setLayoutY(y);
                innerBody.getChildren().add(r);
            } else {
                Polygon tri = new Polygon(
                        0.0, 0.0,
                        20.0, 0.0,
                        10.0, 20.0
                );
                tri.setFill(Color.ORANGE);
                tri.setStroke(Color.rgb(152, 82, 5));
                tri.setStrokeWidth(2);
                tri.setRotate(180);
                tri.setLayoutX(10);
                tri.setLayoutY(y);
                innerBody.getChildren().add(tri);
            }
            y += 25;
        }
    }

    @Override
    protected double getBodyWidth() {
        return body.getWidth();
    }

    @Override
    protected double getBodyHeight() {
        return body.getHeight();
    }
}
