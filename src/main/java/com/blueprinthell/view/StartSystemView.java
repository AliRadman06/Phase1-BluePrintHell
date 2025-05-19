package com.blueprinthell.view;

import com.blueprinthell.controller.PacketController;
import com.blueprinthell.model.*;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.List;

public class StartSystemView extends AbstractDeviceView {
    private Rectangle body;
    private Pane innerBody;
    private Button startButton;
    private Circle lamp;
    private PacketController packetController;
    private Pane wiringLayer;


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
        body = new Rectangle(0, 0, 200, 200);
        innerBody = new Pane();
        innerBody.setLayoutX(5);
        innerBody.setLayoutY(50);
        innerBody.setPrefWidth(190);
        innerBody.setPrefHeight(140);

        lamp = new Circle(180, 25, 15);
        lamp.setFill(Color.rgb(40, 40, 40));
        lamp.setStroke(Color.rgb(40, 40, 40).darker().darker().darker());

        startButton = new Button("Start");
        startButton.setTextFill(Color.WHITE);
        startButton.setLayoutX(5);
        startButton.setLayoutY(11.5);
        startButton.setPrefWidth(70);
        startButton.setStyle("-fx-background-radius: 5;" +
                "-fx-background-color: rgba(40,40,40,1);"
                );
        startButton.setOnAction(e -> sendPacket());

        body.setArcWidth(10);
        body.setArcHeight(10);

        body.setFill(Color.rgb(80, 80, 80));
        body.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
        body.setStrokeWidth(2);

        innerBody.setStyle("-fx-background-color: rgb(40, 40, 40);" +
                "-fx-border-color: rgb(20, 20, 20);" +
                "-fx-background-radius: 10" );



        getChildren().addAll(body, innerBody, startButton, lamp);

        drawPorts();
        renderBufferedPackets();

    }

    public void sendPacket() {
        System.out.println("🔵 دکمه استارت کلیک شد");

        if (packetController == null) {
            System.out.println("⚠️ packetController = null");
            return;
        }
        if (wiringLayer == null) {
            System.out.println("⚠️ wiringLayer = null");
            return;
        }

        StartSystem startSystem = (StartSystem) model;
        if (startSystem.getPacketBuffer().isEmpty()) {
            System.out.println("⚠️ packetBuffer خالی است");
            return;
        }

        Packet packet = startSystem.getPacketBuffer().peek();
        if (packet == null) {
            System.out.println("⚠️ packet == null");
            return;
        }

        for (Port outPort : startSystem.getOutPorts()) {
            System.out.println("⏩ بررسی پورت " + outPort.getShape());

            if (outPort.getShape().toString().equals(packet.getShape().toString())) {
                System.out.println("✅ پورت سازگار پیدا شد");

                Wire wire = findWireConnectedTo(outPort);
                if (wire == null) {
                    System.out.println("❌ سیمی برای پورت پیدا نشد");
                    continue;
                }

                System.out.println("✅ سیم پیدا شد");

                List<Point2D> path = wire.flatten(40);
                packet.setPath(path);
                packet.setSpeed(100);

                packetController.addPacket(packet, new PacketView(packet));
                System.out.println("🚀 پکت ارسال شد");

                startSystem.getPacketBuffer().poll();
                renderBufferedPackets();
                break;
            } else {
                System.out.println("❌ پورت ناسازگار");
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
