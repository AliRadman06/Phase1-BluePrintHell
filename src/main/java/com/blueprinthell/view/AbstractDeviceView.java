package com.blueprinthell.view;

import com.blueprinthell.model.Port;
import com.blueprinthell.model.NetworkDevice;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDeviceView extends Group {
    protected final NetworkDevice model;
    private final List<Node> portNodes = new ArrayList<>();


    public AbstractDeviceView(NetworkDevice model) {
        this.model = model;
        initializeGraphics();
        updatePosition();
        this.setOnMouseClicked(evt -> {
            showPortLocations();
            evt.consume();
        });
    }

    protected abstract void initializeGraphics();

    public void updatePosition() {
        setLayoutX(model.getX());
        setLayoutY(model.getY());
    }


    protected abstract double getBodyWidth();
    protected abstract double getBodyHeight();
    protected NetworkDevice getModel() {
        return model;
    }
    protected void drawPorts() {
        double size   = 20;
        double offset = 1;

        for (Port p : model.getInPorts()) {
            double y = (p.getRelativeY() >= 0)
                    ? getBodyHeight() * p.getRelativeY() - size / 2
                    : (getBodyHeight() - size) / 2;
            double x = -size - offset;
            drawPortShape(p, x, y, size);
            p.setCenter();
        }

        for (Port p : model.getOutPorts()) {
            double y = (p.getRelativeY() >= 0)
                    ? getBodyHeight() * p.getRelativeY() - size / 2
                    : (getBodyHeight() - size) / 2;
            double x = getBodyWidth() + offset;
            drawPortShape(p, x, y, size);
            p.setCenter();
        }
    }

    private Node drawPortShape(Port p, double x, double y, double size) {
        Node shape;
        switch (p.getShape()) {
            case SQUARE -> {
                Rectangle square = new Rectangle(x, y, size, size);
                square.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
                square.setStrokeWidth(2);
                square.setArcHeight(2);
                square.setArcWidth(2);
                square.setFill(p.getDirection() == Port.Direction.IN ? Color.DODGERBLUE : Color.SALMON);
                shape = square;
            }
            case TRIANGLE -> {
                Polygon triangle = new Polygon(
                        x + size / 2, y,
                        x + size, y + size,
                        x, y + size
                );
                triangle.setStroke(Color.rgb(80, 80, 80).darker().darker().darker());
                triangle.setStrokeWidth(2);
                triangle.setFill(p.getDirection() == Port.Direction.IN ? Color.DODGERBLUE : Color.SALMON);
                triangle.setRotate(p.getDirection() == Port.Direction.IN ? 270 : 90);
                shape = triangle;
            }
            default -> throw new IllegalStateException("UnKnown port shape");
        }
        shape.setUserData(p);
        shape.setOnMouseClicked(evt ->
                System.out.println("Hovered PORT: " + p.getDirection() + " on system " + model.getId())
        );
        getChildren().add(shape);
        portNodes.add(shape);
        return shape;
    }

    public List<Node> getAllPortNodes() {
        return List.copyOf(portNodes);
    }

    public List<Point2D> getInputPortLocations() {
        return computePortLocations(model.getInPorts(), Port.Direction.IN);
    }

    public List<Point2D> getOutputPortLocations() {
        return computePortLocations(model.getOutPorts(), Port.Direction.OUT);
    }

    private List<Point2D> computePortLocations(List<Port> ports, Port.Direction dir) {
        List<Point2D> locs = new ArrayList<>();
        double viewX = getTranslateX();       // یا layoutX
        double viewY = getTranslateY();       // یا layoutY
        double w     = 200;
        double h     = 200;
        for (Port p : ports) {
            double x = p.getOwner().getX() + viewX + (dir == Port.Direction.OUT ? w : 0);
            double y = p.getOwner().getY() + viewY + p.getRelativeY() * h;
            p.setCenter();
            locs.add(new Point2D(x, y));
        }
        return locs;
    }

    private void showPortLocations() {
//        List<Point2D> ins  = getInputPortLocations();
//        List<Point2D> outs = getOutputPortLocations();
//
//        StringBuilder msg = new StringBuilder();
//        if (ins.isEmpty()) {
//            msg.append("ورودی ندارد\n");
//        } else {
//            msg.append("ورودی‌ها:\n");
//            for (Point2D p : ins) {
//                msg.append(String.format("  (%.1f, %.1f)\n", p.getX(), p.getY()));
//            }
//        }
//        if (outs.isEmpty()) {
//            msg.append("خروجی ندارد\n");
//        } else {
//            msg.append("خروجی‌ها:\n");
//            for (Point2D p : outs) {
//                msg.append(String.format("  (%.1f, %.1f)\n", p.getX(), p.getY()));
//            }
//        }
//
//        System.out.println("پورت‌های " + model.getId() + ":\n" + msg);
    }

    protected Object model() {
        return model;
    }
}
