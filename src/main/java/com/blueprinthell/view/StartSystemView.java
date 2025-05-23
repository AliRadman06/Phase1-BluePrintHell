package com.blueprinthell.view;

import com.blueprinthell.controller.PacketController;
import com.blueprinthell.model.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;

public class StartSystemView extends AbstractDeviceView {
    private Rectangle body;
    private Pane innerBody;
    private Button startButton;
    private Circle lamp;
    private PacketController packetController;
    private Pane wiringLayer;
    private Timeline squareTimeline;
    private Timeline triangleTimeline;



    public StartSystemView(NetworkDevice model) {
        super(model);
    }

    public void setPacketController(PacketController controller) {
        this.packetController = controller;
    }

    public void setWiringLayer(Pane wiringLayer) {
        this.wiringLayer = wiringLayer;
    }

    @Override
    protected void initializeGraphics() {
        //body
        body = new Rectangle(0, 0, 200, 200);
        body.setArcWidth(10);
        body.setArcHeight(10);
        body.setFill(Color.rgb(80, 80, 80));
        body.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
        body.setStrokeWidth(2);


        //innerBody
        innerBody = new Pane();
        innerBody.setLayoutX(5);
        innerBody.setLayoutY(50);
        innerBody.setPrefWidth(190);
        innerBody.setPrefHeight(140);
        innerBody.setStyle("""
                -fx-background-color: rgb(40, 40, 40); 
                -fx-border-color: rgb(20, 20, 20); 
                -fx-background-radius: 10;
                -fx-border-radius: 10;
                """ );

        //lamp
        lamp = new Circle(180, 25, 15);
        lamp.setFill(Color.rgb(40, 40, 40));
        lamp.setStroke(Color.rgb(40, 40, 40).darker().darker().darker());


        //startButton
        startButton = new Button("Start");
        startButton.setTextFill(Color.WHITE);
        startButton.setLayoutX(5);
        startButton.setLayoutY(11.5);
        startButton.setPrefWidth(70);
        startButton.setStyle("-fx-background-radius: 5;" +
                "-fx-background-color: rgba(40,40,40,1);"
        );
        startButton.setOnAction(e -> startAutoSendPackets());


        getChildren().addAll(body, innerBody, startButton, lamp);

        drawPorts();
        renderBufferedPackets();

    }

    private void startAutoSendPackets() {
        if (packetController == null || wiringLayer == null) return;

        if (squareTimeline == null) {
            squareTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> sendPacket(Packet.ShapeType.SQUARE)));
            squareTimeline.setCycleCount(Timeline.INDEFINITE);
            squareTimeline.play();
        }

        if (triangleTimeline == null) {
            triangleTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> sendPacket(Packet.ShapeType.TRIANGLE)));
            triangleTimeline.setCycleCount(Timeline.INDEFINITE);
            triangleTimeline.play();
        }
    }


    public void sendPacket(Packet.ShapeType type) {
        if (packetController == null || wiringLayer == null) return;

        StartSystem startSystem = (StartSystem) model;
        Packet target = startSystem.getPacketBuffer().stream()
                .filter(p -> p.getShape() == type)
                .findFirst().orElse(null);

        if (target == null) return;

        for (Port outPort : startSystem.getOutPorts()) {
            if (outPort.getShape().toString().equals(type.toString())) {
                Wire wire = findWireConnectedTo(outPort);
                if (wire == null) continue;

                List<Point2D> path = wire.flatten(40);
                target.setPath(path);
                target.setSpeed(100);
                target.setDestinationPort(wire.getInputPort());

                packetController.addPacket(target, new PacketView(target));
                startSystem.getPacketBuffer().remove(target);
                renderBufferedPackets();
                break;
            }
        }
    }



    private Wire findWireConnectedTo(Port port) {

        for (Node node : wiringLayer.getChildren()) {
            if (node.getUserData() instanceof Wire wire) {
                if (wire.getOutputPort().equals(port)) return wire;
            }
        }
        return null;
    }

    public void renderBufferedPackets() {
        innerBody.toFront();
        innerBody.getChildren().clear();

        double y = 10;
        StartSystem startSystem = (StartSystem) super.model();
        for (Packet p : startSystem.getPacketBuffer()) {
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
                tri.setRotate(180 );
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

    protected void setLampOn() {
        lamp.setFill(Color.rgb(10, 101, 0));
    }
}