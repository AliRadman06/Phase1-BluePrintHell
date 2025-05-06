package com.blueprinthell.view;

import com.blueprinthell.model.Port;
import com.blueprinthell.model.NetworkDevice;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public abstract class AbstractDeviceView extends Group {
    protected final NetworkDevice model;

    public AbstractDeviceView(NetworkDevice model) {
        this.model = model;
        initializeGraphics();
        updatePosition();
    }

    protected abstract void initializeGraphics();

    public void updatePosition() {
        setLayoutX(model.getX());
        setLayoutY(model.getY());
    }

    protected abstract double getBodyWidth();
    protected abstract double getBodyHeight();

    protected void drawPorts() {
        double size   = 20;
        double offset = 1;

        for (Port p : model.getInPorts()) {
            double y = (p.getRelativeY() >= 0)
                    ? getBodyHeight() * p.getRelativeY() - size / 2
                    : (getBodyHeight() - size) / 2;
            double x = -size - offset;
            drawPortShape(p, x, y, size);
        }

        for (Port p : model.getOutPorts()) {
            double y = (p.getRelativeY() >= 0)
                    ? getBodyHeight() * p.getRelativeY() - size / 2
                    : (getBodyHeight() - size) / 2;
            double x = getBodyWidth() + offset;
            drawPortShape(p, x, y, size);
        }
    }

    private void drawPortShape(Port p, double x, double y, double size) {
        switch (p.getShape()) {
            case SQUARE -> {
                Rectangle square = new Rectangle(x, y, size, size);
                square.setArcHeight(2);
                square.setArcWidth(2);
                square.setFill(p.getDirection() == Port.Direction.IN ? Color.DODGERBLUE : Color.SALMON);
                square.setOnMouseMoved(evt ->
                        System.out.println("Hovered PORT: " + p.getDirection() + " on System " + model.getId())
                );
                getChildren().add(square);
            }
            case TRIANGLE -> {
                Polygon triangle = new Polygon(
                        x + size / 2, y,
                        x + size, y + size,
                        x, y + size
                );
                triangle.setFill(p.getDirection() == Port.Direction.IN ? Color.DODGERBLUE : Color.SALMON);
                triangle.setOnMouseMoved(evt ->
                        System.out.println("Hovered PORT: " + p.getDirection() + " on System " + model.getId())
                );
                triangle.setRotate(90);
                getChildren().add(triangle);
            }
        }
    }
}
